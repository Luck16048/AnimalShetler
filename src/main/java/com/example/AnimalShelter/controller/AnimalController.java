package com.example.AnimalShelter.controller;

import com.example.AnimalShelter.entity.AnimalEntity;
import com.example.AnimalShelter.service.AnimalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")

public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalEntity> getAll() {
        return  animalService.getAll();
    }

    @GetMapping("/{id}")
    public AnimalEntity getById(@PathVariable long id) {
        return animalService.getById(id);
    }

    @PostMapping
    public AnimalEntity post(@RequestBody AnimalEntity animalEntity) {
        return animalService.post(animalEntity);
    }

    @PutMapping("/{id}")
    public AnimalEntity put(@PathVariable long id, @RequestBody AnimalEntity animalEntity) {
        return  animalService.put(animalEntity);
    }

    @PatchMapping("/{id}")
    public AnimalEntity patch(@PathVariable long id, @RequestBody AnimalEntity animalEntity) {
        return animalService.patch(animalEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        animalService.delete(id);
    }
}
