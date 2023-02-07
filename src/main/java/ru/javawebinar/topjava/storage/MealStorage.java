package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.Map;

public interface MealStorage {
    void create(Meal meal);
    void update(Meal meal);
    void delete(int id);
    Meal read(int id);
    Collection<Meal> readAll();
}
