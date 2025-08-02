package com.movie.service;

import com.movie.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(Long id);
    Movie addMovie(Movie movie);
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);
    List<Movie> getMoviesByGenre(String genre);
    List<Movie> searchMoviesByTitle(String title);
}
