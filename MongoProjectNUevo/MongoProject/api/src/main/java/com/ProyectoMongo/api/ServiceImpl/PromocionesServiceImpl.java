package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;
import com.ProyectoMongo.api.Exception.ValorInvalidoException;
import com.ProyectoMongo.api.Model.ProductoModel;
import com.ProyectoMongo.api.Model.ProductoPromocionModel;
import com.ProyectoMongo.api.Model.PromocionesModel;
import com.ProyectoMongo.api.Repository.IProductoRepository;
import com.ProyectoMongo.api.Repository.IPromocionesRepository;
import com.ProyectoMongo.api.Service.IPromocionesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PromocionesServiceImpl implements IPromocionesService {

    @Autowired
    IPromocionesRepository promocionesRepository;

    @Autowired
    IProductoRepository productoRepository;

    @Override
    public List<PromocionesModel> findAllPromos() {
        return promocionesRepository.findAll();
    }

    @Override
    public PromocionesModel findPromoById(ObjectId id) {
        return promocionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promoción con ID: " + id + " no encontrada"));
    }

    @Override
    public PromocionesModel savepromo(PromocionesModel promocion) {
        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            if (!productoRepository.existsById(productoPromocion.getIdProducto())) {
                throw new RecursoNoEncontradoException("Producto con ID: " + productoPromocion.getIdProducto() + " no encontrado.");
            }
        }

        Set<String> nombresPromociones = new HashSet<>();
        Date now = new Date();

        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            if (!nombresPromociones.add(productoPromocion.getNombre())) {
                throw new RecursoYaExistenteException("La promoción \"" + productoPromocion.getNombre() + "\" ya existe.");
            }

            if (productoPromocion.getFechaInicio().after(productoPromocion.getFechaFin())) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            if (productoPromocion.getFechaInicio().before(now)) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser anterior a hoy.");
            }

            if (productoPromocion.getDescuento() < 0) {
                throw new ValorInvalidoException("El descuento no puede ser negativo.");
            }
        }

        return promocionesRepository.save(promocion);
    }

    @Override
    public PromocionesModel deletePromo(ObjectId id) {
        PromocionesModel promocion = promocionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promoción con ID: " + id + " no encontrada"));

        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            ProductoModel producto = productoRepository.findById(productoPromocion.getIdProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID: " + productoPromocion.getIdProducto() + " no encontrado"));

            if (producto.getPrecioOriginal() != null) {
                producto.setPrecio(producto.getPrecioOriginal());
                producto.setPrecioOriginal(null);
                productoRepository.save(producto);
            }
        }

        promocionesRepository.deleteById(id);
        return promocion;
    }

    @Override
    public PromocionesModel updatePromo(ObjectId id, PromocionesModel promocion) {
        PromocionesModel existingPromocion = promocionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promoción no encontrada con ID: " + id));

        Set<String> nombresPromociones = new HashSet<>();

        if (promocion.getProductoPromocion() != null && !promocion.getProductoPromocion().isEmpty()) {
            for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                if (!nombresPromociones.add(productoPromocion.getNombre())) {
                    throw new RecursoYaExistenteException("La promoción \"" + productoPromocion.getNombre() + "\" ya existe.");
                }

                if (productoPromocion.getFechaInicio().after(productoPromocion.getFechaFin())) {
                    throw new ValorInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
                }

                if (productoPromocion.getDescuento() < 0) {
                    throw new ValorInvalidoException("El descuento no puede ser negativo.");
                }
            }

            existingPromocion.setProductoPromocion(promocion.getProductoPromocion());
        }

        return promocionesRepository.save(existingPromocion);
    }
}
