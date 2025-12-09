/* cart_render.js - Logic hiển thị bảng giỏ hàng */

function renderCart() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const tbody = document.getElementById('cart-body-js');

    if (!tbody) return; // Không phải trang cart thì thoát

    tbody.innerHTML = '';
    let total = 0;

    if (cart.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-muted">Giỏ hàng trống</td></tr>`;
        updateTotalDisplay(0);
    } else {
        cart.forEach((item, idx) => {
            const price = parseFloat(item.price);
            const qty = parseInt(item.quantity);
            const lineTotal = price * qty;
            total += lineTotal;

            tbody.innerHTML += `
            <tr>
                <td><img src="/images/${item.image}" class="img-thumbnail" style="width: 60px;"></td>
                <td class="align-middle fw-semibold">${item.name}</td>
                <td class="align-middle">${price.toLocaleString('vi-VN')} VND</td>
                <td class="align-middle" style="width: 140px;">
                    <div class="input-group input-group-sm">
                        <button class="btn btn-outline-secondary" onclick="updateQty(${idx}, -1)">-</button>
                        <input type="text" class="form-control text-center" value="${qty}" readonly>
                        <button class="btn btn-outline-secondary" onclick="updateQty(${idx}, 1)">+</button>
                    </div>
                </td>
                <td class="align-middle text-danger fw-bold">${lineTotal.toLocaleString('vi-VN')} VND</td>
                <td class="align-middle">
                    <button class="btn btn-sm btn-danger" onclick="removeItem(${idx})"><i class="fa fa-trash"></i></button>
                </td>
            </tr>`;
        });
        updateTotalDisplay(total);
    }

    // Cập nhật icon trên menu luôn
    if (typeof updateCartBadge === 'function') {
        updateCartBadge();
    }
}

function updateTotalDisplay(subtotal) {
    const shipping = subtotal > 0 ? 15000 : 0;
    const grandTotal = subtotal + shipping;

    const subEl = document.getElementById('cart-subtotal-js');
    const shipEl = document.getElementById('cart-shipping-js');
    const grandEl = document.getElementById('cart-grandtotal-js');

    if (subEl) subEl.innerText = subtotal.toLocaleString('vi-VN') + " VND";
    if (shipEl) shipEl.innerText = shipping.toLocaleString('vi-VN') + " VND";
    if (grandEl) grandEl.innerText = grandTotal.toLocaleString('vi-VN') + " VND";
}

function updateQty(idx, delta) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let newQty = parseInt(cart[idx].quantity) + delta;

    if (newQty < 1) return;

    cart[idx].quantity = newQty;
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}

function removeItem(idx) {
    if (!confirm("Xóa sản phẩm này?")) return;
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.splice(idx, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}

// Chạy khi vào trang
document.addEventListener('DOMContentLoaded', renderCart);