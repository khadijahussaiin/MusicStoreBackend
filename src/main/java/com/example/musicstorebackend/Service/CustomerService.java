package com.example.musicstorebackend.Service;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.repository.AlbumRepository;
import com.example.musicstorebackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // A: Kunde reserverer et album
    public Customer reserveAlbum(Long customerId, Long albumId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + albumId));

        // Tilføj album til kundens reservationsliste (ManyToMany)
        customer.getReservations().add(album);
        return customerRepository.save(customer);
    }

    // B: Kunde afmelder album
    public Customer cancelReservation(Long customerId, Long albumId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + albumId));

        customer.getReservations().remove(album);
        return customerRepository.save(customer);
    }

    // C: Kunde ser alle sine reservationer
    public List<Album> getCustomerReservations(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));

        return customer.getReservations();
    }
}