<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard - Sangam Micro Finance</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
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
        
        .profile-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
        }
        
        .profile-avatar {
            width: 100px;
            height: 100px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 40px;
            color: #667eea;
        }
        
        .btn-pay {
            background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            color: white;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-pay:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(40,167,69,0.4);
            color: white;
        }
        
        .notification-item {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
            transition: background 0.3s ease;
        }
        
        .notification-item:hover {
            background: #f8f9fa;
        }
        
        .notification-item:last-child {
            border-bottom: none;
        }
        
        .notification-icon {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
        }
        
        .badge {
            padding: 8px 12px;
            border-radius: 8px;
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
            <li><a href="${pageContext.request.contextPath}/member/dashboard" class="active"><i class="fas fa-tachometer-alt"></i>Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/member/loans"><i class="fas fa-hand-holding-usd"></i>My Loans</a></li>
            <li><a href="${pageContext.request.contextPath}/member/pay-emi"><i class="fas fa-money-bill-wave"></i>Pay EMI</a></li>
            <li><a href="${pageContext.request.contextPath}/notifications"><i class="fas fa-bell"></i>Notifications</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i>Logout</a></li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Topbar -->
        <div class="topbar">
            <div>
                <h5 class="mb-0">Member Dashboard</h5>
                <small class="text-muted">Welcome back, ${member.name}</small>
            </div>
            <div class="d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/notifications" class="btn btn-light position-relative me-3">
                    <i class="fas fa-bell"></i>
                    <c:if test="${unreadCount > 0}">
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            ${unreadCount}
                        </span>
                    </c:if>
                </a>
                <div class="dropdown">
                    <button class="btn btn-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle me-2"></i>${member.name}
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Profile</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
        
        <!-- Profile & Quick Actions -->
        <div class="row g-4 mb-4">
            <div class="col-lg-4">
                <div class="profile-card">
                    <div class="profile-avatar">
                        <i class="fas fa-user"></i>
                    </div>
                    <h4>${member.name}</h4>
                    <p class="mb-2"><i class="fas fa-envelope me-2"></i>${member.email}</p>
                    <p class="mb-2"><i class="fas fa-phone me-2"></i>${member.phone}</p>
                    <p class="mb-3"><i class="fas fa-calendar me-2"></i>Member since ${member.joinDate}</p>
                    <a href="${pageContext.request.contextPath}/member/pay-emi" class="btn-pay">
                        <i class="fas fa-credit-card me-2"></i>Pay EMI Now
                    </a>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="stat-card">
                            <div class="stat-icon success">
                                <i class="fas fa-piggy-bank"></i>
                            </div>
                            <div class="stat-value">₹<fmt:formatNumber value="${member.totalSavings}" pattern="#,##0.00"/></div>
                            <div class="stat-label">Total Savings</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="stat-card">
                            <div class="stat-icon primary">
                                <i class="fas fa-hand-holding-usd"></i>
                            </div>
                            <div class="stat-value">${activeLoans}</div>
                            <div class="stat-label">Active Loans</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="stat-card">
                            <div class="stat-icon warning">
                                <i class="fas fa-file-invoice-dollar"></i>
                            </div>
                            <div class="stat-value">₹<fmt:formatNumber value="${totalLoanAmount}" pattern="#,##0.00"/></div>
                            <div class="stat-label">Total Loan Amount</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="stat-card">
                            <div class="stat-icon info">
                                <i class="fas fa-clock"></i>
                            </div>
                            <div class="stat-value">${pendingEMICount}</div>
                            <div class="stat-label">Pending EMIs</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Pending EMIs & Recent Payments -->
        <div class="row g-4">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-clock me-2"></i>Pending EMIs</span>
                        <a href="${pageContext.request.contextPath}/member/pay-emi" class="btn btn-sm btn-success">Pay Now</a>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>EMI #</th>
                                        <th>Amount</th>
                                        <th>Due Date</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="emi" items="${pendingEMIs}" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <tr>
                                                <td>${emi.emiNumber}</td>
                                                <td>₹<fmt:formatNumber value="${emi.emiAmount}" pattern="#,##0.00"/></td>
                                                <td>${emi.dueDate}</td>
                                                <td><span class="badge bg-warning">Pending</span></td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty pendingEMIs}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted py-3">No pending EMIs</td>
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
                    <div class="card-header">
                        <i class="fas fa-history me-2"></i>Recent Payments
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Amount</th>
                                        <th>Type</th>
                                        <th>Receipt</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="payment" items="${payments}">
                                        <tr>
                                            <td>${payment.paymentDate}</td>
                                            <td>₹<fmt:formatNumber value="${payment.amount}" pattern="#,##0.00"/></td>
                                            <td><span class="badge bg-success">${payment.paymentType}</span></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/receipt?transactionId=${payment.transactionId}" class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-download"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty payments}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted py-3">No payment history</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Notifications -->
        <div class="row g-4 mt-2">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-bell me-2"></i>Recent Notifications</span>
                        <a href="${pageContext.request.contextPath}/notifications" class="btn btn-sm btn-outline-primary">View All</a>
                    </div>
                    <div class="card-body p-0">
                        <c:forEach var="notification" items="${notifications}" varStatus="status">
                            <c:if test="${status.index < 3}">
                                <div class="notification-item d-flex align-items-center">
                                    <div class="notification-icon ${notification.alertClass}">
                                        <i class="fas ${notification.iconClass}"></i>
                                    </div>
                                    <div class="flex-grow-1">
                                        <h6 class="mb-1">${notification.title}</h6>
                                        <p class="mb-0 text-muted">${notification.message}</p>
                                        <small class="text-muted">${notification.createdAt}</small>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <c:if test="${empty notifications}">
                            <div class="text-center text-muted py-4">No notifications</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
