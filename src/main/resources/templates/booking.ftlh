<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Запись на сеанс в бассейн</title>
</head>
<body>
<h1>Запись на сеанс в бассейн</h1>
<#if errorMessage??>
    <p style="color:red;">${errorMessage}</p>
</#if>
<form action="/test" method="get">
    Поиск записи по дате: <input type="date" name="date"><br>
    <input type="submit" value="Поиск"/>
</form>
<form action="/booking" method="post">
    <p>Выберите дату:
        <input type="date" id="date" name="date" required>
    <p>Выберите время:
        <select id="time" name="time" required>
            <option value="10:00">10:00</option>
            <option value="11:00">11:00</option>
            <option value="12:00">12:00</option>
            <option value="13:00">13:00</option>
            <option value="14:00">14:00</option>
            <option value="15:00">15:00</option>
            <option value="16:00">16:00</option>
            <option value="17:00">17:00</option>
            <option value="18:00">18:00</option>
            <option value="19:00">19:00</option>
        </select>
    <p><input type="submit" value="записаться">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
</form>
</body>
</html>