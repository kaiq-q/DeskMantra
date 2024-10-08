package com.example.application.data.Repositories;


import com.example.application.data.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<Users, Integer> {

    @Query("select u from Users u" + " "+
            "left join u.roles r"    + " "+
           "where lower(u.name) like lower(concat('%', :searchTerm, '%'))")
    List<Users> search(@Param("searchTerm") String searchTerm);

    @Query("select u from Users u" + " "+
            "left join u.roles r"    + " "+
            "where lower(u.login) = lower(:searchTerm)")
    Users findByUsername(@Param("searchTerm") String searchTerm);

}
