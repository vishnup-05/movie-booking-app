package com.movie.service.impl;

import com.movie.model.Theater;
import com.movie.repository.TheaterRepository;
import com.movie.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterServiceImpl(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Override
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    @Override
    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    @Override
    public Theater addTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    @Override
    public Theater updateTheater(Long id, Theater theater) {
        if (theaterRepository.existsById(id)) {
            theater.setId(id);
            return theaterRepository.save(theater);
        }
        throw new IllegalArgumentException("Theater with ID " + id + " not found");
    }

    @Override
    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }

    @Override
    public List<Theater> searchTheatersByLocation(String location) {
        return theaterRepository.findByLocationContainingIgnoreCase(location);
    }
}
