package com.example.meat_home.service;

import com.example.meat_home.dto.Setting.CreateSettingRequest;
import com.example.meat_home.dto.Setting.SettingDto;
import com.example.meat_home.dto.Setting.SettingResponse;
import com.example.meat_home.entity.Setting;
import com.example.meat_home.repository.SettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingService {

    private final SettingRepository repository;

    public SettingService(SettingRepository repository) {
        this.repository = repository;
    }

    // --- Create first Setting ---
    @Transactional
    public SettingResponse createSetting(CreateSettingRequest request) {
        if (repository.count() > 0) {
            throw new IllegalStateException("Settings already exist. Use update instead.");
        }

        Setting setting = new Setting();
        setting.setId(1L); // enforce single-row
        setting.setName(request.getName());
        setting.setLogoUrl(request.getLogoUrl());
        setting.setAboutImageUrl(request.getAboutImageUrl());
        setting.setAboutDescription(request.getAboutDescription());
        setting.setTermsAndConditions(request.getTermsAndConditions());
        setting.setFacebookUrl(request.getFacebookUrl());
        setting.setWhatsappNumber(request.getWhatsappNumber());
        setting.setPhoneNumber(request.getPhoneNumber());
        setting.setSecondPhoneNumber(request.getSecondPhoneNumber());

        return toResponse(repository.save(setting));
    }

    // --- Update existing Setting (partial) ---
    @Transactional
    public SettingResponse updateSetting(SettingDto request) {
        Setting setting = repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        if (request.getName() != null) setting.setName(request.getName());
        if (request.getLogoUrl() != null) setting.setLogoUrl(request.getLogoUrl());
        if (request.getAboutImageUrl() != null) setting.setAboutImageUrl(request.getAboutImageUrl());
        if (request.getAboutDescription() != null) setting.setAboutDescription(request.getAboutDescription());
        if (request.getTermsAndConditions() != null) setting.setTermsAndConditions(request.getTermsAndConditions());
        if (request.getFacebookUrl() != null) setting.setFacebookUrl(request.getFacebookUrl());
        if (request.getWhatsappNumber() != null) setting.setWhatsappNumber(request.getWhatsappNumber());
        if (request.getPhoneNumber() != null) setting.setPhoneNumber(request.getPhoneNumber());
        if (request.getSecondPhoneNumber() != null) setting.setSecondPhoneNumber(request.getSecondPhoneNumber());

        return toResponse(repository.save(setting));
    }

    // --- Get Setting ---
    public SettingResponse getSetting() {
        return toResponse(repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Settings not found")));
    }

    // --- Helper (Entity -> DTO) ---
    private SettingResponse toResponse(Setting setting) {
        SettingResponse dto = new SettingResponse();
        dto.setName(setting.getName());
        dto.setLogoUrl(setting.getLogoUrl());
        dto.setAboutImageUrl(setting.getAboutImageUrl());
        dto.setAboutDescription(setting.getAboutDescription());
        dto.setTermsAndConditions(setting.getTermsAndConditions());
        dto.setFacebookUrl(setting.getFacebookUrl());
        dto.setWhatsappNumber(setting.getWhatsappNumber());
        dto.setPhoneNumber(setting.getPhoneNumber());
        dto.setSecondPhoneNumber(setting.getSecondPhoneNumber());
        return dto;
    }
}
