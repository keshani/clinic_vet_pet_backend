package com.clinic.vetpet.modules.admin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Role entity which use to grant access to users
 *
 * @author Keshani
 * @since 2021/11/13
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private RoleTypes name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(RoleTypes name) {
        this.name = name;
    }

    public Role(Integer id, RoleTypes name) {
        this.id = id;
        this.name = name;
    }
}