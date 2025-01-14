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
    var totalPriceToRemove = soLuong * donGia;

    // Lấy tổng giá hiện tại từ phần tử 'gioHangTongGia'
    var currentTotalPrice = parseFloat(document.getElementById('gioHangTongGia').textContent.replace('Tổng Tiền: ', '').replace(' đ', '').trim());

    // Tính toán tổng giá mới sau khi trừ đi tổng giá sản phẩm đã xóa
    var newTotalPrice = currentTotalPrice - totalPriceToRemove;

    // Cập nhật tổng giá vào giao diện
    if (newTotalPrice <= 0) {
        document.getElementById('gioHangTongGia').textContent = 'Tổng Tiền: 0 đ';
    } else {
        document.getElementById('gioHangTongGia').textContent = 'Tổng Tiền: ' + newTotalPrice.toLocaleString() + ' đ';
    }
}

function updateQuantity(productId, newQuantity) {
    // Lấy phần tử input số lượng và giá trị số lượng trước đó
    const quantityInput = document.querySelector(`#soLuong_${productId}`);
    const oldQuantity = parseInt(quantityInput.value); // Số lượng trước khi thay đổi

    // Lấy đơn giá từ thuộc tính data-price
    const unitPrice = parseFloat(quantityInput.getAttribute('data-price'));

    // Kiểm tra số lượng hợp lệ
    if (newQuantity < 1 || isNaN(newQuantity)) {
        alert('Số lượng không hợp lệ.');
        return;
    }

    // Cập nhật lại tổng giá của sản phẩm
    const totalPrice = unitPrice * newQuantity;
    const totalPriceElement = document.querySelector(`#tongGia_${productId}`);
    totalPriceElement.innerText = totalPrice.toLocaleString() + ' đ';

    // Gửi yêu cầu AJAX để cập nhật số lượng trên server
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
                // Cập nhật tổng tiền giỏ hàng sau khi thành công
                updateCartTotal(data.totalPrice); // Cập nhật tổng tiền từ phản hồi server
            } else {
                // Nếu thông báo lỗi "Không đủ số lượng sản phẩm trong kho", khôi phục lại số lượng trước đó
                alert(data.message);  // Thông báo nếu có lỗi (như hết hàng)
                quantityInput.value = newQuantity; // Khôi phục lại số lượng cũ
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // Nếu có lỗi trong quá trình gửi yêu cầu, khôi phục lại số lượng trước đó
            quantityInput.value = oldQuantity;
            totalPriceElement.innerText = (unitPrice * oldQuantity).toLocaleString() + ' đ';
        });
}

function updateCartTotal(totalPrice) {
    // Cập nhật tổng tiền giỏ hàng từ phản hồi server
    document.querySelector('#gioHangTongGia').innerText = 'Tổng Tiền: ' + totalPrice;
}

// Hàm để chọn hoặc bỏ chọn tất cả các checkbox
function toggleSelectAll() {
    const selectAllCheckbox = document.getElementById('selectAll');
    const checkboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll)');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAllCheckbox.checked;
    });
    toggleButtons(); // Gọi lại hàm toggleButtons sau khi thay đổi trạng thái checkbox "Chọn tất cả"
}

// Hàm để kiểm tra trạng thái các checkbox và cập nhật nút Xóa tất cả, Xác nhận đơn hàng
function toggleButtons() {
    const selectedCheckboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll):checked');
    const deleteButton = document.getElementById('deleteSelectedBtn');
    const confirmOrderButton = document.getElementById('confirmOrderBtn');

    // Kích hoạt nút Xóa tất cả và Xác nhận đơn hàng nếu có sản phẩm được chọn
    if (selectedCheckboxes.length > 0) {
        deleteButton.disabled = false;
        confirmOrderButton.disabled = false;

        // Cập nhật giá trị của input ẩn với ID của các sản phẩm được chọn
        const selectedItems = Array.from(selectedCheckboxes).map(checkbox => checkbox.value);
        document.getElementById('selectedItems').value = selectedItems.join(',');
    } else {
        deleteButton.disabled = true;
        confirmOrderButton.disabled = true;
    }
}

// Hàm để tự động điều chỉnh trạng thái checkbox "Chọn tất cả"
function updateSelectAll() {
    const selectAllCheckbox = document.getElementById('selectAll');
    const checkboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll)');
    const totalCheckboxes = checkboxes.length;
    const checkedCheckboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll):checked').length;

    // Nếu tất cả các checkbox đều được chọn, thì chọn "Chọn tất cả"
    selectAllCheckbox.checked = (totalCheckboxes === checkedCheckboxes);

    // Nếu có ít nhất một checkbox không được chọn, bỏ chọn "Chọn tất cả"
    selectAllCheckbox.indeterminate = (checkedCheckboxes > 0 && checkedCheckboxes < totalCheckboxes);
}

// Lắng nghe sự kiện thay đổi trạng thái của các checkbox con
const checkboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll)');
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        toggleButtons(); // Cập nhật trạng thái các nút
        updateSelectAll(); // Cập nhật trạng thái checkbox "Chọn tất cả"
    });
});

window.addEventListener('load', function() {
    // Tự động chọn checkbox "Chọn tất cả" khi vào trang Giỏ Hàng
    const selectAllCheckbox = document.getElementById('selectAll');
    selectAllCheckbox.checked = true; // Đánh dấu "Chọn tất cả" là đã chọn

    // Cập nhật trạng thái của các checkbox con
    toggleSelectAll();
    updateSelectAll(); // Cập nhật trạng thái checkbox "Chọn tất cả" ngay khi trang tải
});

function removeSelectedItems() {
    const selectedCheckboxes = document.querySelectorAll('input[type="checkbox"]:not(#selectAll):checked');
    const selectedIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.value);

    // Hộp thoại xác nhận
    const userConfirmed = confirm("Bạn có chắc chắn muốn xóa các sản phẩm đã chọn khỏi giỏ hàng?");
    if (!userConfirmed) {
        return; // Hủy thao tác nếu người dùng chọn "Cancel"
    }

    // Gửi yêu cầu xóa tới server
    fetch('/khach-hang/gio-hang/xoa-tat-ca', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedIds),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                // Cập nhật giao diện giỏ hàng
                selectedCheckboxes.forEach(checkbox => {
                    const row = checkbox.closest('tr');
                    row.remove();
                });

                // Cập nhật tổng tiền
                const totalPriceElement = document.getElementById('gioHangTongGia');
                totalPriceElement.textContent = `Tổng Tiền: ${data.totalPrice}`;

                // Cập nhật trạng thái các nút
                toggleButtons();
            }
        })
        .catch(error => {
            console.error(error);
            alert("Không thể xóa sản phẩm, vui lòng thử lại.");
        });
}
