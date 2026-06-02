<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Members - Sangam Micro Finance</title>
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
            background: #f8f9fa;
        }
        
        .table td {
            vertical-align: middle;
        }
        
        .badge {
            padding: 8px 12px;
            border-radius: 8px;
        }
        
        .btn-action {
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 12px;
            margin-right: 5px;
        }
        
        .search-box {
            position: relative;
        }
        
        .search-box input {
            padding-left: 40px;
            border-radius: 10px;
        }
        
        .search-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #6c757d;
        }
        
        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
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
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-tachometer-alt"></i>Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/members" class="active"><i class="fas fa-users"></i>Members</a></li>
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
                <h5 class="mb-0">Member Management</h5>
                <small class="text-muted">Manage all members</small>
            </div>
            <a href="${pageContext.request.contextPath}/admin/member/add" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>Add Member
            </a>
        </div>
        
        <!-- Members Card -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <span><i class="fas fa-users me-2"></i>All Members</span>
                <form action="${pageContext.request.contextPath}/admin/members" method="get" class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" name="search" class="form-control" placeholder="Search members..." value="${param.search}">
                </form>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Member</th>
                                <th>Contact</th>
                                <th>Join Date</th>
                                <th>Monthly Savings</th>
                                <th>Total Savings</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="member" items="${members}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar me-3">
                                                ${fn:substring(member.name, 0, 1)}
                                            </div>
                                            <div>
                                                <h6 class="mb-0">${member.name}</h6>
                                                <small class="text-muted">${member.email}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${member.phone}</td>
                                    <td>${member.joinDate}</td>
                                    <td>₹<fmt:formatNumber value="${member.monthlySavingsAmount}" pattern="#,##0.00"/></td>
                                    <td>₹<fmt:formatNumber value="${member.totalSavings}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${member.status == 'active'}">
                                                <span class="badge bg-success">Active</span>
                                            </c:when>
                                            <c:when test="${member.status == 'inactive'}">
                                                <span class="badge bg-secondary">Inactive</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">${member.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/member/edit?id=${member.memberId}" class="btn btn-sm btn-primary btn-action">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/cashier/payment?memberId=${member.memberId}" class="btn btn-sm btn-success btn-action">
                                            <i class="fas fa-money-bill-wave"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/member/delete?id=${member.memberId}" class="btn btn-sm btn-danger btn-action" onclick="return confirm('Are you sure you want to delete this member?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty members}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted py-4">
                                        <i class="fas fa-users fa-3x mb-3"></i>
                                        <p>No members found</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
