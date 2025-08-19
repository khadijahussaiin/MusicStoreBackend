package com.example.musicstorebackend;
// Test 2: CustomerService.reserveAlbum - Kerneforretningslogik
import com.example.musicstorebackend.Service.CustomerService;
import com.example.musicstorebackend.model.Album;
import com.example.musicstorebackend.model.Customer;
import com.example.musicstorebackend.model.Store;
import com.example.musicstorebackend.repository.AlbumRepository;
import com.example.musicstorebackend.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private Album availableAlbum;
    private Album unavailableAlbum;
    private Store testStore;

    @BeforeEach
    void setUp() {
        testStore = new Store("Vinyl House", "Main St 1", "Copenhagen", "1000");

        testCustomer = new Customer("Alice Johnson", "alice@example.com", "12345678");
        testCustomer.setReservations(new ArrayList<>());

        availableAlbum = new Album("Dark Side of the Moon", "Rock", "Pink Floyd", true);
        availableAlbum.setStore(testStore);

        unavailableAlbum = new Album("Thriller", "Pop", "Michael Jackson", false);
        unavailableAlbum.setStore(testStore);
    }

    @Test
    void reserveAlbum_Success_AddsToCustomerReservations() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(availableAlbum));
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.reserveAlbum(1L, 1L);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getReservations().contains(availableAlbum),
                "Album should be added to customer's reservations");
        assertEquals(1, result.getReservations().size(),
                "Customer should have exactly 1 reservation");

        // Verificer repository calls
        verify(customerRepository).findById(1L);
        verify(albumRepository).findById(1L);
        verify(customerRepository).save(testCustomer);
    }

    @Test
    void reserveAlbum_UnavailableAlbum_StillAllowsReservation() {
        // Arrange - I din nuværende logik tillades reservation af ikke-tilgængelige albums
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(unavailableAlbum));
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.reserveAlbum(1L, 1L);

        // Assert - Dette test dokumenterer nuværende adfærd
        assertTrue(result.getReservations().contains(unavailableAlbum),
                "Current implementation allows reservation of unavailable albums");

        // Note: Hvis du ønsker at ændre dette, tilføj validation i servicen
    }

    @Test
    void reserveAlbum_DuplicateReservation_AddsAlbumTwice() {
        // Arrange - Test edge case hvor samme album reserveres to gange
        testCustomer.getReservations().add(availableAlbum); // Allerede reserveret

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(availableAlbum));
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.reserveAlbum(1L, 1L);

        // Assert - Dokumenterer nuværende adfærd (tillader dubletter)
        assertEquals(2, result.getReservations().size(),
                "Current implementation allows duplicate reservations");

        // Note: Overvej at tilføje duplicate check hvis ønsket
    }

    @Test
    void reserveAlbum_CustomerNotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.reserveAlbum(999L, 1L));

        assertEquals("Customer not found with id 999", exception.getMessage());

        // Verificer at album ikke blev hentet hvis kunde ikke findes
        verify(albumRepository, never()).findById(any());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void reserveAlbum_AlbumNotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.reserveAlbum(1L, 999L));

        assertEquals("Album not found with id 999", exception.getMessage());

        // Verificer at kunde ikke blev gemt hvis album ikke findes
        verify(customerRepository, never()).save(any());
        assertEquals(0, testCustomer.getReservations().size(),
                "Customer reservations should remain unchanged");
    }

    @Test
    void reserveAlbum_CustomerWithExistingReservations_AddsCorrectly() {
        // Arrange - Kunde har allerede andre reservationer
        Album existingAlbum = new Album("Abbey Road", "Rock", "The Beatles", true);
        testCustomer.getReservations().add(existingAlbum);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(availableAlbum));
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        // Act
        Customer result = customerService.reserveAlbum(1L, 1L);

        // Assert
        assertEquals(2, result.getReservations().size(),
                "Customer should have 2 reservations after adding new one");
        assertTrue(result.getReservations().contains(existingAlbum),
                "Existing reservation should remain");
        assertTrue(result.getReservations().contains(availableAlbum),
                "New reservation should be added");
    }
}
