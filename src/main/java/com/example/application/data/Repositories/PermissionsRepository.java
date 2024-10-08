package com.example.application.data.Repositories;

import com.example.application.data.Permissions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionsRepository extends CrudRepository<Permissions, Integer> {


}
