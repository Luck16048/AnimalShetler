package com.example.AnimalShelter.repository;

import com.example.AnimalShelter.entity.AnimalEntity;
import org.jooq.DSLContext;

import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class AnimalRepository {

    private final DSLContext dsl;

    public AnimalRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<AnimalEntity> findAll() {
        return dsl.selectFrom("animal")
                .fetchInto(AnimalEntity.class);
    }

    public AnimalEntity findById(long id) {
        return dsl.selectFrom("animal")
                .where("id = ?", id)
                .fetchOneInto(AnimalEntity.class);
    }

    public AnimalEntity save(AnimalEntity animal) {
        dsl.insertInto(table("animal"))
                .columns(field("name"),
                        field("type"),
                        field("gender"),
                        field("status"))
                .values(animal.getName(),
                        animal.getType(),
                        animal.getGender().name(),
                        animal.getStatus().name())
                .execute();

        return animal;
    }

    public AnimalEntity update(AnimalEntity animal) {
        dsl.update(table("animal"))
                .set(field("name"),animal.getName())
                .set(field("type"), animal.getType())
                .set(field("gender"), animal.getGender().name())
                .set(field("status"), animal.getStatus().name())
                .where("id = ?", animal.getId())
                .execute();
        return animal;
    }

    public void delete(long id) {
         dsl.deleteFrom(table("animal"))
                .where("id = ?", id)
                 .execute();
    }
}
