package com.movie.service.impl;

import com.movie.model.Movie;
import com.movie.model.Screen;
import com.movie.model.Showtime;
import com.movie.repository.ShowtimeRepository;
import com.movie.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    @Override
    public Showtime addShowtime(Showtime showtime) {
        validateShowtime(showtime);
        return showtimeRepository.save(showtime);
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime showtime) {
        if (showtimeRepository.existsById(id)) {
            validateShowtime(showtime);
            showtime.setId(id);
            return showtimeRepository.save(showtime);
        }
        throw new IllegalArgumentException("Showtime with ID " + id + " not found");
    }

    @Override
    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    @Override
    public List<Showtime> getShowtimesByMovie(Movie movie) {
        return showtimeRepository.findByMovie(movie);
    }

    @Override
    public List<Showtime> getShowtimesByScreen(Screen screen) {
        return showtimeRepository.findByScreen(screen);
    }

    @Override
    public List<Showtime> getShowtimesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return showtimeRepository.findByStartTimeAfterAndStartTimeBefore(startDate, endDate);
    }

    @Override
    public List<Showtime> getUpcomingShowtimesByMovie(Movie movie) {
        return showtimeRepository.findByMovieAndStartTimeAfter(movie, LocalDateTime.now());
    }

    private void validateShowtime(Showtime showtime) {
        if (showtime.getStartTime().isAfter(showtime.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Additional validation logic could be added here
        // For example, check for overlapping showtimes in the same screen
    }
}
