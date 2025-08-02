# Movie Booking Backend - Documentation

## Project Overview
This project is a RESTful backend application for a movie ticket booking system built with Spring Boot. It allows users to browse movies, theaters, and showtimes, as well as book and manage their movie tickets. The system also includes an administration panel for managing movies, theaters, screens, showtimes, and viewing booking statistics.

## Technology Stack
- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA**: For database interaction and ORM
- **Spring Security**: For authentication and authorization
- **JWT (JSON Web Tokens)**: For stateless authentication
- **H2 Database**: In-memory database for development (can be replaced with PostgreSQL or MySQL for production)
- **Lombok**: To reduce boilerplate code

## System Architecture
The application follows a standard layered architecture:

1. **Controller Layer**: REST APIs for client interaction
2. **Service Layer**: Business logic implementation
3. **Repository Layer**: Data access and persistence
4. **Security Layer**: Authentication and authorization
5. **Model Layer**: Domain entities

## Core Features

### User Management
- User registration and login
- JWT-based authentication
- Role-based authorization (User and Admin roles)

### Movie Management
- Browse all movies
- Search movies by title or genre
- View detailed movie information
- Admin functionality to add, update, and delete movies

### Theater Management
- Browse all theaters
- Search theaters by location
- View theater details including available screens
- Admin functionality to add, update, and delete theaters

### Showtime Management
- View all showtimes for a specific movie
- View showtimes within a date range
- Check available seats for a showtime
- Admin functionality to add, update, and delete showtimes

### Booking Management
- Book seats for a showtime
- View booking history
- Cancel bookings
- Admin functionality to view all bookings

## API Endpoints

### Authentication
- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Authenticate and get JWT token

### Movies
- `GET /api/movies`: Get all movies
- `GET /api/movies/{id}`: Get movie details by ID
- `GET /api/movies/genre/{genre}`: Get movies by genre
- `GET /api/movies/search?title={title}`: Search movies by title
- `POST /api/movies`: Add a new movie (Admin only)
- `PUT /api/movies/{id}`: Update a movie (Admin only)
- `DELETE /api/movies/{id}`: Delete a movie (Admin only)

### Theaters
- `GET /api/theaters`: Get all theaters
- `GET /api/theaters/{id}`: Get theater details by ID
- `GET /api/theaters/search?location={location}`: Search theaters by location
- `POST /api/theaters`: Add a new theater (Admin only)
- `PUT /api/theaters/{id}`: Update a theater (Admin only)
- `DELETE /api/theaters/{id}`: Delete a theater (Admin only)

### Showtimes
- `GET /api/showtimes`: Get all showtimes
- `GET /api/showtimes/{id}`: Get showtime details by ID
- `GET /api/showtimes/movie/{movieId}`: Get showtimes for a specific movie
- `GET /api/showtimes/date-range?startDate={startDate}&endDate={endDate}`: Get showtimes within a date range
- `GET /api/showtimes/{id}/seats`: Get available seats for a showtime
- `POST /api/showtimes`: Add a new showtime (Admin only)
- `PUT /api/showtimes/{id}`: Update a showtime (Admin only)
- `DELETE /api/showtimes/{id}`: Delete a showtime (Admin only)

### Bookings
- `GET /api/bookings`: Get all bookings (Admin only)
- `GET /api/bookings/{id}`: Get booking details by ID
- `GET /api/bookings/my-bookings`: Get current user's bookings
- `POST /api/bookings/showtime/{showtimeId}`: Create a new booking
- `PUT /api/bookings/{id}/cancel`: Cancel a booking

## Database Schema

### Entities and Relationships

1. **Movie**
   - Properties: id, title, description, duration, genre, posterUrl
   - Relationships: One Movie has many Showtimes

2. **Theater**
   - Properties: id, name, location
   - Relationships: One Theater has many Screens

3. **Screen**
   - Properties: id, name
   - Relationships: 
     - Many Screens belong to one Theater
     - One Screen has many Seats
     - One Screen has many Showtimes

4. **Seat**
   - Properties: id, row, number
   - Relationships: 
     - Many Seats belong to one Screen
     - Many Seats can be in many Bookings

5. **Showtime**
   - Properties: id, startTime, endTime, price
   - Relationships: 
     - Many Showtimes belong to one Movie
     - Many Showtimes belong to one Screen
     - One Showtime has many Bookings

6. **User**
   - Properties: id, username, password, email, roles
   - Relationships: One User has many Bookings

7. **Booking**
   - Properties: id, bookingTime, status
   - Relationships: 
     - Many Bookings belong to one User
     - Many Bookings belong to one Showtime
     - Many Bookings have many Seats

## Security Configuration

The application uses Spring Security with JWT for authentication and authorization:
- Anonymous users can access public endpoints (view movies, theaters, showtimes)
- Registered users can book tickets and manage their bookings
- Admin users can manage all resources (movies, theaters, screens, showtimes)

JWT tokens are used for stateless authentication, with tokens valid for 24 hours.

## Development Environment Setup

### Prerequisites
- JDK 17 or later
- Maven 3.6+ or compatible build tool
- An IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Running the Application Locally
1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run` to start the application
4. Access the API at `http://localhost:8080`
5. Access the H2 Console at `http://localhost:8080/h2-console` (if enabled)

### H2 Database Configuration
- JDBC URL: `jdbc:h2:mem:moviedb`
- Username: `sa`
- Password: `` (empty)

## Production Deployment Considerations

For deploying to production, consider the following:
1. Replace H2 with a production-grade database like PostgreSQL or MySQL
2. Use environment variables for sensitive information (database credentials, JWT secret)
3. Implement proper logging
4. Set up proper error handling and monitoring
5. Use HTTPS for secure communication
6. Implement rate limiting for API endpoints
7. Set up CI/CD pipelines for automated testing and deployment

## Future Enhancements
1. Implement email notifications for bookings
2. Add payment integration
3. Implement seat selection UI
4. Add movie ratings and reviews
5. Implement caching for frequently accessed data
6. Add support for concessions and food ordering
7. Implement ticket QR code generation

## Troubleshooting

### Common Issues
1. **Authentication failures**: Check JWT token expiration and user credentials
2. **Booking conflicts**: Make sure seats are available before booking
3. **Database connection issues**: Verify connection properties in application.properties

### Logs
Application logs can be found in the console output or configured log files.

## Contributors
- Development Team

## License
This project is proprietary and confidential.

---
*Last updated: August 1, 2025*
