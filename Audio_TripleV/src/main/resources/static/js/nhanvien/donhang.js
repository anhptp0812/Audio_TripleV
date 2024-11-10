
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

    // Hàm tạo số trang động
    function generatePagination(totalPages) {
    const paginationContainer = document.querySelector('.pagination');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    // Thêm nút "Về đầu"
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-bar-left"></i>
                                      </button>`;

    // Thêm nút "Back"
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-left-circle"></i>
                                      </button>`;

    // Thêm các số trang
    for (let i = 1; i <= Math.min(3, totalPages); i++) {
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">${i}</button>`;
}

    // Thêm nút "..."
    if (totalPages > 3) {
    paginationContainer.innerHTML += `<span class="btn btn-outline-primary">...</span>`;
}

    // Thêm trang cuối cùng nếu cần
    if (totalPages > 3) {
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">${totalPages}</button>`;
}

    // Thêm nút "Next"
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-right-circle"></i>
                                      </button>`;

    // Thêm nút "Về cuối"
    paginationContainer.innerHTML += `<button class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-bar-right"></i>
                                      </button>`;
}

    // Ví dụ gọi hàm với tổng số trang là 10
    generatePagination(10);








    function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
    searchInput.style.display = 'block';
    searchInput.focus();
} else {
    const searchValue = searchInput.value.trim();
    if (searchValue) {
    window.location.href = `sang_pham.html?search=${encodeURIComponent(searchValue)}`;
}
    searchInput.value = '';
    searchInput.style.display = 'none';
}
}

    function viewOrderDetails(orderId) {
    // Lưu id vào localStorage
    localStorage.setItem('orderId', orderId);

    // Chuyển hướng đến trang productProvity
    window.location.href = '/ban-hang/' + orderId; // Chuyển hướng đến URL có id
}

