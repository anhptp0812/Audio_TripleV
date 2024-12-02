// Hàm xóa sản phẩm khỏi giỏ hàng và cập nhật tổng tiền
function removeItemFromCart(sanPhamChiTietId, soLuong, donGia) {
    // Hiển thị hộp thoại xác nhận trước khi xóa sản phẩm
    const confirmation = window.confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?");

    // Nếu người dùng chọn 'OK', thực hiện xóa
    if (confirmation) {
        fetch(`/khach-hang/gio-hang/xoa/${sanPhamChiTietId}`, {
            method: 'GET',
        }).then(response => {
            if (response.ok) {
                // Sau khi xóa thành công, cập nhật lại tổng giá giỏ hàng
                return response.text();
            } else {
                throw new Error("Xóa sản phẩm thất bại");
            }
        }).then(message => {
            alert(message); // Hiển thị thông báo thành công
            updateTotalPriceAfterRemove(soLuong, donGia); // Cập nhật tổng giá sau khi xóa
            window.location.href = '/khach-hang/gio-hang/hien-thi';  // Điều hướng lại đến trang giỏ hàng sau khi xóa
        })
            .catch(error => console.error('Error:', error));
    } else {
        // Nếu người dùng chọn 'Cancel', không làm gì
        console.log("Xóa sản phẩm đã bị hủy.");
    }
}

function updateTotalPriceAfterRemove(soLuong, donGia) {
    // Tính tổng tiền của sản phẩm đã xóa
    var totalPriceToRemove = soLuong * donGia;

    // Cập nhật tổng giá giỏ hàng sau khi trừ đi số tiền của sản phẩm đã xóa
    var currentTotalPrice = parseFloat(document.getElementById('gioHangTongGia').textContent.replace('Tổng Tiền: ', '').replace(' đ', '').trim());
    var newTotalPrice = currentTotalPrice - totalPriceToRemove;

    // Cập nhật tổng giá mới vào giao diện
    if (newTotalPrice === 0) {
        document.getElementById('gioHangTongGia').textContent = 'Tổng Tiền: 0 đ';
    } else {
        document.getElementById('gioHangTongGia').textContent = 'Tổng Tiền: ' + newTotalPrice + ' đ';
    }
}

function updateQuantity(productId, newQuantity) {
    // Lấy giá trị đơn giá từ thuộc tính data-price của input
    const unitPrice = parseFloat(document.querySelector(`#soLuong_${productId}`).getAttribute('data-price'));

    // Kiểm tra số lượng mới có hợp lệ không
    if (newQuantity < 1 || isNaN(newQuantity)) {
        alert('Số lượng không hợp lệ.');
        return;
    }

    // Kiểm tra đơn giá có hợp lệ không
    if (isNaN(unitPrice)) {
        alert('Đơn giá không hợp lệ.');
        return;
    }

    // Cập nhật lại tổng giá của sản phẩm
    const totalPrice = unitPrice * newQuantity;
    const totalPriceElement = document.querySelector(`#tongGia_${productId}`);
    totalPriceElement.innerText = totalPrice.toFixed(2) + ' đ';

    // Cập nhật tổng tiền của giỏ hàng
    updateCartTotal();

    // Gửi yêu cầu AJAX để cập nhật số lượng
    fetch('/khach-hang/gio-hang/cap-nhat-so-luong', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            productId: productId,
            quantity: newQuantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Cập nhật tổng tiền của giỏ hàng
                updateCartTotal();
            } else {
                alert(data.message);  // Hiển thị thông báo nếu có lỗi (như hết hàng)
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateCartTotal() {
    let total = 0;
    document.querySelectorAll('span[id^="tongGia_"]').forEach(function (element) {
        const price = parseFloat(element.innerText.replace(' đ', '').replace(',', ''));
        if (!isNaN(price)) {
            total += price;
        }
    });
    document.querySelector('#gioHangTongGia').innerText = 'Tổng Tiền: ' + total.toFixed(2) + ' đ';
}

document.getElementById('user-icon').addEventListener('click', function (event) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của liên kết

    const dropdown = document.getElementById('user-dropdown');
    dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
});

// Ẩn menu khi click bên ngoài
document.addEventListener('click', function (event) {
    const userIcon = document.getElementById('user-icon');
    const dropdown = document.getElementById('user-dropdown');

    if (!userIcon.contains(event.target) && !dropdown.contains(event.target)) {
        dropdown.style.display = 'none';
    }
});

document.querySelector('#chot-don').addEventListener('click', function () {
    fetch('/khach-hang/don-hang/chot-don', {
        method: 'GET'
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url; // Chuyển hướng đến trang đơn hàng của tôi
            }
        })
        .catch(error => console.error('Error:', error));
});
