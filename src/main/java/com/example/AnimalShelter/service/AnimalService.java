package com.example.AnimalShelter.service;

import com.example.AnimalShelter.entity.AnimalEntity;
import com.example.AnimalShelter.repository.AnimalRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalEntity> getAll() {
        return animalRepository.findAll();
    }

    public AnimalEntity getById(long id) {
        return animalRepository.findById(id);
    }

    public AnimalEntity post(AnimalEntity animalEntity) {
        return  animalRepository.save(animalEntity);
    }

    public AnimalEntity put(AnimalEntity animalEntity) {
        return animalRepository.update(animalEntity);
    }

    public AnimalEntity patch(AnimalEntity animalEntity) {
        return animalRepository.update(animalEntity);
    }
    public void delete(long id) {
        animalRepository.delete(id);
    }
}
