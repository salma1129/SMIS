<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SMIS - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    <div class="card shadow-sm" style="width: 360px;">
        <div class="card-body">
            <h3 class="card-title text-center mb-4">SMIS Login</h3>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input name="username" type="text" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input name="password" type="password" class="form-control" required>
                </div>

                <% String error = (String) request.getAttribute("error");
                    if (error != null) { %>
                <div class="alert alert-danger py-2"><%= error %></div>
                <% } %>

                <button type="submit" class="btn btn-primary w-100">Login</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
