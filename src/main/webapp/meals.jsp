<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="meals?action=add">Add Meal</a><br>
<table border = "1" cellspacing="0" cellpadding="8">
    <tr>
        <td>DateTime</td>
        <td>Description</td>
        <td>Calories</td>
        <td></td>
        <td></td>
    </tr>
    <c:forEach var="mealTo" items="${requestScope.mealsTo}" >
        <tr style = "color:${mealTo.excess ? "red" : "green"};">
            <td>${mealTo.dateTime.toLocalDate()} ${mealTo.dateTime.toLocalTime()}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=update&id=${mealTo.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
</c:forEach>
</table>
</body>
</html>