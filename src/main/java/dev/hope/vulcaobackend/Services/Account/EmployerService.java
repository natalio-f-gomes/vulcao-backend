package dev.hope.vulcaobackend.Services.Account;

import dev.hope.vulcaobackend.DTOs.*;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.MFAModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Repositories.MfaRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.BusinessService;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import dev.hope.vulcaobackend.Services.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class EmployerService {

    @Autowired
    UserRepository userRepository;
    @Autowired GlobalServices globalServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MfaRepository mfaRepository;
    @Autowired ClientService clientService;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    BusinessService businessService;

    public UserModel convertDtoToModel(UserDTO newUser){
        UserModel user = new UserModel();
        user.setId(newUser.getId());
        user.setCreatedAt(newUser.getCreatedAt());
        user.setUpdatedAt(newUser.getUpdatedAt());
        user.setEmail(newUser.getEmail());
        user.setUserType(UserType.EMPLOYER.toString());
        user.setUsername(newUser.getUsername());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPassword(newUser.getPassword());
        if(newUser.getAddress() != null){
            user.setAddress(newUser.getAddress());
        }
        user.setPhoneNumber(newUser.getPhoneNumber());
        return user;
    }

    public void save(UserDTO newUser){
        Validators.validateName(newUser.getFirstName(), newUser.getLastName());
        Validators.validateUsername(newUser.getUsername());
        Validators.validateEmail(newUser.getEmail());
        Validators.validatePassword(newUser.getPassword());
        UserModel user = convertDtoToModel(newUser);
        String userId = globalServices.generateUserId(UserType.EMPLOYER);
        user.setId(userId);
        user.onCreate();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserDTO getProfile() {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Optional<UserModel> isUserPresent = userRepository.findByUsername(sessionUser);
        if(isUserPresent.isPresent()){
            UserModel user = isUserPresent.get();
            if(user.getUserType().equals(UserType.EMPLOYER.toString())){
                return clientService.convertModelToDto(user);
            }

        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Não foi possível carregar o perfil do empresário(a): " + sessionUser);
    }

    private UserModel getEmployer(String username){
        UserModel user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum(a) empresário(a) com este nome de usuário")
        );
        if(!user.getUserType().equals(UserType.EMPLOYER.toString())){
            throw new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum(a) empresário(a) com este nome de usuário");
        }
        return user;
    }

    public void updateName(FullName newName) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserName() -> sessionUser={}", sessionUser);
        UserModel user = getEmployer(sessionUser);
        Validators.validateName(newName.getFirstName(), newName.getLastName());
        user.setFirstName(newName.getFirstName());
        user.setLastName(newName.getLastName());
        userRepository.update(user);
        log.info("editUserPersonalData() -> personal data updated for username={}", sessionUser);
    }

    public void updateAddress(Address address)  {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateAddress() -> sessionUser={}", sessionUser);
        System.out.println("Country: " + address.getCountry());
        UserModel user = getEmployer(sessionUser);
        Validators.validateAddress(address);
        user.setAddress(address);
        userRepository.update(user);
        log.info("updateUserAddress() -> personal address updated for username={}", sessionUser);
    }

    public void updateContact(Contact contact)  {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserContact() -> sessionUser={}", sessionUser);
        UserModel user = getEmployer(sessionUser);
        Validators.validateEmail(contact.getEmail());
        Validators.validatePhoneNumber(contact.getPhoneNumber());
        user.setEmail(contact.getEmail());
        user.setPhoneNumber(contact.getPhoneNumber());
        userRepository.update(user);
        log.info("updateUserContact() -> personal contact updated for username={}", sessionUser);
    }


    public String updateCredentials(Credentials credentials) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserContact() -> sessionUser={}", sessionUser);
        UserModel user = getEmployer(sessionUser);
        Validators.validateUsername(credentials.getUsername());
        Validators.validatePassword(credentials.getPassword());
        user.setUsername(credentials.getUsername());
        user.setPassword(passwordEncoder.encode(credentials.getPassword()));
        userRepository.update(user);
        log.info("updateUserCredentials() -> personal credentials updated for username={}", sessionUser);
        return credentials.getUsername();
    }

    public void resetPassword(String email, ResetPasswordRequest passwordRequest){
        MFAModel mfaData = mfaRepository.findByUserEmail(email).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "Não foram encontrados dados de MFA para o e-mail deste usuário."));
        Validators.validatePassword(passwordRequest.getPassword());
        UserModel user =  globalServices.getEmployerByEmail(email);
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.update(user);
        mfaRepository.deleteMFAData(mfaData);
    }

    public List<BusinessDTO> getBusinesses() {
        UserDTO employer = getProfile();
        List<BusinessModel> businesses = businessRepository.findByCreatorId(employer.getId());
        return businesses.stream().map(
                businessService:: convertModelToDto
        ).toList();
    }
}
