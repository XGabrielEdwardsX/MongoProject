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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
* DepartamentosServiceImpl es la implementación del servicio de departamentos.
* Este servicio maneja todas las operaciones relacionadas con los departamentos,
* incluyendo la creación, lectura, actualización y eliminación de departamentos.
* @see IDepartamentosService
*/
@Service
public class DepartamentosServiceImpl implements IDepartamentosService {

    @Autowired
    IDepartamentosRepository departamentosRepository;

    /**
    * Obtener todos los departamentos.
    * @return Lista de todos los departamentos.
    */
    @Override
    public List<DepartamentosModel> findAllDepartamentos() {
        return departamentosRepository.findAll();
    }

    /**
     * Obtener un departamento por su ID.
    * @param id ID del departamento.
    * @return El departamento correspondiente al ID proporcionado.
    * @throws RecursoNoEncontradoException si el departamento no es encontrado.
    */
    @Override
    public DepartamentosModel findDepartamentoById(ObjectId id) {
        return departamentosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));
    }
    
    /**
    * Guardar un nuevo departamento.
    * @param departamento Objeto del departamento a guardar.
    * @return El departamento guardado.
    * @throws RecursoYaExistenteException si el departamento o alguna ciudad dentro del departamento ya existen.
    */
    @Override
    public DepartamentosModel saveDepartamento(DepartamentosModel departamento) {

        // Verifica si el departamento ya existe por nombre
        if (departamentosRepository.existsByNombre(departamento.getNombre())) {
            throw new RecursoYaExistenteException("El departamento " + departamento.getNombre() + " ya existe.");
        }
    
        Set<String> nombresCiudades = new HashSet<>();
        Set<Integer> codigosPostales = new HashSet<>();

        // Verifica si las ciudades dentro del departamento son únicas y si sus códigos postales son únicos
        for (CiudadesModel ciudad : departamento.getCiudades()) {
            String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
            ciudad.setNombre(nombreNormalizado);
    
            if (!nombresCiudades.add(ciudad.getNombre())) {
                throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " está duplicada en el departamento.");
            }
    
            if (!codigosPostales.add(ciudad.getCodigoPostal())) {
                throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " está duplicado en el departamento.");
            }
    
            if (departamentosRepository.existsByCiudadesNombre(ciudad.getNombre())) {
                throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
            }

            if (departamentosRepository.existsByCiudadesCodigoPostal(ciudad.getCodigoPostal())) {
                throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento.");
            }

        }
    
        return departamentosRepository.save(departamento);
    }

    /**
    * Eliminar un departamento por su ID.
    * @param id ID del departamento a eliminar.
    * @return El departamento eliminado.
    * @throws RecursoNoEncontradoException si el departamento no es encontrado.
    */
    @Override
    public DepartamentosModel deleteDepartamento(ObjectId id) {
        Optional<DepartamentosModel> departamento = departamentosRepository.findById(id);
        if (departamento.isPresent()) {
            departamentosRepository.deleteById(id);
            return departamento.get();  
        }
        throw new RecursoNoEncontradoException("departamento con ID: " + id + " No encontrado");
    } 

    /**
    * Actualizar un departamento existente.
    * @param id ID del departamento a actualizar.
    * @param departamento Objeto del departamento con los datos actualizados.
    * @return El departamento actualizado.
    * @throws RecursoNoEncontradoException si el departamento no es encontrado.
    * @throws RecursoYaExistenteException si alguna ciudad dentro del departamento ya existe en otro departamento.
    */
    @Override
    public DepartamentosModel updateDepartamento(ObjectId id, DepartamentosModel departamento) {
        DepartamentosModel existingDepartamento = departamentosRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));

<<<<<<< Updated upstream
        if (departamento.getNombre() != null) {
            existingDepartamento.setNombre(departamento.getNombre());
=======
<<<<<<< HEAD
    if (departamento.getNombre() != null) {
        existingDepartamento.setNombre(departamento.getNombre());
    }

    if (departamento.getCiudades() != null && !departamento.getCiudades().isEmpty()) {
        // Utilizar un Set para rastrear ciudades existentes por código postal
        Set<Integer> codigosPostalesExistentes = existingDepartamento.getCiudades().stream()
                .map(CiudadesModel::getCodigoPostal)
                .collect(Collectors.toSet());

        List<CiudadesModel> ciudadesActualizadas = existingDepartamento.getCiudades();
        // List<CiudadesModel> ciudadesActualizadas = new ArrayList<>();

        for (CiudadesModel ciudad : departamento.getCiudades()) {
            String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
            ciudad.setNombre(nombreNormalizado);

            // Verificar si el nombre de la ciudad ya existe en otro departamento
            if (departamentosRepository.existsByCiudadesNombre(ciudad.getNombre())
            ) {
            throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
            }

            // Verificar si el código postal ya existe en OTRO departamento o en el mismo departamento pero en otra ciudad
            // if (departamentosRepository.existsByCiudadesCodigoPostalAndIdNot(ciudad.getCodigoPostal(), id) ||
            //         (codigosPostalesExistentes.contains(ciudad.getCodigoPostal()) &&
            //                 !existingDepartamento.getCiudades().stream()
            //                         .filter(c -> c.getCodigoPostal() == ciudad.getCodigoPostal())
            //                         .anyMatch(c -> c.getNombre().equals(ciudad.getNombre())))) {
            //     throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento o en otra ciudad del mismo departamento.");
            // }
            if (
                departamentosRepository.existsByCiudadesCodigoPostal(ciudad.getCodigoPostal()) ||
                (codigosPostalesExistentes.contains(ciudad.getCodigoPostal()))
                ) {
                throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento o en otra ciudad del mismo departamento.");
            }

            // Agregar el nuevo código postal a la lista de existentes
            codigosPostalesExistentes.add(ciudad.getCodigoPostal());

            ciudadesActualizadas.add(ciudad);
=======
        if (departamento.getNombre() != null) {
            existingDepartamento.setNombre(departamento.getNombre());
>>>>>>> b967f0a6647f8c517ec95741685ebd875a374d91
>>>>>>> Stashed changes
        }

        if (departamento.getCiudades() != null && !departamento.getCiudades().isEmpty()) {
            Set<Integer> codigosPostalesExistentes = existingDepartamento.getCiudades().stream()
                    .map(CiudadesModel::getCodigoPostal)
                    .collect(Collectors.toSet());

            List<CiudadesModel> ciudadesActualizadas = existingDepartamento.getCiudades();

            // Verifica si las ciudades dentro del departamento son únicas y si sus códigos postales son únicos
            for (CiudadesModel ciudad : departamento.getCiudades()) {
                String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
                ciudad.setNombre(nombreNormalizado);

                if (departamentosRepository.existsByCiudadesNombre(ciudad.getNombre())) {
                    throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
                }

                if (
                    departamentosRepository.existsByCiudadesCodigoPostal(ciudad.getCodigoPostal()) ||
                    (codigosPostalesExistentes.contains(ciudad.getCodigoPostal()))
                    ) {
                    throw new RecursoYaExistenteException("El código postal " + ciudad.getCodigoPostal() + " ya existe en otro departamento o en otra ciudad del mismo departamento.");
                }

                
                codigosPostalesExistentes.add(ciudad.getCodigoPostal());

                ciudadesActualizadas.add(ciudad);
            }

            existingDepartamento.setCiudades(ciudadesActualizadas);
        }

        return departamentosRepository.save(existingDepartamento);
    }
}
