package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.ServiceeDTO;
import com.banquito.corecobros.companydoc.model.Servicee;
import com.banquito.corecobros.companydoc.repository.ServiceeRepository;
import com.banquito.corecobros.companydoc.util.mapper.ServiceeMapper;
import com.banquito.corecobros.companydoc.util.uniqueId.UniqueIdGeneration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServiceeService {

    private final ServiceeRepository serviceeRepository;
    private final ServiceeMapper mapper;

    public ServiceeService(ServiceeRepository serviceeRepository, ServiceeMapper mapper) {
        this.serviceeRepository = serviceeRepository;
        this.mapper = mapper;
    }

    public List<ServiceeDTO> obtainAllServices() {
        log.info("Va a retornar todos los servicios");
        List<Servicee> services = this.serviceeRepository.findAll();
        return services.stream().map(s -> this.mapper.toDTO(s))
                .collect(Collectors.toList());
    }

    public ServiceeDTO getServiceByUniqueId(String uniqueId) {
        log.info("Va a buscar el servicio con UniqueId: {}", uniqueId);
        Servicee service = this.serviceeRepository.findByUniqueId(uniqueId);
        if (service != null) {
            return this.mapper.toDTO(service);
        } else {
            throw new RuntimeException("No se encontró el servicio con UniqueId: " + uniqueId);
        }
    }

    public List<ServiceeDTO> getServicesByName(String name) {
        log.info("Va a buscar los servicios con nombre: {}", name);
        List<Servicee> services = this.serviceeRepository.findByName(name);
        return services.stream().map(s -> this.mapper.toDTO(s))
                .collect(Collectors.toList());
    }

    public ServiceeDTO create(ServiceeDTO dto) {

        UniqueIdGeneration uniqueIdGenerator = new UniqueIdGeneration();
        String uniqueId;
        boolean uniqueIdExists;

        do {
            uniqueId = uniqueIdGenerator.getUniqueId();
            uniqueIdExists = serviceeRepository.findByUniqueId(uniqueId) != null;
        } while (uniqueIdExists);

        log.info("Va a registrar un servicio: {}", dto);
        Servicee service = mapper.toPersistence(dto);
        service.setUniqueId(uniqueId);
        log.info("Servicio a registrar: {}", service);
        service = this.serviceeRepository.save(service);
        log.info("Se creó el servicio: {}", service);
        return this.mapper.toDTO(service);
    }

    public void updateService(String uniqueId, ServiceeDTO dto) {
        log.info("Va a actualizar el servicio con ID: {}", uniqueId);
        Servicee service = mapper.toPersistence(dto);
        service.setId(uniqueId);
        service = this.serviceeRepository.save(service);
        log.info("Se actualizó el servicio: {}", service);
    }
}
