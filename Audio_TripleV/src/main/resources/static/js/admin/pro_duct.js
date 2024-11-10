
    // Hiển thị danh sách sản phẩm mặc định khi vào trang
    document.addEventListener("DOMContentLoaded", function() {
    showProductList();
});

    function showProductList() {
    document.getElementById('tableTitle').textContent = "Danh sách sản phẩm";
    document.getElementById('productTable').style.display = 'table';
    document.getElementById('colorTable').style.display = 'none';
    document.getElementById('categoryTable').style.display = 'none';
    document.getElementById('brandTable').style.display = 'none';
}

    function showColorTable() {
    document.getElementById('tableTitle').textContent = "Màu Sắc Sản Phẩm";
    document.getElementById('productTable').style.display = 'none';
    document.getElementById('colorTable').style.display = 'table';
    document.getElementById('categoryTable').style.display = 'none';
    document.getElementById('brandTable').style.display = 'none';
}

    function showCategoryTable() {
    document.getElementById('tableTitle').textContent = "Loại Sản Phẩm";
    document.getElementById('productTable').style.display = 'none';
    document.getElementById('colorTable').style.display = 'none';
    document.getElementById('categoryTable').style.display = 'table';
    document.getElementById('brandTable').style.display = 'none';
}

    function showBrandTable() {
    document.getElementById('tableTitle').textContent = "Hãng";
    document.getElementById('productTable').style.display = 'none';
    document.getElementById('colorTable').style.display = 'none';
    document.getElementById('categoryTable').style.display = 'none';
    document.getElementById('brandTable').style.display = 'table';
}

    function openEditProductModal() {
    $('#editProductModal').modal('show');
}

    function openEditColorModal() {
    $('#editColorModal').modal('show');
}

    function openEditCategoryModal() {
    $('#editCategoryModal').modal('show');
}

    function openEditBrandModal() {
    $('#editBrandModal').modal('show');
}

    function openAddProductModal() {
    $('#addProductModal').modal('show');
}

    function handleSearch() {
    const searchValue = document.getElementById('searchInput').value;
    alert('Searching for: ' + searchValue);
}
