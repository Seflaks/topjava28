package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MealStorageMemory implements MealStorage {
    private Map<Integer, Meal> storage = new HashMap<>();
    private int counter = 0;

    {
        MealsUtil.meals.forEach(this::create);
    }

    @Override
    public void create(Meal meal) {
        counter++;
        meal.setId(counter);
        storage.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public Meal read(int id) {
        return storage.get(id);
    }

    @Override
    public Collection<Meal> readAll() {
        return storage.values();
    }
}
