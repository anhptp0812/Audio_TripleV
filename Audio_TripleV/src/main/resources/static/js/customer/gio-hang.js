function updateTotalPrice(sanPhamChiTietId, donGia) {
    // Lấy giá trị của số lượng từ input
    var soLuong = document.getElementById('soLuong_' + sanPhamChiTietId).value;

    // Tính tổng giá
    var tongGia = soLuong * donGia;

    // Cập nhật lại phần tử hiển thị tổng giá
    document.getElementById('tongGia_' + sanPhamChiTietId).textContent = tongGia + ' đ';

    // Cập nhật tổng giá của giỏ hàng nếu cần thiết (gửi thông tin này tới server nếu cần thiết)
    updateCartTotal(); // Hàm này sẽ tính toán tổng giá cho toàn bộ giỏ hàng và gửi lên server nếu cần
}

function updateCartTotal() {
    var totalPrice = 0;
    var totalElements = document.querySelectorAll('[id^="tongGia_"]');
    totalElements.forEach(function (element) {
        totalPrice += parseFloat(element.textContent.replace(' đ', '').trim());
    });

    // Kiểm tra và cập nhật nếu tổng tiền là 0
    if (totalPrice === 0) {
        document.getElementById('gioHangTongGia').textContent = 'Tổng Tiền: 0 đ';
    } else {
        document.getElementById('gioHangTongGia').textContent = totalPrice + ' đ';
    }
}

// Hàm xóa sản phẩm khỏi giỏ hàng và cập nhật tổng tiền
function removeItemFromCart(sanPhamChiTietId, soLuong, donGia) {
    // Gửi yêu cầu xóa sản phẩm khỏi giỏ hàng
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
        window.location.href = '/khach-hang/gio-hang/hien-thi';
    })
        .catch(error => console.error('Error:', error));
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

