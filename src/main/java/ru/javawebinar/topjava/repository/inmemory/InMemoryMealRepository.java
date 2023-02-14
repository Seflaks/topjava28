package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.mealsAdmin.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int idUser) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(idUser, userId -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int idUser) {
        return repository.get(idUser).remove(id) != null;
    }

    @Override
    public Meal get(int id, int idUser) {
        return repository.get(idUser).get(id);
    }

    @Override
    public Collection<Meal> getAll(int idUser) {
        return repository.get(idUser).values();
    }
}

