package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.DataTransferObjects.TaskDTO;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Models.TaskModel;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    @Autowired private TaskRepository taskRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private GlobalAccessControl globalAccessControl;
    @Autowired
    private CompanyRepository companyRepository;

    public void staffDeleteTask(String taskId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is not staff with this username: " + sessionUser));
        TaskModel task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("There is no task with this id: "+ taskId));
        if(!staff.getAssignedPermissions().contains(Permissions.DELETE_TASK)){
            throw new AccessDeniedException("ONLY STAFF WITH DELETE TASK CAN DO THIS ACTION");
        }
        if(!staff.getCompanyId().equals(task.getCompanyId())){
            throw new AccessDeniedException("THIS STAFF CANNOT DELETE THIS TASK BECAUSE THE STAFF "+ sessionUser + " WORKS FOR A COMPANY THAT DOES NOT HAVE THIS TASK");
        }
        taskRepository.deleteTask(task);
    }

    public void employerDeleteTask(String taskId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is not staff with this username: " + sessionUser));
        TaskModel task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("There is no task with this id: "+ taskId));
        List<String> companies = employer.getCompaniesRegistered();
        for(String companyId: companies){
            CompanyModel company = companyRepository.findById(companyId).orElseThrow(()-> new RuntimeException("THIS COMPANY DOES NOT EXIST IN EMPLOYER: "+ employer.getUsername() + " company list"));
            if(company.getId().equals(task.getCompanyId())){
                taskRepository.deleteTask(task);
                return;
            }
        }
        throw new AccessDeniedException("THE EMPLOYER CANNOT DELETE THIS TASK BECAUSE IT COULD NOT BE FOUND  IN ANY OF ITS COMPANIES");

    }


    public void createTaskAsEmployer(TaskModel task, String employerId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this username: "+ sessionUser));
        if(!employer.getId().equals(employerId)){
            throw new AccessDeniedException("Access denied trying to create a task");
        }
        CompanyModel company = companyRepository.findById(task.getCompanyId()).orElseThrow(()-> new RuntimeException("THIS TASK COMPANY ID DOES NOT EXIST"));
        List<String> companyIds = employer.getCompaniesRegistered();
        for(String companyId: companyIds){
            if(companyId.equals(company.getId())){
                globalAccessControl.isFieldValid(task.getTitle());
                globalAccessControl.isFieldValid(task.getDescription());
                task.setId(globalAccessControl.generateNewIdForTask());
                task.setCreatorId(employerId);
                taskRepository.save(task);
                return;
            }
        }
        throw new AccessDeniedException("THE EMPLOYER: "+ employer.getUsername() + " DOES NOT HAVE THE PERMISSIONS TO CREATE A TASK IN THIS COMPANY");
    }
    public void createTaskAsStaff(TaskModel task, String staffId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no staff with this username: "+ sessionUser));
        if(!staff.getId().equals(staffId)){
            throw new AccessDeniedException("Access denied trying to create a task");
        }
        if(!staff.getAssignedPermissions().contains(Permissions.CREATE_TASK)){
            throw new AccessDeniedException("Staff "+ staff.getUsername() + " does not have the required permission to create a task");
        }
        if(!staff.getCompanyId().equals(task.getCompanyId())){
            throw new AccessDeniedException("THIS STAFF CANNOT CREATE A NEW TASK BECAUSE " + sessionUser + " DO NOT WORK FOR THIS COMPANY");
        }
        globalAccessControl.isFieldValid(task.getTitle());
        globalAccessControl.isFieldValid(task.getDescription());
        task.setId(globalAccessControl.generateNewIdForTask());
        task.setCreatorId(staffId);
        taskRepository.save(task);
    }
    /** Employer Assign task to staff*/
    public void assignTaskToStaff(String taskId, String staffId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this username: "+ sessionUser));
        StaffModel staff = staffRepository.findById(staffId).orElseThrow(()-> new RuntimeException("There is no staff with this id: "+ staffId));
        TaskModel task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("There is no task with this id: "+ taskId));
        if(!staff.getCompanyId().equals(task.getCompanyId())){
            throw new AccessDeniedException("STAFF: "+ staff.getUsername() + " does not work in this company");
        }
        List<String> companyIds = employer.getCompaniesRegistered();
        for(String companyId: companyIds){
            if(companyId.equals(task.getCompanyId())){
                //task.setAssigneeId(staffId);
                taskRepository.update(task);
                return;
            }
        }
        throw new AccessDeniedException("THE STAFF: "+ staff.getUsername() + " DOES NOT WORK FOR ANY OF THE: " + employer.getUsername() + " companies" );

    }

    /** Staff Assign task to other peers*/
    public void delegateTaskToPeer(String taskId, String targetStaffId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        StaffModel staffAction = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no staff with this username: "+ sessionUser));
        if(!staffAction.getAssignedPermissions().contains(Permissions.ASSIGN_TASKS)){
            throw new AccessDeniedException("Staff "+ staffAction.getUsername() + " does not have the required permission to create a task");
        }
        StaffModel staffDelegated = staffRepository.findById(targetStaffId).orElseThrow(()-> new RuntimeException("There is no staff with this id: "+ targetStaffId));

        TaskModel task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("There is no task with this id: "+ taskId));
        if(!staffDelegated.getCompanyId().equals(task.getCompanyId())){
            throw new AccessDeniedException("THE STAFF: "+ staffDelegated.getUsername() + " DOES NOT WORK FOR COMPANY WITH ID" + task.getCompanyId());
        }
        //task.setAssigneeId(targetStaffId);
        taskRepository.update(task);
    }

    public void employerUpdateTask(String taskId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this username: "+ sessionUser));

    }

}
