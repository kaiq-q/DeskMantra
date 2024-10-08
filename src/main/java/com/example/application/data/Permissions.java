package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "Permissions", schema = "KaiqueTraining")
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String permission;

    @Column(nullable = true)
    private String description;

    public Permissions(){

    }

    public Permissions(String name, String description) {
        this.permission = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
