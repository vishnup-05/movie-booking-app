package com.movie.repository;

import com.movie.model.Screen;
import com.movie.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
    List<Seat> findByScreenAndRowOrderByNumber(Screen screen, String row);
}
