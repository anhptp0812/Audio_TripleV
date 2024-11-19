<<<<<<< HEAD
=======

>>>>>>> main
document.addEventListener('DOMContentLoaded', function () {
    let cart = [];

    // Hàm thêm sản phẩm vào giỏ hàng
    function addToCart(button) {
        const sanPhamId = button.getAttribute('data-id');
        const sanPham = {
            id: sanPhamId,
            image: button.closest('.product-card').querySelector('img').getAttribute('src'),
            name: button.closest('.product-card').querySelector('p').innerText,
            price: parseInt(button.closest('.product-card').querySelector('.current-price').innerText.replace('đ', '')),
            quantity: 1
        };

        // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
        const existingProduct = cart.find(item => item.id === sanPhamId);
        if (existingProduct) {
            existingProduct.quantity++;
        } else {
            cart.push(sanPham);
        }

        updateCart(); // Gọi hàm cập nhật giỏ hàng
    }

    function updateCart() {
        const cartTableBody = document.getElementById('cart-table-body');
        cartTableBody.innerHTML = '';
        let total = 0;

        cart.forEach((item, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                    <td>
                        <span style="display: inline-block; width: 250px;"><img src="${item.image}" width="=50px" height="50px"/>${item.name}</span>
                    </td>
                    <td>${item.price}đ</td>
                    <td>
                        <input type="number" min="1" value="${item.quantity}" data-index="${index}" class="quantity-input" style="width: 40px;"/>
                    </td>
                    <td>
                        <button data-index="${index}" class="remove-btn btn btn-danger">Xóa</button>
                    </td>
                `;
            cartTableBody.appendChild(row);
            total += item.price * item.quantity;
        });

        document.getElementById('total-price').innerText = `Tổng tiền: ${total}đ`;
        document.getElementById('cart-count').innerText = cart.length;

        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', function () {
                const index = this.getAttribute('data-index');
                cart[index].quantity = parseInt(this.value);
                updateCart();
            });
        });

        document.querySelectorAll('.remove-btn').forEach(button => {
            button.addEventListener('click', function () {
                const index = this.getAttribute('data-index');
                cart.splice(index, 1);
                updateCart();
            });
        });
    }

    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', function () {
            addToCart(button);
            document.getElementById('notification').style.display = 'block';
            setTimeout(() => {
                document.getElementById('notification').style.display = 'none';
            }, 2000);
        });
    });

    document.getElementById('cart-icon').addEventListener('click', function () {
        document.getElementById('cart-container').style.display = 'block'; // Hiển thị giỏ hàng
        document.querySelector('.product-container').classList.add('collapsed'); // Thu gọn sản phẩm
<<<<<<< HEAD
        document.querySelector('.product-container1').classList.add('collapsed'); // Thu gọn sản phẩm
=======
>>>>>>> main
    });

    document.getElementById('close-cart').addEventListener('click', function () {
        document.getElementById('cart-container').style.display = 'none'; // Ẩn giỏ hàng
        document.querySelector('.product-container').classList.remove('collapsed'); // Khôi phục kích thước sản phẩm
<<<<<<< HEAD
        document.querySelector('.product-container1').classList.remove('collapsed'); // Thu gọn sản phẩm
=======
>>>>>>> main
    });
});

function openModal(product, price) {
    document.getElementById('product').value = product;
    document.getElementById('price').value = price;
    document.getElementById('myModal').style.display = "block";
}

function closeModal() {
    document.getElementById('myModal').style.display = "none";
}

window.onclick = function(event) {
    if (event.target == document.getElementById('myModal')) {
        closeModal();
    }
}

// Gửi yêu cầu mua ngay
function submitQuickBuy(event) {
    event.preventDefault();

    const payload = {
        sanPhamChiTietId: parseInt(document.getElementById('product').value),
        tenKhachHang: document.getElementById('name').value,
        sdt: document.getElementById('phone').value,
        diaChi: document.getElementById('address').value,
    };

    fetch('/user/orders/quick-buy', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(response => response.json())
        .then(data => {
            alert(data.message || 'Đặt hàng thành công!');
            closeModal('quick-buy-modal');
        })
        .catch(error => console.error('Lỗi:', error));
}
