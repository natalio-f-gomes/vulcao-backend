package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Mappers.UserMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Cart.CartModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Repositories.Cart.CartRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.Offerings.GlobalAccessControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    CartRepository cartRepository;
    @Autowired
    private GlobalAccessControl globalAccessControl;


    public void save(UserModel newUser){
        if(newUser == null){throw new NullPointerException("THE USER CANNOT BE NULL");}
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        try{

            CartModel cart = new CartModel();
            cart.setId(newUser.getId());
            cartRepository.save(cart);
            newUser.onCreate();
            userRepository.save(newUser);
        }
        catch (Exception e){
            log.debug(e.toString());
            throw new RuntimeException("There was an issue saving the new user Model " + e);
        }
    }

    public UserDTO findUserById(String userId){
        if(userId == null){throw new NullPointerException("THE USER ID CANNOT BE NULL");}
        UserModel user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("THERE ARE NO USERS WITH THIS ID "));
        return userMapper.transformData(user);
    }

    public UserDTO findUserByUsername(String username){
        if(username == null){throw new NullPointerException("THE USERNAME CANNOT BE NULL");}
        UserModel user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("THERE ARE NO USERS WITH THIS username " + username));
        return userMapper.transformData(user);
    }


    public UserDTO findUserByEmail(String email){
        if(email == null){throw new NullPointerException("THE USER ID CANNOT BE NULL");}
        UserModel user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("THERE ARE NO USERS WITH THIS EMAIL " + email));
        return userMapper.transformData(user);
    }

    public List<UserDTO> findAllUsers(){
        return userRepository.findAll().stream().map(
                userMapper::transformData
        ).collect(Collectors.toList());
    }






    //DELETE USER
    public void deleteUserById(String  userID) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        System.out.println("CURRENT USER LOGGED IN: " + sessionUser);
        UserModel user = userRepository.findById(userID).orElseThrow(
                ()-> new RuntimeException("User With ID: " + userID + " not found"));

        if(!sessionUser.equals(user.getUsername())){
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSION TO UPDATE THIS USER");
        }
        userRepository.deleteUserById(userID);
    }

    //FIND USER PROFILE
    public UserDTO findUserProfile(String sessionUser) {
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
        return userMapper.transformData(user);
    }

    //EDIT USER CONTACT
    public void editUserContact(String username, UserContactData updatedUser) throws Exception {
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                    ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
            if(!user.getUsername().equals(username)){
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userRepository.update(user);
        }catch (Exception e){
            throw new Exception();
        }

    }

    //EDIT USER INFO
    public void editUserPersonalData(String username, UserFullName updatedUser) throws Exception {
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                    ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
            if(!user.getUsername().equals(username)){
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            userRepository.update(user);
        }catch (Exception e){
            System.out.println("There was an issue when trying to update the user profile "+e);
            throw new Exception();
        }

    }

    //EDIT USER Address
    public void editUserAddress(String username, Address updatedAddress) throws Exception {
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                    ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
            if(!user.getUsername().equals(username)){
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setAddress(updatedAddress);
            userRepository.update(user);
        }catch (Exception e){
            throw new Exception();
        }


    }

    //EDIT CREDENTIALS
    public void editUserCredentials(String username, UserCredentials newCredentials) throws Exception {
        try{
            if(newCredentials.getUsername()== null || newCredentials.getPassword() == null){
                throw new RuntimeException("BOTH USERNAME AND PASSWORD MUST BE FILLED");
            }
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(
                    ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
            if(!user.getUsername().equals(username)){
                throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
            }
            user.setUsername(newCredentials.getUsername());
            user.setPassword(passwordEncoder.encode(newCredentials.getPassword()));
            userRepository.update(user);
        }catch (Exception e){
            throw new Exception();
        }

    }






}
