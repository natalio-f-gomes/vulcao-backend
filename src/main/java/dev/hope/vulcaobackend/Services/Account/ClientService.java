package dev.hope.vulcaobackend.Services.Account;


import dev.hope.vulcaobackend.DTOs.*;
import dev.hope.vulcaobackend.Models.MFAModel;
import dev.hope.vulcaobackend.Models.Shopping.CartModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.CartRepository;
import dev.hope.vulcaobackend.Repositories.MfaRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import dev.hope.vulcaobackend.Services.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {

    @Autowired
    UserRepository userRepository;
    @Autowired GlobalServices globalServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MfaRepository mfaRepository;
    @Autowired
    private CartRepository cartRepository;

    public UserModel convertDtoToModel(UserDTO newUser){
        UserModel user = new UserModel();
        user.setId(newUser.getId());
        user.setCreatedAt(newUser.getCreatedAt());
        user.setUpdatedAt(newUser.getUpdatedAt());
        user.setEmail(newUser.getEmail());
        user.setUserType(UserType.CLIENT.toString());
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

    public UserDTO convertModelToDto(UserModel model){
        UserDTO user = new UserDTO();
        user.setId(model.getId());
        user.setCreatedAt(model.getCreatedAt());
        user.setUpdatedAt(model.getUpdatedAt());
        user.setEmail(model.getEmail());
        user.setUsername(model.getUsername());
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        if(model.getAddress() != null){
            user.setAddress(model.getAddress());
        }
        user.setPhoneNumber(model.getPhoneNumber());
        return user;
    }

    public void save(UserDTO newUser){
        Validators.validateName(newUser.getFirstName(), newUser.getLastName());
        Validators.validateUsername(newUser.getUsername());
        Validators.validateEmail(newUser.getEmail());
        Validators.validatePassword(newUser.getPassword());
        String userId = globalServices.generateUserId(UserType.CLIENT);
        newUser.setId(userId);
        UserModel user = convertDtoToModel(newUser);
        user.onCreate();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        CartModel cart = new CartModel();
        cart.setId(userId);
        cart.onCreate();
        cartRepository.save(cart);
        System.out.println("UserId: "+ userId);
    }

    public UserDTO getProfile() {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Optional<UserModel> isUserPresent = userRepository.findByUsername(sessionUser);
        if(isUserPresent.isPresent()){
            UserModel user = isUserPresent.get();
            if(user.getUserType().equals(UserType.CLIENT.toString())){
                System.out.println("UserId: "+ user.getId());
                return convertModelToDto(user);
            }
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Não foi possível carregar o perfil do cliente: " + sessionUser);
    }

    private UserModel getClient(String username){
        UserModel user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "There is no client with this username")
        );
        if(!user.getUserType().equals(UserType.CLIENT.toString())){
            throw new ApiException(HttpStatus.NOT_FOUND, "There is no client with this username");
        }
        return user;
    }
    public void updateUserName(FullName newName) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserName() -> sessionUser={}", sessionUser);
        UserModel user = getClient(sessionUser);
        Validators.validateName(newName.getFirstName(), newName.getLastName());
        user.setFirstName(newName.getFirstName());
        user.setLastName(newName.getLastName());
        userRepository.update(user);
        log.info("editUserPersonalData() -> personal data updated for username={}", sessionUser);
    }

    public void updateUserAddress(Address address)  {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserName() -> sessionUser={}", sessionUser);
        UserModel user = getClient(sessionUser);
        Validators.validateAddress(address);
        user.setAddress(address);
        userRepository.update(user);
        log.info("updateUserAddress() -> personal address updated for username={}", sessionUser);
    }

    public void updateUserContact(Contact contact)  {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserContact() -> sessionUser={}", sessionUser);
        UserModel user = getClient(sessionUser);
        Validators.validateEmail(contact.getEmail());
        Validators.validatePhoneNumber(contact.getPhoneNumber());
        user.setEmail(contact.getEmail());
        user.setPhoneNumber(contact.getPhoneNumber());
        userRepository.update(user);
        log.info("updateUserContact() -> personal contact updated for username={}", sessionUser);
    }


    public String updateUserCredentials(Credentials credentials) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("updateUserContact() -> sessionUser={}", sessionUser);
        UserModel user = getClient(sessionUser);
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
        UserModel user =  globalServices.getClientByEmail(email);
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.update(user);
        mfaRepository.deleteMFAData(mfaData);
    }


    public UserDTO findById(String id) {
        return convertModelToDto(userRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
        ));
    }
}
