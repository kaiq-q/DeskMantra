package com.example.application.data.Repositories;


import com.example.application.data.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface RoleRepository extends CrudRepository<Role, Integer> {

    /**
     * Search for roles by name.
     *
     * @param searchTerm the search term in lowercase
     * @return the list of roles matching the search term
     */
    @Query("select r from Role r " +
           "where lower(r.name) like lower(concat('%', :searchTerm, '%'))")
    List<Role> search(@Param("searchTerm") String searchTerm);




}
