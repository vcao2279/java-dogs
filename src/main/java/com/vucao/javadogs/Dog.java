package com.vucao.javadogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Dog
{
    private @Id @GeneratedValue Long id;
    private String breed;
    private double weight;
    private boolean isApartmentSuitable;

    //needed for JPA
    public Dog()
    {

    }

    public Dog(String breed, double weight, boolean isApartmentSuitable)
    {
        this.breed = breed;
        this.weight = weight;
        this.isApartmentSuitable = isApartmentSuitable;
    }
}
