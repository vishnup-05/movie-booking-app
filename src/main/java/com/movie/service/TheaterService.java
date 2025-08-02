package com.movie.service;

import com.movie.model.Theater;

import java.util.List;
import java.util.Optional;

public interface TheaterService {
    List<Theater> getAllTheaters();
    Optional<Theater> getTheaterById(Long id);
    Theater addTheater(Theater theater);
    Theater updateTheater(Long id, Theater theater);
    void deleteTheater(Long id);
    List<Theater> searchTheatersByLocation(String location);
}
