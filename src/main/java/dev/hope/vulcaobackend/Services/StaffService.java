package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Mappers.CompanyMapper;
import dev.hope.vulcaobackend.Mappers.StaffMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class StaffService {
    @Autowired private StaffRepository staffRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private GlobalAccessControl globalAccessControl;
    @Autowired private CompanyMapper companyMapper;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private StaffMapper staffMapper;

    public StaffDTO findStaffById(String id){
        StaffModel staff =  staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff with id "+ id + " not found"));
        return staffMapper.transformData(staff);
    }

    public StaffDTO findStaffProfile(String sessionUser) {
        StaffModel user = staffRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("User With username: " + sessionUser + " not found"));
        return staffMapper.transformData(user);
    }


    public StaffDTO findStaffByEmail(String email){
        StaffModel staff =  staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Executive with email "+ email + " not found"));
        return staffMapper.transformData(staff);
    }

    public List<StaffDTO> findAllStaffs(String companyId){
        CompanyModel company = companyRepository.findById(companyId)
                .orElseThrow(()-> new RuntimeException("Company With Id "+ companyId + " not found"));
        return staffRepository.findCompanyStaffByCompanyId(companyId).stream()
                .map(staffMapper::transformData)
                .collect(Collectors.toList());
    }

    public StaffDTO findStaffByUsername(String username){
        StaffModel staff =  staffRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Executive with username"+ username + " not found"));
        return staffMapper.transformData(staff);
    }

    public void registerStaff(StaffModel newStaff, HttpServletRequest request) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        CompanyModel staffCompany = companyRepository.findById(newStaff.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company with id " + newStaff.getCompanyId() + " not found"));
        EmployerModel executive = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Executive with username" + sessionUser + " not found"));
        newStaff.setCompanyId(staffCompany.getId());
        newStaff.setCompanyName(staffCompany.getName());
        newStaff.setPassword(passwordEncoder.encode(newStaff.getPassword()));
        newStaff.setCreatorUsername(sessionUser);
        newStaff.setCreatorFullName(executive.getFullName());
        newStaff.onCreate();
        staffRepository.save(newStaff);
    }


    public void employerDeleteStaffById(String companyId, String executiveId, String staffID, HttpServletRequest request) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel executive = employerRepository.findById(executiveId)
                .orElseThrow(() -> new RuntimeException("Employer WITH ID "+ executiveId + " not found"));
        if (!sessionUser.equals(executive.getUsername())){
            throw new AccessDeniedException("Access Denied: The user that is trying to delete is not logged in");
        }
        StaffModel staffToBeDeleted = staffRepository.findById(staffID)
                .orElseThrow(() -> new RuntimeException("staff WITH ID "+ staffID + " not found"));
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(()
                -> new RuntimeException("Company " + companyId + " not found"));
        if(!staffToBeDeleted.getCompanyId().equals(company.getId()) ){
            throw new AccessDeniedException("You do not have permissions to delete this staff");
        }
        if(!staffToBeDeleted.getCreatorUsername().equals(executive.getUsername()) || !staffToBeDeleted.getCreatorId().equals(executive.getId())){
            throw new AccessDeniedException("You do not have permissions to delete this staff");
        }
        staffRepository.deleteStaff(staffToBeDeleted);
        System.out.println("SUCCESS: Staff deleted by employer");

    }

    public void staffDeleteStaffById(String companyId, String staffID, String staffIdToBeDeleted, HttpServletRequest request) throws AccessDeniedException {
        String sessionUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findById(staffID)
                .orElseThrow(() -> new RuntimeException("EXECUTIVE WITH ID "+ staffID + " not found"));
        if (!sessionUsername.equals(staff.getUsername())){
            throw new AccessDeniedException("Access Denied: The user that is trying to delete is not logged in");
        }
        StaffModel staffToBeDeleted = staffRepository.findById(staffIdToBeDeleted)
                .orElseThrow(() -> new RuntimeException("staff WITH ID "+ staffIdToBeDeleted + " not found"));
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(()
                -> new RuntimeException("Company " + companyId + " not found"));
        if(!staffToBeDeleted.getCompanyId().equals(company.getId()) ){
            throw new AccessDeniedException("You do not have permissions to delete this staff");
        }


        staffRepository.deleteStaff(staffToBeDeleted);
            System.out.println("SUCCESS: Staff deleted by employee");

    }

    public void assignPermissions(String staffID, Set<Permissions> permissionsSet, HttpServletRequest request) {
            String sessionUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            EmployerModel employer = employerRepository.findByUsername(sessionUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Employer with username " + sessionUsername + " not found"));
            StaffModel staff = staffRepository.findById(staffID).orElseThrow(() -> new RuntimeException("staff with id " + staffID + " not found"));
            staff.setAssignedPermissions(permissionsSet);
            staffRepository.save(staff);

    }

    public void employerUpdateStaff(String staffID, StaffModel updatedStaff, HttpServletRequest request) throws AccessDeniedException {
        String sessionUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUsername)
                .orElseThrow(()-> new UsernameNotFoundException("Employer with username "+ sessionUsername + " not found"));
        StaffModel staff = staffRepository.findById(staffID).orElseThrow(()-> new RuntimeException("staff with id "+ staffID + " not found"));
        staff.setUsername(updatedStaff.getUsername());
        staff.setFirstName(updatedStaff.getFirstName());
        staff.setLastName(updatedStaff.getLastName());
        staff.setEmail(updatedStaff.getEmail());
        staff.setJobTitle(updatedStaff.getJobTitle());
        staff.setAddress(updatedStaff.getAddress());
        staff.setDepartment(updatedStaff.getDepartment());
        staff.setAssignedPermissions(updatedStaff.getAssignedPermissions());
        if(!updatedStaff.getPassword().isBlank()){
            staff.setPassword(passwordEncoder.encode(updatedStaff.getPassword()));
        }
        staff.setHiredAt(updatedStaff.getHiredAt());
        staff.setPhoneNumber (updatedStaff.getPhoneNumber());
        staff.setSalary(updatedStaff.getSalary());
        staff.onUpdate();
        staffRepository.update(staff);
    }

    public void updateStaffAddress(String staffId, Address newAddress) throws AccessDeniedException {
        String sessionUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUsername)
                .orElseThrow(()-> new UsernameNotFoundException("Staff with username "+ sessionUsername + " not found"));

        if(!staff.getId().equals(staffId)){
            throw new AccessDeniedException("You do not have the permissions to do this action.");
        }
        staff.setAddress(newAddress);
        staff.onUpdate();
        staffRepository.update(staff);
    }


    public void employerAssignPermissionsToStaff(String staffID, Set<Permissions> newPermissions, HttpServletRequest request) throws AccessDeniedException {
        System.out.println(newPermissions);
        String sessionUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUsername)
                .orElseThrow(()-> new UsernameNotFoundException("Employer with username "+ sessionUsername + " not found"));
        StaffModel staff = staffRepository.findById(staffID).orElseThrow(()-> new RuntimeException("staff with id "+ staffID + " not found"));
        staff.setAssignedPermissions(newPermissions);
        staff.onUpdate();
        staffRepository.update(staff);
    }



    //EDIT STAFF CONTACT
    public void editStaffContact(String staffId, UserContactData updatedStaff, HttpServletRequest request) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("Staff With username: " + sessionUser + " not found"));
        if(!staff.getId().equals(staffId)){
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
        }
        staff.setEmail(updatedStaff.getEmail());
        staff.setPhoneNumber(updatedStaff.getPhoneNumber());
        staff.onUpdate();
        staffRepository.update(staff);
    }


    //EDIT StaffDTO INFO
    public void editStaffProfile(String staffId, UserFullName updatedUser, HttpServletRequest request) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("Staff With username: " + sessionUser + " not found"));
        if(!staff.getId().equals(staffId)){
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
        }
        staff.setFirstName(updatedUser.getFirstName());
        staff.setLastName(updatedUser.getLastName());
        staff.onUpdate();
        staffRepository.update(staff);
    }

    //EDIT CREDENTIALS
    public void editStaffCredentials(String username, UserCredentials newCredentials, HttpServletRequest request) throws AccessDeniedException {
        if(newCredentials.getUsername()== null || newCredentials.getPassword() == null){
            throw new RuntimeException("BOTH USERNAME AND PASSWORD MUST BE FILLED");
        }
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(
                ()-> new UsernameNotFoundException("Staff With username: " + sessionUser + " not found"));
        if(!staff.getUsername().equals(username)){
            throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO UPDATE THIS USER DATA");
        }

        staff.setUsername(newCredentials.getUsername());
        staff.setPassword(passwordEncoder.encode(newCredentials.getPassword()));
        staff.onUpdate();
        staffRepository.update(staff);
    }


}

