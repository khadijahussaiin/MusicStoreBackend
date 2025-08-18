package com.example.musicstorebackend.controller;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Store;
import com.example.musicstorebackend.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@CrossOrigin("*")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // A: Tildel/knyt et album til en butik
    /* Bruges ikke slettes
    @PutMapping("/{storeId}/assign/{albumId}")
    public ResponseEntity<Store> assignAlbumToStore(@PathVariable Long storeId, @PathVariable Long albumId) {
        return ResponseEntity.ok(storeService.assignAlbumToStore(storeId, albumId));
    }*/

    // B: Se butiks detaljer inkl. albums
    @GetMapping("details/{storeId}")
    public ResponseEntity<Store> getStoreDetails(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreDetails(storeId));
    }

    // C: Liste over kundens reserverede albums, der er tilgængelige i butikken
    @GetMapping("/customer/{customerId}/available")
    public ResponseEntity<List<Album>> getAvailableReservations(@PathVariable Long customerId) {
        return ResponseEntity.ok(storeService.getAvailableReservations(customerId));
    }
}
