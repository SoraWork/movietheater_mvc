package com.movietheater.movietheater_mvc.services.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.dtos.movie.MovieCreateDTO;
import com.movietheater.movietheater_mvc.dtos.movie.MovieDTO;
import com.movietheater.movietheater_mvc.dtos.schedule.ScheduleDTO;
import com.movietheater.movietheater_mvc.dtos.type.TypeDTO;
import com.movietheater.movietheater_mvc.entities.Movie;
import com.movietheater.movietheater_mvc.entities.Schedule;
import com.movietheater.movietheater_mvc.repositories.CinemaRoomRepository;
import com.movietheater.movietheater_mvc.repositories.MovieRepository;
import com.movietheater.movietheater_mvc.repositories.ScheduleRepository;
import com.movietheater.movietheater_mvc.repositories.TypeRepository;
import com.movietheater.movietheater_mvc.services.MovieService;


import com.movietheater.movietheater_mvc.entities.Type;

import jakarta.persistence.criteria.Predicate;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final TypeRepository typeRepository;
    private final ScheduleRepository scheduleRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public MovieServiceImpl(MovieRepository movieRepository, TypeRepository typeRepository, ScheduleRepository scheduleRepository, CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.scheduleRepository = scheduleRepository;
        this.typeRepository = typeRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> findAll() {
       // Get List of entities
       var movies = movieRepository.findAll();
       // Convert List of entities to List of DTOs
       var movieDTOs = movies.stream().map(movie -> {
           var movieDTO = new MovieDTO();

           movieDTO.setMovieId(movie.getMovieId());
           movieDTO.setMovieNameEnglish(movie.getMovieNameEnglish());
           movieDTO.setMovieNameVn(movie.getMovieNameVn());
           movieDTO.setFromDate(movie.getFromDate());
           movieDTO.setToDate(movie.getToDate());
           movieDTO.setActor(movie.getActor());
           movieDTO.setDirector(movie.getDirector());
           movieDTO.setDuration(movie.getDuration());
           movieDTO.setVersion(movie.getVersion());
           movieDTO.setContent(movie.getContent());
           movieDTO.setLargeImage(movie.getLargeImage());
           return movieDTO;
       }).toList();

       return movieDTOs;
       }
    

    @Override
    public Page<MovieDTO> findAll(String keyword, Pageable pageable) {
        // Find recipe by keyword
        Specification<Movie> specification = (root, query, criteriaBuilder) -> {
            // Neu keyword null thi tra ve null
            if (keyword == null) {
                return null;
            }
        // Neu keyword khong null
        // WHERE LOWER(movieNameEnglish) LIKE %keyword%
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("movieNameEnglish")),
        "%" + keyword.toLowerCase() + "%");

         // WHERE LOWER(movieNameVn) LIKE %keyword%
         Predicate desPredicate = criteriaBuilder.like(root.get("movieNameVn"),
         "%" + keyword.toLowerCase() + "%");
          // WHERE LOWER(title) LIKE %keyword% OR LOWER(description) LIKE %keyword%
          return criteriaBuilder.or(titlePredicate, desPredicate);
        };
        var movies = movieRepository.findAll(specification, pageable);

        // Covert Page<Movie> to Page<MovieDTO>
        var movieDTOs = movies.map(movie -> {
            var movieDTO = new MovieDTO();
            movieDTO.setMovieId(movie.getMovieId());
            movieDTO.setMovieNameEnglish(movie.getMovieNameEnglish());
            movieDTO.setMovieNameVn(movie.getMovieNameVn());
            movieDTO.setFromDate(movie.getFromDate());
            movieDTO.setToDate(movie.getToDate());
            movieDTO.setActor(movie.getActor());
            movieDTO.setDirector(movie.getDirector());
            movieDTO.setDuration(movie.getDuration());
            movieDTO.setVersion(movie.getVersion());
            return movieDTO;
        });
        return movieDTOs;

    
    }

    @Override
    public MovieDTO findById(UUID id) {
        // Get entity by id
        var movie = movieRepository.findById(id).orElse(null);
        // Check if entity is null then return null
        if (movie == null) {
            return null;
        }
        // Convert entity to DTO
        var movieDTO = new MovieDTO();
        movieDTO.setMovieId(movie.getMovieId());
        movieDTO.setMovieNameEnglish(movie.getMovieNameEnglish());
        movieDTO.setMovieNameVn(movie.getMovieNameVn());
        movieDTO.setFromDate(movie.getFromDate());
        movieDTO.setToDate(movie.getToDate());
        movieDTO.setActor(movie.getActor());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setVersion(movie.getVersion());
        movieDTO.setContent(movie.getContent());
        movieDTO.setLargeImage(movie.getLargeImage());
         // Check if type is not null then set typeDTO to movieDTO
         if (movie.getTypes() != null) {
            var typeDTOs = new HashSet<TypeDTO>();
            movie.getTypes().forEach(type -> {
                var typeDTO = new TypeDTO();
                typeDTO.setId(type.getId());
                typeDTO.setTypeName(type.getTypeName());
                typeDTOs.add(typeDTO);
            });
            movieDTO.setTypes(typeDTOs);
        }
        if (movie.getCinemaRoomId() != null) {
            var cinemaRoom = cinemaRoomRepository.findById(movie.getCinemaRoomId()).orElse(null);
            if (cinemaRoom != null) {
                movieDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
            }
        }
        // Check if schedule is not null then set scheduleDTO to movieDTO
        if (movie.getSchedules() != null) {
            var scheduleDTOs = new HashSet<ScheduleDTO>();
            movie.getSchedules().forEach(schedule -> {
                var scheduleDTO = new ScheduleDTO();
                scheduleDTO.setId(schedule.getId());
                scheduleDTO.setScheduleTime(schedule.getScheduleTime());
                scheduleDTOs.add(scheduleDTO);
            });
            movieDTO.setSchedules(scheduleDTOs);
        }

    
        return movieDTO;
    }

    @Override
    public MovieDTO create(MovieCreateDTO movieCreateDTO) {
       // Check if movieCreateDTO is null then throw exception
        if (movieCreateDTO == null) {
            throw new IllegalArgumentException("MovieCreateDTO cannot be null");
        }
        // Convert DTO to entity
        var movie = new Movie();
        movie.setMovieNameEnglish(movieCreateDTO.getMovieNameEnglish());
        movie.setMovieNameVn(movieCreateDTO.getMovieNameVn());
        movie.setFromDate(movieCreateDTO.getFromDate());
        movie.setToDate(movieCreateDTO.getToDate());
        movie.setActor(movieCreateDTO.getActor());
        movie.setDirector(movieCreateDTO.getDirector());
        movie.setDuration(movieCreateDTO.getDuration());
        movie.setVersion(movieCreateDTO.getVersion());
        movie.setLargeImage(movieCreateDTO.getLargeImage());
        movie.setContent(movieCreateDTO.getContent());

        movie.generateShowDates();

        if(movieCreateDTO.getTypeName() != null && !movieCreateDTO.getTypeName().isEmpty()) {
            var existingTypes = typeRepository.findAll();
            System.out.println(existingTypes);
            Set<Type> types = movieCreateDTO.getTypeName().stream()
                    .map(typeName -> {
                        var existingType = existingTypes.stream()
                                .filter(type -> type.getTypeName().equals(typeName))
                                .findFirst()
                                .orElse(null);

                        if (existingType == null) {
                            throw new IllegalArgumentException("Type with name " + typeName + " does not exist");
                        }

                        return existingType;
                    })
                    .collect(Collectors.toSet());

                    movie.setTypes(types);
        }
        if(movieCreateDTO.getCinemaRoomName() != null && !movieCreateDTO.getCinemaRoomName().isEmpty()) {
            var existingCinemaRoom = cinemaRoomRepository.findByCinemaRoomName(movieCreateDTO.getCinemaRoomName());

            if (existingCinemaRoom == null) {
                throw new IllegalArgumentException("Cinema room with name " + movieCreateDTO.getCinemaRoomName() + " does not exist");
            }

            movie.setCinemaRoomId(existingCinemaRoom.getId());
        }
        if(movieCreateDTO.getScheduleTime() != null && !movieCreateDTO.getScheduleTime().isEmpty()) {
            var existingSchedules = scheduleRepository.findAll();
            System.out.println(existingSchedules);
            Set<Schedule> schedules = movieCreateDTO.getScheduleTime().stream()
                    .map(scheduleName -> {
                        var existingSchedule = existingSchedules.stream()
                                .filter(schedule -> schedule.getScheduleTime().equals(scheduleName))
                                .findFirst()
                                .orElse(null);
        
                        if (existingSchedule == null) {
                            throw new IllegalArgumentException("Schedule with name " + scheduleName + " does not exist");
                        }
        
                        return existingSchedule;
                    })
                    .collect(Collectors.toSet());
        
            movie.setSchedules(schedules); 
        }

        // Save entity
        var savedMovie = movieRepository.save(movie);
        // Convert entity to DTO
        var movieDTO = new MovieDTO();
        movieDTO.setMovieId(savedMovie.getMovieId());
        movieDTO.setMovieNameEnglish(savedMovie.getMovieNameEnglish());
        movieDTO.setMovieNameVn(savedMovie.getMovieNameVn());
        movieDTO.setFromDate(savedMovie.getFromDate());
        movieDTO.setToDate(savedMovie.getToDate());
        movieDTO.setActor(savedMovie.getActor());
        movieDTO.setDirector(savedMovie.getDirector());
        movieDTO.setDuration(savedMovie.getDuration());
        movieDTO.setVersion(savedMovie.getVersion());
        movieDTO.setLargeImage(savedMovie.getLargeImage());
         // Check if type is not null then set typeDTO to movieDTO
        if (savedMovie.getTypes() != null) {
            var typeDTOs = new HashSet<TypeDTO>();
            savedMovie.getTypes().forEach(type -> {
                var typeDTO = new TypeDTO();
                typeDTO.setId(type.getId());
                typeDTO.setTypeName(type.getTypeName());
                typeDTOs.add(typeDTO);
            });
            movieDTO.setTypes(typeDTOs);
        }
        if (savedMovie.getCinemaRoomId() != null) {
            var cinemaRoom = cinemaRoomRepository.findById(savedMovie.getCinemaRoomId()).orElse(null);
            if (cinemaRoom != null) {
                movieDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
            }
        }
        // Check if schedule is not null then set scheduleDTO to movieDTO
        if (savedMovie.getSchedules() != null) {
            var scheduleDTOs = new HashSet<ScheduleDTO>();
            savedMovie.getSchedules().forEach(schedule -> {
                var scheduleDTO = new ScheduleDTO();
                scheduleDTO.setId(schedule.getId());
                scheduleDTO.setScheduleTime(schedule.getScheduleTime());
                scheduleDTOs.add(scheduleDTO);
            });
            movieDTO.setSchedules(scheduleDTOs);
        }

        return movieDTO;
        }
        
    

    @Override
    public MovieDTO update(UUID id, MovieDTO movieDTO) {
            // Check if movieDTO is null then throw exception
            if (movieDTO == null) {
                throw new IllegalArgumentException("MovieDTO cannot be null");
            }
        
            // Get entity by id
            var movie = movieRepository.findById(id).orElse(null);
        
            // Check if entity is null then return null
            if (movie == null) {
                return null;
            }
        
            // Check if movie with the same title movieNameEnglish already exists
            var existingMovie = movieRepository.findByMovieNameEnglish(movieDTO.getMovieNameEnglish());
            if (existingMovie != null && !existingMovie.getMovieId().equals(id)) {
                throw new IllegalArgumentException("Movie with name " + movieDTO.getMovieNameEnglish() + " already exists");
            }
        
            // Check if movie with the same title movieNameVn already exists
            var existingMovieVn = movieRepository.findByMovieNameVn(movieDTO.getMovieNameVn());
            if (existingMovieVn != null && !existingMovieVn.getMovieId().equals(id)) {
                throw new IllegalArgumentException("Movie with name " + movieDTO.getMovieNameVn() + " already exists");
            }
        
            // Update basic fields
            movie.setMovieNameEnglish(movieDTO.getMovieNameEnglish());
            movie.setMovieNameVn(movieDTO.getMovieNameVn());
            movie.setFromDate(movieDTO.getFromDate());
            movie.setToDate(movieDTO.getToDate());
            movie.setActor(movieDTO.getActor());
            movie.setDirector(movieDTO.getDirector());
            movie.setDuration(movieDTO.getDuration());
            movie.setVersion(movieDTO.getVersion());
            movie.setLargeImage(movieDTO.getLargeImage());
            movie.setContent(movieDTO.getContent());;
        
            // Update types
            if (movieDTO.getTypeName() != null && !movieDTO.getTypeName().isEmpty()) {
                var existingTypes = typeRepository.findAll();
                System.out.println(existingTypes);
                Set<Type> types = movieDTO.getTypeName().stream()
                        .map(typeName -> {
                            var existingType = existingTypes.stream()
                                    .filter(type -> type.getTypeName().equals(typeName))
                                    .findFirst()
                                    .orElse(null);
    
                            if (existingType == null) {
                                throw new IllegalArgumentException("Type with name " + typeName + " does not exist");
                            }
    
                            return existingType;
                        })
                        .collect(Collectors.toSet());
    
                        movie.setTypes(types);
            }
        
            // Update cinema room
            if (movieDTO.getCinemaRoomName() != null && !movieDTO.getCinemaRoomName().isEmpty()) {
                var existingCinemaRoom = cinemaRoomRepository.findByCinemaRoomName(movieDTO.getCinemaRoomName());
                if (existingCinemaRoom == null) {
                    throw new IllegalArgumentException("Cinema room with name " + movieDTO.getCinemaRoomName() + " does not exist");
                }
                movie.setCinemaRoomId(existingCinemaRoom.getId());
            } else {
                movie.setCinemaRoomId(null);
            }
        
            // Update schedules
            if (movieDTO.getScheduleTime() != null && !movieDTO.getScheduleTime().isEmpty()) {
                var existingSchedules = scheduleRepository.findAll();
            System.out.println(existingSchedules);
            Set<Schedule> schedules = movieDTO.getScheduleTime().stream()
                    .map(scheduleName -> {
                        var existingSchedule = existingSchedules.stream()
                                .filter(schedule -> schedule.getScheduleTime().equals(scheduleName))
                                .findFirst()
                                .orElse(null);
        
                        if (existingSchedule == null) {
                            throw new IllegalArgumentException("Schedule with name " + scheduleName + " does not exist");
                        }
        
                        return existingSchedule;
                    })
                    .collect(Collectors.toSet());
        
            movie.setSchedules(schedules); 
            } else {
                movie.setSchedules(null);
            }
        
            // Save the updated entity
            var updatedMovie = movieRepository.save(movie);
        
            // Convert entity to DTO
            var updatedMovieDTO = new MovieDTO();
            updatedMovieDTO.setMovieId(updatedMovie.getMovieId());
            updatedMovieDTO.setMovieNameEnglish(updatedMovie.getMovieNameEnglish());
            updatedMovieDTO.setMovieNameVn(updatedMovie.getMovieNameVn());
            updatedMovieDTO.setFromDate(updatedMovie.getFromDate());
            updatedMovieDTO.setToDate(updatedMovie.getToDate());
            updatedMovieDTO.setActor(updatedMovie.getActor());
            updatedMovieDTO.setDirector(updatedMovie.getDirector());
            updatedMovieDTO.setDuration(updatedMovie.getDuration());
            updatedMovieDTO.setVersion(updatedMovie.getVersion());
            updatedMovieDTO.setLargeImage(updatedMovie.getLargeImage());
        
            // Set types in DTO
            if (updatedMovie.getTypes() != null) {
                var typeDTOs = updatedMovie.getTypes().stream()
                        .map(type -> {
                            var typeDTO = new TypeDTO();
                            typeDTO.setId(type.getId());
                            typeDTO.setTypeName(type.getTypeName());
                            return typeDTO;
                        })
                        .collect(Collectors.toSet());
                updatedMovieDTO.setTypes(typeDTOs);
            }
        
            // Set cinema room name in DTO
            if (updatedMovie.getCinemaRoomId() != null) {
                var cinemaRoom = cinemaRoomRepository.findById(updatedMovie.getCinemaRoomId()).orElse(null);
                if (cinemaRoom != null) {
                    updatedMovieDTO.setCinemaRoomName(cinemaRoom.getCinemaRoomName());
                }
            }
        
            // Set schedules in DTO
            if (updatedMovie.getSchedules() != null) {
                var scheduleDTOs = updatedMovie.getSchedules().stream()
                        .map(schedule -> {
                            var scheduleDTO = new ScheduleDTO();
                            scheduleDTO.setId(schedule.getId());
                            scheduleDTO.setScheduleTime(schedule.getScheduleTime());
                            return scheduleDTO;
                        })
                        .collect(Collectors.toSet());
                updatedMovieDTO.setSchedules(scheduleDTOs);
            }
        
            return updatedMovieDTO;
        }
        

    @Override
    public boolean deleteById(UUID id) {
       // Check if entity exists
        var movie = movieRepository.findById(id).orElse(null);
        // Check if entity is null then return false
        if (movie == null) {
            return false;
        }
        // Delete entity
        movieRepository.delete(movie);
        // Check if entity is deleted
        var isDeleted = movieRepository.findById(id).orElse(null) == null;
        return isDeleted;
    }

    @Override
    public List<Movie> getMoviesByShowDate(LocalDate showDate) {
        var movies = movieRepository.findByShowDate(showDate);
        System.out.println(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByTypeName(String typeName) {
        return movieRepository.findByTypeName(typeName);
    }

   

}
