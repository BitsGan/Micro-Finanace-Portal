<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Record Payment - Sangam Micro Finance</title>
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
        
        .payment-form {
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
        
        .btn-record {
            background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
            border: none;
            border-radius: 10px;
            padding: 15px;
            font-weight: 600;
            width: 100%;
            color: white;
            transition: all 0.3s ease;
        }
        
        .btn-record:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(40,167,69,0.4);
            color: white;
        }
        
        .amount-display {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            margin-bottom: 30px;
        }
        
        .amount-display h3 {
            margin: 0;
            font-size: 36px;
        }
        
        .quick-amounts {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }
        
        .quick-amounts button {
            padding: 10px 20px;
            border: 2px solid #e9ecef;
            background: white;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .quick-amounts button:hover {
            border-color: var(--primary-color);
            background: rgba(0,123,255,0.1);
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
            <li><a href="${pageContext.request.contextPath}/cashier/dashboard"><i class="fas fa-tachometer-alt"></i>Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/cashier/payment" class="active"><i class="fas fa-money-bill-wave"></i>Record Payment</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/members"><i class="fas fa-users"></i>Members</a></li>
            <li><a href="${pageContext.request.contextPath}/notifications"><i class="fas fa-bell"></i>Notifications</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i>Logout</a></li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Topbar -->
        <div class="topbar">
            <div>
                <h5 class="mb-0">Record Payment</h5>
                <small class="text-muted">Record offline payment from member</small>
            </div>
        </div>
        
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="payment-form">
                    <h4 class="mb-4"><i class="fas fa-money-bill-wave me-2"></i>Payment Details</h4>
                    
                    <!-- Success/Error Messages -->
                    <c:if test="${not empty success}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fas fa-check-circle me-2"></i>${success}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/payment/offline" method="post">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <select class="form-select" id="memberId" name="memberId" required>
                                        <option value="">Select Member</option>
                                        <c:forEach var="member" items="${members}">
                                            <option value="${member.memberId}" ${param.memberId == member.memberId ? 'selected' : ''}>${member.name} - ${member.phone}</option>
                                        </c:forEach>
                                    </select>
                                    <label for="memberId"><i class="fas fa-user me-2"></i>Select Member</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <select class="form-select" id="paymentType" name="paymentType" required>
                                        <option value="emi">EMI Payment</option>
                                        <option value="savings">Savings Deposit</option>
                                        <option value="loan_repayment">Loan Repayment</option>
                                        <option value="other">Other</option>
                                    </select>
                                    <label for="paymentType"><i class="fas fa-tag me-2"></i>Payment Type</label>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="number" class="form-control" id="amount" name="amount" placeholder="Amount" required min="1" step="0.01">
                                    <label for="amount"><i class="fas fa-rupee-sign me-2"></i>Amount (₹)</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <select class="form-select" id="paymentMode" name="paymentMode" required>
                                        <option value="cash">Cash</option>
                                        <option value="cheque">Cheque</option>
                                        <option value="bank_transfer">Bank Transfer</option>
                                    </select>
                                    <label for="paymentMode"><i class="fas fa-credit-card me-2"></i>Payment Mode</label>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-floating">
                            <textarea class="form-control" id="notes" name="notes" placeholder="Notes" style="height: 100px;"></textarea>
                            <label for="notes"><i class="fas fa-sticky-note me-2"></i>Notes (Optional)</label>
                        </div>
                        
                        <button type="submit" class="btn btn-record">
                            <i class="fas fa-check-circle me-2"></i>Record Payment
                        </button>
                    </form>
                </div>
            </div>
            
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-info-circle me-2"></i>Quick Tips
                    </div>
                    <div class="card-body">
                        <ul class="list-unstyled">
                            <li class="mb-3">
                                <i class="fas fa-check-circle text-success me-2"></i>
                                Select the correct member from the dropdown
                            </li>
                            <li class="mb-3">
                                <i class="fas fa-check-circle text-success me-2"></i>
                                Choose the appropriate payment type
                            </li>
                            <li class="mb-3">
                                <i class="fas fa-check-circle text-success me-2"></i>
                                Enter the exact amount received
                            </li>
                            <li class="mb-3">
                                <i class="fas fa-check-circle text-success me-2"></i>
                                Specify the payment mode (Cash/Cheque/Transfer)
                            </li>
                            <li>
                                <i class="fas fa-check-circle text-success me-2"></i>
                                Add notes for future reference
                            </li>
                        </ul>
                    </div>
                </div>
                
                <div class="card mt-4">
                    <div class="card-header">
                        <i class="fas fa-receipt me-2"></i>Recent Payments
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-sm mb-0">
                                <thead>
                                    <tr>
                                        <th>Member</th>
                                        <th>Amount</th>
                                        <th>Receipt</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="payment" items="${todayPayments}" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <tr>
                                                <td>${payment.memberName}</td>
                                                <td>₹<fmt:formatNumber value="${payment.amount}" pattern="#,##0.00"/></td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/receipt?transactionId=${payment.transactionId}" class="btn btn-sm btn-outline-primary">
                                                        <i class="fas fa-download"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty todayPayments}">
                                        <tr>
                                            <td colspan="3" class="text-center text-muted py-3">No payments today</td>
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
        // Quick amount buttons
        function setAmount(amount) {
            document.getElementById('amount').value = amount;
        }
    </script>
</body>
</html>
