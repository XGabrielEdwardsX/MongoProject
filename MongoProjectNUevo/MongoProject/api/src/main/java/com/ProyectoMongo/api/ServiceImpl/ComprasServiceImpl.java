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

    @Override
    public List<ComprasModel> findAllCompras() {
        return compraRepository.findAll();
    }

    @Override
    public ComprasModel findCompraById(ObjectId id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada"));
    }

    @Override
    public ComprasModel saveCompra(ComprasModel compra) {
        if (compra.getId() == null) {
            compra.setId(new ObjectId());
        }

        if (!usuarioRepository.existsById(compra.getIdUsuario())) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
        }

        compra.setFechaCompra(new Date());

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
        productoRepository.findAll().forEach(producto -> {
            if (producto.getImagenes() != null) {
                producto.getImagenes().forEach(imagen -> imagenes.add(imagen.getImagen()));
            }
        });

        BigDecimal precioTotalCompra = BigDecimal.ZERO;

        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            if (detalle.getImagenPersonalizada() != null && !imagenes.contains(detalle.getImagenPersonalizada())) {
                throw new RecursoNoEncontradoException("Imagen no encontrada: " + detalle.getImagenPersonalizada());
            }

            if (detalle.getCantidad() < 0) {
                throw new ValorInvalidoException("La cantidad no puede ser negativa");
            }

            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            boolean stockSuficiente = false;
            for (StockModel stock : producto.getStock()) {
                if (stock.getTalla().equals(detalle.getTalla()) && stock.getColor().equals(detalle.getColor()) && stock.getCantidad() >= detalle.getCantidad()) {
                    stockSuficiente = true;
                    stock.setCantidad((int) (stock.getCantidad() - detalle.getCantidad()));
                    break;
                }
            }

            if (!stockSuficiente) {
                throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + detalle.getIdTipo());
            }

            BigDecimal precioProducto = producto.getPrecio();
            if (precioProducto.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValorInvalidoException("El precio no puede ser negativo");
            }

            BigDecimal precioTotalDetalle = precioProducto.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setPrecio(precioTotalDetalle);
            precioTotalCompra = precioTotalCompra.add(precioTotalDetalle);
            productoRepository.save(producto);
        }

        compra.setPrecioTotal(precioTotalCompra);
        return compraRepository.save(compra);
    }

    @Override
    public ComprasModel deleteCompra(ObjectId id) {
        ComprasModel compra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada"));

        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            for (StockModel stock : producto.getStock()) {
                if (stock.getTalla().equals(detalle.getTalla()) && stock.getColor().equals(detalle.getColor())) {
                    stock.setCantidad((int) (stock.getCantidad() + detalle.getCantidad()));
                    break;
                }
            }

            productoRepository.save(producto);
        }

        compraRepository.deleteById(id);
        return compra;
    }

    @Override
    public ComprasModel updateCompra(ObjectId id, ComprasModel compra) {
        ComprasModel existingCompra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada con ID: " + id));

        if (existingCompra.isCompraActiva()) {
            throw new CompraActivaException("No se puede actualizar una compra activa.");
        }

        if (compra.isCompraActiva()) {
            existingCompra.setCompraActiva(true);
        } else {
            for (DetallesCompraModel nuevoDetalle : compra.getDetallesCompra()) {
                for (DetallesCompraModel detalleOriginal : existingCompra.getDetallesCompra()) {
                    if (nuevoDetalle.getIdTipo().equals(detalleOriginal.getIdTipo()) &&
                        nuevoDetalle.getTalla().equals(detalleOriginal.getTalla()) &&
                        nuevoDetalle.getColor().equals(detalleOriginal.getColor())) {

                        int diferenciaCantidad = (int) (nuevoDetalle.getCantidad() - detalleOriginal.getCantidad());

                        if (diferenciaCantidad != 0) {
                            ProductoModel producto = productoRepository.findById(nuevoDetalle.getIdTipo())
                                    .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID: " + nuevoDetalle.getIdTipo() + " no encontrado"));

                            for (StockModel stock : producto.getStock()) {
                                if (stock.getTalla().equals(nuevoDetalle.getTalla()) && stock.getColor().equals(nuevoDetalle.getColor())) {
                                    stock.setCantidad(stock.getCantidad() - diferenciaCantidad);

                                    if (stock.getCantidad() < 0) {
                                        throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + nuevoDetalle.getIdTipo());
                                    }
                                    break;
                                }
                            }

                            productoRepository.save(producto);
                        } else {
                            detalleOriginal.setCantidad(nuevoDetalle.getCantidad());
                        }
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
        productoRepository.findAll().forEach(producto -> producto.getImagenes().forEach(imagen -> imagenes.add(imagen.getImagen())));

        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            if (detalle.getImagenPersonalizada() != null && !imagenes.contains(detalle.getImagenPersonalizada())) {
                throw new RecursoNoEncontradoException("Imagen no encontrada: " + detalle.getImagenPersonalizada());
            }

            if (detalle.getCantidad() < 0) {
                throw new ValorInvalidoException("La cantidad no puede ser negativa");
            }
        }

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

        BigDecimal precioTotalCompra = BigDecimal.ZERO;
        for (DetallesCompraModel detalle : existingCompra.getDetallesCompra()) {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));

            BigDecimal precioProducto = producto.getPrecio();
            if (precioProducto.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValorInvalidoException("El precio no puede ser negativo");
            }

            BigDecimal precioTotalDetalle = precioProducto.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            precioTotalCompra = precioTotalCompra.add(precioTotalDetalle);
        }
        existingCompra.setPrecioTotal(precioTotalCompra);

        return compraRepository.save(existingCompra);
    }
}
