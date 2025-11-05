// cart.render.js
// Logic render giỏ hàng từ localStorage cho cart.html

function renderCart() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const tbody = document.getElementById('cart-body-js');
    tbody.innerHTML = '';
    let total = 0;
    if (cart.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="cart-empty"><i class="fa fa-shopping-cart"></i> Giỏ hàng của bạn đang trống.</td></tr>`;
    } else {
        cart.forEach((item, idx) => {
            const price = parseFloat(item.price);
            const lineTotal = price * item.quantity;
            total += lineTotal;
            tbody.innerHTML += `
            <tr>
                <td><img src="/images/${item.image}" alt="${item.name}" class="img-thumbnail" style="max-width: 80px;max-height:80px;"></td>
                <td class="fw-bold align-middle">${item.name}</td>
                <td class="align-middle">${price.toLocaleString()} VND</td>
                <td style="width: 120px;" class="align-middle">
                    <div class="d-flex align-items-center justify-content-center gap-1">
                        <button class="btn btn-outline-secondary btn-sm" onclick="updateQty(${idx}, -1)">-</button>
                        <input type="number" min="1" value="${item.quantity}" class="form-control form-control-sm text-center" style="width: 60px;" onchange="setQty(${idx}, this.value)">
                        <button class="btn btn-outline-secondary btn-sm" onclick="updateQty(${idx}, 1)">+</button>
                    </div>
                </td>
                <td class="align-middle text-danger fw-bold">${lineTotal.toLocaleString()} VND</td>
                <td class="align-middle"><button class="btn btn-sm btn-danger" onclick="removeItem(${idx})"><i class="fa fa-times"></i></button></td>
            </tr>`;
        });
        // Hiển thị tổng tiền ngay dưới bảng
        tbody.innerHTML += `<tr>
            <td colspan="4" class="text-end fw-bold">Tổng tiền:</td>
            <td colspan="2" class="text-danger fw-bold">${total.toLocaleString()} VND</td>
        </tr>`;
    }
    document.querySelectorAll('.cart-total-js').forEach(e => e.textContent = total.toLocaleString() + ' VND');
}

function updateQty(idx, delta) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart[idx].quantity = Math.max(1, cart[idx].quantity + delta);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}
function setQty(idx, val) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart[idx].quantity = Math.max(1, parseInt(val) || 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}
function removeItem(idx) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.splice(idx, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}
function clearCart() {
    localStorage.removeItem('cart');
    renderCart();
}
document.addEventListener('DOMContentLoaded', function() {
    renderCart();
    const clearBtn = document.querySelector('button.btn-danger.btn-sm');
    if (clearBtn) clearBtn.onclick = clearCart;
});
