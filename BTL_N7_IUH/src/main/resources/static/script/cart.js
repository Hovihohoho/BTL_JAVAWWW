// cart.script.js
// Logic thêm sản phẩm vào localStorage và cập nhật badge giỏ hàng
function addToCart(product) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let found = cart.find(item => item.id == product.id);
    if (found) {
        found.quantity += 1;
    } else {
        cart.push({...product, quantity: 1});
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    showToast('Đã thêm vào giỏ hàng!');
    updateCartBadge();
}

function updateCartBadge() {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let total = cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
    let badge = document.querySelector('.fa-shopping-bag ~ .badge, .fa-shopping-bag + span.badge');
    if (badge) badge.textContent = total;
}

function showToast(msg) {
    let toast = document.createElement('div');
    toast.className = 'position-fixed top-0 end-0 m-4 p-3 bg-success text-white rounded shadow';
    toast.style.zIndex = 9999;
    toast.textContent = msg;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 1500);
}

document.addEventListener('DOMContentLoaded', function() {
    updateCartBadge();
    document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const product = {
                id: this.getAttribute('data-id'),
                name: this.getAttribute('data-name'),
                price: this.getAttribute('data-price'),
                image: this.getAttribute('data-image')
            };
            addToCart(product);
        });
    });
});

