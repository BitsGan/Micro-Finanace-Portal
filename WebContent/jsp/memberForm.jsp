<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty member ? 'Add' : 'Edit'} Member - Sangam Micro Finance</title>
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
        }
        
        .form-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }
        
        .form-floating {
            margin-bottom: 20px;
        }
        
        .form-floating input, .form-floating select {
            border-radius: 10px;
            border: 2px solid #e9ecef;
        }
        
        .form-floating input:focus, .form-floating select:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(0,123,255,0.25);
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            border: none;
            border-radius: 10px;
            padding: 15px;
            font-weight: 600;
            width: 100%;
            color: white;
            transition: all 0.3s ease;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(0,123,255,0.4);
            color: white;
        }
        
        .btn-cancel {
            background: #6c757d;
            border: none;
            border-radius: 10px;
            padding: 15px;
            font-weight: 600;
            color: white;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-cancel:hover {
            background: #5a6268;
            color: white;
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
                <h5 class="mb-0">${empty member ? 'Add New' : 'Edit'} Member</h5>
                <small class="text-muted">${empty member ? 'Create a new member account' : 'Update member details'}</small>
            </div>
        </div>
        
        <!-- Form -->
        <div class="form-card">
            <form action="${pageContext.request.contextPath}/admin/member/${empty member ? 'add' : 'edit'}" method="post">
                <c:if test="${not empty member}">
                    <input type="hidden" name="memberId" value="${member.memberId}">
                </c:if>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="name" name="name" placeholder="Full Name" 
                                   value="${member.name}" required>
                            <label for="name"><i class="fas fa-user me-2"></i>Full Name</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email" 
                                   value="${member.email}" required ${not empty member ? 'readonly' : ''}>
                            <label for="email"><i class="fas fa-envelope me-2"></i>Email Address</label>
                        </div>
                    </div>
                </div>
                
                <c:if test="${empty member}">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-floating">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                                <label for="password"><i class="fas fa-lock me-2"></i>Password</label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-floating">
                                <input type="date" class="form-control" id="joinDate" name="joinDate" placeholder="Join Date" 
                                       value="${member.joinDate}" required>
                                <label for="joinDate"><i class="fas fa-calendar me-2"></i>Join Date</label>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="tel" class="form-control" id="phone" name="phone" placeholder="Phone" 
                                   value="${member.phone}" required>
                            <label for="phone"><i class="fas fa-phone me-2"></i>Phone Number</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="emergencyContact" name="emergencyContact" 
                                   placeholder="Emergency Contact" value="${member.emergencyContact}">
                            <label for="emergencyContact"><i class="fas fa-phone-alt me-2"></i>Emergency Contact</label>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="occupation" name="occupation" 
                                   placeholder="Occupation" value="${member.occupation}">
                            <label for="occupation"><i class="fas fa-briefcase me-2"></i>Occupation</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="monthlySavings" name="monthlySavings" 
                                   placeholder="Monthly Savings" value="${member.monthlySavingsAmount}" min="100" required>
                            <label for="monthlySavings"><i class="fas fa-piggy-bank me-2"></i>Monthly Savings (₹)</label>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="annualIncome" name="annualIncome" 
                                   placeholder="Annual Income" value="${member.annualIncome}" min="0">
                            <label for="annualIncome"><i class="fas fa-rupee-sign me-2"></i>Annual Income (₹)</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <select class="form-select" id="status" name="status">
                                <option value="active" ${member.status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${member.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                <option value="suspended" ${member.status == 'suspended' ? 'selected' : ''}>Suspended</option>
                            </select>
                            <label for="status"><i class="fas fa-toggle-on me-2"></i>Status</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-floating">
                    <textarea class="form-control" id="address" name="address" placeholder="Address" 
                              style="height: 100px;">${member.address}</textarea>
                    <label for="address"><i class="fas fa-map-marker-alt me-2"></i>Address</label>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <a href="${pageContext.request.contextPath}/admin/members" class="btn btn-cancel w-100">
                            <i class="fas fa-times me-2"></i>Cancel
                        </a>
                    </div>
                    <div class="col-md-6">
                        <button type="submit" class="btn btn-submit">
                            <i class="fas fa-save me-2"></i>${empty member ? 'Add' : 'Update'} Member
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
