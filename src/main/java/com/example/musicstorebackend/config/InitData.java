package com.example.musicstorebackend.config;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.model.Store;
import com.example.musicstorebackend.repository.AlbumRepository;
import com.example.musicstorebackend.repository.CustomerRepository;
import com.example.musicstorebackend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // --- Stores (3 butikker) ---
        Store store1 = new Store("Vinyl House", "Main St 1", "Copenhagen", "1000");
        Store store2 = new Store("Music World", "Baker St 22", "Aarhus", "8000");
        Store store3 = new Store("Retro Records", "Vinylvej 3", "Odense", "5000");
        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);

        // --- Albums (5 albums, hvert album tilhører en butik) ---
        Album album1 = new Album("Dark Side of the Moon", "Rock", "Pink Floyd", true);
        album1.setStore(store1); // Album1 ligger i Vinyl House

        Album album2 = new Album("Thriller", "Pop", "Michael Jackson", true);
        album2.setStore(store2); // Album2 ligger i Music World

        Album album3 = new Album("Back in Black", "Rock", "AC/DC", false);
        album3.setStore(store1); // Album3 ligger også i Vinyl House

        Album album4 = new Album("Abbey Road", "Rock", "The Beatles", true);
        album4.setStore(store3); // Album4 ligger i Retro Records

        Album album5 = new Album("Kind of Blue", "Jazz", "Miles Davis", false);
        album5.setStore(store2); // Album5 ligger i Music World

        Album album6 = new Album("khadija world", "Punjabi", "chuci", false);

        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);
        albumRepository.save(album4);
        albumRepository.save(album5);
        albumRepository.save(album6);

        // --- Customers (3 kunder, hver reserverer flere albums) ---
        Customer cust1 = new Customer("Alice Johnson", "alice@example.com", "12345678");
        cust1.getReservations().add(album1); // Alice reserverer Dark Side of the Moon
        cust1.getReservations().add(album2); // Alice reserverer også Thriller

        Customer cust2 = new Customer("Bob Smith", "bob@example.com", "87654321");
        cust2.getReservations().add(album3); // Bob reserverer Back in Black
        cust2.getReservations().add(album4); // Bob reserverer også Abbey Road

        Customer cust3 = new Customer("Charlie Brown", "charlie@example.com", "11223344");
        cust3.getReservations().add(album2); // Charlie reserverer Thriller
        cust3.getReservations().add(album4); // Charlie reserverer Abbey Road
        cust3.getReservations().add(album5); // Charlie reserverer også Kind of Blue

        customerRepository.save(cust1);
        customerRepository.save(cust2);
        customerRepository.save(cust3);
    }
}
