package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Mappers.CompanyMapper;
import dev.hope.vulcaobackend.Mappers.EmployerMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service()
public class EmployerService {

    @Autowired private EmployerRepository employerRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployerMapper employerMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;


    public EmployerDTO findEmployerByUsername(String username){
        EmployerModel employer = employerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Employer with username " + username + " not found"));
        return employerMapper.transformData(employer);
    }

    public EmployerDTO findEmployerByEmail(String email){
        EmployerModel employer = employerRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Employer with email " + email + " not found"));
        return employerMapper.transformData(employer);
    }


    public EmployerDTO findEmployerById(String id){
        EmployerModel employer = employerRepository.findById(id).orElseThrow(()-> new RuntimeException("Employer with id " + id + " not found"));
        return employerMapper.transformData(employer);
    }

    public void registerEmployer(EmployerModel newEmployer){
        try {
            newEmployer.onCreate();
            newEmployer.setPassword(passwordEncoder.encode(newEmployer.getPassword()));
            employerRepository.save(newEmployer);
        }
          catch(Exception e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Executive with the same username or email already exists");
        }
    }

//    public List<BusinessDTO> listMyCompanies(String username){
//        EmployerModel employer = employerRepository.findByUsername(username)
//            .orElseThrow(()-> new UsernameNotFoundException("Employer with username "+ username + " not found"));
//        return businessRepository.findBusinessModelsByCreatorId(employer.getId()).stream()
//                .map(businessMapper::transformData)
//                .collect(Collectors.toList());
//    }

    public EmployerModel checkEmployerPermissions(String username) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(username).orElseThrow(() -> new
                UsernameNotFoundException("Employer with username " + username + " does not exist"));
        if(!sessionUser.equals(employer.getUsername())){
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS PROFILE");
        }
        return employer;
    }
    public void updateEmployerProfile(String username, UserFullName employerName) throws AccessDeniedException {
        EmployerModel employer = checkEmployerPermissions(username);
        employer.setFirstName(employerName.getFirstName());
        employer.setLastName(employerName.getLastName());
        employer.onUpdate();
        employerRepository.update(employer);

    }

    public void updateEmployerAddress(String username, Address address) throws AccessDeniedException {
        EmployerModel employer = checkEmployerPermissions(username);
        employer.setAddress(address);
        employer.onUpdate();
        employerRepository.update(employer);
    }

    public void updateEmployerCredentials(String username, UserCredentials credentials) throws AccessDeniedException {
        EmployerModel employer = checkEmployerPermissions(username);
        if(credentials.getPassword().isBlank() || credentials.getUsername().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        employer.setUsername(credentials.getUsername());
        employer.setPassword(passwordEncoder.encode(credentials.getPassword()));
        employer.onUpdate();
        employerRepository.update(employer);
    }

    public void updateEmployerContact(String username, UserContactData contactData) throws AccessDeniedException {
        EmployerModel employer = checkEmployerPermissions(username);
        employer.setEmail(contactData.getEmail());
        employer.setPhoneNumber(contactData.getPhoneNumber());
        employer.onUpdate();
        employerRepository.update(employer);
    }

    public EmployerDTO findEmployerProfile(String sessionUser) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
        return employerMapper.transformData(employer);
    }

    public List<EmployerDTO> findAllEmployers(){
        return employerRepository.findAll().stream()
                .map(employerMapper::transformData).collect(Collectors.toList());
    }

    public List<CompanyDTO> listMyCompanies(String username){
        EmployerModel employer = employerRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Employer with username "+ username + " not found"));
        return companyRepository.findByCreatorUsername(employer.getUsername()).stream()
                .map(companyMapper::transformData)
                .collect(Collectors.toList());
    }


}
