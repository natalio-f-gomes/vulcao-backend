package dev.hope.vulcaobackend.Services.Offerings;


import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MovieDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.MovieModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.AuthRepository;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.*;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class MovieService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuthRepository authRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private TVShowRepository tvShowRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private MusicRepository musicRepository;
    @Autowired private OfferingsMapper offeringsMapper;

    private static final CharSequence[] SYMBOLS = {
            "[", "]", "{", "}", ",", "=", "...",
            "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
            "/", "\\", "|", ":", ";", "?", "<", ">",
            "~", "`", "'", "\"", "_",
            "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=",
            "/*", "*/", "//", "/**"
    };
    public boolean isMovieValid(MovieModel movie) {
        // Null check
        if (movie == null) {
            throw new RuntimeException("Movie object cannot be null");
        }

        // Name validation
        if (movie.getName() == null || movie.getName().isBlank()) {
            throw new RuntimeException("Movie name cannot be null or empty");
        }
        if (movie.getName().length() > 100) {
            throw new RuntimeException("Movie name cannot exceed 100 characters");
        }

        // Description validation
        if (movie.getDescription() == null || movie.getDescription().isBlank()) {
            throw new RuntimeException("Movie description cannot be null or empty");
        }
        if (movie.getDescription().length() > 2500) {
            throw new RuntimeException("Movie description cannot exceed 2500 characters");
        }

        // Price validation
        if (movie.getPricePerUnit() == null) {
            throw new RuntimeException("Movie price cannot be null");
        }
        if (movie.getPricePerUnit() <= 0) {
            throw new RuntimeException("Movie price must be greater than 0");
        }

        // Unit / currency / stock
        if (movie.getUnitType() == null) {
            throw new RuntimeException("Movie unit type cannot be null");
        }
        if (movie.getCurrency() == null) {
            throw new RuntimeException("Movie currency cannot be null");
        }
        if (movie.getStock() == null) {
            throw new RuntimeException("Movie stock cannot be null");
        }
        if (movie.getStock() < 0) {
            throw new RuntimeException("Movie stock cannot be negative");
        }

        // Company validation
        if (movie.getCompanyName() == null || movie.getCompanyName().isBlank()) {
            throw new RuntimeException("Movie must have a company name");
        }
        if (movie.getCompanyId() == null || movie.getCompanyId().isBlank()) {
            throw new RuntimeException("Movie must have a company ID");
        }

        // Directors
        if (movie.getDirectors() == null || movie.getDirectors().isEmpty()) {
            throw new RuntimeException("Movie must have at least one director");
        }
        for (String director : movie.getDirectors()) {
            if (director == null || director.isBlank()) {
                throw new RuntimeException("Director name cannot be null or empty");
            }
            if (director.length() > 100) {
                throw new RuntimeException("Director name cannot exceed 100 characters");
            }
        }

        // Lead actors
        if (movie.getLeadActors() == null || movie.getLeadActors().isEmpty()) {
            throw new RuntimeException("Movie must have at least one lead actor");
        }
        for (String actor : movie.getLeadActors()) {
            if (actor == null || actor.isBlank()) {
                throw new RuntimeException("Actor name cannot be null or empty");
            }
            if (actor.length() > 100) {
                throw new RuntimeException("Actor name cannot exceed 100 characters");
            }
        }

        // Producers (optional but validate if present)
        if (movie.getProducers() != null) {
            for (String producer : movie.getProducers()) {
                if (producer != null && producer.length() > 100) {
                    throw new RuntimeException("Producer name cannot exceed 100 characters");
                }
            }
        }

        // Genres
        if (movie.getGenres() == null || movie.getGenres().isEmpty()) {
            throw new RuntimeException("Movie must have at least one genre");
        }

        // Languages
        if (movie.getLanguages() == null || movie.getLanguages().isEmpty()) {
            throw new RuntimeException("Movie must have at least one language");
        }

        // Release date
        if (movie.getReleaseDate() == null) {
            throw new RuntimeException("Movie release date cannot be null");
        }
        if (movie.getReleaseDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Movie release date cannot be in the future");
        }

        // Country
        if (movie.getCountry() == null) {
            throw new RuntimeException("Movie country cannot be null");
        }

        // Age rating
        if (movie.getAgeRating() == null) {
            throw new RuntimeException("Movie age rating cannot be null");
        }

        log("MOVIE " + movie.getName() + " is Valid");
        return true;
    }

    public boolean canEmployerPerformAction(EmployerModel employer, HttpServletRequest request){
        String ip = IPService.getClientIpAddress(request);
        List<AuthModel> authData = authRepository.findByUserIpAddress(ip);
        if(authData.isEmpty()){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        for(AuthModel auth: authData){
            if(auth.getUsername().equals(employer.getUsername()) && auth.getEmail().equals(employer.getEmail())){
               return true;
            }
        }
        throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");
    }

    public boolean canStaffPerformAction(StaffModel staff, HttpServletRequest request, Permissions permission){
        String ip = IPService.getClientIpAddress(request);
        List<AuthModel> authData = authRepository.findByUserIpAddress(ip);
        if(authData.isEmpty()){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        if(!staff.getAssignedPermissions().contains(permission)){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        for(AuthModel auth: authData){
            if(auth.getUsername().equals(staff.getUsername()) && auth.getEmail().equals(staff.getEmail())){
                return true;
            }
        }
        throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");
    }

    public void employerSaveMovie(MovieModel newMovie, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(newMovie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newMovie.setId(UUID.randomUUID().toString());
        newMovie.setCompanyName(company.getName());
        newMovie.setCompanyId(company.getId());
        newMovie.setCreatorUsername(employer.getUsername());
        newMovie.setCreatorId(employer.getId());
        newMovie.onCreate();
        isMovieValid(newMovie);
        if(canEmployerPerformAction(employer, request)){movieRepository.save(newMovie);}
    }

    public void staffSaveMovie(MovieModel newMovie, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(newMovie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newMovie.setId(UUID.randomUUID().toString());
        newMovie.setCompanyName(company.getName());
        newMovie.setCompanyId(company.getId());
        newMovie.setCreatorUsername(staff.getUsername());
        newMovie.setCreatorId(staff.getId());
        newMovie.onCreate();
        isMovieValid(newMovie);
        if(canStaffPerformAction(staff, request, Permissions.ADD_PRODUCT)){movieRepository.save(newMovie);}
    }



    public void employerUpdateMovie(MovieModel movie, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(movie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isMovieValid(movie);
        movie.onUpdate();
        if(canEmployerPerformAction(employer, request)){movieRepository.update(movie);}

    }

    public void staffUpdateMovie(MovieModel movie, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(movie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isMovieValid(movie);
        movie.onUpdate();
        if(canStaffPerformAction(staff, request, Permissions.UPDATE_PRODUCT)){movieRepository.update(movie);}
    }

    public void employerDeleteMovie(String movieId, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        MovieModel movie = movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(movie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canEmployerPerformAction(employer, request)){movieRepository.deleteMovie(movie);}
    }

    public void staffDeleteMovie(String movieId, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        MovieModel movie = movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(movie.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canStaffPerformAction(staff, request, Permissions.DELETE_PRODUCT)){movieRepository.deleteMovie(movie);}
    }

    public List<MovieDTO> findAll(){
        return movieRepository.findAll().stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMoviesByCompanyId(String companyId){
        return movieRepository.findByCompanyId(companyId).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMoviesByCompanyName(String companyName){
        return movieRepository.findByCompanyId(companyName).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMoviesByCreatorId(String creatorId){
        return movieRepository.findByCreatorId(creatorId).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMoviesByCreatorName(String creatorUsername){
        return movieRepository.findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMoviesByCreatorUsername(String creatorUsername){
        return movieRepository. findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public MovieDTO findMovieById(String id){
        MovieModel movie = movieRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        return offeringsMapper.transformMovieModelToDTO(movie);
    }

    public List<MovieDTO> findMoviesByPrice(Integer price){
        return movieRepository.findByPrice(price).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }

    public List<MovieDTO> findMovieByName(String companyName){
        return movieRepository.findByName(companyName).stream().map(offeringsMapper::transformMovieModelToDTO).collect(Collectors.toList());
    }


}


