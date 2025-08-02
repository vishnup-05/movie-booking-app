package com.movie.service;

import com.movie.model.Booking;
import com.movie.model.Seat;
import com.movie.model.Showtime;
import com.movie.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingService {
    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(Long id);
    Booking createBooking(User user, Showtime showtime, Set<Seat> seats);
    Booking updateBookingStatus(Long id, Booking.BookingStatus status);
    void cancelBooking(Long id);
    List<Booking> getBookingsByUser(User user);
    List<Booking> getBookingsByShowtime(Showtime showtime);
    List<Booking> getBookingsByUserAndStatus(User user, Booking.BookingStatus status);
    boolean isSeatAvailableForShowtime(Seat seat, Showtime showtime);
    Set<Seat> getAvailableSeatsForShowtime(Showtime showtime);
}
