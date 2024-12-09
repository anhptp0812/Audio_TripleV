
function toggleProductList() {
    // Lấy phần tử danh sách sản phẩm (productList)
    const productList = document.getElementById('productList');

    // Kiểm tra trạng thái hiển thị của phần tử và thay đổi
    if (productList.style.display === 'none' || productList.style.display === '') {
        productList.style.display = 'table-row';  // Hiển thị danh sách
    } else {
        productList.style.display = 'none';  // Ẩn danh sách
    }
}

function viewOrderDetails(orderId) {
    // Lưu id vào localStorage
    localStorage.setItem('orderId', orderId);

    // Chuyển hướng đến trang productProvity
    window.location.href = '/ban-hang/' + orderId; // Chuyển hướng đến URL có id
}

document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const trangThai = params.get("trangThai");

    if (trangThai) {
        document.querySelectorAll('.btn-group .btn').forEach(btn => {
            if (btn.textContent.trim() === trangThai) {
                btn.classList.add("active");
            }
        });
    }
});
