package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.corecobros.companydoc.dto.ServiceeDTO;
import com.banquito.corecobros.companydoc.service.ServiceeService;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
    RequestMethod.PUT })
@RestController
@RequestMapping("/api/v1/servicees")
public class ServiceeController {

    private final ServiceeService serService;

    public ServiceeController(ServiceeService serService) {
        this.serService = serService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceeDTO>> getAllServices() {
        return ResponseEntity.ok(this.serService.obtainAllServices());
    }

    @GetMapping("/{uniqueId}")
    public ResponseEntity<ServiceeDTO> getServiceByUniqueId(@PathVariable String uniqueId) {
        ServiceeDTO serviceDTO = this.serService.getServiceByUniqueId(uniqueId);
        if (serviceDTO != null) {
            return ResponseEntity.ok(serviceDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ServiceeDTO>> getServicesByName(@PathVariable String name) {
        List<ServiceeDTO> services = this.serService.getServicesByName(name);
        return ResponseEntity.ok(services);
    }

    @PostMapping("/")
    public ResponseEntity<ServiceeDTO> createService(@RequestBody ServiceeDTO dto) {
        try {
            this.serService.create(dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        
    }

    @PutMapping("/{uniqueId}")
    public ResponseEntity<Void> updateService(@PathVariable String uniqueId, @RequestBody ServiceeDTO dto) {
        this.serService.updateService(uniqueId, dto);
        return ResponseEntity.ok().build();
    }

}
