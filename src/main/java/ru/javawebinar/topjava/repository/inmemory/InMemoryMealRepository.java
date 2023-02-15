package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.adminMeals.forEach(meal -> save(meal, 2));
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
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? new ArrayList<>() :
                repository.get(userId).values().stream()
                        .filter(meal -> DateTimeUtil.isBetweenHalfOpenDT(meal.getDateTime(), startDateTime, endDateTime))
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? new ArrayList<>() :
                repository.get(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

