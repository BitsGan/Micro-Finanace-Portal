<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loans - Sangam Micro Finance</title>
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
        
        .filter-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .filter-tabs a {
            padding: 10px 20px;
            border-radius: 10px;
            text-decoration: none;
            color: #6c757d;
            background: white;
            transition: all 0.3s ease;
        }
        
        .filter-tabs a:hover, .filter-tabs a.active {
            background: var(--primary-color);
            color: white;
        }
        
        .loan-details {
            font-size: 12px;
            color: #6c757d;
        }
        
        .progress {
            height: 8px;
            border-radius: 4px;
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
            <li><a href="${pageContext.request.contextPath}/admin/members"><i class="fas fa-users"></i>Members</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/loans" class="active"><i class="fas fa-hand-holding-usd"></i>Loans</a></li>
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
                <h5 class="mb-0">Loan Management</h5>
                <small class="text-muted">Manage all loans</small>
            </div>
            <a href="${pageContext.request.contextPath}/admin/loan/apply" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>New Loan
            </a>
        </div>
        
        <!-- Filter Tabs -->
        <div class="filter-tabs">
            <a href="${pageContext.request.contextPath}/admin/loans" class="${empty param.status ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/admin/loans?status=pending" class="${param.status == 'pending' ? 'active' : ''}">Pending</a>
            <a href="${pageContext.request.contextPath}/admin/loans?status=active" class="${param.status == 'active' ? 'active' : ''}">Active</a>
            <a href="${pageContext.request.contextPath}/admin/loans?status=completed" class="${param.status == 'completed' ? 'active' : ''}">Completed</a>
            <a href="${pageContext.request.contextPath}/admin/loans?status=rejected" class="${param.status == 'rejected' ? 'active' : ''}">Rejected</a>
        </div>
        
        <!-- Loans Card -->
        <div class="card">
            <div class="card-header">
                <i class="fas fa-hand-holding-usd me-2"></i>Loan Applications
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Member</th>
                                <th>Loan Amount</th>
                                <th>Interest Rate</th>
                                <th>Tenure</th>
                                <th>EMI</th>
                                <th>Status</th>
                                <th>Progress</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="loan" items="${loans}">
                                <tr>
                                    <td>
                                        <h6 class="mb-0">${loan.memberName}</h6>
                                        <small class="loan-details">Applied: ${loan.requestDate}</small>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${loan.loanAmount}" pattern="#,##0.00"/></td>
                                    <td>${loan.interestRate}%</td>
                                    <td>${loan.tenureMonths} months</td>
                                    <td>₹<fmt:formatNumber value="${loan.emiAmount}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${loan.status == 'pending'}">
                                                <span class="badge bg-warning">Pending</span>
                                            </c:when>
                                            <c:when test="${loan.status == 'approved' || loan.status == 'active'}">
                                                <span class="badge bg-success">Active</span>
                                            </c:when>
                                            <c:when test="${loan.status == 'completed'}">
                                                <span class="badge bg-primary">Completed</span>
                                            </c:when>
                                            <c:when test="${loan.status == 'rejected'}">
                                                <span class="badge bg-danger">Rejected</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${loan.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${loan.totalEmis > 0}">
                                            <div class="d-flex align-items-center">
                                                <div class="progress flex-grow-1 me-2" style="width: 80px;">
                                                    <div class="progress-bar bg-success" style="width: ${(loan.paidEmis / loan.totalEmis) * 100}%"></div>
                                                </div>
                                                <small>${loan.paidEmis}/${loan.totalEmis}</small>
                                            </div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${loan.status == 'pending'}">
                                            <button class="btn btn-sm btn-success btn-action" data-bs-toggle="modal" data-bs-target="#approveModal${loan.loanId}">
                                                <i class="fas fa-check"></i> Approve
                                            </button>
                                            <button class="btn btn-sm btn-danger btn-action" data-bs-toggle="modal" data-bs-target="#rejectModal${loan.loanId}">
                                                <i class="fas fa-times"></i> Reject
                                            </button>
                                        </c:if>
                                        <a href="#" class="btn btn-sm btn-info btn-action">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                                
                                <!-- Approve Modal -->
                                <div class="modal fade" id="approveModal${loan.loanId}" tabindex="-1">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Approve Loan</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <form action="${pageContext.request.contextPath}/admin/loan/approve" method="post">
                                                <div class="modal-body">
                                                    <input type="hidden" name="loanId" value="${loan.loanId}">
                                                    <p>Are you sure you want to approve this loan?</p>
                                                    <p><strong>Member:</strong> ${loan.memberName}</p>
                                                    <p><strong>Amount:</strong> ₹<fmt:formatNumber value="${loan.loanAmount}" pattern="#,##0.00"/></p>
                                                    <div class="mb-3">
                                                        <label class="form-label">Remarks (Optional)</label>
                                                        <textarea name="remarks" class="form-control" rows="3"></textarea>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                    <button type="submit" class="btn btn-success">Approve Loan</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Reject Modal -->
                                <div class="modal fade" id="rejectModal${loan.loanId}" tabindex="-1">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Reject Loan</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <form action="${pageContext.request.contextPath}/admin/loan/reject" method="post">
                                                <div class="modal-body">
                                                    <input type="hidden" name="loanId" value="${loan.loanId}">
                                                    <p>Are you sure you want to reject this loan?</p>
                                                    <p><strong>Member:</strong> ${loan.memberName}</p>
                                                    <p><strong>Amount:</strong> ₹<fmt:formatNumber value="${loan.loanAmount}" pattern="#,##0.00"/></p>
                                                    <div class="mb-3">
                                                        <label class="form-label">Reason for Rejection</label>
                                                        <textarea name="remarks" class="form-control" rows="3" required></textarea>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                    <button type="submit" class="btn btn-danger">Reject Loan</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty loans}">
                                <tr>
                                    <td colspan="8" class="text-center text-muted py-4">
                                        <i class="fas fa-hand-holding-usd fa-3x mb-3"></i>
                                        <p>No loans found</p>
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
