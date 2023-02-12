package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.Map;

public interface MealStorage {
    Meal save(Meal meal);
    boolean delete(int id);
    Meal get(int id);
    Collection<Meal> readAll();
}
