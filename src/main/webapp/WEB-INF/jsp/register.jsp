<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LMS Registration</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" />
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .register-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
        }
        .register-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0;
            padding: 20px;
            text-align: center;
        }
        .register-header h2 {
            margin: 0;
            font-weight: 600;
        }
        .btn-register {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
            padding: 12px;
            font-weight: 600;
            transition: transform 0.2s;
        }
        .btn-register:hover {
            transform: translateY(-2px);
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center align-items-center min-vh-100">
        <div class="col-md-6">
            <div class="card register-card">
                <div class="register-header">
                    <h2>Create Account</h2>
                    <p class="mb-0">Join Leave Management System</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form method="post" action="/register">
                        <div class="mb-3">
                            <label for="name" class="form-label fw-bold">Full Name</label>
                            <div class="input-group">
                                    <span class="input-group-text bg-light">
                                        <i class="bi bi-person"></i>
                                    </span>
                                <input type="text" class="form-control" id="name" name="name"
                                       placeholder="Enter your full name" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label fw-bold">Email</label>
                            <div class="input-group">
                                    <span class="input-group-text bg-light">
                                        <i class="bi bi-envelope"></i>
                                    </span>
                                <input type="email" class="form-control" id="email" name="email"
                                       placeholder="Enter your email" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label fw-bold">Password</label>
                            <div class="input-group">
                                    <span class="input-group-text bg-light">
                                        <i class="bi bi-lock"></i>
                                    </span>
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Create a password" required>
                            </div>
                            <div class="form-text">Minimum 3 characters</div>
                        </div>
                        <div class="mb-4">
                            <label for="role" class="form-label fw-bold">Role</label>
                            <div class="input-group">
                                    <span class="input-group-text bg-light">
                                        <i class="bi bi-briefcase"></i>
                                    </span>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="" disabled selected>Select your role</option>
                                    <option value="EMPLOYEE">Employee</option>
                                    <option value="MANAGER">Manager</option>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-register w-100 mb-3">
                            <i class="bi bi-person-plus me-2"></i>Register
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <p class="mb-0">Already have an account?
                            <a href="/login" class="text-decoration-none fw-bold" style="color: #667eea;">
                                Login here
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>