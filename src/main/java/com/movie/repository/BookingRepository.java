package com.movie.repository;

import com.movie.model.Booking;
import com.movie.model.Showtime;
import com.movie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByShowtime(Showtime showtime);
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.showtime.id = :showtimeId AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByShowtimeId(Long showtimeId);
}
