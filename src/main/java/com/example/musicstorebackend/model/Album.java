package com.example.musicstorebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String artist;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnoreProperties("albums")  // Dette er korrekt
    private Store store;

    @ManyToMany(mappedBy = "reservations")
    @JsonIgnoreProperties("reservations")  // Tilføj denne linje
    private List<Customer> customers = new ArrayList<>();

    // 🔹 Constructors
    public Album() {}

    public Album(String title, String genre, String artist, boolean available) {
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.available = available;
    }

    // 🔹 Getters & Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }
}
