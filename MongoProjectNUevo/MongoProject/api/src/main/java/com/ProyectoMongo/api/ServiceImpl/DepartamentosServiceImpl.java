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
import java.util.Set;
import java.util.stream.Collectors;

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
        if (departamentosRepository.existsByNombre(departamento.getNombre())) {
            throw new RecursoYaExistenteException("El departamento " + departamento.getNombre() + " ya existe.");
        }

        Set<String> nombresCiudades = new HashSet<>();
        Set<Integer> codigosPostales = new HashSet<>();

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

    @Override
    public DepartamentosModel deleteDepartamento(ObjectId id) {
        DepartamentosModel departamento = departamentosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));

        departamentosRepository.deleteById(id);
        return departamento;
    }

    @Override
    public DepartamentosModel updateDepartamento(ObjectId id, DepartamentosModel departamento) {
        DepartamentosModel existingDepartamento = departamentosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento con ID: " + id + " no encontrado"));

        if (departamento.getNombre() != null) {
            existingDepartamento.setNombre(departamento.getNombre());
        }

        if (departamento.getCiudades() != null && !departamento.getCiudades().isEmpty()) {
            Set<Integer> codigosPostalesExistentes = existingDepartamento.getCiudades().stream()
                    .map(CiudadesModel::getCodigoPostal)
                    .collect(Collectors.toSet());

            List<CiudadesModel> ciudadesActualizadas = existingDepartamento.getCiudades();

            for (CiudadesModel ciudad : departamento.getCiudades()) {
                String nombreNormalizado = ciudad.getNombre().trim().toLowerCase();
                ciudad.setNombre(nombreNormalizado);

                if (departamentosRepository.existsByCiudadesNombre(ciudad.getNombre())) {
                    throw new RecursoYaExistenteException("La ciudad " + ciudad.getNombre() + " ya existe en otro departamento.");
                }

                if (departamentosRepository.existsByCiudadesCodigoPostal(ciudad.getCodigoPostal()) ||
                    codigosPostalesExistentes.contains(ciudad.getCodigoPostal())) {
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
