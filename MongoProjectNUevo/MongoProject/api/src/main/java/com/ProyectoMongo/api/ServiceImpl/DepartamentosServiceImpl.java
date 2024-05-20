package com.ProyectoMongo.api.ServiceImpl;

import com.ProyectoMongo.api.Exception.RecursoNoEncontradoException;
import com.ProyectoMongo.api.Exception.RecursoYaExistenteException;

import com.ProyectoMongo.api.Model.CiudadesModel;
import com.ProyectoMongo.api.Model.DepartamentosModel;
import com.ProyectoMongo.api.Repository.IDepartamentosRepository;
import com.ProyectoMongo.api.Service.IDepartamentosService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DepartamentosServiceImpl implements IDepartamentosService {

    @Autowired
    IDepartamentosRepository departamentosRepository;

    @Override
    public List<DepartamentosModel> findAllDepartamentos() {
        return departamentosRepository.findAll();
    }

    @Override
    public DepartamentosModel findDepartamentoById(ObjectId id) {
        return departamentosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));
    }

    @Override
    public DepartamentosModel saveDepartamento(DepartamentosModel departamento) {
        // Verificar si el departamento ya existe
        if (departamentosRepository.existsByNombre(departamento.getNombre())) {
            throw new RecursoYaExistenteException("El departamento " + departamento.getNombre() + " ya existe.");
        }
    
        Set<String> nombresCiudades = new HashSet<>();
        Set<Integer> codigosPostales = new HashSet<>();
    
        for (CiudadesModel ciudad : departamento.getCiudades()) {
            // Normalizar el nombre de la ciudad
            String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
            ciudad.setNombre(nombreNormalizado);
    
            // Verificar nombres de ciudades duplicados
            if (!nombresCiudades.add(ciudad.getNombre())) {
                throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " está duplicada en el departamento.");
            }
    
            // Verificar códigos postales duplicados
            if (!codigosPostales.add(ciudad.getCodigoPostal())) {
                throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " está duplicado en el departamento.");
            }
    
            // Verificar si la ciudad ya existe en otro departamento
            if (departamentosRepository.existsByCiudadesNombre(ciudad.getNombre())) {
                throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
            }

            if (departamentosRepository.existsByCiudadesCodigoPostal(ciudad.getCodigoPostal())) {
                throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento.");
            }

        }
    
        return departamentosRepository.save(departamento);
    }

    @Override
    public DepartamentosModel deleteDepartamento(ObjectId id) {
        Optional<DepartamentosModel> departamento = departamentosRepository.findById(id);
        if (departamento.isPresent()) {
            departamentosRepository.deleteById(id);
            return departamento.get();  // Return the deleted department
        }
        throw new RecursoNoEncontradoException("departamento con ID: " + id + " No encontrado");
    } 

    @Override
    public DepartamentosModel updateDepartamento(ObjectId id, DepartamentosModel departamento) {
        DepartamentosModel existingDepartamento = departamentosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));
    
        // Actualizar solo los campos presentes en la solicitud
        if (departamento.getNombre() != null) {
            existingDepartamento.setNombre(departamento.getNombre());
        }
    
        if (departamento.getCiudades() != null && !departamento.getCiudades().isEmpty()) {
            Set<String> nombresCiudades = new HashSet<>();
            Set<Integer> codigosPostales = new HashSet<>();
            List<CiudadesModel> ciudadesActualizadas = new ArrayList<>(existingDepartamento.getCiudades());
    
            for (CiudadesModel ciudad : departamento.getCiudades()) {
                String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
                ciudad.setNombre(nombreNormalizado);
    
                if (nombresCiudades.contains(ciudad.getNombre())) {
                    throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en este departamento.");
                }
    
                if (!codigosPostales.add(ciudad.getCodigoPostal())) {
                    throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento.");
                }
    
                if (departamentosRepository.existsByCiudadesNombreAndIdNot(ciudad.getNombre(), id)) {
                    throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
                }
    
                if (departamentosRepository.existsByCiudadesCodigoPostalAndIdNot(ciudad.getCodigoPostal(), id)) {
                    throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento.");
                }
    
                // Añadir la nueva ciudad si no existe
                boolean ciudadExistente = false;
                for (CiudadesModel ciudadExistenteModel : ciudadesActualizadas) {
                    if (ciudadExistenteModel.getNombre().equals(ciudad.getNombre()) &&
                        ciudadExistenteModel.getCodigoPostal() == ciudad.getCodigoPostal()) {
                        ciudadExistente = true;
                        break;
                    }
                }
                if (!ciudadExistente) {
                    ciudadesActualizadas.add(ciudad);
                }
    
                nombresCiudades.add(ciudad.getNombre());
            }
    
            existingDepartamento.setCiudades(ciudadesActualizadas);
        }
    
        return departamentosRepository.save(existingDepartamento);
    }
    
}