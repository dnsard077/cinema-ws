package com.example.cinema.cinemaws.util;

import com.example.cinema.cinemaws.model.*;
import com.example.cinema.cinemaws.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class DatabaseSeederTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @Mock
    private StudioRepository studioRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedData() throws Exception {
        // Given
        this.setSeederEnabled(true);

        // Arrange
        when(cinemaRepository.findAll()).thenReturn(Collections.emptyList());
        when(studioRepository.findAll()).thenReturn(Collections.emptyList());
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // When
        databaseSeeder.run();

        // Then
        verify(cinemaRepository, times(5)).save(any(Cinema.class));
        verify(studioRepository, times(0)).save(any(Studio.class));
        verify(movieRepository, times(10)).save(any(Movie.class));
        verify(userRepository, times(5)).save(any(User.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedStudios() throws Exception {
        // Given
        this.setSeederEnabled(true);
        Cinema cinema = new Cinema();
        cinema.setName("Cinema 1");
        when(cinemaRepository.findAll()).thenReturn(List.of(cinema));
        when(studioRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        databaseSeeder.run();

        // Then
        verify(studioRepository, times(3)).save(any(Studio.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedSeats() throws Exception {
        // Given
        this.setSeederEnabled(true);
        Studio studio = new Studio();
        studio.setTotalSeats(100);
        when(studioRepository.findAll()).thenReturn(List.of(studio));

        // When
        databaseSeeder.run();

        // Then
        verify(seatRepository, times(100)).save(any(Seat.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedMovies() throws Exception {
        // Given
        this.setSeederEnabled(true);
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        databaseSeeder.run();

        // Then
        verify(movieRepository, times(10)).save(any(Movie.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedSchedules() throws Exception {
        // Given
        this.setSeederEnabled(true);
        Movie movie = new Movie();
        Studio studio = new Studio();
        studio.setTotalSeats(1);
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(studioRepository.findAll()).thenReturn(List.of(studio));

        // When
        databaseSeeder.run();

        // Then
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedUsers() throws Exception {
        // Given
        this.setSeederEnabled(true);
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        databaseSeeder.run();

        // Then
        verify(userRepository, times(5)).save(any(User.class));
    }

    @Test
    public void givenSeederEnabled_whenRun_thenSeedReservationsAndPayments() throws Exception {
        // Given
        this.setSeederEnabled(true);
        User user = new User();
        user.setUsername("user1");
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        databaseSeeder.run();

        // Then
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void givenSeederDisabled_whenRun_thenNotSeedData() throws Exception {
        // Given
        this.setSeederEnabled(false);

        // When
        databaseSeeder.run();

        // Then
        verify(cinemaRepository, never()).save(any(Cinema.class));
        verify(studioRepository, never()).save(any(Studio.class));
        verify(seatRepository, never()).save(any(Seat.class));
        verify(movieRepository, never()).save(any(Movie.class));
        verify(scheduleRepository, never()).save(any(Schedule.class));
        verify(userRepository, never()).save(any(User.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    private void setSeederEnabled(boolean enabled) throws Exception {
        Field field = DatabaseSeeder.class.getDeclaredField("isSeederEnabled");
        field.setAccessible(true);
        field.set(databaseSeeder, enabled);
    }
}