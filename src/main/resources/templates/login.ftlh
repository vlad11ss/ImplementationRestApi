<!DOCTYPE html>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>
<h4>Авторизация</h4>
<form action="/login" method="post">
    Эл. почта: <input type="email" name="username" required><br><br>
    Пароль: <input type="password" name="password" required><br><br>
    <input type="submit" value="Вход"/>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
</form>
<a href="/registration">Зарегистрироваться</a>
</body>
</html>