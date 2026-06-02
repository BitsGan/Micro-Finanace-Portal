<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apply for Loan - Sangam Micro Finance</title>
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
        
        .form-floating input, .form-floating select, .form-floating textarea {
            border-radius: 10px;
            border: 2px solid #e9ecef;
        }
        
        .form-floating input:focus, .form-floating select:focus, .form-floating textarea:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(0,123,255,0.25);
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
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
            box-shadow: 0 5px 20px rgba(40,167,69,0.4);
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
        
        .loan-calculator {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
        }
        
        .calculator-result {
            background: rgba(255,255,255,0.1);
            border-radius: 10px;
            padding: 20px;
            margin-top: 20px;
        }
        
        .result-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
        
        .result-item:last-child {
            margin-bottom: 0;
            padding-top: 10px;
            border-top: 1px solid rgba(255,255,255,0.2);
        }
        
        .result-value {
            font-weight: 700;
            font-size: 18px;
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
                <h5 class="mb-0">Apply for Loan</h5>
                <small class="text-muted">Fill in the details to apply for a new loan</small>
            </div>
        </div>
        
        <div class="row g-4">
            <!-- Loan Calculator -->
            <div class="col-lg-4">
                <div class="loan-calculator">
                    <h4><i class="fas fa-calculator me-2"></i>Loan Calculator</h4>
                    <p class="mb-0">Estimate your EMI before applying</p>
                    
                    <div class="calculator-result">
                        <div class="result-item">
                            <span>Loan Amount</span>
                            <span class="result-value" id="calcAmount">₹0</span>
                        </div>
                        <div class="result-item">
                            <span>Interest Rate</span>
                            <span class="result-value" id="calcRate">12%</span>
                        </div>
                        <div class="result-item">
                            <span>Tenure</span>
                            <span class="result-value" id="calcTenure">0 months</span>
                        </div>
                        <div class="result-item">
                            <span>Total Interest</span>
                            <span class="result-value" id="calcInterest">₹0</span>
                        </div>
                        <div class="result-item">
                            <span>Total Amount</span>
                            <span class="result-value" id="calcTotal">₹0</span>
                        </div>
                        <div class="result-item">
                            <span>Monthly EMI</span>
                            <span class="result-value" id="calcEMI">₹0</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Application Form -->
            <div class="col-lg-8">
                <div class="form-card">
                    <h4 class="mb-4"><i class="fas fa-file-alt me-2"></i>Loan Application</h4>
                    
                    <form action="${pageContext.request.contextPath}/member/loan/apply" method="post" id="loanForm">
                        <c:if test="${not empty members}">
                            <div class="form-floating">
                                <select class="form-select" id="memberId" name="memberId" required>
                                    <option value="">Select Member</option>
                                    <c:forEach var="m" items="${members}">
                                        <option value="${m.memberId}">${m.name} - ${m.phone}</option>
                                    </c:forEach>
                                </select>
                                <label for="memberId"><i class="fas fa-user me-2"></i>Select Member</label>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty member}">
                            <input type="hidden" name="memberId" value="${member.memberId}">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>Applying for loan as: <strong>${member.name}</strong>
                            </div>
                        </c:if>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="number" class="form-control" id="loanAmount" name="loanAmount" 
                                           placeholder="Loan Amount" min="1000" required onchange="calculateLoan()">
                                    <label for="loanAmount"><i class="fas fa-rupee-sign me-2"></i>Loan Amount (₹)</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="number" class="form-control" id="interestRate" name="interestRate" 
                                           placeholder="Interest Rate" value="12.00" min="1" max="36" step="0.01" required onchange="calculateLoan()">
                                    <label for="interestRate"><i class="fas fa-percent me-2"></i>Interest Rate (%)</label>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="number" class="form-control" id="tenureMonths" name="tenureMonths" 
                                           placeholder="Tenure" min="3" max="60" required onchange="calculateLoan()">
                                    <label for="tenureMonths"><i class="fas fa-calendar-alt me-2"></i>Tenure (Months)</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <select class="form-select" id="purpose" name="purpose">
                                        <option value="Personal">Personal</option>
                                        <option value="Business">Business</option>
                                        <option value="Education">Education</option>
                                        <option value="Medical">Medical</option>
                                        <option value="Home Improvement">Home Improvement</option>
                                        <option value="Other">Other</option>
                                    </select>
                                    <label for="purpose"><i class="fas fa-tag me-2"></i>Loan Purpose</label>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-floating">
                            <textarea class="form-control" id="description" name="description" 
                                      placeholder="Description" style="height: 100px;"></textarea>
                            <label for="description"><i class="fas fa-edit me-2"></i>Additional Details (Optional)</label>
                        </div>
                        
                        <div class="form-check mb-4">
                            <input class="form-check-input" type="checkbox" id="agree" required>
                            <label class="form-check-label" for="agree">
                                I agree to the <a href="#">terms and conditions</a> and confirm that all information provided is accurate.
                            </label>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/member/loans" class="btn btn-cancel w-100">
                                    <i class="fas fa-times me-2"></i>Cancel
                                </a>
                            </div>
                            <div class="col-md-6">
                                <button type="submit" class="btn btn-submit">
                                    <i class="fas fa-paper-plane me-2"></i>Submit Application
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function calculateLoan() {
            const amount = parseFloat(document.getElementById('loanAmount').value) || 0;
            const rate = parseFloat(document.getElementById('interestRate').value) || 12;
            const tenure = parseInt(document.getElementById('tenureMonths').value) || 0;
            
            if (amount > 0 && tenure > 0) {
                // Simple Interest Calculation
                const timeInYears = tenure / 12;
                const interest = (amount * rate * timeInYears) / 100;
                const totalAmount = amount + interest;
                const emi = totalAmount / tenure;
                
                // Update calculator display
                document.getElementById('calcAmount').textContent = '₹' + amount.toLocaleString('en-IN');
                document.getElementById('calcRate').textContent = rate + '%';
                document.getElementById('calcTenure').textContent = tenure + ' months';
                document.getElementById('calcInterest').textContent = '₹' + interest.toFixed(2);
                document.getElementById('calcTotal').textContent = '₹' + totalAmount.toFixed(2);
                document.getElementById('calcEMI').textContent = '₹' + emi.toFixed(2);
            }
        }
        
        // Calculate on page load if values exist
        calculateLoan();
    </script>
</body>
</html>
