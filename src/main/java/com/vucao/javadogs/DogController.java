package com.vucao.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class DogController
{
    private final DogRepository dogrepos;
    private final DogResourceAssembler assembler;

    public DogController(DogRepository dogrepos, DogResourceAssembler assembler)
    {
        this.dogrepos = dogrepos;
        this.assembler = assembler;
    }

    @GetMapping("/dogs")
    public Resources<Resource<Dog>> all()
    {
        List<Resource<Dog>> dogs = dogrepos.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/{id}")
    public Resource<Dog> findOne(@PathVariable Long id)
    {
        Dog foundDog = dogrepos.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));
        return assembler.toResource(foundDog);
    }

    @GetMapping("/breeds")
    public Resources<Resource<Dog>> allBreeds()
    {
        List<Resource<Dog>> dogs = dogrepos.findAllByOrderByBreedAsc()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allBreeds()).withRel("breeds"));
    }

    @GetMapping("/weight")
    public Resources<Resource<Dog>> allWeights()
    {
        List<Resource<Dog>> dogs = dogrepos.findAllByOrderByWeightDesc()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allWeights()).withRel("wight"));
    }

    @GetMapping("/breeds/{breed}")
    public Resources<Resource<Dog>> findBreed(@PathVariable String breed)
    {
        List<Resource<Dog>> dogs = dogrepos.findByBreed(breed)
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs);
    }

    @GetMapping("/apartment")
    public Resources<Resource<Dog>> findByApartment()
    {
        List<Resource<Dog>> dogs = dogrepos.findByIsApartmentSuitable(true)
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs);
    }

    @PutMapping("/dogs/{id}")
    public ResponseEntity<?> updateDog(@RequestBody Dog newDog, @PathVariable Long id)
        throws URISyntaxException
    {
        Dog updatedDog = dogrepos.findById(id)
                .map(dog ->
                {
                    dog.setBreed(newDog.getBreed());
                    dog.setWeight(newDog.getWeight());
                    dog.setApartmentSuitable(newDog.isApartmentSuitable());
                    return dogrepos.save(dog);
                }).orElseGet(()->
                {
                    newDog.setId(id);
                    return dogrepos.save(newDog);
                });

        Resource<Dog> resource = assembler.toResource(updatedDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id)
    {
        dogrepos.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
