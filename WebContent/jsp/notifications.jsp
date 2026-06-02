<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notifications - Sangam Micro Finance</title>
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
        
        .notification-item {
            padding: 20px;
            border-bottom: 1px solid #e9ecef;
            transition: background 0.3s ease;
            display: flex;
            align-items: flex-start;
        }
        
        .notification-item:hover {
            background: #f8f9fa;
        }
        
        .notification-item:last-child {
            border-bottom: none;
        }
        
        .notification-item.unread {
            background: rgba(0,123,255,0.05);
        }
        
        .notification-icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 20px;
            flex-shrink: 0;
        }
        
        .notification-icon.success { background: rgba(40,167,69,0.1); color: #28a745; }
        .notification-icon.warning { background: rgba(255,193,7,0.1); color: #ffc107; }
        .notification-icon.error { background: rgba(220,53,69,0.1); color: #dc3545; }
        .notification-icon.info { background: rgba(0,123,255,0.1); color: #007bff; }
        
        .notification-content {
            flex-grow: 1;
        }
        
        .notification-title {
            font-weight: 600;
            margin-bottom: 5px;
            color: #333;
        }
        
        .notification-message {
            color: #6c757d;
            margin-bottom: 5px;
        }
        
        .notification-time {
            font-size: 12px;
            color: #adb5bd;
        }
        
        .notification-actions {
            margin-left: 15px;
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
            <li><a href="${pageContext.request.contextPath}/admin/loans"><i class="fas fa-hand-holding-usd"></i>Loans</a></li>
            <li><a href="${pageContext.request.contextPath}/cashier/payment"><i class="fas fa-money-bill-wave"></i>Payments</a></li>
            <li><a href="${pageContext.request.contextPath}/notifications" class="active"><i class="fas fa-bell"></i>Notifications</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i>Logout</a></li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Topbar -->
        <div class="topbar">
            <div>
                <h5 class="mb-0">Notifications</h5>
                <small class="text-muted">View all your notifications</small>
            </div>
            <button class="btn btn-primary" onclick="markAllAsRead()">
                <i class="fas fa-check-double me-2"></i>Mark All as Read
            </button>
        </div>
        
        <!-- Notifications Card -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <span><i class="fas fa-bell me-2"></i>All Notifications</span>
                <span class="badge bg-primary" id="unreadCount">0 unread</span>
            </div>
            <div class="card-body p-0">
                <c:forEach var="notification" items="${notifications}">
                    <div class="notification-item ${notification.read ? '' : 'unread'}" id="notif-${notification.notificationId}">
                        <div class="notification-icon ${notification.alertClass}">
                            <i class="fas ${notification.iconClass}"></i>
                        </div>
                        <div class="notification-content">
                            <div class="notification-title">${notification.title}</div>
                            <div class="notification-message">${notification.message}</div>
                            <div class="notification-time">
                                <i class="far fa-clock me-1"></i>${notification.createdAt}
                            </div>
                        </div>
                        <div class="notification-actions">
                            <c:if test="${!notification.read}">
                                <button class="btn btn-sm btn-outline-primary" onclick="markAsRead(${notification.notificationId})">
                                    <i class="fas fa-check"></i>
                                </button>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty notifications}">
                    <div class="empty-state">
                        <i class="fas fa-bell-slash"></i>
                        <h5>No Notifications</h5>
                        <p class="text-muted">You don't have any notifications yet.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function markAsRead(notificationId) {
            fetch('${pageContext.request.contextPath}/notifications/mark-read', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'notificationId=' + notificationId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('notif-' + notificationId).classList.remove('unread');
                    updateUnreadCount();
                }
            })
            .catch(error => console.error('Error:', error));
        }
        
        function markAllAsRead() {
            fetch('${pageContext.request.contextPath}/notifications/mark-read', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'notificationId=all'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.querySelectorAll('.notification-item').forEach(item => {
                        item.classList.remove('unread');
                    });
                    updateUnreadCount();
                }
            })
            .catch(error => console.error('Error:', error));
        }
        
        function updateUnreadCount() {
            const unreadItems = document.querySelectorAll('.notification-item.unread').length;
            document.getElementById('unreadCount').textContent = unreadItems + ' unread';
        }
        
        // Update unread count on page load
        updateUnreadCount();
    </script>
</body>
</html>
