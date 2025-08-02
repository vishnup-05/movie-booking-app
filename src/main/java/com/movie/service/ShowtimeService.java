package com.movie.service;

import com.movie.model.Movie;
import com.movie.model.Screen;
import com.movie.model.Showtime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    List<Showtime> getAllShowtimes();
    Optional<Showtime> getShowtimeById(Long id);
    Showtime addShowtime(Showtime showtime);
    Showtime updateShowtime(Long id, Showtime showtime);
    void deleteShowtime(Long id);
    List<Showtime> getShowtimesByMovie(Movie movie);
    List<Showtime> getShowtimesByScreen(Screen screen);
    List<Showtime> getShowtimesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Showtime> getUpcomingShowtimesByMovie(Movie movie);
}
