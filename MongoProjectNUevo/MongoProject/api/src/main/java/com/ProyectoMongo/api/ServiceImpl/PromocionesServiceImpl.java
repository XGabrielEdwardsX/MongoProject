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

/**
 * PromocionesServiceImpl es la implementación del servicio de promociones.
 * Este servicio maneja todas las operaciones relacionadas con las promociones,
 * incluyendo la creación, lectura, actualización y eliminación de promociones.
 * 
 * @see IPromocionesService
 */

@Service
public class PromocionesServiceImpl implements IPromocionesService {

    @Autowired
    IPromocionesRepository promocionesRepository;
    @Autowired
    IProductoRepository productoRepository;

    /**
    * Obtener todas las promociones.
    * @return Lista de todas las promociones.
    */
    @Override
    public List<PromocionesModel> findAllPromos() {
        return promocionesRepository.findAll();
    }

    /**
    * Obtener una promoción por su ID.
    * @param id ID de la promoción.
    * @return La promoción correspondiente al ID proporcionado.
    * @throws RecursoNoEncontradoException si la promoción no es encontrada.
    */
    @Override
    public PromocionesModel findPromoById(ObjectId id) {
        return promocionesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promoción con ID: " + id + " no encontrada"));
    }

    /**
    * Guardar una nueva promoción.
    * @param promocion Objeto de promoción a guardar.
    * @return La promoción guardada.
    * @throws RecursoNoEncontradoException si algún producto asociado a la promoción no es encontrado.
    * @throws RecursoYaExistenteException si alguna promoción con el mismo nombre ya existe.
    * @throws ValorInvalidoException si las fechas de la promoción no son válidas.
    */   
    @Override
    public PromocionesModel savepromo(PromocionesModel promocion) {
        // Verifica si los productos asociados a la promoción existen
        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            Optional<ProductoModel> producto = productoRepository.findById(productoPromocion.getIdProducto());
            if (!producto.isPresent()) {
                throw new RecursoNoEncontradoException("Producto con ID: " + productoPromocion.getIdProducto() + " no encontrado.");
            }
        }
        
        // Verifica si hay promociones duplicadas y valida las fechas
        Set<String> nombresPromociones = new HashSet<>();
        for (ProductoPromocionModel productoPromocion : promocion.getProductoPromocion()) {
            if (!nombresPromociones.add(productoPromocion.getNombre())) {
                throw new RecursoYaExistenteException("La promoción \"" + productoPromocion.getNombre() + "\" ya existe.");
            }

            Date now = new Date(); 

            
            if (productoPromocion.getFechaInicio().after(productoPromocion.getFechaFin())) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }
            
            if (productoPromocion.getFechaInicio().before(now)) {
                throw new ValorInvalidoException("La fecha de inicio no puede ser anterior a hoy.");
            }
           
        }
    
        return promocionesRepository.save(promocion);
    }

    /**
    * Eliminar una promoción por su ID.
    * @param id ID de la promoción a eliminar.
    * @return La promoción eliminada.
    * @throws RecursoNoEncontradoException si la promoción no es encontrada.
    */
    @Override
    public PromocionesModel deletePromo(ObjectId id) {
        Optional<PromocionesModel> promocion = promocionesRepository.findById(id);
        if (promocion.isPresent()) {
            promocionesRepository.deleteById(id);
            return promocion.get();
        }
        throw new RecursoNoEncontradoException("Promoción con ID: " + id + " no encontrada");
    }

    /**
    * Actualizar una promoción existente.
    * @param id ID de la promoción a actualizar.
    * @param promocion Objeto de promoción con los datos actualizados.
    * @return La promoción actualizada.
    * @throws RecursoNoEncontradoException si la promoción no es encontrada.
    * @throws RecursoYaExistenteException si alguna promoción con el mismo nombre ya existe.
    * @throws ValorInvalidoException si las fechas de la promoción no son válidas.
    */
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