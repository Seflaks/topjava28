package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int idUser) {
        return repository.save(meal, idUser);
    }

    public void delete(int id, int idUser) {
        checkNotFoundWithId(repository.delete(id, idUser), id);
    }

    public Meal get(int id, int idUser) {
        return checkNotFoundWithId(repository.get(id, idUser), id);
    }

    public Collection<Meal> getAll(int idUser) {
        return repository.getAll(idUser);
    }

    public void update(Meal meal, int idUser) {
        checkNotFoundWithId(repository.save(meal, idUser), meal.getId());
    }
}