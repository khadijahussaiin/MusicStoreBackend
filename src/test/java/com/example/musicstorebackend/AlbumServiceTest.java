package com.example.musicstorebackend;
// Test 1: AlbumService.deleteAlbum()

import com.example.musicstorebackend.Service.AlbumService;
import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.model.Store;
import com.example.musicstorebackend.repository.AlbumRepository;
import com.example.musicstorebackend.repository.CustomerRepository;
import com.example.musicstorebackend.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AlbumService albumService;

    private Album albumToDelete;
    private Store testStore;
    private Customer customer1;
    private Customer customer2;
    private Customer customerWithoutReservation;

    @BeforeEach
    void setUp() {
        // Setup store
        testStore = new Store("Vinyl House", "Main St 1", "Copenhagen", "1000");
        testStore.setAlbums(new ArrayList<>());

        // Setup album to delete
        albumToDelete = new Album("Dark Side of the Moon", "Rock", "Pink Floyd", true);
        albumToDelete.setStore(testStore);
        testStore.getAlbums().add(albumToDelete);

        // Setup customers with reservations
        customer1 = new Customer("Alice Johnson", "alice@example.com", "12345678");
        customer1.setReservations(new ArrayList<>());
        customer1.getReservations().add(albumToDelete);

        customer2 = new Customer("Bob Smith", "bob@example.com", "87654321");
        customer2.setReservations(new ArrayList<>());
        customer2.getReservations().add(albumToDelete);

        // Customer without reservation (should not be affected)
        customerWithoutReservation = new Customer("Charlie Brown", "charlie@example.com", "11223344");
        customerWithoutReservation.setReservations(new ArrayList<>());
    }

    @Test
    void deleteAlbum_Success_RemovesFromCustomersAndStore() {
        // Arrange - Setup alle mocks
        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumToDelete));
        when(customerRepository.findAll()).thenReturn(Arrays.asList(
                customer1, customer2, customerWithoutReservation));

        // Act - Udfør sletning
        albumService.deleteAlbum(1L);

        // Assert - Verificer at alt er fjernet korrekt

        // 1. Verificer at album blev fundet
        verify(albumRepository).findById(1L);

        // 2. Verificer at alle kunder blev hentet
        verify(customerRepository).findAll();

        // 3. Verificer at album blev fjernet fra kunder der havde det reserveret
        verify(customerRepository).save(customer1);
        verify(customerRepository).save(customer2);
        // customerWithoutReservation skulle IKKE gemmes da den ikke havde reservationen
        verify(customerRepository, never()).save(customerWithoutReservation);

        // 4. Verificer at album blev fjernet fra kunders reservationslister
        assertFalse(customer1.getReservations().contains(albumToDelete),
                "Album should be removed from customer1's reservations");
        assertFalse(customer2.getReservations().contains(albumToDelete),
                "Album should be removed from customer2's reservations");
        assertTrue(customerWithoutReservation.getReservations().isEmpty(),
                "Customer without reservation should still have empty list");

        // 5. Verificer at store blev opdateret
        verify(storeRepository).save(testStore);
        assertFalse(testStore.getAlbums().contains(albumToDelete),
                "Album should be removed from store's album list");

        // 6. Verificer at album blev slettet fra database
        verify(albumRepository).delete(albumToDelete);
    }

    @Test
    void deleteAlbum_AlbumNotFound_ThrowsException() {
        // Arrange
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> albumService.deleteAlbum(999L));

        assertEquals("Album not found", exception.getMessage());

        // Verificer at ingen andre operationer blev udført
        verify(customerRepository, never()).findAll();
        verify(customerRepository, never()).save(any());
        verify(storeRepository, never()).save(any());
        verify(albumRepository, never()).delete(any());
    }

    @Test
    void deleteAlbum_AlbumWithoutStore_HandledGracefully() {
        // Arrange - Album uden butik
        Album orphanAlbum = new Album("Orphan Album", "Rock", "Unknown", true);
        orphanAlbum.setStore(null); // Ingen butik

        when(albumRepository.findById(1L)).thenReturn(Optional.of(orphanAlbum));
        when(customerRepository.findAll()).thenReturn(Arrays.asList());

        // Act - Skal ikke kaste exception
        assertDoesNotThrow(() -> albumService.deleteAlbum(1L));

        // Assert - Verificer at album stadig blev slettet
        verify(albumRepository).delete(orphanAlbum);
        verify(storeRepository, never()).save(any()); // Ingen store at opdatere
    }
}