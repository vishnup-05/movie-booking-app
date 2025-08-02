package com.movie.service.impl;

import com.movie.model.Booking;
import com.movie.model.Seat;
import com.movie.model.Showtime;
import com.movie.model.User;
import com.movie.repository.BookingRepository;
import com.movie.repository.SeatRepository;
import com.movie.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    @Transactional
    public Booking createBooking(User user, Showtime showtime, Set<Seat> seats) {
        // Validate seats are available
        for (Seat seat : seats) {
            if (!isSeatAvailableForShowtime(seat, showtime)) {
                throw new IllegalArgumentException("Seat " + seat.getRow() + seat.getNumber() + " is not available for the selected showtime");
            }
        }

        // Create new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setSeats(seats);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(status);
            return bookingRepository.save(booking);
        }
        throw new IllegalArgumentException("Booking with ID " + id + " not found");
    }

    @Override
    public void cancelBooking(Long id) {
        updateBookingStatus(id, Booking.BookingStatus.CANCELLED);
    }

    @Override
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @Override
    public List<Booking> getBookingsByShowtime(Showtime showtime) {
        return bookingRepository.findByShowtime(showtime);
    }

    @Override
    public List<Booking> getBookingsByUserAndStatus(User user, Booking.BookingStatus status) {
        return bookingRepository.findByUserAndStatus(user, status);
    }

    @Override
    public boolean isSeatAvailableForShowtime(Seat seat, Showtime showtime) {
        List<Booking> confirmedBookings = bookingRepository.findConfirmedBookingsByShowtimeId(showtime.getId());

        for (Booking booking : confirmedBookings) {
            if (booking.getSeats().contains(seat)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Set<Seat> getAvailableSeatsForShowtime(Showtime showtime) {
        // Get all seats in the screen
        List<Seat> allSeats = seatRepository.findByScreen(showtime.getScreen());

        // Get all booked seats for this showtime
        List<Booking> confirmedBookings = bookingRepository.findConfirmedBookingsByShowtimeId(showtime.getId());
        Set<Seat> bookedSeats = confirmedBookings.stream()
                .flatMap(booking -> booking.getSeats().stream())
                .collect(Collectors.toSet());

        // Return all seats that are not in the booked set
        return allSeats.stream()
                .filter(seat -> !bookedSeats.contains(seat))
                .collect(Collectors.toSet());
    }
}
