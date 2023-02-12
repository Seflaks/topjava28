package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealStorage implements MealStorage {
    private Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
        }
        storage.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id) {
        return storage.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public Collection<Meal> readAll() {
        return storage.values();
    }
}
