<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LMS Dashboard</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .sidebar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            color: white;
        }
        .sidebar a {
            color: rgba(255,255,255,0.8);
            text-decoration: none;
            padding: 10px 15px;
            display: block;
            border-radius: 10px;
            transition: all 0.3s;
        }
        .sidebar a:hover, .sidebar a.active {
            background: rgba(255,255,255,0.2);
            color: white;
            transform: translateX(5px);
        }
        .sidebar i {
            margin-right: 10px;
        }
        .card-dashboard {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            transition: transform 0.3s;
        }
        .card-dashboard:hover {
            transform: translateY(-5px);
        }
        .balance-card {
            border-left: 5px solid;
            border-radius: 10px;
        }
        .balance-annual { border-left-color: #667eea; }
        .balance-sick { border-left-color: #f56565; }
        .balance-study { border-left-color: #48bb78; }
        .progress-bar-annual { background-color: #667eea; }
        .progress-bar-sick { background-color: #f56565; }
        .progress-bar-study { background-color: #48bb78; }
        .badge-pending { background-color: #fbbf24; color: #1a202c; }
        .badge-approved { background-color: #48bb78; }
        .badge-rejected { background-color: #f56565; }
        .navbar-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .btn-primary-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            color: white;
        }
        .btn-primary-custom:hover {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
            color: white;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="/dashboard">
            <i class="bi bi-calendar-check me-2"></i>LMS
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                        <span class="nav-link text-white">
                            <i class="bi bi-person-circle me-1"></i>
                            ${employee.name} (${employee.role})
                        </span>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="/logout">
                        <i class="bi bi-box-arrow-right me-1"></i>Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 p-0 sidebar">
            <div class="p-3">
                <div class="text-center mb-4">
                    <img src="https://via.placeholder.com/80" class="rounded-circle bg-white p-1 mb-2" alt="Profile">
                    <h5>${employee.name}</h5>
                    <p class="small mb-0">${employee.email}</p>
                </div>
                <hr class="bg-white">
                <a href="/dashboard" class="active">
                    <i class="bi bi-speedometer2"></i> Dashboard
                </a>
                <sec:authorize access="hasRole('MANAGER')">
                    <a href="/pending-approvals">
                        <i class="bi bi-clock-history"></i> Pending Approvals
                    </a>
                </sec:authorize>
                <a href="#">
                    <i class="bi bi-calendar-week"></i> Team Calendar
                </a>
                <a href="#">
                    <i class="bi bi-graph-up"></i> Reports
                </a>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 p-4">
            <!-- Welcome Banner -->
            <div class="alert alert-info bg-light border-0 rounded-3 mb-4">
                <h4 class="alert-heading">
                    <i class="bi bi-sun"></i> Welcome back, ${employee.name}!
                </h4>
                <p class="mb-0">Today is <strong><fmt:formatDate value="<%= new java.util.Date() %>" pattern="EEEE, MMMM d, yyyy"/></strong></p>
            </div>

            <!-- Manager: Pending Approvals Section -->
            <sec:authorize access="hasRole('MANAGER')">
                <c:if test="${not empty pendingLeaves}">
                    <div class="card card-dashboard mb-4 border-0">
                        <div class="card-header bg-white border-0">
                            <h5 class="mb-0">
                                <i class="bi bi-clock-history me-2 text-warning"></i>
                                Pending Approvals
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Employee</th>
                                        <th>Type</th>
                                        <th>Dates</th>
                                        <th>Reason</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pendingLeaves}" var="leave">
                                        <tr>
                                            <td>${leave.employeeName}</td>
                                            <td><span class="badge bg-secondary">${leave.leaveType}</span></td>
                                            <td>${leave.formattedStartDate} - ${leave.formattedEndDate}</td>
                                            <td>${leave.reason}</td>
                                            <td>
                                                <form action="/approve-leave" method="post" style="display: inline;">
                                                    <input type="hidden" name="leaveId" value="${leave.id}">
                                                    <button type="submit" class="btn btn-sm btn-success">
                                                        <i class="bi bi-check-circle"></i> Approve
                                                    </button>
                                                </form>
                                                <form action="/reject-leave" method="post" style="display: inline;">
                                                    <input type="hidden" name="leaveId" value="${leave.id}">
                                                    <button type="submit" class="btn btn-sm btn-danger">
                                                        <i class="bi bi-x-circle"></i> Reject
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
            </sec:authorize>

            <!-- Apply for Leave Form -->
            <div class="card card-dashboard mb-4 border-0">
                <div class="card-header bg-white border-0">
                    <h5 class="mb-0">
                        <i class="bi bi-plus-circle me-2 text-primary"></i>
                        Apply for Leave
                    </h5>
                </div>
                <div class="card-body">
                    <form action="/apply-leave" method="post" class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Leave Type</label>
                            <select class="form-select" name="leaveType" required>
                                <option value="" disabled selected>Select type</option>
                                <option value="ANNUAL">Annual Leave</option>
                                <option value="SICK">Sick Leave</option>
                                <option value="STUDY">Study Leave</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label fw-bold">Start Date</label>
                            <input type="date" class="form-control" name="startDate"
                                   min="<%= java.time.LocalDate.now() %>" required>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label fw-bold">End Date</label>
                            <input type="date" class="form-control" name="endDate"
                                   min="<%= java.time.LocalDate.now() %>" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Reason</label>
                            <input type="text" class="form-control" name="reason"
                                   placeholder="Reason for leave" required>
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary-custom w-100">
                                <i class="bi bi-send me-2"></i>Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Leave Status Table -->
            <div class="card card-dashboard mb-4 border-0">
                <div class="card-header bg-white border-0">
                    <h5 class="mb-0">
                        <i class="bi bi-table me-2 text-info"></i>
                        My Leave Requests
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty leaveRequests}">
                            <div class="alert alert-light text-center py-4">
                                <i class="bi bi-inbox fs-1 d-block mb-3 text-muted"></i>
                                <h6 class="text-muted">No leave requests found</h6>
                                <p class="small text-muted">Apply for leave using the form above</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Type</th>
                                        <th>Dates</th>
                                        <th>Reason</th>
                                        <th>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${leaveRequests}" var="req">
                                        <tr>
                                            <td>
                                                <span class="badge bg-secondary">${req.leaveType}</span>
                                            </td>
                                            <td>
                                                    ${req.formattedStartDate} - ${req.formattedEndDate}
                                            </td>
                                            <td>${req.reason}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${req.status == 'APPROVED'}">
                                                                <span class="badge bg-success">
                                                                    <i class="bi bi-check-circle"></i> Approved
                                                                </span>
                                                    </c:when>
                                                    <c:when test="${req.status == 'REJECTED'}">
                                                                <span class="badge bg-danger">
                                                                    <i class="bi bi-x-circle"></i> Rejected
                                                                </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                                <span class="badge bg-warning text-dark">
                                                                    <i class="bi bi-clock"></i> Pending
                                                                </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <p class="text-muted small mt-2">
                                <i class="bi bi-info-circle"></i> Leave resets on 1st January every year
                            </p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Leave Balance Cards -->
            <div class="row">
                <div class="col-md-4 mb-3">
                    <div class="card balance-card balance-annual border-0 shadow-sm">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <h6 class="fw-bold mb-0">Annual Leave</h6>
                                <span class="badge bg-primary">${balances.ANNUAL.used} / ${balances.ANNUAL.total} days</span>
                            </div>
                            <div class="progress mb-2" style="height: 10px;">
                                <div class="progress-bar progress-bar-annual" role="progressbar"
                                     style="width: ${balances.ANNUAL.percentage}%;"
                                     aria-valuenow="${balances.ANNUAL.percentage}" aria-valuemin="0" aria-valuemax="100">
                                </div>
                            </div>
                            <small class="text-muted">${balances.ANNUAL.remaining} days remaining</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-3">
                    <div class="card balance-card balance-sick border-0 shadow-sm">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <h6 class="fw-bold mb-0">Sick Leave</h6>
                                <span class="badge bg-danger">${balances.SICK.used} / ${balances.SICK.total} days</span>
                            </div>
                            <div class="progress mb-2" style="height: 10px;">
                                <div class="progress-bar progress-bar-sick" role="progressbar"
                                     style="width: ${balances.SICK.percentage}%;"
                                     aria-valuenow="${balances.SICK.percentage}" aria-valuemin="0" aria-valuemax="100">
                                </div>
                            </div>
                            <small class="text-muted">${balances.SICK.remaining} days remaining</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-3">
                    <div class="card balance-card balance-study border-0 shadow-sm">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <h6 class="fw-bold mb-0">Study Leave</h6>
                                <span class="badge bg-success">${balances.STUDY.used} / ${balances.STUDY.total} days</span>
                            </div>
                            <div class="progress mb-2" style="height: 10px;">
                                <div class="progress-bar progress-bar-study" role="progressbar"
                                     style="width: ${balances.STUDY.percentage}%;"
                                     aria-valuenow="${balances.STUDY.percentage}" aria-valuemin="0" aria-valuemax="100">
                                </div>
                            </div>
                            <small class="text-muted">${balances.STUDY.remaining} days remaining</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>