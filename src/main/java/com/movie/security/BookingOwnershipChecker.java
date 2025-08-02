package com.movie.security;

import com.movie.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class BookingOwnershipChecker {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingOwnershipChecker(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean isOwner(Authentication authentication, Long bookingId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getUser().getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}
