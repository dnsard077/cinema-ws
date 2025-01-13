package com.example.cinema.cinemaws.util;

import com.example.cinema.cinemaws.model.*;
import com.example.cinema.cinemaws.repository.*;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final CinemaRepository cinemaRepository;
    private final StudioRepository studioRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final Faker faker = new Faker(new Locale("en-US"));
    private final PasswordEncoder passwordEncoder;
    @Value("${database.seeder.enabled}")
    private boolean isSeederEnabled;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            log.info("Database seeding is disabled.");
            return;
        }

        log.info("Seeding Cinema");
        for (int i = 0; i < 5; i++) {
            Cinema cinema = new Cinema();
            cinema.setName(faker.company().name());
            cinema.setLocation(faker.address().city());
            cinema.setContactNumber(faker.phoneNumber().phoneNumber());
            cinemaRepository.save(cinema);
        }


        log.info("Seeding Studio");
        for (Cinema cinema : cinemaRepository.findAll()) {
            for (int i = 0; i < 3; i++) {
                Studio studio = new Studio();
                studio.setCinema(cinema);
                studio.setStudioName(faker.company().name() + " Studio");
                studio.setTotalSeats(faker.number().numberBetween(50, 200));
                studioRepository.save(studio);
            }
        }

        log.info("Seeding Seat");
        for (Studio studio : studioRepository.findAll()) {
            for (int i = 1; i <= studio.getTotalSeats(); i++) {
                Seat seat = new Seat();
                seat.setStudio(studio);
                seat.setSeatNumber("Seat " + i);
                seat.setIsAvailable(true);
                seatRepository.save(seat);
            }
        }

        log.info("Seeding Movie");
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            movie.setTitle(faker.book().title());
            movie.setGenre(faker.book().genre());
            movie.setDuration(faker.number().numberBetween(90, 180));
            movie.setReleaseDate(faker.date().future(365, TimeUnit.DAYS).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            movieRepository.save(movie);
        }


        log.info("Seeding Schedule");
        for (Movie movie : movieRepository.findAll()) {
            for (Studio studio : studioRepository.findAll()) {
                Schedule schedule = new Schedule();
                schedule.setMovie(movie);
                schedule.setStudio(studio);


                LocalDateTime startTime = faker.date().future(365, TimeUnit.DAYS).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();


                int randomHour = ThreadLocalRandom.current().nextInt(0, 24);
                int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
                startTime = startTime.withHour(randomHour).withMinute(randomMinute).withSecond(0).withNano(0);

                schedule.setStartTime(startTime);


                int durationInHours = ThreadLocalRandom.current().nextInt(2, 6);


                int randomMinutes = ThreadLocalRandom.current().nextInt(0, 60);
                int randomSeconds = ThreadLocalRandom.current().nextInt(0, 60);


                LocalDateTime endTime = startTime.plusHours(durationInHours)
                        .plusMinutes(randomMinutes)
                        .plusSeconds(randomSeconds);
                schedule.setEndTime(endTime);


                scheduleRepository.save(schedule);
            }
        }

        log.info("Seeding User");
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setPassword(passwordEncoder.encode("string"));
            user.setEmail(faker.internet().emailAddress());
            user.setRole(User.Role.CUSTOMER);
            userRepository.save(user);
        }

        log.info("Seeding Reservation & Payment");
        for (User user : userRepository.findAll()) {
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setReservationDate(faker.date().future(365, TimeUnit.DAYS).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            reservation.setTotalAmount(faker.number().randomDouble(2, 1000, 10000000));
            reservationRepository.save(reservation);

            Payment payment = new Payment();
            payment.setReservation(reservation);
            payment.setPaymentMethod(faker.finance().creditCard());
            payment.setPaymentStatus("Paid");
            payment.setAmountPaid(faker.number().randomDouble(2, 1000, 10000000));
            paymentRepository.save(payment);
        }
        log.info("Done Seeding");
    }
}