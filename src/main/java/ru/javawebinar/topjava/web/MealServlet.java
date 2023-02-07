package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MealStorageMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new MealStorageMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            log.debug("redirect to meals");
            request.setAttribute("mealsTo", MealsUtil.getListMealTo(storage.readAll(), MealsUtil.CALORIES_PER_DAY_LIMIT));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                storage.delete(id);
                response.sendRedirect("meals");
        } else if (action.equals("update")) {
            request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);
        } else if (action.equals("add")) {
            request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);
        }
    }
}
