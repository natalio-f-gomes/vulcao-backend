package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.TVShowDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Offerings.TVShowModel;
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

@Slf4j
@Service
public class TVShowService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuthRepository  authRepository;
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
    public boolean isTVShowValid(TVShowModel tvShow) {
        if (tvShow == null) {
            throw new RuntimeException("TV Show object cannot be null");
        }

        if (tvShow.getName() == null || tvShow.getName().isBlank()) {
            throw new RuntimeException("TV Show name cannot be null or empty");
        }
        if (tvShow.getName().length() > 100) {
            throw new RuntimeException("TV Show name cannot exceed 100 characters");
        }

        if (tvShow.getDescription() == null || tvShow.getDescription().isBlank()) {
            throw new RuntimeException("TV Show description cannot be null or empty");
        }

        if (tvShow.getPricePerUnit() == null || tvShow.getPricePerUnit() <= 0) {
            throw new RuntimeException("TV Show price must be greater than 0");
        }

        if (tvShow.getUnitType() == null || tvShow.getCurrency() == null) {
            throw new RuntimeException("Unit type and currency are required");
        }

        if (tvShow.getStock() == null || tvShow.getStock() < 0) {
            throw new RuntimeException("Stock must be non-negative");
        }

        if (tvShow.getCompanyId() == null || tvShow.getCompanyName() == null) {
            throw new RuntimeException("TV Show must be linked to a company");
        }

        // Directors
        if (tvShow.getDirectors() == null || tvShow.getDirectors().isEmpty()) {
            throw new RuntimeException("At least one director is required");
        }

        // Actors
        if (tvShow.getLeadActors() == null || tvShow.getLeadActors().isEmpty()) {
            throw new RuntimeException("At least one lead actor is required");
        }

        // Genres & languages
        if (tvShow.getGenres() == null || tvShow.getGenres().isEmpty()) {
            throw new RuntimeException("At least one genre is required");
        }
        if (tvShow.getLanguages() == null || tvShow.getLanguages().isEmpty()) {
            throw new RuntimeException("At least one language is required");
        }

        // Release date
        if (tvShow.getReleaseDate() == null || tvShow.getReleaseDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Invalid release date");
        }

        // TV-specific
        if (tvShow.getSeasons() == null || tvShow.getSeasons() <= 0) {
            throw new RuntimeException("Seasons must be greater than 0");
        }

        if (tvShow.getEpisodes() == null || tvShow.getEpisodes() <= 0) {
            throw new RuntimeException("Episodes must be greater than 0");
        }

        if (tvShow.getEpisodeRuntime() != null && tvShow.getEpisodeRuntime() <= 0) {
            throw new RuntimeException("Episode runtime must be greater than 0");
        }

        if (tvShow.getStatus() == null) {
            throw new RuntimeException("TV Show status is required");
        }

        if (tvShow.getMediaType() == null) {
            throw new RuntimeException("Media type is required");
        }

        log.info("TV SHOW {} is valid", tvShow.getName());
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

    public void employerSaveTVShow(TVShowModel tvShow, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        CompanyModel company = companyRepository.findById(tvShow.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        tvShow.setId(UUID.randomUUID().toString());
        tvShow.setCompanyName(company.getName());
        tvShow.setCompanyId(company.getId());
        tvShow.setCreatorUsername(employer.getUsername());
        tvShow.setCreatorId(employer.getId());
        tvShow.onCreate();

        isTVShowValid(tvShow);

        if (canEmployerPerformAction(employer, request)) {
            tvShowRepository.save(tvShow);
        }
    }

    public void staffSaveTVShow(TVShowModel tvShow, String sessionUser, HttpServletRequest request) {
        StaffModel staff = staffRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found"));

        CompanyModel company = companyRepository.findById(tvShow.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        tvShow.setId(UUID.randomUUID().toString());
        tvShow.setCompanyName(company.getName());
        tvShow.setCompanyId(company.getId());
        tvShow.setCreatorUsername(staff.getUsername());
        tvShow.setCreatorId(staff.getId());
        tvShow.onCreate();

        isTVShowValid(tvShow);

        if (canStaffPerformAction(staff, request, Permissions.ADD_PRODUCT)) {
            tvShowRepository.save(tvShow);
        }
    }

    public void employerUpdateTVShow(TVShowModel tvShow, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        CompanyModel company = companyRepository.findById(tvShow.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isTVShowValid(tvShow);
        tvShow.onUpdate();

        if (canEmployerPerformAction(employer, request)) {
            tvShowRepository.update(tvShow);
        }
    }

    public void staffUpdateTVShow(TVShowModel tvShow, String sessionUser, HttpServletRequest request) {
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(tvShow.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isTVShowValid(tvShow);
        tvShow.onUpdate();

        if (canStaffPerformAction(staff, request,Permissions.UPDATE_PRODUCT)) {
            tvShowRepository.update(tvShow);
        }
    }


    public void employerDeleteTVShow(String tvShowId, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        TVShowModel tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new RuntimeException("TV Show not found"));

        if (canEmployerPerformAction(employer, request)) {
            tvShowRepository.deleteTvShow(tvShow);
        }
    }

    public void staffDeleteTVShow(String showId, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        TVShowModel show = tvShowRepository.findById(showId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(show.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canStaffPerformAction(staff, request, Permissions.DELETE_PRODUCT)){tvShowRepository.deleteTvShow(show);}
    }

    public List<TVShowDTO> findAll(){
        return tvShowRepository.findAll().stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMoviesByCompanyId(String companyId){
        return tvShowRepository.findByCompanyId(companyId).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMoviesByCompanyName(String companyName){
        return tvShowRepository.findByCompanyId(companyName).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMoviesProductsById(String creatorId){
        return tvShowRepository.findByCreatorId(creatorId).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMoviesByCreatorName(String creatorUsername){
        return tvShowRepository.findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMoviesByCreatorId(String creatorUsername){
        return tvShowRepository. findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public TVShowDTO findProductsById(String id){
        TVShowModel movie = tvShowRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        return offeringsMapper.transformTVShowModelToDTO(movie);
    }

    public List<TVShowDTO> findMoviesByPrice(Integer price){
        return tvShowRepository.findByPrice(price).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }

    public List<TVShowDTO> findMovieByName(String companyName){
        return tvShowRepository.findByName(companyName).stream().map(offeringsMapper::transformTVShowModelToDTO).collect(Collectors.toList());
    }


}


