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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private static final String PLACEHOLDER_EMBED = "https://www.youtube.com/embed/dQw4w9WgXcQ";

    private static final String[] TITLE_SUFFIXES = {
            "",
            " II",
            " III",
            ": Origins",
            ": Reloaded",
            ": Redemption",
            ": Uprising"
    };

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
            seedUser("guest", "guest@cyan.com", "Guest@123", Role.USER);

            if (ratingRepository.count() == 0 && movies.size() >= 2) {
                ratingRepository.saveAll(List.of(
                        Rating.builder().movie(movies.get(0)).user(viewer).value(5).build(),
                        Rating.builder().movie(movies.get(1)).user(viewer).value(4).build()
                ));
            }

            if (commentRepository.count() == 0 && movies.size() >= 2) {
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
        if (movieRepository.count() >= 1000) {
            Pageable pageable = PageRequest.of(0, 100);
            return movieRepository.findAllByOrderByRatingDescReleaseYearDesc(pageable);
        }

        List<Movie> allMovies = buildAllMovies();
        List<Movie> saved = new ArrayList<>(allMovies.size());
        for (int i = 0; i < allMovies.size(); i += 100) {
            int end = Math.min(i + 100, allMovies.size());
            saved.addAll(movieRepository.saveAll(allMovies.subList(i, end)));
        }
        return saved;
    }

    private List<Movie> buildAllMovies() {
        List<Movie> list = new ArrayList<>();
        list.addAll(buildFeaturedMovies());
        list.addAll(buildActionMovies());
        list.addAll(buildComedyMovies());
        list.addAll(buildDramaMovies());
        list.addAll(buildSciFiMovies());
        list.addAll(buildHorrorMovies());
        list.addAll(buildRomanceMovies());
        list.addAll(buildThrillerMovies());
        list.addAll(buildAnimationMovies());
        list.addAll(buildAdventureMovies());
        list.addAll(buildMysteryMovies());
        list.addAll(buildDocumentaryMovies());
        list.addAll(buildFantasyMovies());
        return list;
    }

    private List<Movie> buildFeaturedMovies() {
        return new ArrayList<>(List.of(
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
        ));
    }

    private List<Movie> generateGenreMovies(
            String genre,
            String[] titleBases,
            String[] descriptions,
            String[] thumbnails,
            int genreSalt
    ) {
        List<Movie> list = new ArrayList<>();
        int idx = 0;
        for (String base : titleBases) {
            for (String suffix : TITLE_SUFFIXES) {
                String title = (base + suffix);
                String description = descriptions[idx % descriptions.length];
                String thumbnail = thumbnails[idx % thumbnails.length];
                int releaseYear = 1990 + ((idx * 11 + genreSalt * 3) % 35);
                double rating = naturalRating(idx, genreSalt);
                list.add(Movie.builder()
                        .title(title)
                        .description(description)
                        .thumbnailUrl(thumbnail)
                        .youtubeEmbedUrl(PLACEHOLDER_EMBED)
                        .genre(genre)
                        .releaseYear(releaseYear)
                        .rating(rating)
                        .build());
                idx++;
            }
        }
        return list;
    }

    private double naturalRating(int idx, int genreSalt) {
        int mod = (idx + genreSalt) % 20;
        double raw;
        if (mod == 0) {
            raw = 5.3;
        } else if (mod == 1) {
            raw = 5.7;
        } else if (mod == 2) {
            raw = 9.1;
        } else if (mod == 3) {
            raw = 9.3;
        } else {
            raw = 6.2 + ((mod - 4) * 0.14);
        }
        if (raw < 5.0) raw = 5.0;
        if (raw > 9.5) raw = 9.5;
        return Math.round(raw * 10.0) / 10.0;
    }

    private List<Movie> buildActionMovies() {
        String[] bases = {
                "Bullet Storm", "Iron Siege", "Dead Zone Protocol", "Steel Warrior",
                "Hard Contract", "Phantom Edge", "Concrete Jungle", "Midnight Strike",
                "Rogue Mercenary", "Blackout Brigade", "Scar Tissue", "Last Insurgent"
        };
        String[] descriptions = {
                "A disgraced soldier is pulled back into action when a weapons syndicate threatens his city.",
                "Special forces breach a fortified island to rescue a kidnapped diplomat before time runs out.",
                "An elite operative navigates a collapsing city overrun by militarized drones.",
                "A retired bodyguard agrees to one last job that turns into a citywide manhunt.",
                "An armored convoy ambush forces a rookie marshal to finish a job far above her clearance.",
                "A veteran extracts a defector while every agency on the planet tries to stop him.",
                "A detective uncovers a weapons ring inside the department she was sworn to protect.",
                "A mercenary team is betrayed mid-mission and has to fight its way out of a hostile state.",
                "A family man takes up arms after cartel couriers cross the wrong quiet town.",
                "A cyber-heist goes loud when bullets replace keystrokes on the server room floor.",
                "A former SEAL hunts the private military that framed his unit.",
                "A street racer is coerced into a counter-terror sting through the heart of the city.",
                "An ex-assassin turns against the handlers who raised her.",
                "A border raid awakens a dormant defense network and reshapes an entire war.",
                "A squad of misfits defends a coastal town from a paramilitary invasion.",
                "A detective with a stolen badge turns a fugitive chase into an underground war.",
                "A stuntman is pulled into a real conspiracy when a film set becomes a crime scene.",
                "Two rival operatives must cooperate to stop a rogue general from selling a warhead.",
                "A veteran vigilante teaches a naive apprentice the price of justice on the edge.",
                "A wrongly convicted Ranger breaks out to expose the traitor running his unit."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1517602302552-471fe67acf66?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Action", bases, descriptions, thumbnails, 1);
    }

    private List<Movie> buildComedyMovies() {
        String[] bases = {
                "Office Chaos", "Wrong Number", "Double Booked", "Weekend Disaster",
                "Mistaken Identity", "Holiday Fiasco", "Rental Rage", "Fake Vacation",
                "Bad Neighbors", "Roommate Wars", "Sugar Rush", "Parking Riot"
        };
        String[] descriptions = {
                "A junior employee accidentally emails the company's darkest secrets to the entire firm.",
                "A mistaken text message sends two strangers on a cross-country adventure neither planned.",
                "Two rival event planners discover they booked the same mansion for opposing weddings.",
                "A barista pretends to be an heiress for a weekend and drags her best friend along.",
                "A mild-mannered librarian is repeatedly confused for an international jewel thief.",
                "A family holiday spirals when every relative arrives with a secret to unload.",
                "A pair of roommates try to evict a subletter who refuses to acknowledge the lease ended.",
                "A broke couple livestreams a fake honeymoon from their apartment with increasingly absurd lies.",
                "The new neighbors turn out to be reality TV producers and the block becomes their pilot.",
                "A rival dessert shop opens across the street and the two owners begin a passive-aggressive war.",
                "A shy accountant enters a karaoke championship to impress a crush who doesn't exist.",
                "A parking dispute escalates into a neighborhood-wide conspiracy of petty revenge.",
                "A failed food truck owner wins a cooking contest using his grandmother's questionable recipes.",
                "A wedding planner must stop three separate ceremonies from colliding in one hotel.",
                "A dog walker accidentally signs a record deal after his howling mutt goes viral.",
                "A pair of estranged cousins compete for an inheritance by completing absurd errands.",
                "Two rival food critics pose as a happy couple to get invited to the same restaurant launch.",
                "A delivery driver mistaken for a talent agent ends up managing a rising comedy act.",
                "A class reunion trip goes off the rails when old crushes and old grudges meet again.",
                "A harried executive swaps lives with her assistant for a week and neither survives it gracefully."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1518929458119-e5bf444c30f4?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Comedy", bases, descriptions, thumbnails, 2);
    }

    private List<Movie> buildDramaMovies() {
        String[] bases = {
                "Last Letter", "The Weight of Rain", "Quiet Harbor", "Before the Frost",
                "Paper Lanterns", "Fading Signal", "Whisper Creek", "The Long Hour",
                "After the Storm", "Cedar Avenue", "House of Strangers", "Unwritten Years"
        };
        String[] descriptions = {
                "A dying author discovers a box of unsent letters that change everything he believed about his life.",
                "Two estranged siblings reconnect to settle their mother's estate and unbury decades of silence.",
                "A small-town nurse chooses between staying loyal to family or saving a patient the town hates.",
                "A widower returning to his childhood home confronts the family he once left behind.",
                "A teacher risks her career to protect a gifted student from a system that wants him expelled.",
                "A pair of former classmates meet again at a reunion and unravel why their friendship ended.",
                "A factory worker fights to keep his elderly father from losing the only home he has known.",
                "A ballet dancer recovering from injury questions whether her craft is still worth the pain.",
                "A retired translator revisits the journals of a woman he once loved during wartime.",
                "A truck driver and a runaway teenager share a long journey that neither expected to survive.",
                "A lawyer takes on an unwinnable case that forces her to face an old family secret.",
                "A chef returns to her coastal hometown and confronts the rumors that drove her away.",
                "A minister in a fading parish struggles to reconcile doubt with the congregation he serves.",
                "A young woman inherits her grandfather's bookstore and uncovers a life she never imagined for him.",
                "A musician loses her hearing and rebuilds herself around the silence she feared.",
                "An architect returns to a project he abandoned and meets the family still waiting for him.",
                "A quiet mechanic becomes an unlikely confidant to the grieving neighbors who bring him their cars.",
                "A teenage caretaker navigates her mother's illness while discovering an unexpected mentor.",
                "A retired detective writes the story of his worst case and finally names the woman he failed.",
                "A pair of sisters open a bakery and confront the legacy of the recipes their father left behind."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1524985069026-dd778a71c7b4?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1518929458119-e5bf444c30f4?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Drama", bases, descriptions, thumbnails, 3);
    }

    private List<Movie> buildSciFiMovies() {
        String[] bases = {
                "Signal Lost", "Orbital", "Vector Prime", "Colony Drift",
                "Synthetic Dawn", "Quantum Harbor", "Pulse Horizon", "Nebula Zero",
                "Helix Protocol", "Deep Transit", "Perfect Recursion", "Cold Arcadia"
        };
        String[] descriptions = {
                "Scientists intercept an alien transmission that begins rewriting every computer system on Earth.",
                "Astronauts stranded on a dying space station must work with their AI to survive re-entry.",
                "A pilot discovers the colony she supplies each month has never existed on any real map.",
                "A terraforming crew wakes from cryosleep to find their planet already inhabited by humans.",
                "An engineer downloads her dead husband's memories and realizes he is still changing them.",
                "A diplomat negotiates with a species that perceives time as a single simultaneous moment.",
                "A cargo ship follows a rescue beacon to a derelict fleet that should have been scrapped decades ago.",
                "A lab assistant discovers that every successful experiment erases one of her memories.",
                "A colony ship's captain is woken two centuries early to resolve a contradiction in the mission log.",
                "A forensic analyst investigates crimes on a planet where every witness has been cloned twice.",
                "A synthetic child is placed with a grieving family and slowly reshapes their grief in ways it cannot explain.",
                "A signal from Earth's future interrupts a deep-space mission with instructions no one agrees to follow.",
                "An archivist is sent back along her own timeline to recover a single corrupted memory.",
                "A mining moon rebels against its corporate owners after discovering what the ore is truly used for.",
                "A team of physicists recreate an alien machine and accidentally open a port to their own past.",
                "A pair of pilots ferry refugees through a civil war fought over terraforming patents.",
                "An asteroid outpost becomes the last free port in a solar system under a sudden blockade.",
                "A courier is trusted with a memory crystal that every faction in the system wants erased.",
                "A botanist stranded on a living planet learns the vegetation is documenting her every move.",
                "A deep-space mechanic finds a signal that predicts every technical failure one minute before it happens."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1446776877081-d282a0f896e2?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1465101162946-4377e57745c3?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Sci-Fi", bases, descriptions, thumbnails, 4);
    }

    private List<Movie> buildHorrorMovies() {
        String[] bases = {
                "The Hollow", "Nightwatch", "The Cellar", "Dim Chapel",
                "Bone Orchard", "Quiet Shrine", "Black Harvest", "Fever Moon",
                "The Drowning", "Thin Walls", "Salt Path", "The Longing"
        };
        String[] descriptions = {
                "A family moves into an old farmhouse and discovers something ancient living in its walls.",
                "A security guard at a museum notices the exhibits behaving differently after midnight.",
                "A couple renovating a countryside home finds a sealed door that nobody on the street can see.",
                "A podcaster investigating a cult's disbanding begins hearing its chant in her own apartment.",
                "A rural pastor's sermons begin attracting people who should have died years ago.",
                "A children's show host confronts the puppets that survived long after the show was cancelled.",
                "A late-night diner becomes a way station for travelers who cannot leave without a trade.",
                "An orchard caretaker watches a new tree grow overnight and refuses to tell the owners what it produces.",
                "A film restorer splicing an old reel realizes the extras on screen are watching her back.",
                "A sleep clinic loses a patient who continues to appear on the cameras long after she was discharged.",
                "A coastal lighthouse keeper reports a wreck that never existed and cannot explain what washed ashore.",
                "A young nanny follows the children of the house into rooms she is sure were never built.",
                "A fisherman keeps pulling the same drowned stranger from three different lakes.",
                "A folklore researcher records a song and cannot stop herself from humming it at night.",
                "A moving crew delivers a crate that nobody ordered and nobody can get rid of.",
                "A baker's new apprentice refuses to work after midnight and the reason terrifies the owner.",
                "A forest ranger discovers footprints that only appear when the trail is empty.",
                "A funeral director whose clients keep returning tries to resign and cannot find the door.",
                "A radio DJ broadcasts at an impossible frequency and the signal brings listeners who never left their cars.",
                "A couple is gifted a portrait that ages in reverse and hides what the family used to look like."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1513106580091-1d82408b8cd6?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1458682625221-3a45f8a844c7?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Horror", bases, descriptions, thumbnails, 5);
    }

    private List<Movie> buildRomanceMovies() {
        String[] bases = {
                "Summer Letters", "Two Coasts", "A Quiet Spark", "Paper Boats",
                "Postcards Home", "Slow Dance", "The Lighthouse Season", "Tangerine Sky",
                "Seventh Avenue", "Borrowed Hours", "Long Way Back", "Morning Trains"
        };
        String[] descriptions = {
                "A travel writer and a widowed innkeeper fall for each other over a season of quiet postcards.",
                "Two pen pals who met online agree to see each other only after a year of written confessions.",
                "A florist and a musician keep crossing paths in the same small city for reasons neither can name.",
                "A journalist returns to her hometown and rediscovers the quiet boy she left behind at graduation.",
                "A grumpy bookstore owner and a free-spirited illustrator are forced to host an author tour together.",
                "A chef and a restaurant critic keep ending up at the same table through three strange nights.",
                "A pair of photographers covering the same wedding circuit begin writing letters neither mails.",
                "A long-distance couple must decide who will move for their relationship and who will stay.",
                "A taxi driver recognizes the woman he once loved, twenty years after she left for another life.",
                "A violinist substituting in a small-town orchestra finds herself drawn to its retiring conductor.",
                "A house-sitter and a returning expat negotiate an old family estate through a long wet autumn.",
                "A painter and a farmer share an old studio barn and debate what to do when the lease ends.",
                "A rideshare driver keeps picking up the same passenger until neither pretends it is accidental.",
                "A divorcee and a single father meet at a cooking class and start over together one recipe at a time.",
                "A radio host falls for a caller who refuses to say his name and starts leaving songs between segments.",
                "A train conductor keeps meeting the same commuter on the late run and decides to change his route.",
                "A baker writes names in the crust of every loaf until one customer notices hers is always there.",
                "A retired dancer gives lessons to a shy engineer who turns out to know exactly which step to take.",
                "A bookbinder restores a couple's old letters and realizes they wrote the story of her parents.",
                "A pair of rival food stall owners are forced to share a corner at a seaside night market."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1518929458119-e5bf444c30f4?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1524985069026-dd778a71c7b4?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Romance", bases, descriptions, thumbnails, 6);
    }

    private List<Movie> buildThrillerMovies() {
        String[] bases = {
                "The Watcher", "Cold Ledger", "Final Deposit", "Quiet Witness",
                "Glass House", "Chain of Custody", "Blind Drop", "Inner Harbor",
                "Red Courier", "Two Names", "Night Ferry", "The Arrangement"
        };
        String[] descriptions = {
                "A data analyst discovers her firm is laundering more than money and has one night to prove it.",
                "A defense attorney is blackmailed with evidence she has never seen before and cannot refute.",
                "A housekeeper overhears the wrong phone call and has to leave the city before dawn.",
                "A forensic accountant investigating a minor fraud unravels a conspiracy she was never meant to see.",
                "A journalist is sent a package from a source who has been missing for a decade.",
                "A family is told to continue their vacation as if nothing has happened and not ask why.",
                "A retired police officer takes a night-shift security job and recognizes a face from an unsolved case.",
                "A courier makes a routine drop and realizes she has been chosen to take the fall for something bigger.",
                "A woman wakes up with a stranger's name on her passport and a ticket to a country she has never heard of.",
                "A prosecutor's star witness disappears and every record of his life follows within the week.",
                "A stockbroker stumbles into an insider-trading ring and cannot tell who is running it.",
                "A nanny uses her old counterintelligence training to figure out which of her employer's friends is a threat.",
                "A private investigator takes a cheating case that turns into surveillance of her own life.",
                "A ferry captain realizes the same passenger has ridden his route every night for a month.",
                "A small-town reporter starts asking about an abandoned factory and wakes up to a town that no longer remembers her.",
                "A financial auditor is offered a promotion if she signs off on one final account she knows is fraudulent.",
                "A night doorman keeps a log of everyone who enters the building and gets hired to stop a murder.",
                "A translator hears a confession at a diplomatic dinner that she cannot legally repeat and cannot ignore.",
                "A radio engineer overhears an encrypted transmission and recognizes the voice on the other end.",
                "A family hides a guest for a single night and becomes the focus of a citywide search."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1517602302552-471fe67acf66?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1458682625221-3a45f8a844c7?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Thriller", bases, descriptions, thumbnails, 7);
    }

    private List<Movie> buildAnimationMovies() {
        String[] bases = {
                "Paper Sparrow", "Tiny Kingdom", "The Lantern Kid", "Crayon City",
                "Sugar Comet", "Origami Ocean", "Button Bear", "Cloud Atlas Jr",
                "Marshmallow Moon", "Lemon Grove", "Bramble Hollow", "Pocket Dragon"
        };
        String[] descriptions = {
                "A sparrow made of folded paper guides a lost child through a city that rewrites itself.",
                "The smallest inhabitant of a hidden kingdom rises to lead its forgotten guardians.",
                "A kid whose lantern never goes out travels the mainland to relight the towns it lost.",
                "A crayon drawing comes to life and tries to protect the kid who outgrew the box it lived in.",
                "A comet made of spun sugar lands in a bakery town and rewrites every family recipe.",
                "A paper boat kid sails his imagination across an ocean of origami storms and whale choirs.",
                "A worn-out button bear wakes on the night his family is about to throw him away.",
                "A cloud shaped like a family watches over a tiny town that cannot remember a single rainy day.",
                "A candy moon calls the village children outside for a night of marshmallow parades.",
                "A pair of lemon-grove cats outwit a caravan of raccoon thieves one citrus at a time.",
                "The bramble hollow elects a new mayor and the smallest hedgehog in town wins by accident.",
                "A dragon small enough to fit in a jacket pocket refuses to be left behind on a grand quest.",
                "A rescue puppy and an old clockmaker build a machine that sorts lost dreams.",
                "A village of folded-paper people wake up one morning to a strangely curious pair of scissors.",
                "A baker's apprentice becomes the guardian of a sugar comet and the town that shelters it.",
                "A tiny mechanic repairs the stars above her village and misses the one that repairs her.",
                "A choir of forest animals rehearses for an autumn festival while the woods threaten to close early.",
                "A brother and sister build a lantern boat to find their grandfather's lost recipe island.",
                "A kite kid teaches a gust of wind the name of every hill in her overgrown neighborhood.",
                "A retired circus bear returns to the village that loved him to train the next little act."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Animation", bases, descriptions, thumbnails, 8);
    }

    private List<Movie> buildAdventureMovies() {
        String[] bases = {
                "Copper Compass", "Wild Meridian", "Salt Road", "The Glacier Run",
                "Seventh Sail", "Monsoon Trail", "Rift Valley", "Lost Horizon Mile",
                "Driftwood Empire", "Northern Light", "Granite Passage", "Open Country"
        };
        String[] descriptions = {
                "Two siblings follow their father's journal across a desert toward a city he swore was real.",
                "A bush pilot agrees to ferry a naturalist across a mountain range carved by a forgotten river.",
                "A caravan master leads a trade party through a valley no one has come back from in a generation.",
                "A climbing team ascending a dormant peak find that it wakes up every hundred years.",
                "A sailor with a borrowed boat races monsoons to return a friend's ashes to an island on the map of nothing.",
                "Two guides cross a frozen coast to reach a lighthouse that keeps vanishing between passes.",
                "A geologist hunts a meteor strike across five countries and finds it has hunters of its own.",
                "An off-road courier is paid in old coins to deliver a letter to a town that prefers to stay hidden.",
                "A botanist's expedition crosses the flooded grasslands to catalogue a flower that blooms once a century.",
                "A pair of cousins retrace their grandfather's smuggling routes and find the cargo still waiting.",
                "An archaeologist and a mountain rescue pilot search for a lost monastery under a crumbling glacier.",
                "A coastal surveyor's team is trapped on an island that keeps growing every day.",
                "A teenage apprentice joins a postal rider's route across a country at the edge of a civil war.",
                "A photographer documents a migration of reindeer and realizes something is following the herd.",
                "A trail master leads a motley expedition into wilds that insist on being experienced alone.",
                "A pair of rival guides are forced into a partnership to rescue a caravan lost in a canyon.",
                "A fisherman tracks a rumor to a chain of islands where every village makes the same lighthouse keeper.",
                "A cartographer's notebook hints at a mountain pass no king has claimed and one season will reveal it.",
                "A ranger escorts a refugee family across a border that changes shape with the weather.",
                "A retired mountaineer accepts one last climb to bury a friend at a summit neither of them named."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1465101162946-4377e57745c3?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1446776877081-d282a0f896e2?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Adventure", bases, descriptions, thumbnails, 9);
    }

    private List<Movie> buildMysteryMovies() {
        String[] bases = {
                "Missing Heir", "The Dover File", "Ash Library", "Lamplight Detective",
                "Chapel Knot", "Ivory Notebook", "Blackwater Address", "The Quiet Lodger",
                "Case 47", "Silver Envelope", "Harbor Inquiry", "The Unnamed Guest"
        };
        String[] descriptions = {
                "A private investigator searches for a missing heir and discovers three people are claiming the estate.",
                "A cold-case archivist reopens a single folder and the town around her begins to forget its own history.",
                "A librarian notices patrons checking out the same book for twenty years and not a single copy matches.",
                "A rain-coat detective follows a lamplighter's route and pieces together a disappearance none of the gas lamps witnessed.",
                "A wedding photographer captures a silhouette in every group shot that no guest remembers meeting.",
                "An ex-journalist inherits her grandfather's notebook and the names in it start dying in order.",
                "A landlord sublets a quiet attic apartment and the lodger will not stop asking about the previous tenant.",
                "A harbor inspector investigates a missing container and finds a decades-old manifest under a new label.",
                "A forensic linguist reads a forged letter and recognizes the handwriting of someone declared dead.",
                "A retired magician consults on a string of thefts that keep leaving behind identical playing cards.",
                "A piano tuner finds a message hidden in a client's old upright that a past owner did not want found.",
                "A cruise ship's night manager investigates a guest no passenger manifest has listed.",
                "A hotel detective pieces together the disappearance of a regular guest who checked in for the last time decades ago.",
                "A genealogist chasing an impossible bloodline walks into the same name on four consecutive headstones.",
                "A pathologist notices every body in a small town shares a tiny identical mark.",
                "A bookshop cat witnesses every suspect in a small theft ring and makes friends with one too many.",
                "A painter's restoration of a family portrait reveals a face that should not be standing behind the children.",
                "A night watchman at an antiquarian auction recognizes a lot from a murder case he closed.",
                "A translator working on a dead writer's memoirs discovers she is not the first to be hired for the job.",
                "A maid investigating her employer's routine realizes she has been hired to replace someone."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1517602302552-471fe67acf66?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1458682625221-3a45f8a844c7?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Mystery", bases, descriptions, thumbnails, 10);
    }

    private List<Movie> buildDocumentaryMovies() {
        String[] bases = {
                "Atlas of Silence", "After the Factory", "Saltwater School", "Twenty Minutes to Shore",
                "The Copy Room", "Generations Project", "Night Kitchen Diaries", "A Working Season",
                "Quiet Elections", "Paper Archives", "Daybreak Crews", "The Quiet Broadcasts"
        };
        String[] descriptions = {
                "A year with the last letter carriers of a district that the national service has already retired.",
                "Five workers talk about life after their town's central factory closed for the final time.",
                "Teachers in a coastal school explain how they keep an entire curriculum afloat during each storm season.",
                "An intimate look at the lifeguards, ferrymen, and volunteers of a crowded summer waterfront.",
                "A nine-month diary of a legal copy room, filmed entirely between midnight and dawn.",
                "Three generations of a single family reflect on how they chose to live in a disappearing neighborhood.",
                "A restaurant's kitchen staff walks through a week of service, from opening deliveries to the last dishes.",
                "A traveling farmhand's season is followed through four harvests and the families that hired him.",
                "A careful portrait of a small-town election season, seen entirely through the volunteers who run it.",
                "An archive is packed up and moved to a new city; the archivists narrate every cart and carton.",
                "Each pre-dawn crew that keeps a major city running speaks about the quiet before the rush.",
                "A community radio station records a year of listener calls and local broadcasts.",
                "A portrait of a monastery bakery whose bread is shipped only once a month to a single town.",
                "Volunteers rebuild a historic lighthouse and the community that watches them argue about the paint color.",
                "A documentary crew spends a season following the workers who maintain a remote mountain railway.",
                "A retired fisherman teaches a film student the knots, tides, and weather he still trusts more than forecasts.",
                "The life of a migratory vineyard crew is followed from first pruning through to the bottling line.",
                "A portrait of public librarians who keep an after-hours reading group open through a winter of layoffs.",
                "A street theater company prepares a single production across seven boroughs and one lost rehearsal space.",
                "A record store's closing sale becomes the excuse for decades of regulars to explain why it mattered."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1446776877081-d282a0f896e2?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1465101162946-4377e57745c3?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1524985069026-dd778a71c7b4?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Documentary", bases, descriptions, thumbnails, 11);
    }

    private List<Movie> buildFantasyMovies() {
        String[] bases = {
                "Moth and Crown", "The Wandering Vault", "Nettle Queen", "Low Hollow Feast",
                "Coven Tides", "The Lantern Oath", "Moonbridge", "Saltwick Wood",
                "The Copper Wolf", "Runebound", "Starling Road", "The Quiet Spell"
        };
        String[] descriptions = {
                "A cloistered scribe inherits a throne she never wanted and the kingdom begins to remember why.",
                "A traveling vault full of forbidden books chooses a new librarian once every hundred years.",
                "The nettle queen of a crumbling marsh offers an outcast witch a last chance at her lineage.",
                "A pair of farmhands are invited to the winter feast of the low hollow and cannot politely refuse.",
                "A coven of tidal witches guard a coastline from the creatures that the old treaty left behind.",
                "An apprentice lamplighter is bound by oath to a flame that remembers every person it has watched.",
                "A runaway prince and a roadside healer cross a bridge of moonlight that charges strange tolls.",
                "Two foresters return to their hometown to find the woods have overrun the old borders in their absence.",
                "A copper wolf chooses a blacksmith's daughter as its last champion before the long hunt begins.",
                "A rune-bound courier carries a letter whose words cannot be read until the correct person sees them.",
                "Three sisters follow a flock of starlings across a kingdom's borders to reach a promised meeting.",
                "A village's last storyteller is offered a spell that can end the story of a single neighbor.",
                "A novice mage and a retired knight are forced to share a quest neither of them believes in anymore.",
                "The guild of quiet spells accepts a new apprentice who refuses to whisper anything at all.",
                "A charcoal burner's wife takes on a contract with a forest spirit to save their youngest.",
                "A wayfarer crosses a kingdom made entirely of borrowed favors and has none of her own to trade.",
                "A knight and a beast-speaker are sent to convince a dragon to return a stolen chapel bell.",
                "A wizard's last will assigns his tower to the person who can find which wall it is hiding behind today.",
                "A queen's cartographer is tasked with charting a forest that prefers not to be measured.",
                "A pair of mountain siblings inherit a cursed loom and learn to weave the future out of carefulness."
        };
        String[] thumbnails = {
                "https://images.unsplash.com/photo-1513106580091-1d82408b8cd6?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1200&q=80"
        };
        return generateGenreMovies("Fantasy", bases, descriptions, thumbnails, 12);
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
