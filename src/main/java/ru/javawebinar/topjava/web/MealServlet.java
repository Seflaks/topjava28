package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MemoryMealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new MemoryMealStorage();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.equals("") ? null : Integer.parseInt(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        storage.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("delete")) {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                storage.delete(id);
                response.sendRedirect("meals");
                log.debug("delete and redirect to meals");
            } else if (action.equals("update")) {
                request.setAttribute("meal", storage.get(Integer.parseInt(request.getParameter("id"))));
                request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);
                log.debug("update and redirect to meals");
            } else if (action.equals("add")) {
                request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);
                log.debug("add and redirect to meals");
            }
        } else {
            log.debug("redirect to meals");
            request.setAttribute("mealsTo", MealsUtil.getListMealTo(storage.readAll(), MealsUtil.CALORIES_PER_DAY_LIMIT));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }
}
