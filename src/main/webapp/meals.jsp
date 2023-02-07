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
<table border = "1" cellspacing="0" cellpadding="8">
    <tr>
        <td>DateTime</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach var="mealTo" items="${requestScope.mealsTo}" >
        <tr style = "color:${mealTo.excess ? "red" : "green"};">
            <td>${mealTo.dateTime.toLocalDate()} ${mealTo.dateTime.toLocalTime()}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
</c:forEach>
</table>
</body>
</html>