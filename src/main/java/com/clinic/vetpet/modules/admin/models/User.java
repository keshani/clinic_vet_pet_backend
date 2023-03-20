package com.clinic.vetpet.modules.admin.models;

import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User entity for store user information
 *
 * @author Keshani
 * @since 2023/03/15
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @NotNull
    @Column(name = "USER_FULL_NAME")
    private String userFullName;

    @NotNull
    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @NotNull
    @Column(name = "ENABLED")
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<AnimalDetail> animals;

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String userFullName, String password, boolean enabled) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.password = password;
        this.enabled = enabled;
    }
}
