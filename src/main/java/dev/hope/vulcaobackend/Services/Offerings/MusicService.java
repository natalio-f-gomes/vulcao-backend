package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MusicDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Offerings.MusicModel;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MusicService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuthRepository  authRepository;
    @Autowired private BookRepository bookRepository;
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
    public boolean isMusicValid(MusicModel music) {
        if (music == null) {
            throw new RuntimeException("Music object cannot be null");
        }

        if (music.getName() == null || music.getName().isBlank()) {
            throw new RuntimeException("Music name cannot be null or empty");
        }
        if (music.getName().length() > 100) {
            throw new RuntimeException("Music name cannot exceed 100 characters");
        }

        if (music.getDescription() == null || music.getDescription().isBlank()) {
            throw new RuntimeException("TV Show description cannot be null or empty");
        }

        if (music.getPricePerUnit() == null || music.getPricePerUnit() <= 0) {
            throw new RuntimeException("TV Show price must be greater than 0");
        }

        if (music.getUnitType() == null || music.getCurrency() == null) {
            throw new RuntimeException("Unit type and currency are required");
        }

        if (music.getStock() == null || music.getStock() < 0) {
            throw new RuntimeException("Stock must be non-negative");
        }

        if (music.getCompanyId() == null || music.getCompanyName() == null) {
            throw new RuntimeException("Music  must be linked to a company");
        }

        if (music.getAlbumName() == null || music.getWriters().isEmpty() || music.getRecordLabel().isBlank()) {
            throw new RuntimeException("Music must be linked to a company");
        }

        for (String writer : music.getWriters()) {
            if (writer == null || writer.isBlank()) {
                throw new RuntimeException("Director name cannot be null or empty");
            }
            if (writer.length() > 100) {
                throw new RuntimeException("Director name cannot exceed 100 characters");
            }
        }


        log.info("Music {} is valid", music.getName());
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

    public void employerSaveMusic(MusicModel music, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        CompanyModel company = companyRepository.findById(music.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        music.setId(UUID.randomUUID().toString());
        music.setCompanyName(company.getName());
        music.setCompanyId(company.getId());
        music.setCreatorUsername(employer.getUsername());
        music.setCreatorId(employer.getId());
        music.onCreate();

        isMusicValid(music);

        if (canEmployerPerformAction(employer, request)) {
            musicRepository.save(music);
        }
    }

    public void staffSaveMusic(MusicModel music, String sessionUser, HttpServletRequest request) {
        StaffModel staff = staffRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found"));

        CompanyModel company = companyRepository.findById(music.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        music.setId(UUID.randomUUID().toString());
        music.setCompanyName(company.getName());
        music.setCompanyId(company.getId());
        music.setCreatorUsername(staff.getUsername());
        music.setCreatorId(staff.getId());
        music.onCreate();

        isMusicValid(music);

        if (canStaffPerformAction(staff, request, Permissions.ADD_PRODUCT)) {
            musicRepository.save(music);
        }
    }

    public void employerUpdateMusic(MusicModel music, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        CompanyModel company = companyRepository.findById(music.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isMusicValid(music);
        music.onUpdate();

        if (canEmployerPerformAction(employer, request)) {
            musicRepository.update(music);
        }
    }

    public void staffUpdateMusic(MusicModel music, String sessionUser, HttpServletRequest request) {
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(music.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isMusicValid(music);
        music.onUpdate();

        if (canStaffPerformAction(staff, request,Permissions.UPDATE_PRODUCT)) {
            musicRepository.update(music);
        }
    }


    public void employerDeleteMusic(String musicId, String sessionUser, HttpServletRequest request) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        MusicModel music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("TV Show not found"));

        if (canEmployerPerformAction(employer, request)) {
            musicRepository.deleteMusic(music);
        }
    }

    public void staffDeleteMusic(String musicId, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        MusicModel music = musicRepository.findById(musicId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(music.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canStaffPerformAction(staff, request, Permissions.DELETE_PRODUCT)){
            musicRepository.deleteMusic(music);}
    }

    public List<MusicDTO> findAll(){
        return musicRepository.findAll().stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicByCompanyId(String companyId){
        return musicRepository.findByCompanyId(companyId).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicByCompanyName(String companyName){
        return musicRepository.findByCompanyId(companyName).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicProductsById(String creatorId){
        return musicRepository.findByCreatorId(creatorId).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicByCreatorName(String creatorUsername){
        return musicRepository.findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicByCreatorId(String creatorUsername){
        return musicRepository. findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public MusicDTO findMusicById(String id){
        MusicModel music = musicRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        return offeringsMapper.transformMusicModelToDTO(music);
    }

    public List<MusicDTO> findMusicByPrice(Integer price){
        return musicRepository.findByPrice(price).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }

    public List<MusicDTO> findMusicByName(String companyName){
        return musicRepository.findByName(companyName).stream().map(offeringsMapper::transformMusicModelToDTO).collect(Collectors.toList());
    }


}


