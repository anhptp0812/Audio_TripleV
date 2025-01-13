$(document).ready(function () {
    $('#imageFile').on('change', function (event) {
        const file = event.target.files[0]; // Lấy file vừa chọn
        const previewImage = $('#previewImage');
        const currentImageDiv = $('#currentImageDiv');

        if (file) {
            // Nếu có ảnh mới được chọn
            const reader = new FileReader();
            reader.onload = function (e) {
                // Hiển thị ảnh xem trước
                previewImage
                    .attr('src', e.target.result)
                    .css('display', 'block'); // Hiển thị ảnh
            };
            reader.readAsDataURL(file); // Đọc file ảnh để hiển thị xem trước

            // Ẩn hình ảnh hiện tại
            currentImageDiv.css('display', 'none');
        } else {
            // Nếu không có file nào được chọn, hiển thị lại ảnh hiện tại
            previewImage.css('display', 'none');
            currentImageDiv.css('display', 'block');
        }
    });
});

// Hàm hiển thị thông báo
function updateNotification(message, type) {
    const notification = $('#notification');
    notification
        .removeClass()
        .addClass(`alert alert-${type}`)
        .text(message)
        .show();
}

// Hàm cập nhật hình ảnh qua AJAX
function updateHinhAnh() {
    const fileInput = document.getElementById('imageFile');
    const file = fileInput.files[0]; // Lấy file hình ảnh

    if (!file) {
        updateNotification('Vui lòng chọn một hình ảnh!', 'danger');
        return;
    }

    const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
    const fileExtension = file.name.split('.').pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
        updateNotification('Định dạng file không hợp lệ. Chỉ chấp nhận: jpg, jpeg, png, gif.', 'danger');
        return;
    }

    const maxSize = 5 * 1024 * 1024; // 5MB
    if (file.size > maxSize) {
        updateNotification('Dung lượng file quá lớn. Vui lòng chọn file nhỏ hơn 5MB.', 'danger');
        return;
    }

    const formData = new FormData();
    formData.append('id', $('#id').val());
    formData.append('ten', file.name); // Lưu tên file
    formData.append('ngayTao', $('#ngayTao').val());
    formData.append('ngayCapNhat', $('#ngayCapNhat').val() || new Date().toISOString().split('T')[0]);
    formData.append('imageFile', file);

    $('#loadingIndicator').show();

    $.ajax({
        url: '/admin/hinh-anh/update',
        type: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            alert('Cập nhật hình ảnh thành công!');
            window.location.href = '/admin/hinh-anh/hien-thi';
        },
        error: function (xhr) {
            const errorMessage = xhr.responseJSON?.message || 'Có lỗi xảy ra khi cập nhật hình ảnh.';
            updateNotification(errorMessage, 'danger');
        },
        complete: function () {
            $('#loadingIndicator').hide();
        }
    });
}
