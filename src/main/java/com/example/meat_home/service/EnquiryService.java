package com.example.meat_home.service;

import com.example.meat_home.entity.*;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.EnquiryRepository;
import com.example.meat_home.repository.EnquiryResponseRepository;
import com.example.meat_home.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final EnquiryResponseRepository enquiryResponseRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public Enquiry createEnquiry(Long customerId, String message) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")); // 404

        if (message == null || message.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message cannot be empty"); // 400
        }

        Enquiry enquiry = new Enquiry();
        enquiry.setCustomer(customer);
        enquiry.setMessage(message.trim());

        return enquiryRepository.save(enquiry);
    }

    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    public Enquiry getEnquiryById(Long enquiryId) {
        return enquiryRepository.findById(Math.toIntExact(enquiryId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enquiry not found")); // 404
    }

    public EnquiryResponse respondToEnquiry(Long enquiryId, Long staffId, String responseMessage) {
        Enquiry enquiry = enquiryRepository.findById(Math.toIntExact(enquiryId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enquiry not found")); // 404

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found")); // 404

        if (responseMessage == null || responseMessage.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Response message cannot be empty"); // 400
        }

        EnquiryResponse response = EnquiryResponse.builder()
                .enquiry(enquiry)
                .responder(staff)
                .responseMessage(responseMessage.trim())
                .respondedAt(LocalDateTime.now())
                .build();

        enquiry.getResponses().add(response);
        enquiry.setStatus(EnquiryStatus.ANSWERED);

        enquiryRepository.save(enquiry);
        return enquiryResponseRepository.save(response);
    }

    public void closeEnquiry(Long enquiryId) {
        Enquiry enquiry = enquiryRepository.findById(Math.toIntExact(enquiryId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enquiry not found")); // 404

        if (enquiry.getStatus() == EnquiryStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enquiry is already closed"); // 400
        }

        enquiry.setStatus(EnquiryStatus.CLOSED);
        enquiryRepository.save(enquiry);
    }
}