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
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenSeederEnabled_whenRunCalled_thenDataSeededCorrectly() throws Exception {
        // Given
        setSeederEnabled(true);

        // Arrange
        Cinema cinema1 = new Cinema();
        cinema1.setName("Cinema 1");
        Cinema cinema2 = new Cinema();
        cinema2.setName("Cinema 2");
        Cinema cinema3 = new Cinema();
        cinema3.setName("Cinema 3");
        Cinema cinema4 = new Cinema();
        cinema4.setName("Cinema 4");
        Cinema cinema5 = new Cinema();
        cinema5.setName("Cinema 5");
        when(cinemaRepository.findAll()).thenReturn(List.of(cinema1, cinema2, cinema3, cinema4, cinema5));
        when(studioRepository.findAll()).thenReturn(Collections.emptyList());
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Plan samplePlan = new Plan();
        samplePlan.setDuration(30);
        when(planRepository.findAll()).thenReturn(List.of(samplePlan, samplePlan, samplePlan, samplePlan, samplePlan));
        when(planRepository.count()).thenReturn(5L);
        Studio studio1 = new Studio();
        studio1.setTotalSeats(100);
        Studio studio2 = new Studio();
        studio2.setTotalSeats(100);
        Studio studio3 = new Studio();
        studio3.setTotalSeats(100);
        when(studioRepository.findAll()).thenReturn(List.of(studio1, studio2, studio3));

        // When
        databaseSeeder.run();

        // Then
        verify(cinemaRepository, times(5)).save(any(Cinema.class));
        verify(studioRepository, times(15)).save(any(Studio.class));
        verify(seatRepository, times(300)).save(any(Seat.class));
        verify(movieRepository, times(10)).save(any(Movie.class));
        verify(userRepository, times(5)).save(any(User.class));
    }

    @Test
    public void givenNoStudios_whenRunCalled_thenStudiosSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

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
    public void givenStudios_whenRunCalled_thenSeatsSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

        Studio studio = new Studio();
        studio.setTotalSeats(100);
        when(studioRepository.findAll()).thenReturn(List.of(studio));

        // When
        databaseSeeder.run();

        // Then
        verify(seatRepository, times(100)).save(any(Seat.class));
    }

    @Test
    public void givenNoMovies_whenRunCalled_thenMoviesSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        databaseSeeder.run();

        // Then
        verify(movieRepository, times(10)).save(any(Movie.class));
    }

    @Test
    public void givenMoviesAndStudios_whenRunCalled_thenSchedulesSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

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
    public void givenNoUsers_whenRunCalled_thenUsersSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

        // When
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Plan samplePlan = new Plan();
        samplePlan.setDuration(30);
        when(planRepository.findAll()).thenReturn(List.of(samplePlan));

        databaseSeeder.run();

        // Then
        verify(userRepository, times(5)).save(any(User.class));
    }

    @Test
    public void givenUsersAndSchedules_whenRunCalled_thenReservationsAndPaymentsSeeded() throws Exception {
        // Given
        setSeederEnabled(true);

        User user = new User();
        user.setUsername("user1");

        // When
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());
        when(scheduleRepository.findAll()).thenReturn(List.of(new Schedule(), new Schedule()));

        Plan samplePlan = new Plan();
        samplePlan.setDuration(30);
        when(planRepository.findAll()).thenReturn(List.of(samplePlan));

        databaseSeeder.run();

        // Then
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void givenSeederDisabled_whenRunCalled_thenNoDataSeeded() throws Exception {
        // Given
        setSeederEnabled(false);

        // When
        databaseSeeder.run();

        // Then
        verifyNoInteractions(cinemaRepository, studioRepository, seatRepository, movieRepository,
                scheduleRepository, userRepository, reservationRepository, paymentRepository);
    }

    private void setSeederEnabled(boolean enabled) throws Exception {
        Field field = DatabaseSeeder.class.getDeclaredField("isSeederEnabled");
        field.setAccessible(true);
        field.set(databaseSeeder, enabled);
    }
}