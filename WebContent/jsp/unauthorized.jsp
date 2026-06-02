<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unauthorized - Sangam Micro Finance</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: #f5f6fa;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .unauthorized-container {
            text-align: center;
            padding: 40px;
        }
        
        .unauthorized-icon {
            font-size: 100px;
            color: #dc3545;
            margin-bottom: 30px;
        }
        
        .unauthorized-title {
            font-size: 48px;
            font-weight: 700;
            color: #333;
            margin-bottom: 10px;
        }
        
        .unauthorized-message {
            font-size: 20px;
            color: #6c757d;
            margin-bottom: 30px;
        }
        
        .btn-home {
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
        
        .btn-home:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(0,123,255,0.4);
            color: white;
        }
    </style>
</head>
<body>
    <div class="unauthorized-container">
        <div class="unauthorized-icon">
            <i class="fas fa-lock"></i>
        </div>
        <div class="unauthorized-title">Access Denied</div>
        <div class="unauthorized-message">
            You do not have permission to access this page.<br>
            Please contact your administrator if you believe this is an error.
        </div>
        <a href="${pageContext.request.contextPath}/login" class="btn-home">
            <i class="fas fa-home me-2"></i>Go to Home
        </a>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
