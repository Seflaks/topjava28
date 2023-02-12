<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
  <h3><a href="index.html">Home</a></h3>
  <hr>
  <h2>${param.action == 'add' ? 'Add ' : 'Edit '} Meal</h2>
  <form method="post" action="meals">
      <fieldset>
      <input type="hidden" name="id" value="${meal.id}">
      <p>Выберите дату: <input type="datetime-local" value="${meal.dateTime}" name="dateTime"></p>
      <p>Описание: <input type="text" value="${meal.description}" name="description"></p>
      <p>Калории: <input type="number" value="${meal.calories}" name="calories"></p>
      <p><input type="submit" value="Отправить"><input type="reset" value="Сбросить"><button onclick="window.history.back()" type="button">Назад</button></p>
      </fieldset>
  </form>
</body>
</html>
