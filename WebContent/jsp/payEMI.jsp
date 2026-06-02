<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pay EMI - Sangam Micro Finance</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Razorpay -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
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
        
        .emi-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        
        .emi-card:hover {
            border-color: var(--primary-color);
            transform: translateY(-3px);
        }
        
        .emi-card.selected {
            border-color: var(--primary-color);
            background: rgba(0,123,255,0.05);
        }
        
        .emi-amount {
            font-size: 28px;
            font-weight: 700;
            color: #28a745;
        }
        
        .btn-pay-now {
            background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
        }
        
        .btn-pay-now:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(40,167,69,0.4);
            color: white;
        }
        
        .payment-summary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
        }
        
        .payment-summary h4 {
            margin-bottom: 20px;
        }
        
        .summary-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid rgba(255,255,255,0.2);
        }
        
        .summary-item:last-child {
            border-bottom: none;
            margin-bottom: 0;
            padding-bottom: 0;
        }
        
        .summary-total {
            font-size: 24px;
            font-weight: 700;
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
            <li><a href="${pageContext.request.contextPath}/member/loans"><i class="fas fa-hand-holding-usd"></i>My Loans</a></li>
            <li><a href="${pageContext.request.contextPath}/member/pay-emi" class="active"><i class="fas fa-money-bill-wave"></i>Pay EMI</a></li>
            <li><a href="${pageContext.request.contextPath}/notifications"><i class="fas fa-bell"></i>Notifications</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i>Logout</a></li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Topbar -->
        <div class="topbar">
            <div>
                <h5 class="mb-0">Pay EMI</h5>
                <small class="text-muted">Select an EMI to pay</small>
            </div>
        </div>
        
        <div class="row g-4">
            <!-- Pending EMIs -->
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-clock me-2"></i>Pending EMIs
                    </div>
                    <div class="card-body">
                        <c:forEach var="emi" items="${pendingEMIs}">
                            <div class="emi-card" id="emi-${emi.emiId}">
                                <div class="row align-items-center">
                                    <div class="col-md-4">
                                        <div class="emi-amount">₹<fmt:formatNumber value="${emi.emiAmount}" pattern="#,##0.00"/></div>
                                        <small class="text-muted">EMI #${emi.emiNumber}</small>
                                    </div>
                                    <div class="col-md-4">
                                        <p class="mb-1"><i class="fas fa-calendar me-2"></i>Due Date: ${emi.dueDate}</p>
                                        <p class="mb-0 text-muted"><i class="fas fa-file-invoice me-2"></i>Loan ID: ${emi.loanId}</p>
                                    </div>
                                    <div class="col-md-4 text-end">
                                        <button class="btn btn-pay-now" onclick="selectEMI(${emi.emiId}, ${emi.emiAmount}, ${emi.loanId})">
                                            <i class="fas fa-credit-card me-2"></i>Pay Now
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty pendingEMIs}">
                            <div class="text-center text-muted py-5">
                                <i class="fas fa-check-circle fa-3x mb-3 text-success"></i>
                                <h5>No Pending EMIs</h5>
                                <p>You have no pending EMIs to pay. Great job!</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <!-- Payment Summary -->
            <div class="col-lg-4">
                <div class="payment-summary">
                    <h4><i class="fas fa-receipt me-2"></i>Payment Summary</h4>
                    <div class="summary-item">
                        <span>Selected EMI</span>
                        <span id="selectedEmiNumber">-</span>
                    </div>
                    <div class="summary-item">
                        <span>EMI Amount</span>
                        <span id="selectedEmiAmount">₹0.00</span>
                    </div>
                    <div class="summary-item">
                        <span>Convenience Fee</span>
                        <span>₹0.00</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-total">Total Amount</span>
                        <span class="summary-total" id="totalAmount">₹0.00</span>
                    </div>
                    <button class="btn btn-light w-100 mt-3" id="payButton" onclick="processPayment()" disabled>
                        <i class="fas fa-lock me-2"></i>Proceed to Pay
                    </button>
                </div>
                
                <!-- Payment Methods -->
                <div class="card mt-4">
                    <div class="card-header">
                        <i class="fas fa-shield-alt me-2"></i>Secure Payment
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-center gap-3">
                            <i class="fab fa-cc-visa fa-2x text-primary"></i>
                            <i class="fab fa-cc-mastercard fa-2x text-danger"></i>
                            <i class="fab fa-cc-amex fa-2x text-info"></i>
                            <i class="fas fa-university fa-2x text-success"></i>
                        </div>
                        <p class="text-center text-muted mt-3 mb-0">
                            <small>Your payment is secured with 256-bit SSL encryption</small>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        let selectedEmiId = null;
        let selectedEmiAmount = 0;
        let selectedLoanId = null;
        
        function selectEMI(emiId, amount, loanId) {
            // Remove previous selection
            document.querySelectorAll('.emi-card').forEach(card => {
                card.classList.remove('selected');
            });
            
            // Add selection to current
            document.getElementById('emi-' + emiId).classList.add('selected');
            
            // Update values
            selectedEmiId = emiId;
            selectedEmiAmount = amount;
            selectedLoanId = loanId;
            
            // Update summary
            document.getElementById('selectedEmiNumber').textContent = 'EMI #' + emiId;
            document.getElementById('selectedEmiAmount').textContent = '₹' + amount.toFixed(2);
            document.getElementById('totalAmount').textContent = '₹' + amount.toFixed(2);
            
            // Enable pay button
            document.getElementById('payButton').disabled = false;
            document.getElementById('payButton').innerHTML = '<i class="fas fa-credit-card me-2"></i>Proceed to Pay ₹' + amount.toFixed(2);
        }
        
        function processPayment() {
            if (!selectedEmiId) {
                alert('Please select an EMI to pay');
                return;
            }
            
            // Create payment record on server
            fetch('${pageContext.request.contextPath}/payment/process', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'memberId=${member.memberId}&amount=' + selectedEmiAmount + '&emiId=' + selectedEmiId + '&loanId=' + selectedLoanId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Open Razorpay checkout
                    var options = {
                        "key": data.keyId,
                        "amount": data.amount,
                        "currency": "INR",
                        "name": "Sangam Micro Finance",
                        "description": "EMI Payment",
                        "order_id": data.razorpayOrderId,
                        "handler": function (response) {
                            verifyPayment(data.paymentId, response.razorpay_payment_id, 
                                response.razorpay_order_id, response.razorpay_signature);
                        },
                        "prefill": {
                            "name": "${member.name}",
                            "email": "${member.email}",
                            "contact": "${member.phone}"
                        },
                        "theme": {
                            "color": "#28a745"
                        }
                    };
                    var rzp = new Razorpay(options);
                    rzp.open();
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error processing payment. Please try again.');
            });
        }
        
        function verifyPayment(paymentId, razorpayPaymentId, razorpayOrderId, razorpaySignature) {
            fetch('${pageContext.request.contextPath}/payment/verify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'paymentId=' + paymentId + '&razorpayPaymentId=' + razorpayPaymentId + 
                      '&razorpayOrderId=' + razorpayOrderId + '&razorpaySignature=' + razorpaySignature
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Payment successful! Transaction ID: ' + data.transactionId);
                    window.location.reload();
                } else {
                    alert('Payment verification failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error verifying payment. Please contact support.');
            });
        }
    </script>
</body>
</html>
