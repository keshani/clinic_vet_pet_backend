package com.clinic.vetpet.modules.animals.models;

import com.clinic.vetpet.modules.admin.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * City entity to store City data
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Getter
@Setter
@Entity
@Table(name = "ANIMAL_DETAIL")
public class AnimalDetail implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ANIMAL_NAME", nullable = false)
    private String animalName;

    @Column(name = "ANIMAL_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalTypes animalType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonIgnore
    private User owner;


}
