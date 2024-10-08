package com.example.application.services;

import com.example.application.data.Repositories.RoleRepository;
import com.example.application.data.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

   private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){

        this.roleRepository = roleRepository;

    }

    public List<Role> findAllRoles(String filter){
        if (filter == null || filter.isEmpty()){

            List<Role> roleList = new ArrayList<>();
            roleRepository.findAll().forEach(roleList::add);
            return roleList;

        }else{
            return  roleRepository.search(filter);
        }
    }

    public long countRoles(){
        return roleRepository.count();
    }

    public void deleteRole(Role role){
        roleRepository.delete(role);
    }

    public void saveRole(Role role){
        if (role.getName().isEmpty()){
            System.err.println("Role is null. Are you sure you have connected your form to the application");
            return;
        }
        roleRepository.save(role);
    }

}
