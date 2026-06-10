package com.example.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnimalEntity {
    private long id;
    private String name;
    private String type;
    private GenderEnum gender;
    private StatusEnum status;
}
