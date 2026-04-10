package com.cyan.streaming.config;

import com.cyan.streaming.model.Comment;
import com.cyan.streaming.model.Movie;
import com.cyan.streaming.model.Rating;
import com.cyan.streaming.model.Role;
import com.cyan.streaming.model.User;
import com.cyan.streaming.repository.CommentRepository;
import com.cyan.streaming.repository.MovieRepository;
import com.cyan.streaming.repository.RatingRepository;
import com.cyan.streaming.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {
            List<Movie> movies = seedMovies();
            User admin = seedUser("admin", "admin@cyan.local", "Admin@123", Role.ADMIN);
            User viewer = seedUser("viewer", "viewer@cyan.local", "Viewer@123", Role.USER);

            if (ratingRepository.count() == 0 && !movies.isEmpty()) {
                ratingRepository.saveAll(List.of(
                        Rating.builder().movie(movies.get(0)).user(viewer).value(5).build(),
                        Rating.builder().movie(movies.get(1)).user(viewer).value(4).build()
                ));
            }

            if (commentRepository.count() == 0 && !movies.isEmpty()) {
                commentRepository.saveAll(List.of(
                        Comment.builder()
                                .movie(movies.get(0))
                                .user(viewer)
                                .content("The atmosphere and soundtrack make this a perfect late-night watch.")
                                .build(),
                        Comment.builder()
                                .movie(movies.get(1))
                                .user(admin)
                                .content("A sharp reboot with strong visuals and a moody detective vibe.")
                                .build()
                ));
            }
        };
    }

    private List<Movie> seedMovies() {
        if (movieRepository.count() > 0) {
            return movieRepository.findAllByOrderByRatingDescReleaseYearDesc();
        }

        List<Movie> movies = List.of(
                Movie.builder()
                        .title("Interstellar")
                        .description("A team of explorers travels beyond our galaxy through a wormhole to find humanity a new home.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/zSWdZVtXT7E")
                        .genre("Sci-Fi")
                        .releaseYear(2014)
                        .rating(8.7)
                        .build(),
                Movie.builder()
                        .title("The Batman")
                        .description("Bruce Wayne uncovers corruption across Gotham while tracking a sadistic serial killer targeting the city's elite.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1517602302552-471fe67acf66?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/mqqft2x_Aa4")
                        .genre("Action")
                        .releaseYear(2022)
                        .rating(7.8)
                        .build(),
                Movie.builder()
                        .title("Dune")
                        .description("Paul Atreides begins a dangerous journey across Arrakis as powerful houses battle for control of the spice.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/8g18jFHCLXk")
                        .genre("Adventure")
                        .releaseYear(2021)
                        .rating(8.0)
                        .build(),
                Movie.builder()
                        .title("Everything Everywhere All at Once")
                        .description("An exhausted laundromat owner is pulled into a surreal multiverse conflict that spans infinite realities.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1524985069026-dd778a71c7b4?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/wxN1T1uxQ2g")
                        .genre("Drama")
                        .releaseYear(2022)
                        .rating(7.9)
                        .build(),
                Movie.builder()
                        .title("Barbie")
                        .description("Barbie begins questioning her world and heads into the real one for a bright, satirical identity adventure.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1502134249126-9f3755a50d78?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/pBk4NYhWNMM")
                        .genre("Comedy")
                        .releaseYear(2023)
                        .rating(7.2)
                        .build(),
                Movie.builder()
                        .title("Spider-Man: Across the Spider-Verse")
                        .description("Miles Morales swings across the multiverse and collides with a team of Spider-People protecting its fragile balance.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/shW9i6k8cB0")
                        .genre("Animation")
                        .releaseYear(2023)
                        .rating(8.5)
                        .build(),
                Movie.builder()
                        .title("Nope")
                        .description("Two siblings running a California horse ranch investigate a terrifying phenomenon hovering in the skies above them.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1513106580091-1d82408b8cd6?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/In8fuzj3gck")
                        .genre("Horror")
                        .releaseYear(2022)
                        .rating(6.8)
                        .build(),
                Movie.builder()
                        .title("Palm Springs")
                        .description("Two wedding guests get stuck in a chaotic time loop and turn repeated days into a surprisingly sharp romance.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1518929458119-e5bf444c30f4?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/CpBLtXduh_k")
                        .genre("Comedy")
                        .releaseYear(2020)
                        .rating(7.4)
                        .build(),
                Movie.builder()
                        .title("Glass Onion")
                        .description("Detective Benoit Blanc is invited to a private island where a tech billionaire's eccentric game turns deadly.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/gj5ibYSz8C0")
                        .genre("Mystery")
                        .releaseYear(2022)
                        .rating(7.1)
                        .build(),
                Movie.builder()
                        .title("Top Gun: Maverick")
                        .description("Pete Mitchell returns to train a new generation of aviators for a mission that demands everything from them.")
                        .thumbnailUrl("https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1200&q=80")
                        .youtubeEmbedUrl("https://www.youtube.com/embed/giXco2jaZ_4")
                        .genre("Action")
                        .releaseYear(2022)
                        .rating(8.3)
                        .build()
        );

        return movieRepository.saveAll(movies);
    }

    private User seedUser(String username, String email, String password, Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .build()));
    }
}
