package com.vucao.javadogs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long>
{
    List<Dog> findAllByOrderByBreedAsc();
    List<Dog> findAllByOrderByWeightDesc();
    List<Dog> findByIsApartmentSuitable(boolean isApartmentSuitable);
    List<Dog> findByBreed(String breed);
}
