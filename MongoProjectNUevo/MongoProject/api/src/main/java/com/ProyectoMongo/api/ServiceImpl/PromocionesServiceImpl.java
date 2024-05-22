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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

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
            Optional<ProductoModel> producto = productoRepository.findById(productoPromocion.getIdProducto());
            if (!producto.isPresent()) {
                throw new RecursoNoEncontradoException("Producto con ID: " + productoPromocion.getIdProducto() + " no encontrado.");
            }
        }
        
        // Validar unicidad de los nombres de promociones dentro de la lista
        Set<String> nombresPromociones = new HashSet<>();
        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            if (!nombresPromociones.add(productoPromocion.getNombre())) {
                throw new RecursoYaExistenteException("La promoción \"" + productoPromocion.getNombre() + "\" ya existe.");
            }

            Date now = new Date(); //Día actual

            // Validar las fechas de la promoción
            if (productoPromocion.getFechaInicio().after(productoPromocion.getFechaFin())) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }
            //if (productoPromocion.getFechaInicio().compareTo(now) < 0) {
            if (productoPromocion.getFechaInicio().before(now)) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser anterior a hoy.");
            }
            //productoPromocion.setActiva(now.after(productoPromocion.getFechaInicio()) && now.before(productoPromocion.getFechaFin()));
        }
    
        return promocionesRepository.save(promocion);
    }

    @Override
    public PromocionesModel deletePromo(ObjectId id) {
        Optional<PromocionesModel> promocion = promocionesRepository.findById(id);
        if (promocion.isPresent()) {
            promocionesRepository.deleteById(id);
            return promocion.get();
        }
        throw new RecursoNoEncontradoException("Promoción con ID: " + id + " no encontrada");
    }

    @Override
    public PromocionesModel updatePromo(ObjectId id, PromocionesModel promocion) {
        PromocionesModel existingPromocion = promocionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promoción no encontrada con ID: " + id));
    
        if (promocion.getProductoPromocion() != null && !promocion.getProductoPromocion().isEmpty()) {
            Set<String> nombresPromociones = new HashSet<>();
            for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
                if (!nombresPromociones.add(productoPromocion.getNombre())) {
                    throw new RecursoYaExistenteException("La promoción \"" + productoPromocion.getNombre() + "\" ya existe.");
                }
    
                if (productoPromocion.getFechaInicio().after(productoPromocion.getFechaFin())) {
                    throw new ValorInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
                }
            }
    
            existingPromocion.setProductoPromocion(promocion.getProductoPromocion());
        }
    
        return promocionesRepository.save(existingPromocion);
    }
}