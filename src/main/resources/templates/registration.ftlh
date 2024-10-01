<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<hr>
<h4>Регистрация</h4>
<form action="/registration" method="post">
    Имя : <input type="text" name="name"><br><br>
    Фамилия : <input type="text" name="secondName"><br><br>
    Эл. почта: <input type="email" name="email"><br><br>
    Номер телефона: <input type="text" name="phoneNumber"><br><br>
    Пароль: <input type="password" name="password"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Регистрация"/>
</form>
</body>
</html>