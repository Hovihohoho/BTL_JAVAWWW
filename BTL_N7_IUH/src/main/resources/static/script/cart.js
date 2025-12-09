/* cart.js - Unified, robust cart logic (replace your existing cart.js) */
(function () {
    "use strict";

    /* ---------- Helpers ---------- */
    function safeParseInt(v, fallback = 0) {
        const n = parseInt(v);
        return isNaN(n) ? fallback : n;
    }
    function safeParseFloat(v, fallback = 0) {
        const n = parseFloat(v);
        return isNaN(n) ? fallback : n;
    }

    /* ---------- Toast ---------- */
    function showToast(msg, type = 'success') {
        let container = document.getElementById('toast-container-global');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container-global';
            container.style.cssText = 'position: fixed; top: 80px; right: 20px; z-index: 9999;';
            document.body.appendChild(container);
        }
        const toast = document.createElement('div');
        toast.className = `toast show align-items-center text-white ${type === 'success' ? 'bg-success' : 'bg-danger'} border-0 mb-2 shadow`;
        toast.style.minWidth = '220px';
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body" style="font-size:0.95rem;">
                    <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2"></i>${msg}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" aria-label="Close"></button>
            </div>
        `;
        // close handler
        toast.querySelector('.btn-close').addEventListener('click', () => toast.remove());
        container.appendChild(toast);
        setTimeout(() => { if (toast.parentElement) toast.remove(); }, 3500);
    }

    /* ---------- Core: addToCart / updateBadge ---------- */
    function getCart() {
        try {
            return JSON.parse(localStorage.getItem('cart')) || [];
        } catch (e) {
            return [];
        }
    }
    function saveCart(cart) {
        localStorage.setItem('cart', JSON.stringify(cart));
    }

    // product: {id, name, price, image, quantity}
    window.addToCart = function (product) {
        if (!product || !product.id) {
            console.error('addToCart: invalid product', product);
            showToast('Lỗi: dữ liệu sản phẩm không hợp lệ', 'error');
            return;
        }

        const cart = getCart();
        const idx = cart.findIndex(it => String(it.id) === String(product.id));
        const qtyToAdd = safeParseInt(product.quantity, 1);

        if (idx > -1) {
            cart[idx].quantity = safeParseInt(cart[idx].quantity, 0) + qtyToAdd;
        } else {
            const p = {
                id: String(product.id),
                name: String(product.name || ''),
                price: safeParseFloat(product.price, 0),
                image: product.image || '',
                quantity: qtyToAdd
            };
            cart.push(p);
        }
        saveCart(cart);
        updateCartBadge();
        showToast(`Đã thêm ${qtyToAdd} sản phẩm vào giỏ hàng!`);
    };

    function findBadges() {
        // Các selector dự phòng: class chuyên dụng, bootstrap default, và selector cũ
        const selectors = [
            '.cart-badge',                      // recommended (add this class to badge)
            '.navbar .badge',                   // common
            '.badge-cart',                      // fallback
            '.badge[data-cart]',                // fallback attribute
            '.fa-shopping-bag ~ .badge',        // old try
            '.fa-shopping-bag + .badge'         // neighboring
        ];
        const set = new Set();
        selectors.forEach(sel => {
            document.querySelectorAll(sel || '').forEach(el => set.add(el));
        });
        return Array.from(set);
    }

    window.updateCartBadge = function () {
        const cart = getCart();
        const total = cart.reduce((s, it) => s + safeParseInt(it.quantity, 0), 0);

        const badges = findBadges();
        if (badges.length === 0) {
            // last resort: try to find any badge inside header
            const guess = document.querySelector('header .badge, nav .badge, .top-bar .badge, .navbar .badge');
            if (guess) badges.push(guess);
        }

        badges.forEach(b => {
            b.textContent = total;
            b.style.display = total > 0 ? 'inline-block' : 'none';
        });

        // also expose total for server-side Thymeleaf fallback if needed
        window.__CART_TOTAL = total;
    };

    /* ---------- UI init: attach click handlers ---------- */
    function initAddButtons() {
        // 1) Buttons using data-* attributes (recommended)
        document.querySelectorAll('.add-to-cart-btn, .add-to-cart-btn-detail').forEach(btn => {
            if (btn._cartBound) return; // prevent double bind
            btn._cartBound = true;
            btn.addEventListener('click', function (e) {
                // read data- attributes
                const id = this.getAttribute('data-id') || this.dataset.id;
                const name = this.getAttribute('data-name') || this.dataset.name || this.getAttribute('title') || '';
                const price = this.getAttribute('data-price') || this.dataset.price || this.getAttribute('data-value') || 0;
                const image = this.getAttribute('data-image') || this.dataset.image || '';
                // allow qty input near the button
                let qty = 1;
                // try to find nearby qty input with id qtyInput or input[name="qty"]
                const qtyInput = document.getElementById('qtyInput') || this.querySelector('#qtyInput') || this.closest('.product-info')?.querySelector('#qtyInput') || this.closest('form')?.querySelector('input[name="qty"]');
                if (qtyInput) qty = safeParseInt(qtyInput.value, 1);
                // compose product
                const product = { id, name, price: safeParseFloat(price, 0), image, quantity: qty };
                window.addToCart(product);
            });
        });

        // 2) Buttons that expect inline JS product object on window (Thymeleaf inline approach)
        // If page defines window.PAGE_PRODUCT and there is a button with class .add-to-cart-inline
        const inlineBtns = document.querySelectorAll('.add-to-cart-inline');
        inlineBtns.forEach(btn => {
            if (btn._inlineBound) return;
            btn._inlineBound = true;
            btn.addEventListener('click', function () {
                // PAGE_PRODUCT should be defined by server: window.PAGE_PRODUCT = {...}
                if (window.PAGE_PRODUCT) {
                    const qty = document.getElementById('qtyInput') ? safeParseInt(document.getElementById('qtyInput').value, 1) : 1;
                    const product = Object.assign({}, window.PAGE_PRODUCT, { quantity: qty });
                    window.addToCart(product);
                } else {
                    console.error('Expected window.PAGE_PRODUCT for inline add but not found.');
                    showToast('Lỗi: dữ liệu sản phẩm chưa sẵn sàng', 'error');
                }
            });
        });
    }

    /* ---------- Public helpers for cart page (render/update/remove) ---------- */
    window.renderCart = function () {
        try {
            const cart = getCart();
            const tbody = document.getElementById('cart-body-js');
            if (!tbody) return;
            tbody.innerHTML = '';
            let subtotal = 0;
            if (cart.length === 0) {
                tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-muted">Giỏ hàng trống</td></tr>`;
            } else {
                cart.forEach((item, idx) => {
                    const price = safeParseFloat(item.price, 0);
                    const qty = safeParseInt(item.quantity, 0);
                    const line = price * qty;
                    subtotal += line;
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td><img src="/images/${item.image}" class="img-thumbnail" style="width:60px;max-height:60px;object-fit:cover;"></td>
                        <td class="align-middle fw-semibold">${item.name}</td>
                        <td class="align-middle">${price.toLocaleString('vi-VN')} VND</td>
                        <td class="align-middle" style="width:140px;">
                            <div class="input-group input-group-sm">
                                <button class="btn btn-outline-secondary" data-idx="${idx}" data-delta="-1">-</button>
                                <input type="text" class="form-control text-center" value="${qty}" readonly>
                                <button class="btn btn-outline-secondary" data-idx="${idx}" data-delta="1">+</button>
                            </div>
                        </td>
                        <td class="align-middle text-danger fw-bold">${line.toLocaleString('vi-VN')} VND</td>
                        <td class="align-middle">
                            <button class="btn btn-sm btn-danger remove-item-btn" data-idx="${idx}"><i class="fa fa-trash"></i></button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
            updateTotalDisplay(subtotal);
            updateCartBadge();
        } catch (e) {
            console.error(e);
        }
    };

    window.updateTotalDisplay = function (subtotal) {
        const shipping = subtotal > 0 ? 15000 : 0;
        const grandTotal = subtotal + shipping;
        const subEl = document.getElementById('cart-subtotal-js');
        const shipEl = document.getElementById('cart-shipping-js');
        const grandEl = document.getElementById('cart-grandtotal-js');
        if (subEl) subEl.innerText = subtotal.toLocaleString('vi-VN') + " VND";
        if (shipEl) shipEl.innerText = shipping.toLocaleString('vi-VN') + " VND";
        if (grandEl) grandEl.innerText = grandTotal.toLocaleString('vi-VN') + " VND";
    };

    document.addEventListener('click', function (e) {
        // qty +/- on cart page
        const btn = e.target.closest('button[data-idx][data-delta]');
        if (btn) {
            const idx = safeParseInt(btn.getAttribute('data-idx'), -1);
            const delta = safeParseInt(btn.getAttribute('data-delta'), 0);
            const cart = getCart();
            if (cart[idx]) {
                const newQty = safeParseInt(cart[idx].quantity, 0) + delta;
                if (newQty < 1) return;
                cart[idx].quantity = newQty;
                saveCart(cart);
                window.renderCart();
            }
        }
        // remove item
        const rem = e.target.closest('.remove-item-btn');
        if (rem) {
            const idx = safeParseInt(rem.getAttribute('data-idx'), -1);
            const cart = getCart();
            if (cart[idx]) {
                if (!confirm('Xóa sản phẩm này?')) return;
                cart.splice(idx, 1);
                saveCart(cart);
                window.renderCart();
            }
        }
    });

    /* ---------- Init on DOM ready ---------- */
    document.addEventListener('DOMContentLoaded', function () {
        // Bind events + update badge + render cart if on cart page
        initAddButtons();
        window.updateCartBadge();
        // if cart page
        if (document.getElementById('cart-body-js')) {
            window.renderCart();
        }
        // If window.PAGE_PRODUCT available (Thymeleaf inline), bind it to any default inline btns
        // Example server-side: <script>window.PAGE_PRODUCT = [[${productJson}]];</script>
    });

})();
