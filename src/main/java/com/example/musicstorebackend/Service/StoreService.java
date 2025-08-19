package com.example.musicstorebackend.Service;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.model.Store;
import com.example.musicstorebackend.repository.AlbumRepository;
import com.example.musicstorebackend.repository.CustomerRepository;
import com.example.musicstorebackend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // 1: Se butiks detaljer inkl. albums
    public Store getStoreDetails(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id " + storeId));
    }

    // 2: Liste over kundens reserverede albums, der er tilgængelige
    public List<Album> getAvailableReservations(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));

        return customer.getReservations()
                .stream()
                .filter(Album::isAvailable) // kun albums der er på lager
                .collect(Collectors.toList());
    }
}