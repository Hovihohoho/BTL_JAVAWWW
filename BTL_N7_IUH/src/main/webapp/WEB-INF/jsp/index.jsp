<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Frubana - Rau củ quả hữu cơ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .product-card img {
            height: 200px;
            object-fit: cover;
        }
        /* Tùy chỉnh màu thương hiệu */
        .text-frubana { color: #6d9c3a; }
        .bg-frubana-dark { background-color: #5cb85c !important; }
        .fw-semibold { font-weight: 600 !important; }
    </style>
</head>
<body>

<div class="top-bar py-2 bg-frubana-dark text-white" style="font-size: 0.8rem;">
    <div class="container d-flex justify-content-between align-items-center">
        <div class="contact-info d-none d-md-block">
            <span class="me-3"><i class="fas fa-map-marker-alt"></i> Địa Chỉ: 235 Hoàng Quốc Việt, Bắc Từ Liêm, Hà Nội</span>
            <span><i class="fas fa-envelope"></i> Email: apteact@gmail.com</span>
        </div>
        <div class="policy-links">
            <a href="#" class="text-white text-decoration-none me-3">Chính sách bảo mật</a>
            <span class="mx-1 d-none d-sm-inline">/</span>
            <a href="#" class="text-white text-decoration-none me-3 d-none d-sm-inline">Điều khoản sử dụng</a>
        </div>
    </div>
</div>

<nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top shadow-sm">
    <div class="container">
        <a class="navbar-brand fs-2 fw-bold text-frubana" href="/">
            Frubana
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 fw-semibold">
                <li class="nav-item">
                    <a class="nav-link text-frubana active" aria-current="page" href="/">Trang chủ</a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-dark" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Sản phẩm
                    </a>
                    <ul class="dropdown-menu border-0 shadow-sm" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="/products/fruit">Trái cây</a></li>
                        <li><a class="dropdown-item" href="/products/vegetable">Rau củ quả</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="/products/all">Xem tất cả</a></li>
                    </ul>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-dark" href="/testimonials">Lời chứng thực</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-dark" href="/contact">Liên hệ</a>
                </li>
            </ul>

            <div class="d-flex align-items-center">

                <button class="btn p-2" type="button" title="Tìm kiếm">
                    <i class="fas fa-search text-dark"></i>
                </button>

                <a href="/cart" class="btn p-2 position-relative" title="Giỏ hàng">
                    <i class="fas fa-shopping-bag text-frubana fs-5"></i>
                    <span class="badge rounded-pill bg-danger position-absolute top-0 start-100 translate-middle">
                        <c:out value="${cartItemCount}" default="0"/>
                    </span>
                </a>

                <div class="dropdown">
                    <button class="btn dropdown-toggle text-dark fw-semibold" type="button" id="userMenu" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user-circle me-1"></i> Hồ Việt
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end border-0 shadow-sm" aria-labelledby="userMenu">
                        <li><a class="dropdown-item" href="/profile">Thông tin cá nhân</a></li>
                        <li><a class="dropdown-item" href="/orders">Lịch sử đơn hàng</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="/logout">Đăng xuất</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>

<div class="container my-5">
    <div class="p-5 mb-4 bg-light rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold text-success">100% Thực phẩm hữu cơ</h1>
            <p class="col-md-8 fs-4">Rau củ quả tươi ngon &amp; Thực phẩm an toàn cho gia đình bạn.</p>
            <a class="btn btn-success btn-lg" href="/client/products" role="button">Xem tất cả sản phẩm</a>
        </div>
    </div>

    <hr>

    <h2 class="mb-4 text-center">Sản phẩm nổi bật</h2>

    <div class="row row-cols-1 row-cols-md-4 g-4">
        <c:forEach var="product" items="${products}">
            <div class="col">
                <div class="card h-100 product-card shadow-sm">
                    <img src="/static/images/${product.imageUrl}" class="card-img-top" alt="${product.name}">
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        <p class="card-text">
                            Giá:
                            <span class="text-decoration-line-through text-muted me-2">
                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VND"/>
                            </span>
                            <c:if test="${not empty product.salePrice}">
                                <span class="fw-bold text-danger">
                                    <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="VND"/>
                                </span>
                            </c:if>
                            <c:if test="${empty product.salePrice}">
                                <span class="fw-bold text-success">
                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="VND"/>
                                </span>
                            </c:if>
                        </p>
                        <a href="#" class="btn btn-warning btn-sm">Thêm vào giỏ</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>