<form action="/authenticateTheUser" method="POST">

    <input type="text" name="username" placeholder="Tên đăng nhập hoặc Email">
    <input type="password" name="password" placeholder="Mật khẩu">

    <button type="submit">Sign in</button>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>