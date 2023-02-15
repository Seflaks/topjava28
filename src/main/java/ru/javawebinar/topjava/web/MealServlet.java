package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController repository;


    @Override
    public void init() {
//        repository = new InMemoryMealRepository();
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        repository = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (!id.isEmpty()) {
            repository.update(meal, Integer.parseInt(id));
        } else {
            repository.create(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                repository.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                String startDateStr = request.getParameter("startDate");
                LocalDate startDate;
                if (startDateStr == null || startDateStr.length() == 0) {
                    startDate = LocalDate.MIN;
                } else {
                    startDate = LocalDate.parse(startDateStr);
                }
                String endDateStr = request.getParameter("endDate");
                LocalDate endDate;
                if (endDateStr == null || endDateStr.length() == 0) {
                    endDate = LocalDate.now().plusYears(100);
                } else {
                    endDate = LocalDate.parse(endDateStr);
                }
                String startTimeStr = request.getParameter("startTime");
                LocalTime startTime;
                if (startTimeStr == null || startTimeStr.length() == 0) {
                    startTime = LocalTime.MIN;
                } else {
                    startTime = LocalTime.parse(startTimeStr);
                }
                String endTimeStr = request.getParameter("endTime");
                LocalTime endTime;
                if (endTimeStr == null || endTimeStr.length() == 0) {
                    endTime = LocalTime.MAX;
                } else {
                    endTime = LocalTime.parse(endTimeStr);
                }
                request.setAttribute("meals", repository.getBetween(startDate, startTime, endDate, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", repository.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
