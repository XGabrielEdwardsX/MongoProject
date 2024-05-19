package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Enum.Tarjeta;
import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.StockInsuficienteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ComprasModel;
import com.ProyectoMongo.api.Model.DetallesCompraModel;
import com.ProyectoMongo.api.Model.ProductoModel;
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

        for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
            if (!productoRepository.existsById(detalle.getIdTipo())) {
                throw new RecursoNoEncontradoException("Producto con ID: " + detalle.getIdTipo() + " no encontrado");
            }
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

    // Verificar existencia de los productos
        for (DetallesCompraModel detalle : compra.getDetallesCompra())  {
            ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));
            
            // Verificar stock disponible
            boolean stockSuficiente = producto.getStock().stream().anyMatch(stock ->
                stock.getTalla().equals(detalle.getTalla()) &&
                stock.getColor().equals(detalle.getColor()) &&
                stock.getCantidad() >= detalle.getCantidad()
            );

            if (!stockSuficiente) {
                throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + detalle.getIdTipo());
            }
        }

        return compraRepository.save(compra);
    }

    @Override
    public ComprasModel deleteCompra(ObjectId id) {
        Optional<ComprasModel> compra = compraRepository.findById(id);
        if (compra.isPresent()) {
            compraRepository.deleteById(id);
            return compra.get();
        }
        throw new RecursoNoEncontradoException("Compra con ID: " + id + " no encontrada");
    }

    @Override
    public ComprasModel updateCompra(ObjectId id, ComprasModel compra) {
    ComprasModel existingCompra = compraRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada con ID: " + id));
    
        // Verificar existencia del usuario
        if (!usuarioRepository.existsById(compra.getIdUsuario())) {
            throw new RecursoNoEncontradoException("Usuario con ID: " + compra.getIdUsuario() + " no encontrado");
        }

    // Verificar existencia de los productos
    for (DetallesCompraModel detalle : compra.getDetallesCompra()) {
        if (!productoRepository.existsById(detalle.getIdTipo())) {
            throw new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado");
        }
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
    
    if (!codigosPostales.contains((int)compra.getDestinatario().getCodigoPostalCiudad())) {
        throw new RecursoNoEncontradoException("Código postal " + compra.getDestinatario().getCodigoPostalCiudad() + " no encontrado");
    }

    // Verificar existencia de los productos
    for (DetallesCompraModel detalle : compra.getDetallesCompra())  {
        ProductoModel producto = productoRepository.findById(detalle.getIdTipo())
                .orElseThrow(() -> new RecursoNoEncontradoException("El " + detalle.getTipo() + " con ID: " + detalle.getIdTipo() + " no encontrado"));
        
        // Verificar stock disponible
        boolean stockSuficiente = producto.getStock().stream().anyMatch(stock ->
            stock.getTalla().equals(detalle.getTalla()) &&
            stock.getColor().equals(detalle.getColor()) &&
            stock.getCantidad() >= detalle.getCantidad()
        );

        if (!stockSuficiente) {
            throw new StockInsuficienteException("Stock insuficiente para el producto con ID: " + detalle.getIdTipo());
        }
    }
            // Actualizar los campos de la compra existente solo si todas las validaciones han pasado
            existingCompra.setIdUsuario(compra.getIdUsuario());
            existingCompra.setDetallesCompra(compra.getDetallesCompra());
            existingCompra.setTarjeta(compra.getTarjeta());
            existingCompra.setEstado(compra.getEstado());
            existingCompra.setDescripcion(compra.getDescripcion());
            existingCompra.setFechaCompra(new Date());
            existingCompra.setDestinatario(compra.getDestinatario());

        return compraRepository.save(existingCompra);
    }   

}