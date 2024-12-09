// Hàm để hiển thị bảng đã chọn và cập nhật trạng thái nút tương ứng
function showTable(tableId, buttonId, title) {
    hideAllTables();
    if (tableId === 'category'){
        document.getElementById(tableId).style.display = 'flex';
        document.querySelector(".product-table").style.display = 'none';

    } else {
        if (document.querySelector(".product-table")){
            document.querySelector(".product-table").style.display = 'block';
        }
        document.getElementById(tableId).style.display = 'table';
        document.getElementById(buttonId).style.display = 'block'; // Hiện nút "Thêm" tương ứng
        updateTableTitle(title);
    }
}

// Hàm ẩn tất cả bảng và nút "Thêm"
function hideAllTables() {
    const tables = ['category', 'productTable', 'colorTable', 'categoryTable', 'brandTable', 'imageTable'];
    tables.forEach(table => {
        if (document.getElementById(table)){
            document.getElementById(table).style.display = 'none';
        }
    });

    const buttons = ['nutThem', 'nutThemMauSac', 'nutThemLoai', 'nutThemHang', 'nutThemHinhAnh'];
    buttons.forEach(button => {
        document.getElementById(button).style.display = 'none';
    });
}

// Cập nhật tiêu đề bảng
function updateTableTitle(title) {
    if (title === ''){
        document.getElementById('tableTitle').style.display = 'none';
    } else {
        document.getElementById('tableTitle').style.display = 'block';
    }
    document.getElementById("tableTitle").innerText = title;
}

// Gán sự kiện click cho các nút sidebar
document.getElementById("btnToanBo").addEventListener("click", function() {
    showTable('productTable', 'nutThem', "Toàn Bộ Sản Phẩm");
});

document.getElementById("btnMauSac").addEventListener("click", function() {
    showTable('colorTable', 'nutThemMauSac', "Màu Sắc Sản Phẩm");
});

document.getElementById("btnLoai").addEventListener("click", function() {
    showTable('categoryTable', 'nutThemLoai', "Loại Sản Phẩm");
});

document.getElementById("btnHang").addEventListener("click", function() {
    showTable('brandTable', 'nutThemHang', "Hãng");
});

document.getElementById("btnHinhAnh").addEventListener("click", function() {
    showTable('imageTable', 'nutThemHinhAnh', "Hình ảnh");
});

document.getElementById("btnBanHang").addEventListener("click", function() {
    showTable('category', 'category', "");
});

// Hàm mở modal chỉnh sửa
function openEditModal(modalId, data) {
    for (const [key, value] of Object.entries(data)) {
        document.getElementById(key).value = value;
    }
    $(`#${modalId}`).modal('show');
}

function openEditColorModal(colorData) {
    openEditModal('editColorModal', colorData);
}

function openEditCategoryModal(categoryData) {
    openEditModal('editCategoryModal', categoryData);
}

function openEditBrandModal(brandData) {
    openEditModal('editBrandModal', brandData);
}

function openEditProductModal(productData) {
    openEditModal('editProductModal', productData);
}

function openEditImageModal(imageData) {
    openEditModal('editImageModal', imageData);
}

// Hàm lưu cho các thực thể khác nhau
function saveEntity(url, data, successMessage, modalId) {
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        success: function() {
            alert(successMessage);
            $(`#${modalId}`).modal('hide');
            // Cập nhật bảng tương ứng tại đây (nếu cần)
        },
        error: function() {
            alert('Có lỗi xảy ra khi lưu.');
        }
    });
}

function saveColor() {
    const colorData = {
        name: document.getElementById('colorName').value,
        createdDate: document.getElementById('colorCreatedDate').value,
        updatedDate: document.getElementById('colorUpdatedDate').value
    };
    saveEntity('/api/color/save', colorData, 'Lưu màu sắc thành công!', 'editColorModal');
}

function saveCategory() {
    const categoryData = {
        // Thu thập dữ liệu loại sản phẩm tại đây
    };
    saveEntity('/api/category/save', categoryData, 'Lưu loại sản phẩm thành công!', 'editCategoryModal');
}

function saveBrand() {
    const brandData = {
        // Thu thập dữ liệu hãng tại đây
    };
    saveEntity('/api/brand/save', brandData, 'Lưu hãng thành công!', 'editBrandModal');
}

function saveProduct() {
    const productData = {
        // Thu thập dữ liệu sản phẩm tại đây
    };
    saveEntity('/api/product/save', productData, 'Lưu sản phẩm thành công!', 'editProductModal');
}

function saveImage() {
    const imageData = {
        // Thu thập dữ liệu sản phẩm tại đây
    };
    saveEntity('/api/image/save', imageData, 'Lưu hình ảnh thành công!', 'editImageModal');
}

function handleSearch() {
    const searchValue = document.getElementById('searchInput').value;
    alert('Đang tìm kiếm: ' + searchValue);
}

window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const activated = urlParams.get('activated');
    if (activated === 'spct') {
        document.getElementById("btnToanBo").click();
    }
    if (activated === 'colors') {
        document.getElementById("btnMauSac").click(); // Kích hoạt nút "Màu sắc sản phẩm"
    }
    if (activated === 'loaisp') {
        document.getElementById("btnLoai").click();
    }
    if (activated === 'hang') {
        document.getElementById("btnHang").click();
    }
    if (activated === 'hinhAnh') {
        document.getElementById("btnHinhAnh").click();
    }
};