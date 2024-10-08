package com.example.application.services;

import com.example.application.data.Permissions;
import com.example.application.data.Repositories.PermissionsRepository;
import com.example.application.data.Role;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionsService {

    private final PermissionsRepository permissionsRepository;

    public PermissionsService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    public Set<Permissions> findAllPermissions() {
        List<Permissions> permissionsList = (List<Permissions>) permissionsRepository.findAll();
        return new HashSet<>(permissionsList);  // Convert List to Set
    }


}
