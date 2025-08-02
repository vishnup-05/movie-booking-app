package com.movie.controller;

import com.movie.model.Booking;
import com.movie.model.Seat;
import com.movie.model.Showtime;
import com.movie.model.User;
import com.movie.repository.UserRepository;
import com.movie.service.BookingService;
import com.movie.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ShowtimeService showtimeService;
    private final UserRepository userRepository;

    @Autowired
    public BookingController(BookingService bookingService, ShowtimeService showtimeService,
                            UserRepository userRepository) {
        this.bookingService = bookingService;
        this.showtimeService = showtimeService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookingOwnershipChecker.isOwner(authentication, #id)")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(bookingService.getBookingsByUser(currentUser));
    }

    @PostMapping("/showtime/{showtimeId}")
    public ResponseEntity<?> createBooking(@PathVariable Long showtimeId,
                                           @RequestBody Set<Long> seatIds) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<Showtime> showtimeOpt = showtimeService.getShowtimeById(showtimeId);
            if (showtimeOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Showtime showtime = showtimeOpt.get();

            // Collect all the seats
            Set<Seat> seats = showtimeOpt.get().getScreen().getSeats().stream()
                .filter(seat -> seatIds.contains(seat.getId()))
                .collect(java.util.stream.Collectors.toSet());

            if (seats.size() != seatIds.size()) {
                return ResponseEntity.badRequest().body("One or more seats are invalid");
            }

            // Check seat availability
            for (Seat seat : seats) {
                if (!bookingService.isSeatAvailableForShowtime(seat, showtime)) {
                    return ResponseEntity.badRequest().body("Seat " + seat.getRow() + seat.getNumber() + " is not available");
                }
            }

            Booking booking = bookingService.createBooking(currentUser, showtime, seats);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            Optional<Booking> bookingOpt = bookingService.getBookingById(id);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Booking booking = bookingOpt.get();
            User currentUser = getCurrentUser();

            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(currentUser.getId()) &&
                    !currentUser.getRoles().contains("ROLE_ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.CANCELLED);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}
