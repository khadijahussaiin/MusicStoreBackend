package com.example.musicstorebackend.controller;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.Service.CustomerService;
import com.example.musicstorebackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    // Hent alle kunder
    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    // 1: Kunde reserverer et album
    @PostMapping("/{customerId}/reserve/{albumId}")
    public ResponseEntity<Customer> reserveAlbum(@PathVariable Long customerId, @PathVariable Long albumId) {
        return ResponseEntity.ok(customerService.reserveAlbum(customerId, albumId));
    }

    // 2: Kunde afmelder et album
    @DeleteMapping("/{customerId}/cancel/{albumId}")
    public ResponseEntity<Customer> cancelReservation(@PathVariable Long customerId, @PathVariable Long albumId) {
        return ResponseEntity.ok(customerService.cancelReservation(customerId, albumId));
    }

    // 3: Se alle kundens reservationer
    @GetMapping("/reservations/{customerId}")
    public ResponseEntity<List<Album>> getCustomerReservations(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerReservations(customerId));
    }
}
