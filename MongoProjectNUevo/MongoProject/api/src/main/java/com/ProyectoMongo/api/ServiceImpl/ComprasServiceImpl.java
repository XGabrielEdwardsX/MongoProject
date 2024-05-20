    package com.ProyectoMongo.api.ServiceImpl;

    import com.ProyectoMongo.api.Enum.Tarjeta;
    import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
    import com.ProyectoMongo.api.Exception.StockInsuficienteException;
    import com.ProyectoMongo.api.Exception.ValorInvalidoException;
    import com.ProyectoMongo.api.Model.ComprasModel;
    import com.ProyectoMongo.api.Model.DetallesCompraModel;
    import com.ProyectoMongo.api.Model.ProductoModel;
    import com.ProyectoMongo.api.Model.StockModel;
    import com.ProyectoMongo.api.Repository.IComprasRepository;
    import com.ProyectoMongo.api.Repository.IDepartamentosRepository;
    import com.ProyectoMongo.api.Repository.IProductoRepository;
    import com.ProyectoMongo.api.Repository.IUsuariosRepository;
    import com.ProyectoMongo.api.Service.IComprasService;

import org.bson.types.ObjectId;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.Date;
    import java.util.HashSet;
    import java.util.List;
import java.util.Optional;
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
            if (!usuarioRepository.existsById(compra.getIdUsuario())) {
                throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
            }
            String tipoTarjetaNormalizado = compra.getTarjeta().getTipo().trim().toUpperCase();
                
            try {
                Tarjeta.valueOf(tipoTarjetaNormalizado);
                compra.getTarjeta().setTipo(tipoTarjetaNormalizado);  // Asegurar que el tipo esté normalizado en el modelo
                
            } catch (IllegalArgumentException e) {
                    
                throw new ValorInvalidoException("Valor inválido para la tarjeta: " + compra.getTarjeta().getTipo());
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

        // Lógica número 1 para validar que el código de la ciudad exista
    /*  Set<Integer> codigosPostales = new HashSet<>();

        departamentosRepository.findAll().forEach(departamento -> {
        if (departamento != null && !departamento.getCiudades().isEmpty()) {
            codigosPostales.addAll(departamento.getCiudades().stream()
                .map(CiudadesModel::getCodigoPostal)
                .collect(Collectors.toList()));
        }
    });
        boolean codigoPostalEncontrado = codigosPostales.stream()
        .anyMatch(codigoPostal -> codigoPostal == compra.getDestinatario().getCodigoPostalCiudad());

        if (!codigoPostalEncontrado) {
        throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
        }    */

        Set<Integer> codigosPostales = new HashSet<>();
        departamentosRepository.findAll().forEach(departamento -> 
            departamento.getCiudades().forEach(ciudad -> codigosPostales.add((int) ciudad.getCodigoPostal()))
        );
        
        if (!codigosPostales.contains((int)compra.getDestinatario().getCodigoPostalCiudad())) {
            throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
        }
       // Verificar existencia de los productos y stock disponible
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

            // Subtract detalle.getCantidad from stock.getCantidad
            int newCantidad = (int) (stock.getCantidad() - detalle.getCantidad());

            stock.setCantidad(newCantidad);
            break;
        }
    }

    if (!stockSuficiente) {
        throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + detalle.getIdTipo());
    }

    // Guardar el producto actualizado
    productoRepository.save(producto);
}

return compraRepository.save(compra);
        }

        @Override
        public ComprasModel deleteCompra(ObjectId id) {
            ComprasModel compra = compraRepository.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada"));
        
            // Reintegran las cantidades al stock
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
        
            // Return the deleted purchase (optional)
            return compra; // You can optionally return the deleted purchase
        }

        @Override
        public ComprasModel updateCompra(ObjectId id, ComprasModel compra) {
        ComprasModel existingCompra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada con ID: " + id));
        
            // Verificar existencia del usuario
            if (!usuarioRepository.existsById(compra.getIdUsuario())) {
                throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
            }
        // Normalizar y validar el tipo de tarjeta
            String tipoTarjetaNormalizado = compra.getTarjeta().getTipo().trim().toUpperCase();
            try {
                Tarjeta.valueOf(tipoTarjetaNormalizado);
                compra.getTarjeta().setTipo(tipoTarjetaNormalizado);  // Asegurar que el tipo esté normalizado en el modelo
            } catch (IllegalArgumentException e) {
                throw new ValorInvalidoException("Valor inválido para la tarjeta: " + compra.getTarjeta().getTipo());
            }

            // Validar fecha de expiración de la tarjeta
            if (compra.getTarjeta().getFechaVencimiento().before(new Date())) {
                throw new ValorInvalidoException("La fecha de vencimiento de la tarjeta no puede ser en el pasado: " + compra.getTarjeta().getFechaVencimiento());
            }

            // Validar formato del número de tarjeta
            String numeroTarjetaStr = String.valueOf(compra.getTarjeta().getNumero());
            if (!numeroTarjetaStr.matches("^\\d{16}$")) {
                throw new ValorInvalidoException("La tarjeta debe contener 16 dígitos");
            }

            // Lógica número 2 para validar que el codigoPostal de alguna ciudad exista
        /* Set<Integer> codigosPostales = new HashSet<>();

            departamentosRepository.findAll().forEach(departamento -> {
            if (departamento != null && !departamento.getCiudades().isEmpty()) {
                for (CiudadesModel ciudad : departamento.getCiudades()) {
                    codigosPostales.add(ciudad.getCodigoPostal());
                }
            }
            });
        
            boolean codigoPostalEncontrado = false;
            for (Integer codigoPostalSet : codigosPostales) {
            if (codigoPostalSet == compra.getDestinatario().getCodigoPostalCiudad()) {
                codigoPostalEncontrado = true;
                break;
            }
        }
        
        if (!codigoPostalEncontrado) {
            throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
        }
        */

        // Lógica hemos ido manejando a lo largo del BACKEND
        Set<Integer> codigosPostales = new HashSet<>();
        departamentosRepository.findAll().forEach(departamento -> 
            departamento.getCiudades().forEach(ciudad -> codigosPostales.add((int) ciudad.getCodigoPostal()))
        );
        
        
        // Ajustar stock basado en la diferencia de cantidades antes y después de la actualización
    for (DetallesCompraModel nuevoDetalle : compra.getDetallesCompra()) {
        ProductoModel producto = productoRepository.findById(nuevoDetalle.getIdTipo())
                .orElseThrow(() -> new RecursoNoEncontradoException("El " + nuevoDetalle.getTipo() + " con ID: " + nuevoDetalle.getIdTipo() + " no encontrado"));

        Optional<DetallesCompraModel> detalleExistenteOpt = existingCompra.getDetallesCompra().stream()
                .filter(detalle -> detalle.getIdTipo().equals(nuevoDetalle.getIdTipo()) &&
                                   detalle.getTalla().equals(nuevoDetalle.getTalla()) &&
                                   detalle.getColor().equals(nuevoDetalle.getColor()))
                .findFirst();

            long cantidadAnterior = detalleExistenteOpt.map(DetallesCompraModel::getCantidad).orElse(0L);
            long diferenciaCantidad = nuevoDetalle.getCantidad() - cantidadAnterior;

        boolean stockSuficiente = false;
        for (StockModel stock : producto.getStock()) {
            boolean tallaCoincide = stock.getTalla().equals(nuevoDetalle.getTalla());
            boolean colorCoincide = stock.getColor().equals(nuevoDetalle.getColor());

            if (tallaCoincide && colorCoincide) {
                if (stock.getCantidad() + cantidadAnterior < nuevoDetalle.getCantidad()) {
                    throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + nuevoDetalle.getIdTipo());
                }

                stockSuficiente = true;
                stock.setCantidad(stock.getCantidad() - (int) diferenciaCantidad);
                break;
            }
        }

        if (!stockSuficiente) {
            throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + nuevoDetalle.getIdTipo());
        }

        }
        if (compra.getIdUsuario() != null) {
            existingCompra.setIdUsuario(compra.getIdUsuario());
        }
        if (compra.getDetallesCompra() != null && !compra.getDetallesCompra().isEmpty()) {
            existingCompra.setDetallesCompra(compra.getDetallesCompra());
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

        return compraRepository.save(existingCompra);
    }

    }