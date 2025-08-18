package com.example.musicstorebackend.controller;

import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.Service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "http://localhost:5500") // WebStorm frontend
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    // A. Opret album + tildel butik
    @PostMapping("/add")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album, @RequestParam Long storeId) {
        Album createdAlbum = albumService.createAlbum(album, storeId);
        return new ResponseEntity<>(createdAlbum, HttpStatus.CREATED);
    }

    // B. Hent alle albums
    @GetMapping("/all")
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    // C. Opdater album + evt. butik
    @PutMapping("/update/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id,
                                             @RequestBody Album updatedAlbum,
                                             @RequestParam(required = false) Long storeId) {
        Album album = albumService.updateAlbum(id, updatedAlbum, storeId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    // D. Slet album
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 uden body
    }
}
