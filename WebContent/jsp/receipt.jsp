<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Receipt - Sangam Micro Finance</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        body {
            background: #f5f6fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .receipt-container {
            max-width: 800px;
            margin: 50px auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .receipt-header {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            color: white;
            padding: 40px;
            text-align: center;
        }
        
        .receipt-header h2 {
            margin: 0;
            font-weight: 600;
        }
        
        .receipt-header p {
            margin: 10px 0 0;
            opacity: 0.9;
        }
        
        .receipt-stamp {
            text-align: center;
            margin: 30px 0;
        }
        
        .stamp {
            display: inline-block;
            padding: 15px 40px;
            border: 4px solid #28a745;
            color: #28a745;
            font-size: 24px;
            font-weight: bold;
            border-radius: 10px;
            transform: rotate(-5deg);
        }
        
        .receipt-amount {
            text-align: center;
            margin: 30px 0;
        }
        
        .amount {
            font-size: 48px;
            font-weight: 700;
            color: #28a745;
        }
        
        .receipt-details {
            padding: 40px;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #e9ecef;
        }
        
        .detail-row:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: #555;
        }
        
        .detail-value {
            color: #333;
        }
        
        .receipt-footer {
            background: #f8f9fa;
            padding: 30px;
            text-align: center;
        }
        
        .receipt-footer p {
            margin: 5px 0;
            color: #6c757d;
        }
        
        .btn-download {
            background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            color: white;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
            transition: all 0.3s ease;
        }
        
        .btn-download:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(40,167,69,0.4);
            color: white;
        }
        
        .btn-back {
            background: #6c757d;
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 600;
            color: white;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
            margin-right: 10px;
            transition: all 0.3s ease;
        }
        
        .btn-back:hover {
            background: #5a6268;
            color: white;
        }
        
        @media print {
            body { background: white; }
            .receipt-container { box-shadow: none; margin: 0; }
            .btn-download, .btn-back { display: none; }
        }
    </style>
</head>
<body>
    <div class="receipt-container">
        <!-- Header -->
        <div class="receipt-header">
            <h2><i class="fas fa-university me-2"></i>SANGAM MICRO FINANCE</h2>
            <p>Payment Receipt</p>
            <p><i class="fas fa-map-marker-alt me-2"></i>123 Finance Street, Mumbai, India</p>
            <p><i class="fas fa-phone me-2"></i>+91 9876543210</p>
        </div>
        
        <!-- Stamp -->
        <div class="receipt-stamp">
            <div class="stamp">PAID</div>
        </div>
        
        <!-- Amount -->
        <div class="receipt-amount">
            <div class="amount">₹<fmt:formatNumber value="${payment.amount}" pattern="#,##0.00"/></div>
            <p class="text-muted">Payment Amount</p>
        </div>
        
        <!-- Details -->
        <div class="receipt-details">
            <div class="detail-row">
                <span class="detail-label">Transaction ID</span>
                <span class="detail-value">${payment.transactionId}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Date & Time</span>
                <span class="detail-value">${formattedDate}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Member Name</span>
                <span class="detail-value">${member.name}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Email</span>
                <span class="detail-value">${member.email}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Phone</span>
                <span class="detail-value">${member.phone}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Payment Type</span>
                <span class="detail-value">${payment.paymentType}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Payment Mode</span>
                <span class="detail-value">${payment.paymentMode}</span>
            </div>
            <c:if test="${not empty payment.razorpayPaymentId}">
                <div class="detail-row">
                    <span class="detail-label">Payment ID</span>
                    <span class="detail-value">${payment.razorpayPaymentId}</span>
                </div>
            </c:if>
            <c:if test="${not empty payment.notes}">
                <div class="detail-row">
                    <span class="detail-label">Notes</span>
                    <span class="detail-value">${payment.notes}</span>
                </div>
            </c:if>
        </div>
        
        <!-- Footer -->
        <div class="receipt-footer">
            <p><i class="fas fa-check-circle text-success me-2"></i>Thank you for your payment!</p>
            <p>This is a computer-generated receipt and does not require a signature.</p>
            <p>For any queries, please contact us at support@sangamfinance.com</p>
            
            <a href="javascript:history.back()" class="btn-back">
                <i class="fas fa-arrow-left me-2"></i>Back
            </a>
            <a href="${pageContext.request.contextPath}/receipt/download?transactionId=${payment.transactionId}" class="btn-download">
                <i class="fas fa-download me-2"></i>Download Receipt
            </a>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
