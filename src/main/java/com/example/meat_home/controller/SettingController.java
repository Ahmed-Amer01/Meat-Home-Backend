package com.example.meat_home.controller;

import com.example.meat_home.dto.Setting.CreateSettingRequest;
import com.example.meat_home.dto.Setting.SettingDto;
import com.example.meat_home.dto.Setting.SettingResponse;
import com.example.meat_home.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/settings")
public class SettingController {

    private final SettingService service;

    public SettingController(SettingService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SettingResponse createSetting(@Valid @RequestBody CreateSettingRequest request) {
        return service.createSetting(request);
    }

    @GetMapping
    public SettingResponse getSetting() {
        return service.getSetting();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SettingResponse updateSetting(@RequestBody SettingDto dto) {
        return service.updateSetting(dto);
    }
}
