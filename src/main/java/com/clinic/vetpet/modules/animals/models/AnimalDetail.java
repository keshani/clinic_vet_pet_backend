package com.clinic.vetpet.modules.animals.models;

import com.clinic.vetpet.modules.admin.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Animal entity to store Animal data
 *
 * @author Keshani
 * @since 2023/03/15
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ANIMAL_DETAIL")
public class AnimalDetail implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "ANIMAL_NAME", nullable = false)
    private String animalName;

    @NotNull
    @Column(name = "ANIMAL_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalTypes animalType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonIgnore
    private User owner;


}
