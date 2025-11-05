// cart.badge.js
// Script dùng chung để cập nhật badge giỏ hàng trên mọi trang

function updateCartBadgeProductCount() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const badge = document.querySelector('.fa-shopping-bag ~ .badge, .fa-shopping-bag + span.badge');
    if (badge) badge.textContent = cart.length;
}
document.addEventListener('DOMContentLoaded', updateCartBadgeProductCount);
// Nếu có thao tác thêm/xóa sản phẩm, hãy gọi updateCartBadgeProductCount() sau khi thao tác xong.
