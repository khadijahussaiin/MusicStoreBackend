package com.example.musicstorebackend.controller;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.Service.CustomerService;
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

    // A: Kunde reserverer et album
    @PostMapping("/{customerId}/reserve/{albumId}")
    public ResponseEntity<Customer> reserveAlbum(@PathVariable Long customerId, @PathVariable Long albumId) {
        return ResponseEntity.ok(customerService.reserveAlbum(customerId, albumId));
    }

    // B: Kunde afmelder et album
    @DeleteMapping("/{customerId}/cancel/{albumId}")
    public ResponseEntity<Customer> cancelReservation(@PathVariable Long customerId, @PathVariable Long albumId) {
        return ResponseEntity.ok(customerService.cancelReservation(customerId, albumId));
    }

    // C: Se alle kundens reservationer
    @GetMapping("/{customerId}/reservations")
    public ResponseEntity<List<Album>> getCustomerReservations(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerReservations(customerId));
    }
//-----------------------------------------Husk at slette hvis ikke bruges
    // Bonus: Opret ny kunde
    @PostMapping("/add")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    // Bonus: Se alle kunder
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
