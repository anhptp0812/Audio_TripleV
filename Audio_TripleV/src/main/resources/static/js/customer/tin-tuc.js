document.querySelectorAll('.add-to-cart').forEach(button => {
    button.addEventListener('click', function () {
        const sanPhamChiTietId = this.getAttribute('data-sanpham-id');  // Lấy ID sản phẩm chi tiết
        const soLuong = this.getAttribute('data-soLuong');  // Lấy số lượng từ data attribute

        // Kiểm tra giá trị trong console
        console.log("SanPhamChiTietId: " + sanPhamChiTietId);
        console.log("SoLuong: " + soLuong);

        // Kiểm tra nếu giá trị không hợp lệ
        if (isNaN(sanPhamChiTietId) || isNaN(soLuong) || soLuong <= 0) {
            alert("ID sản phẩm hoặc số lượng không hợp lệ.");
            return;
        }

        // Gửi yêu cầu POST với cả sanPhamChiTietId và soLuong
        fetch('/khach-hang/gio-hang/them-san-pham', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `sanPhamChiTietId=${sanPhamChiTietId}&soLuong=${soLuong}`  // Thêm soLuong vào body
        })
            .then(response => response.text())
            .then(message => alert(message))
            .catch(error => console.error('Error:', error));
    });
});
function updateQuantity(productId, quantity) {
    fetch(`/gio-hang/cap-nhat-so-luong`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productId, quantity }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload(); // Reload trang để cập nhật hiển thị
            } else {
                alert('Cập nhật thất bại: ' + data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}
