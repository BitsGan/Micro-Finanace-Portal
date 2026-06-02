<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Sangam Micro Finance</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Custom CSS -->
    <style>
        :root {
            --primary-color: #007bff;
            --sidebar-width: 250px;
        }
        
        body {
            background: #f5f6fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            height: 100vh;
            width: var(--sidebar-width);
            background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
            padding: 20px 0;
            z-index: 1000;
        }
        
        .sidebar-brand {
            color: white;
            text-align: center;
            padding: 20px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }
        
        .sidebar-brand h4 {
            margin: 0;
            font-weight: 600;
        }
        
        .sidebar-menu {
            list-style: none;
            padding: 0;
            margin-top: 30px;
        }
        
        .sidebar-menu li {
            margin: 5px 0;
        }
        
        .sidebar-menu a {
            color: rgba(255,255,255,0.7);
            text-decoration: none;
            padding: 15px 25px;
            display: block;
            transition: all 0.3s ease;
        }
        
        .sidebar-menu a:hover, .sidebar-menu a.active {
            color: white;
            background: rgba(255,255,255,0.1);
            border-left: 4px solid var(--primary-color);
        }
        
        .sidebar-menu i {
            width: 25px;
            margin-right: 10px;
        }
        
        .main-content {
            margin-left: var(--sidebar-width);
            padding: 20px;
        }
        
        .topbar {
            background: white;
            padding: 15px 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            height: 100%;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }
        
        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            margin-bottom: 15px;
        }
        
        .stat-icon.primary { background: rgba(0,123,255,0.1); color: #007bff; }
        .stat-icon.success { background: rgba(40,167,69,0.1); color: #28a745; }
        .stat-icon.warning { background: rgba(255,193,7,0.1); color: #ffc107; }
        .stat-icon.danger { background: rgba(220,53,69,0.1); color: #dc3545; }
        .stat-icon.info { background: rgba(23,162,184,0.1); color: #17a2b8; }
        
        .stat-value {
            font-size: 28px;
            font-weight: 700;
            color: #333;
        }
        
        .stat-label {
            color: #6c757d;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }
        
        .card-header {
            background: white;
            border-bottom: 1px solid #e9ecef;
            padding: 20px 25px;
            font-weight: 600;
        }
        
        .table {
            margin-bottom: 0;
        }
        
        .table th {
            border-top: none;
            font-weight: 600;
            color: #555;
        }
        
        .badge {
            padding: 8px 12px;
            border-radius: 8px;
        }
        
        .btn-action {
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 12px;
        }
        
        .notification-dropdown {
            position: relative;
        }
        
        .notification-badge {
            position: absolute;
            top: -5px;
            right: -5px;
            background: #dc3545;
            color: white;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            font-size: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-brand">
            <h4><i class="fas fa-university me-2"></i>Sangam</h4>
            <small class="text-muted">Micro Finance</small>
        </div>
        <ul class="sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="active"><i class="fas fa-tachometer-alt"></i>Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/members"><i class="fas fa-users"></i>Members</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/loans"><i class="fas fa-hand-holding-usd"></i>Loans</a></li>
            <li><a href="${pageContext.request.contextPath}/cashier/payment"><i class="fas fa-money-bill-wave"></i>Payments</a></li>
            <li><a href="${pageContext.request.contextPath}/notifications"><i class="fas fa-bell"></i>Notifications</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i>Logout</a></li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Topbar -->
        <div class="topbar">
            <div>
                <h5 class="mb-0">Admin Dashboard</h5>
                <small class="text-muted">Welcome back, Administrator</small>
            </div>
            <div class="d-flex align-items-center">
                <div class="notification-dropdown me-3">
                    <a href="${pageContext.request.contextPath}/notifications" class="btn btn-light">
                        <i class="fas fa-bell"></i>
                        <span class="notification-badge">${unreadCount}</span>
                    </a>
                </div>
                <div class="dropdown">
                    <button class="btn btn-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle me-2"></i>Admin
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Settings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
        
        <!-- Statistics Cards -->
        <div class="row g-4 mb-4">
            <div class="col-xl-3 col-md-6">
                <div class="stat-card">
                    <div class="stat-icon primary">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-value">${stats.totalMembers}</div>
                    <div class="stat-label">Total Members</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="stat-card">
                    <div class="stat-icon success">
                        <i class="fas fa-hand-holding-usd"></i>
                    </div>
                    <div class="stat-value">₹<fmt:formatNumber value="${stats.totalCollection}" pattern="#,##0.00"/></div>
                    <div class="stat-label">Total Collection</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="stat-card">
                    <div class="stat-icon warning">
                        <i class="fas fa-file-invoice-dollar"></i>
                    </div>
                    <div class="stat-value">${stats.activeLoans}</div>
                    <div class="stat-label">Active Loans</div>
                </div>
            </div>
            <div class="col-xl-3 col-md-6">
                <div class="stat-card">
                    <div class="stat-icon danger">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="stat-value">${stats.overdueEmis}</div>
                    <div class="stat-label">Overdue EMIs</div>
                </div>
            </div>
        </div>
        
        <!-- Charts Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-chart-bar me-2"></i>Monthly Collection</span>
                        <a href="#" class="btn btn-sm btn-primary">View Report</a>
                    </div>
                    <div class="card-body">
                        <canvas id="collectionChart" height="100"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-chart-pie me-2"></i>Loan Status
                    </div>
                    <div class="card-body">
                        <canvas id="loanStatusChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Tables Row -->
        <div class="row g-4">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-clock me-2"></i>Pending Loan Applications</span>
                        <a href="${pageContext.request.contextPath}/admin/loans?status=pending" class="btn btn-sm btn-outline-primary">View All</a>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Member</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="loan" items="${pendingLoans}" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <tr>
                                                <td>${loan.memberName}</td>
                                                <td>₹<fmt:formatNumber value="${loan.loanAmount}" pattern="#,##0.00"/></td>
                                                <td>${loan.requestDate}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/admin/loans" class="btn btn-sm btn-success btn-action">Review</a>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty pendingLoans}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted py-3">No pending loan applications</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-exclamation-circle me-2"></i>Overdue EMIs</span>
                        <a href="#" class="btn btn-sm btn-outline-danger">View All</a>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Member</th>
                                        <th>EMI Amount</th>
                                        <th>Due Date</th>
                                        <th>Days Overdue</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="emi" items="${overdueEMIs}" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <tr>
                                                <td>${emi.memberName}</td>
                                                <td>₹<fmt:formatNumber value="${emi.emiAmount}" pattern="#,##0.00"/></td>
                                                <td>${emi.dueDate}</td>
                                                <td><span class="badge bg-danger">${emi.daysOverdue} days</span></td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty overdueEMIs}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted py-3">No overdue EMIs</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Monthly Collection Chart
        var ctx1 = document.getElementById('collectionChart').getContext('2d');
        var collectionChart = new Chart(ctx1, {
            type: 'bar',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                datasets: [{
                    label: 'Collection (₹)',
                    data: [${stats.monthlyCollection * 0.8}, ${stats.monthlyCollection * 0.9}, ${stats.monthlyCollection}, ${stats.monthlyCollection * 1.1}, ${stats.monthlyCollection * 1.2}, ${stats.monthlyCollection * 1.1}],
                    backgroundColor: 'rgba(0, 123, 255, 0.7)',
                    borderColor: 'rgba(0, 123, 255, 1)',
                    borderWidth: 2,
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '₹' + value.toLocaleString();
                            }
                        }
                    }
                }
            }
        });
        
        // Loan Status Chart
        var ctx2 = document.getElementById('loanStatusChart').getContext('2d');
        var loanStatusChart = new Chart(ctx2, {
            type: 'doughnut',
            data: {
                labels: ['Active', 'Pending', 'Completed', 'Rejected'],
                datasets: [{
                    data: [${stats.activeLoans}, ${stats.pendingLoans}, ${stats.completedLoans}, ${stats.totalLoans - stats.activeLoans - stats.pendingLoans - stats.completedLoans}],
                    backgroundColor: [
                        'rgba(40, 167, 69, 0.8)',
                        'rgba(255, 193, 7, 0.8)',
                        'rgba(0, 123, 255, 0.8)',
                        'rgba(220, 53, 69, 0.8)'
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    </script>
</body>
</html>
