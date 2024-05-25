package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.*;
import com.ProyectoMongo.api.Model.*;
import com.ProyectoMongo.api.Repository.*;
import com.ProyectoMongo.api.Service.IComprasService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ComprasServiceImpl implementa la interfaz IComprasService y proporciona
 * la lógica de negocio para manejar las operaciones relacionadas con las compras.
 * 
 * @see IComprasService
 */
@Service
public class ComprasServiceImpl implements IComprasService {

    @Autowired
    private IComprasRepository compraRepository;

    @Autowired
    private IUsuariosRepository usuarioRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IDepartamentosRepository departamentosRepository;

    @Autowired
    private IPromocionesRepository promocionesRepository;

    /**
     * Recupera todas las compras almacenadas.
     * 
     * @return una lista de todas las compras
     */
    @Override
    public List<ComprasModel> findAllCompras() {
        return compraRepository.findAll();
    }

    /**
     * Recupera una compra por su ID.
     * 
     * @param id el identificador único de la compra
     * @return la compra encontrada
     * @throws RecursoNoEncontradoException si la compra no es encontrada
     */
    @Override
    public ComprasModel findCompraById(ObjectId id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada"));
    }

    /**
     * Guarda una nueva compra o actualiza una existente.
     * 
     * @param compra la compra a guardar
     * @return la compra guardada
     * @throws RecursoNoEncontradoException si el usuario o una imagen personalizada no son encontrados
     * @throws ValorInvalidoException si el formato del número de tarjeta es inválido o la fecha de vencimiento es en el pasado
     * @throws StockInsuficienteException si el stock de un producto es insuficiente
     */
    @Override
    public ComprasModel saveCompra(ComprasModel compra) {
        if (compra.getId() == null) {
            // Genera un nuevo ObjectId si el id es nulo
            compra.setId(new ObjectId());
        }   

        if (!usuarioRepository.existsById(compra.getIdUsuario())) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
        }

        compra.setFechaCompra(new Date());

        // Validar formato del número de tarjeta
        if (compra.getTarjeta().getFechaVencimiento().before(new Date())) {
            throw new ValorInvalidoException("La fecha de vencimiento de la tarjeta no puede ser en el pasado: " + compra.getTarjeta().getFechaVencimiento());
        }

        String numeroTarjetaStr = String.valueOf(compra.getTarjeta().getNumero());
        if (!numeroTarjetaStr.matches("^\\d{16}$")) {
            throw new ValorInvalidoException("Formato inválido para el número de tarjeta: " + compra.getTarjeta().getNumero());
        }

        // Lógica para validar que el código de la ciudad exista
        Set<Integer> codigosPostales = new HashSet<>();
        departamentosRepository.findAll().forEach(departamento ->
                departamento.getCiudades().forEach(ciudad -> codigosPostales.add((int) ciudad.getCodigoPostal()))
        );

        if (!codigosPostales.contains((int) compra.getDestinatario().getCodigoPostalCiudad())) {
            throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
        }

        Set<String> imagenes = new HashSet<>();

        // Obtener las imágenes personalizadas de todos los productos
        productoRepository.findAll().forEach(producto -> producto.getImagenes().forEach(imagen -> imagenes.add(imagen.getImagen())));

        // Iterar sobre los detalles de la compra
        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            if (!imagenes.contains(detalle.getImagenPersonalizada())) {
                throw new RecursoNoEncontradoException("Imagen no encontrada: " + detalle.getImagenPersonalizada());
            }
        }
        
        BigDecimal precioTotalCompra = BigDecimal.ZERO;

        // Verificar existencia de los productos, stock disponible y calcular el precio total
        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            boolean stockSuficiente = false;
            for (StockModel stock : producto.getStock()) {
                boolean tallaCoincide = stock.getTalla().equals(detalle.getTalla());
                boolean colorCoincide = stock.getColor().equals(detalle.getColor());
                boolean cantidadSuficiente = stock.getCantidad() >= detalle.getCantidad();

                if (tallaCoincide && colorCoincide && cantidadSuficiente) {
                    stockSuficiente = true;

                    // Restar la cantidad del detalle a la cantidad del stock
                    int newCantidad = (int) stock.getCantidad() - (int) detalle.getCantidad();
                    stock.setCantidad(newCantidad);
                    break;
                }
            }

            if (!stockSuficiente) {
                throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + detalle.getIdTipo());
            }

            // Calcular precio del producto con descuento si aplica
            BigDecimal precioProducto = producto.getPrecio();
            Date now = new Date();
            for (PromocionesModel promocion : promocionesRepository.findAll()) {
                for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                    if (productoPromocion.getIdProducto().equals(detalle.getIdTipo()) &&
                        now.after(productoPromocion.getFechaInicio()) && now.before(productoPromocion.getFechaFin())) {
                        BigDecimal descuento = precioProducto.multiply(BigDecimal.valueOf(productoPromocion.getDescuento() / 100.0));
                        precioProducto = precioProducto.subtract(descuento);
                        break;
                    }
                }
            }
            
            // Calcular el precio total para este detalle y sumarlo al precio total de la compra
            BigDecimal precioTotalDetalle = precioProducto.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setPrecio(precioTotalDetalle);
            precioTotalCompra = precioTotalCompra.add(precioTotalDetalle);

            // Guardar el producto actualizado
            productoRepository.save(producto);
        }

        // Asignar el precio total calculado a la compra
        compra.setPrecioTotal(precioTotalCompra);

        // Guardar la compra con los detalles actualizados
        return compraRepository.save(compra);
    }

    /**
     * Elimina una compra por su ID.
     * 
     * @param id el identificador único de la compra
     * @return la compra eliminada
     * @throws RecursoNoEncontradoException si la compra no es encontrada
     */
    @Override
    public ComprasModel deleteCompra(ObjectId id) {
        ComprasModel compra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada"));

        // Reintegrar las cantidades al stock
        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            for (StockModel stock : producto.getStock()) {
                boolean tallaCoincide = stock.getTalla().equals(detalle.getTalla());
                boolean colorCoincide = stock.getColor().equals(detalle.getColor());

                if (tallaCoincide && colorCoincide) {
                    stock.setCantidad(stock.getCantidad() + Math.toIntExact(detalle.getCantidad()));
                    break;
                }
            }

            // Guardar el producto actualizado
            productoRepository.save(producto);
        }

        // Eliminar la compra
        compraRepository.deleteById(id);

        // Devolver la compra eliminada (opcional)
        return compra;
    }

    /**
     * Actualiza una compra existente por su ID.
     * 
     * @param id el identificador único de la compra a actualizar
     * @param compra la compra con los nuevos datos
     * @return la compra actualizada
     * @throws RecursoNoEncontradoException si la compra o los recursos asociados no son encontrados
     * @throws CompraActivaException si se intenta actualizar una compra activa
     * @throws StockInsuficienteException si el stock de un producto es insuficiente
     * @throws ValorInvalidoException si el formato del número de tarjeta es inválido o la fecha de vencimiento es en el pasado
     */
    @Override
    public ComprasModel updateCompra(ObjectId id, ComprasModel compra) {
        final ComprasModel existingCompra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada con ID: " + id));

        if (existingCompra.isCompraActiva()) {
            throw new CompraActivaException("No se puede actualizar una compra activa.");
        }
        
        // Permitir la actualización solo si se está estableciendo compraActiva a true
        if (compra.isCompraActiva()) {
            existingCompra.setCompraActiva(true);
        } else {
            for (DetallesCompraModel nuevoDetalle : compra.getDetallesCompra()) {
                for (DetallesCompraModel detalleOriginal : existingCompra.getDetallesCompra()) {
                    if (nuevoDetalle.getIdTipo().equals(detalleOriginal.getIdTipo()) &&
                        nuevoDetalle.getTalla().equals(detalleOriginal.getTalla()) &&
                        nuevoDetalle.getColor().equals(detalleOriginal.getColor())) {

                        int diferenciaCantidad = (int) (nuevoDetalle.getCantidad() - detalleOriginal.getCantidad());

                        // Solo actualizar el stock si hay cambios en la cantidad
                        if (diferenciaCantidad != 0) {
                            ProductoModel producto = productoRepository.findById(nuevoDetalle.getIdTipo())
                                    .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID: " + nuevoDetalle.getIdTipo() + " no encontrado"));
                            
                            // Actualizar el stock si la imagen existe
                            for (StockModel stock : producto.getStock()) {
                                if (stock.getTalla().equals(nuevoDetalle.getTalla()) &&
                                    stock.getColor().equals(nuevoDetalle.getColor())) {
                                    stock.setCantidad(stock.getCantidad() - diferenciaCantidad);

                                    // Verificar si la cantidad es válida después del cambio
                                    if (stock.getCantidad() < 0) {
                                        throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + nuevoDetalle.getIdTipo());
                                    }
                                    break;
                                }
                            }

                            productoRepository.save(producto);
                        } else {
                            // Actualizar la cantidad del detalle original en la compra
                            detalleOriginal.setCantidad(nuevoDetalle.getCantidad());
                        }

                        // Salir del bucle de detalles originales
                        break;
                    }
                }
            }
        }

        if (!usuarioRepository.existsById(compra.getIdUsuario())) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
        }

        if (compra.getTarjeta().getFechaVencimiento().before(new Date())) {
            throw new ValorInvalidoException("La fecha de vencimiento de la tarjeta no puede ser en el pasado: " + compra.getTarjeta().getFechaVencimiento());
        }

        String numeroTarjetaStr = String.valueOf(compra.getTarjeta().getNumero());
        if (!numeroTarjetaStr.matches("^\\d{16}$")) {
            throw new ValorInvalidoException("Formato inválido para el número de tarjeta: " + compra.getTarjeta().getNumero());
        }

        Set<Integer> codigosPostales = new HashSet<>();
        departamentosRepository.findAll().forEach(departamento ->
                departamento.getCiudades().forEach(ciudad -> codigosPostales.add((int) ciudad.getCodigoPostal()))
        );

        if (!codigosPostales.contains((int) compra.getDestinatario().getCodigoPostalCiudad())) {
            throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
        }

        Set<String> imagenes = new HashSet<>();

        // Obtener las imágenes personalizadas de todos los productos
        productoRepository.findAll().forEach(producto -> producto.getImagenes().forEach(imagen -> imagenes.add(imagen.getImagen())));

        // Iterar sobre los detalles de la compra
        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            if (!imagenes.contains(detalle.getImagenPersonalizada())) {
                throw new RecursoNoEncontradoException("Imagen no encontrada: " + detalle.getImagenPersonalizada());
            }
        }

        // Actualizar otros campos de la compra si se proporcionan
        if (compra.getIdUsuario() != null) {
            existingCompra.setIdUsuario(compra.getIdUsuario());
        }
        if (compra.getTarjeta() != null) {
            existingCompra.setTarjeta(compra.getTarjeta());
        }
        if (compra.getEstado() != null) {
            existingCompra.setEstado(compra.getEstado());
        }
        if (compra.getDescripcion() != null) {
            existingCompra.setDescripcion(compra.getDescripcion());
        }
        existingCompra.setFechaCompra(new Date());
        if (compra.getDestinatario() != null) {
            existingCompra.setDestinatario(compra.getDestinatario());
        }

        if (compra.getDetallesCompra() != null && !compra.getDetallesCompra().isEmpty()) {
            existingCompra.setDetallesCompra(compra.getDetallesCompra());
        }

        // Calcular el precio total de la compra actualizada
        BigDecimal precioTotalCompra = BigDecimal.ZERO;
        for (DetallesCompraModel detalle : existingCompra.getDetallesCompra()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            BigDecimal precioProducto = producto.getPrecio();
            Date now = new Date();
            for (PromocionesModel promocion : promocionesRepository.findAll()) {
                for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                    if (productoPromocion.getIdProducto().equals(detalle.getIdTipo()) &&
                        now.after(productoPromocion.getFechaInicio()) && now.before(productoPromocion.getFechaFin())) {
                        BigDecimal descuento = precioProducto.multiply(BigDecimal.valueOf(productoPromocion.getDescuento() / 100.0));
                        precioProducto = precioProducto.subtract(descuento);
                        break;
                    }
                }
            }

            BigDecimal precioTotalDetalle = precioProducto.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            precioTotalCompra = precioTotalCompra.add(precioTotalDetalle);
        }
        existingCompra.setPrecioTotal(precioTotalCompra);

        return compraRepository.save(existingCompra);
    }
}
