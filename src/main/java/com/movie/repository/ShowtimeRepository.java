package com.movie.repository;

import com.movie.model.Movie;
import com.movie.model.Screen;
import com.movie.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovie(Movie movie);
    List<Showtime> findByScreen(Screen screen);
    List<Showtime> findByStartTimeAfterAndStartTimeBefore(LocalDateTime startDate, LocalDateTime endDate);
    List<Showtime> findByMovieAndStartTimeAfter(Movie movie, LocalDateTime startDate);
}
