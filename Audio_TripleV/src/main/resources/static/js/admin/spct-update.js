function updateSanPhamChiTiet(event) {
    event.preventDefault();
    const userConfirmed = confirm("Bạn có chắc chắn muốn cập nhật sản phẩm không?");
    if (!userConfirmed) {
        event.preventDefault();
    } else {
        const formData = new FormData();

        // Lấy giá trị từ các trường và thêm vào FormData
        formData.append('id', document.getElementById('id').value);
        const sanPham = document.getElementById('sanPham').value;
        const mauSac = document.getElementById('mauSac').dataset.value;
        const hang = document.getElementById('hang').dataset.value;
        const hinhAnh = document.getElementById('hinhAnh').value;
        const loaiSanPham = document.getElementById('loaiSanPham').value;
        const donGia = parseFloat(document.getElementById('donGia').value);
        const soLuong = parseInt(document.getElementById('soLuong').value, 10);
        const trangThai = document.getElementById('trangThai').value;
        const moTa = document.getElementById('moTa').value;
        const thoiGianBaoHanh = document.getElementById('thoiGianBaoHanh').value;
        const ngayTao = new Date().toISOString().split('T')[0];

        // Thêm các giá trị vào formData
        formData.append('sanPham', sanPham);
        formData.append('mauSac', mauSac);
        formData.append('hang', hang);
        formData.append('hinhAnh', hinhAnh);
        formData.append('loaiSanPham', loaiSanPham);
        formData.append('donGia', donGia);
        formData.append('soLuong', soLuong);
        formData.append('trangThai', trangThai);
        formData.append('moTa', moTa);
        formData.append('thoiGianBaoHanh', thoiGianBaoHanh);
        formData.append('ngayTao', ngayTao);

        // Kiểm tra các trường bắt buộc
        if (!sanPham || !loaiSanPham || !trangThai) {
            alert('Vui lòng điền tất cả thông tin.');
            return;
        }
        if (!mauSac) {
            alert('Vui lòng chọn màu sắc từ danh sách gợi ý.');
            return;
        }
        if (!hang) {
            alert('Vui lòng chọn hãng từ danh sách gợi ý.');
            return;
        }
        // Kiểm tra tính hợp lệ của donGia và soLuong
        if (isNaN(donGia) || donGia <= 1000) {
            alert('Đơn giá phải là số và lớn hơn 1000.');
            return;
        }

        if (isNaN(soLuong) || soLuong <= 0) {
            alert('Số lượng phải là số và lớn hơn 0.');
            return;
        }

        $.ajax({
            url: '/admin/spct/update',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("Cập nhật sản phẩm thành công!");
                window.location.href = '/admin/spct/hien-thi';
            },
            error: function (xhr) {
                let errorMessage = 'Có lỗi xảy ra.';
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                alert(errorMessage);
            }
        });
    }
}

// Hàm thiết lập chức năng gợi ý
function setupAutocomplete(inputId, suggestionsListId) {
    const inputElement = document.getElementById(inputId);
    const suggestionsList = document.getElementById(suggestionsListId);
    if (!inputElement || !suggestionsList) return;

    const suggestions = suggestionsList.querySelectorAll('.suggestion-item');
    suggestionsList.style.width = `${inputElement.offsetWidth}px`;

    // Khi nhập liệu
    inputElement.addEventListener('input', function () {
        const inputValue = this.value.toLowerCase();
        let hasSuggestions = false;

        suggestions.forEach(suggestion => {
            if (suggestion.textContent.toLowerCase().includes(inputValue)) {
                suggestion.style.display = 'block';
                hasSuggestions = true;
            } else {
                suggestion.style.display = 'none';
            }
        });

        // Hiển thị danh sách nếu có gợi ý
        suggestionsList.style.display = hasSuggestions ? 'block' : 'none';

        // Không tự động điền giá trị nếu người dùng đang nhập
    });

    // Khi chọn gợi ý
    suggestions.forEach(suggestion => {
        suggestion.addEventListener('click', function () {
            inputElement.value = this.textContent;
            inputElement.dataset.value = this.dataset.value;
            suggestionsList.style.display = 'none';
        });
    });

    // Ẩn gợi ý khi click bên ngoài
    document.addEventListener('click', function (e) {
        if (!inputElement.contains(e.target) && !suggestionsList.contains(e.target)) {
            suggestionsList.style.display = 'none';
        }
    });

    // **Tự động chọn giá trị hiện tại (nếu có) khi tải trang**
    if (inputElement.value) {
        suggestions.forEach(suggestion => {
            if (suggestion.textContent === inputElement.value) {
                inputElement.dataset.value = suggestion.dataset.value;
            }
        });
    }
}

// Áp dụng cho "Màu Sắc" và "Hãng"
setupAutocomplete('mauSac', 'mauSacSuggestionsList');
setupAutocomplete('hang', 'hangSuggestionsList');

// Tự động chọn giá trị hiện tại khi tải trang
document.addEventListener('DOMContentLoaded', function () {
    const autoFillCurrentValue = (inputId, suggestionsListId) => {
        const input = document.getElementById(inputId);
        const suggestions = document.querySelectorAll(`#${suggestionsListId} .suggestion-item`);
        if (input && suggestions.length > 0) {
            suggestions.forEach(suggestion => {
                // Kiểm tra nếu giá trị trong input trùng với giá trị gợi ý, chọn gợi ý đó
                if (suggestion.textContent === input.value) {
                    input.dataset.value = suggestion.dataset.value;
                }
            });
        }
    };

    autoFillCurrentValue('mauSac', 'mauSacSuggestionsList');
    autoFillCurrentValue('hang', 'hangSuggestionsList');
});

// Kiểm tra trước khi submit form
document.getElementById('formId')?.addEventListener('submit', function (event) {
    const ensureCurrentSelected = (inputId, suggestionsListId) => {
        const input = document.getElementById(inputId);
        if (!input?.dataset.value || input.value === '') {
            const selectedItem = document.querySelector(`#${suggestionsListId} .suggestion-item`);
            if (selectedItem) {
                input.value = selectedItem.textContent;
                input.dataset.value = selectedItem.dataset.value;
            }
        }
    };

    ensureCurrentSelected('mauSac', 'mauSacSuggestionsList');
    ensureCurrentSelected('hang', 'hangSuggestionsList');
});

document.getElementById('hinhAnh').addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const imageName = selectedOption.text; // Tên ảnh lấy từ option
    const imageUrl = imageName ? `/images/tainghe/${imageName}` : '/images/default.jpg'; // Nếu không có, hiển thị ảnh mặc định

    const previewImage = document.getElementById('previewImage');
    if (imageName) {
        previewImage.src = imageUrl;
        previewImage.alt = imageName;
        previewImage.style.display = 'block';
    } else {
        previewImage.style.display = 'none';
    }
});
