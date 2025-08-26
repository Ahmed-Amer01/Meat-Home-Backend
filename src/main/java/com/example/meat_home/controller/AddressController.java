package com.example.meat_home.controller;

import com.example.meat_home.dto.Address.AddressDto;
import com.example.meat_home.dto.Address.CreateAddressDto;
import com.example.meat_home.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /** Get all addresses */
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAddresses());
    }

    /** Get address by ID */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        AddressDto dto = addressService.getAddressById(id);
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    /** Create new address */
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody CreateAddressDto dto) {
        AddressDto created = addressService.createAddress(dto);
        return (created != null) ? ResponseEntity.ok(created) : ResponseEntity.badRequest().build();
    }

    /** Delete address by ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        return (addressService.deleteAddressById(id))
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /** Partial update (PATCH) */
    @PatchMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddressPatch(
            @PathVariable Long id,
            @RequestBody CreateAddressDto dto
    ) {
        AddressDto updated = addressService.updateAddressPatch(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}
