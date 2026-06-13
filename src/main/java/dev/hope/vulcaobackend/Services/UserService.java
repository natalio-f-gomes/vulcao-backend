package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Mappers.UserMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Repositories.MfaRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GlobalAccessControl globalAccessControl;



    public void save(UserModel newUser){
        if(newUser == null){
            log.warn("save() called with a null UserModel");
            throw new NullPointerException("THE USER CANNOT BE NULL");
        }
        // NOTE: never log newUser.getPassword() — only non-sensitive identifiers
        log.debug("save() -> creating new user, username={}, email={}", newUser.getUsername(), newUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        try{
            newUser.onCreate();

            userRepository.save(newUser);
            log.info("save() -> user persisted successfully, username={}, id={}", newUser.getUsername(), newUser.getId());
        }
        catch (Exception e){
            log.error("save() -> failed to persist user username={}: {}", newUser.getUsername(), e.getMessage(), e);
            throw new RuntimeException("There was an issue saving the new user Model " + e);
        }
    }

    public UserDTO findUserById(String userId){
        if(userId == null){
            log.warn("findUserById() called with a null userId");
            throw new NullPointerException("THE USER ID CANNOT BE NULL");
        }
        log.debug("findUserById() -> looking up userId={}", userId);
        UserModel user = userRepository.findById(userId).orElseThrow(()-> {
            log.warn("findUserById() -> no user found for userId={}", userId);
            return new RuntimeException("THERE ARE NO USERS WITH THIS ID ");
        });
        log.debug("findUserById() -> found user username={} for userId={}", user.getUsername(), userId);
        return userMapper.transformData(user);
    }

    public UserDTO findUserByUsername(String username){
        if(username == null){
            log.warn("findUserByUsername() called with a null username");
            throw new NullPointerException("THE USERNAME CANNOT BE NULL");
        }
        log.debug("findUserByUsername() -> looking up username={}", username);
        UserModel user = userRepository.findByUsername(username).orElseThrow(()-> {
            log.warn("findUserByUsername() -> no user found for username={}", username);
            return new RuntimeException("THERE ARE NO USERS WITH THIS username " + username);
        });
        log.debug("findUserByUsername() -> found user id={} for username={}", user.getId(), username);
        return userMapper.transformData(user);
    }


    public UserDTO findUserByEmail(String email){
        if(email == null){
            log.warn("findUserByEmail() called with a null email");
            throw new NullPointerException("THE USER ID CANNOT BE NULL");
        }
        log.debug("findUserByEmail() -> looking up email={}", email);
        UserModel user = userRepository.findByEmail(email).orElseThrow(()-> {
            log.warn("findUserByEmail() -> no user found for email={}", email);
            return new RuntimeException("THERE ARE NO USERS WITH THIS EMAIL " + email);
        });
        log.debug("findUserByEmail() -> found user username={} for email={}", user.getUsername(), email);
        return userMapper.transformData(user);
    }

    public List<UserDTO> findAllUsers(){
        log.debug("findAllUsers() -> fetching all users");
        List<UserDTO> users = userRepository.findAll().stream().map(
                userMapper::transformData
        ).collect(Collectors.toList());
        log.debug("findAllUsers() -> returned {} users", users.size());
        return users;
    }


    //DELETE USER
    public void deleteUserById(String  userID) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("deleteUserById() -> sessionUser={}, targetUserId={}", sessionUser, userID);

        UserModel user = userRepository.findById(userID).orElseThrow(
                ()-> {
                    log.warn("deleteUserById() -> user not found, targetUserId={}", userID);
                    return new RuntimeException("User With ID: " + userID + " not found");
                });

        if(!sessionUser.equals(user.getUsername())){
            log.warn("deleteUserById() -> access denied, sessionUser={} attempted to delete username={}", sessionUser, user.getUsername());
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSION TO UPDATE THIS USER");
        }
        userRepository.deleteUserById(userID);
        log.info("deleteUserById() -> user deleted, userId={}, username={}", userID, user.getUsername());
    }

    //FIND USER PROFILE
    public UserDTO findUserProfile(String sessionUser) {
        log.debug("findUserProfile() -> sessionUser={}", sessionUser);
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                ()-> {
                    log.warn("findUserProfile() -> user not found, username={}", sessionUser);
                    return new UsernameNotFoundException("User With username: " + sessionUser + " not found");
                });
        log.debug("findUserProfile() -> profile resolved for username={}, id={}", sessionUser, user.getId());
        return userMapper.transformData(user);
    }

    //EDIT USER CONTACT
    public void editUserContact(String userId, UserContactData updatedUser) throws Exception {
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            log.debug("editUserContact() -> sessionUser={}, targetUserId={}", sessionUser, userId);

            UserModel user = userRepository.findById(userId).orElseThrow(
                    ()-> {
                        log.warn("editUserContact() -> user not found, userId={}", userId);
                        return new UsernameNotFoundException("User With username: " + sessionUser + " not found");
                    });
            if(!user.getUsername().equals(sessionUser)){
                log.warn("editUserContact() -> access denied, sessionUser={} attempted to edit username={}", sessionUser, user.getUsername());
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userRepository.update(user);
            log.info("editUserContact() -> contact updated for username={}", sessionUser);
        }catch (Exception e){
            log.error("editUserContact() -> failed for userId={}: {}", userId, e.getMessage(), e);
            throw new Exception();
        }

    }

    //EDIT USER INFO
    public void editUserPersonalData(String userId, UserFullName updatedUser) throws Exception {
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            log.debug("editUserPersonalData() -> sessionUser={}, targetUserId={}", sessionUser, userId);

            UserModel user = userRepository.findById(userId).orElseThrow(
                    ()-> {
                        log.warn("editUserPersonalData() -> user not found, userId={}", userId);
                        return new UsernameNotFoundException("User With username: " + sessionUser + " not found");
                    });
            if(!user.getUsername().equals(sessionUser)){
                log.warn("editUserPersonalData() -> access denied, sessionUser={} attempted to edit username={}", sessionUser, user.getUsername());
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            userRepository.update(user);
            log.info("editUserPersonalData() -> personal data updated for username={}", sessionUser);
        }catch (Exception e){
            log.error("editUserPersonalData() -> failed for userId={}: {}", userId, e.getMessage(), e);
            throw new Exception();
        }

    }

    //EDIT USER Address
    public void editUserAddress(String userId, Address updatedAddress) throws AccessDeniedException {

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        log.debug("editUserAddress() -> sessionUser={}, targetUserId={}", sessionUser, userId);

        UserModel user = userRepository.findById(userId).orElseThrow(
                ()-> {
                    log.warn("editUserAddress() -> user not found, userId={}", userId);
                    return new UsernameNotFoundException("User With username: " + sessionUser + " not found");
                });
        if(!user.getUsername().equals(sessionUser)){
            log.warn("editUserAddress() -> access denied, sessionUser={} attempted to edit username={}", sessionUser, user.getUsername());
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
        }
        user.setAddress(updatedAddress);
        userRepository.update(user);
        log.info("editUserAddress() -> address updated for username={}", sessionUser);
    }

    //EDIT CREDENTIALS
    public void editUserCredentials(String userId, UserCredentials newCredentials) throws Exception {
        try{
            if(newCredentials.getUsername()== null || newCredentials.getPassword() == null){
                log.warn("editUserCredentials() -> rejected: username or password missing, userId={}", userId);
                throw new RuntimeException("BOTH USERNAME AND PASSWORD MUST BE FILLED");
            }
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            // NOTE: never log newCredentials.getPassword()
            log.debug("editUserCredentials() -> sessionUser={}, targetUserId={}, newUsername={}", sessionUser, userId, newCredentials.getUsername());

            UserModel user = userRepository.findById(userId).orElseThrow(
                    ()-> {
                        log.warn("editUserCredentials() -> user not found, userId={}", userId);
                        return new UsernameNotFoundException("User With username: " + sessionUser + " not found");
                    });
            if(!user.getUsername().equals(sessionUser)){
                log.warn("editUserCredentials() -> access denied, sessionUser={} attempted to edit username={}", sessionUser, user.getUsername());
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setUsername(newCredentials.getUsername());
            user.setPassword(passwordEncoder.encode(newCredentials.getPassword()));
            userRepository.update(user);
            log.info("editUserCredentials() -> credentials updated, oldUsername={}, newUsername={}", sessionUser, newCredentials.getUsername());
        }catch (Exception e){
            log.error("editUserCredentials() -> failed for userId={}: {}", userId, e.getMessage(), e);
            throw new Exception();
        }

    }
}
