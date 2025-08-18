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

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Opret album + tildel butik
    public Album createAlbum(Album album, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        album.setStore(store);
        return albumRepository.save(album);
    }

    // Hent alle albums
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
    // Opdater album inkl. butik/store
    public Album updateAlbum(Long id, Album updatedAlbum) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        album.setTitle(updatedAlbum.getTitle());
        album.setGenre(updatedAlbum.getGenre());
        album.setArtist(updatedAlbum.getArtist());
        album.setAvailable(updatedAlbum.isAvailable());

        if (updatedAlbum.getStore() != null && updatedAlbum.getStore().getId() != null) {
            Store store = storeRepository.findById(updatedAlbum.getStore().getId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));
            album.setStore(store);
        }

        return albumRepository.save(album);
    }
    // Slet album (fjern også fra kunders reservationer + store)
    public void deleteAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        // Fjern fra alle kunder
        List<Customer> customers = customerRepository.findAll();
        for (Customer c : customers) {
            if (c.getReservations().contains(album)) {
                c.getReservations().remove(album);
                customerRepository.save(c);
            }
        }

        // Fjern fra store
        Store store = album.getStore();
        if (store != null) {
            store.getAlbums().remove(album);
            storeRepository.save(store);
        }

        albumRepository.delete(album);
    }
}
