package com.vucao.javadogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}