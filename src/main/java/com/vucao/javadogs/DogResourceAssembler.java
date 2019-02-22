package com.vucao.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DogResourceAssembler implements ResourceAssembler<Dog, Resource<Dog>>
{
    @Override
    public Resource<Dog> toResource(Dog dog)
    {
        return new Resource<Dog>(dog,
                linkTo(methodOn(DogController.class).findOne(dog.getId())).withSelfRel(),
                linkTo(methodOn(DogController.class).all()).withRel("dogs"),
                linkTo(methodOn(DogController.class).allBreeds()).withRel("breeds"),
                linkTo(methodOn(DogController.class).allWeights()).withRel("weight"),
                linkTo(methodOn(DogController.class).findBreed(dog.getBreed().toLowerCase())).withSelfRel()
        );
    }
}
