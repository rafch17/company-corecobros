package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.corecobros.companydoc.dto.ServiceeDTO;
import com.banquito.corecobros.companydoc.service.ServiceeService;

@RestController
@RequestMapping("/api/v1/servicee")
public class ServiceeController {

    private final ServiceeService serService;

    public ServiceeController(ServiceeService serService) {
        this.serService = serService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceeDTO>> getAllServices() {
        return ResponseEntity.ok(this.serService.obtainAllServices());
    }

    @GetMapping("/unique/{uniqueID}")
    public ResponseEntity<ServiceeDTO> getServiceByUniqueID(@PathVariable String uniqueID) {
        ServiceeDTO serviceDTO = this.serService.getServiceByUniqueID(uniqueID);
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

    @PostMapping("/create")
    public ResponseEntity<Void> createService(@RequestBody ServiceeDTO dto) {
        this.serService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{uniqueID}")
    public ResponseEntity<Void> updateService(@PathVariable String uniqueID, @RequestBody ServiceeDTO dto) {
        this.serService.updateService(uniqueID, dto);
        return ResponseEntity.ok().build();
    }

}
