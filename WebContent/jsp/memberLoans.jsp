<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Loans - Sangam Micro Finance</title>
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
        
        .loan-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
        }
        
        .loan-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }
        
        .loan-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .loan-amount {
            font-size: 28px;
            font-weight: 700;
            color: #333;
        }
        
        .loan-status {
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .loan-details {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .detail-item {
            text-align: center;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        
        .detail-value {
            font-size: 18px;
            font-weight: 700;
            color: #333;
        }
        
        .detail-label {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }
        
        .progress {
            height: 10px;
            border-radius: 5px;
            margin-bottom: 10px;
        }
        
        .progress-bar {
            border-radius: 5px;
        }
        
        .btn-apply {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            color: white;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-apply:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(0,123,255,0.4);
            color: white;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
        }
        
        .empty-state i {
            font-size: 64px;
            color: #dee2e6;
            margin-bottom: 20px;
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
            <li><a href="${pageContext.request.contextPath}/member/dashboard"><i class="fas fa-tachometer-alt"></i>Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/member/loans" class="active"><i class="fas fa-hand-holding-usd"></i>My Loans</a></li>
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
                <h5 class="mb-0">My Loans</h5>
                <small class="text-muted">View all your loan details</small>
            </div>
            <a href="${pageContext.request.contextPath}/member/loan/apply" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>Apply for Loan
            </a>
        </div>
        
        <!-- Loans List -->
        <c:forEach var="loan" items="${loans}">
            <div class="loan-card">
                <div class="loan-header">
                    <div>
                        <div class="loan-amount">₹<fmt:formatNumber value="${loan.loanAmount}" pattern="#,##0.00"/></div>
                        <small class="text-muted">Loan ID: #${loan.loanId}</small>
                    </div>
                    <span class="loan-status 
                        <c:choose>
                            <c:when test="${loan.status == 'pending'}">bg-warning text-dark</c:when>
                            <c:when test="${loan.status == 'approved' || loan.status == 'active'}">bg-success</c:when>
                            <c:when test="${loan.status == 'completed'}">bg-primary</c:when>
                            <c:when test="${loan.status == 'rejected'}">bg-danger</c:when>
                            <c:otherwise>bg-secondary</c:otherwise>
                        </c:choose>
                    ">${loan.status}</span>
                </div>
                
                <div class="loan-details">
                    <div class="detail-item">
                        <div class="detail-value">${loan.interestRate}%</div>
                        <div class="detail-label">Interest Rate</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-value">${loan.tenureMonths} months</div>
                        <div class="detail-label">Tenure</div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-value">₹<fmt:formatNumber value="${loan.emiAmount}" pattern="#,##0.00"/></div>
                        <div class="detail-label">Monthly EMI</div>
                    </div>
                </div>
                
                <c:if test="${loan.status == 'active' || loan.status == 'approved'}">
                    <div class="mb-2">
                        <div class="d-flex justify-content-between">
                            <small>Repayment Progress</small>
                            <small>${loan.paidEmis}/${loan.totalEmis} EMIs Paid</small>
                        </div>
                        <div class="progress">
                            <div class="progress-bar bg-success" style="width: ${(loan.paidEmis / loan.totalEmis) * 100}%"></div>
                        </div>
                    </div>
                </c:if>
                
                <div class="row mt-3">
                    <div class="col-md-6">
                        <p class="mb-1"><strong>Total Amount:</strong> ₹<fmt:formatNumber value="${loan.totalAmount}" pattern="#,##0.00"/></p>
                        <p class="mb-0"><strong>Total Interest:</strong> ₹<fmt:formatNumber value="${loan.totalInterest}" pattern="#,##0.00"/></p>
                    </div>
                    <div class="col-md-6 text-end">
                        <p class="mb-1"><strong>Applied:</strong> ${loan.requestDate}</p>
                        <c:if test="${not empty loan.approvalDate}">
                            <p class="mb-0"><strong>Approved:</strong> ${loan.approvalDate}</p>
                        </c:if>
                    </div>
                </div>
                
                <c:if test="${loan.status == 'active'}">
                    <div class="mt-3 text-end">
                        <a href="${pageContext.request.contextPath}/member/pay-emi" class="btn btn-success">
                            <i class="fas fa-credit-card me-2"></i>Pay EMI
                        </a>
                    </div>
                </c:if>
            </div>
        </c:forEach>
        
        <c:if test="${empty loans}">
            <div class="card">
                <div class="card-body">
                    <div class="empty-state">
                        <i class="fas fa-hand-holding-usd"></i>
                        <h5>No Loans Found</h5>
                        <p class="text-muted">You haven't applied for any loans yet.</p>
                        <a href="${pageContext.request.contextPath}/member/loan/apply" class="btn btn-primary mt-3">
                            <i class="fas fa-plus me-2"></i>Apply for Loan
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
