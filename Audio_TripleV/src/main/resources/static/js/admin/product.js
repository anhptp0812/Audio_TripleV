
    function showProductList() {
    hideAllTables();
    document.getElementById('productTable').style.display = 'table';
}

    function showColorTable() {
    hideAllTables();
    document.getElementById('colorTable').style.display = 'table';
}

    function showCategoryTable() {
    hideAllTables();
    document.getElementById('categoryTable').style.display = 'table';
}

    function showBrandTable() {
    hideAllTables();
    document.getElementById('brandTable').style.display = 'table';
}

    function hideAllTables() {
    document.getElementById('productTable').style.display = 'none';
    document.getElementById('colorTable').style.display = 'none';
    document.getElementById('categoryTable').style.display = 'none';
    document.getElementById('brandTable').style.display = 'none';
}

    function openEditColorModal(colorName, createdDate, updatedDate) {
    document.getElementById('colorName').value = colorName;
    document.getElementById('colorCreatedDate').value = createdDate;
    document.getElementById('colorUpdatedDate').value = updatedDate;
    $('#editColorModal').modal('show');
}

    function openEditCategoryModal(categoryName, createdDate, updatedDate) {
    document.getElementById('categoryName').value = categoryName;
    document.getElementById('categoryCreatedDate').value = createdDate;
    document.getElementById('categoryUpdatedDate').value = updatedDate;
    $('#editCategoryModal').modal('show');
}

    function openEditBrandModal(brandName, createdDate, updatedDate) {
    document.getElementById('brandName').value = brandName;
    document.getElementById('brandCreatedDate').value = createdDate;
    document.getElementById('brandUpdatedDate').value = updatedDate;
    $('#editBrandModal').modal('show');
}

    function openEditProductModal(productName, productColor, productBrand, productCategory) {
    document.getElementById('productName').value = productName;
    document.getElementById('productColor').value = productColor;
    document.getElementById('productBrand').value = productBrand;
    document.getElementById('productCategory').value = productCategory;
    document.getElementById('productStatus').value = productStatus;
    document.getElementById('productCreatedDate').value = productCreatedDate;
    document.getElementById('productUpdatedDate').value = productUpdatedDate;
    $('#editProductModal').modal('show');
}

    function saveColor() {
    // Code to save color
    $('#editColorModal').modal('hide');
}

    function saveCategory() {
    // Code to save category
    $('#editCategoryModal').modal('hide');
}

    function saveBrand() {
    // Code to save brand
    $('#editBrandModal').modal('hide');
}

    function saveProduct() {
    // Code to save product
    $('#editProductModal').modal('hide');
}

    function handleSearch() {
    const searchValue = document.getElementById('searchInput').value;
    alert('Searching for: ' + searchValue);
}
