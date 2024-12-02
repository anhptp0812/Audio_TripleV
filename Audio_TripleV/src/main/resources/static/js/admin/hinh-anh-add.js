function addHinhAnh() {
    const imageFile = $('#imageFile')[0].files[0]; // Lấy file hình ảnh

    // Kiểm tra file
    if (!imageFile) {
        updateNotification('Vui lòng chọn một hình ảnh!', 'danger');
        return;
    }

    // Kiểm tra định dạng file
    const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
    const fileExtension = imageFile.name.split('.').pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
        updateNotification('Định dạng file không hợp lệ. Chỉ chấp nhận: jpg, jpeg, png, gif.', 'danger');
        return;
    }

    // Kiểm tra kích thước file (giới hạn ví dụ: 5MB)
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (imageFile.size > maxSize) {
        updateNotification('Dung lượng file quá lớn. Vui lòng chọn file nhỏ hơn 5MB.', 'danger');
        return;
    }

    // Gửi file
    const formData = new FormData();
    formData.append('imageFile', imageFile); // Thêm file hình ảnh vào FormData

    // Hiển thị loading
    $('#loadingIndicator').show();

    $.ajax({
        url: '/hinh-anh/add', // Đường dẫn đến endpoint để thêm hình ảnh
        type: 'POST',
        data: formData, // Gửi dữ liệu dưới dạng FormData
        contentType: false, // Không cần định dạng content-type vì FormData tự động xử lý
        processData: false, // Không xử lý dữ liệu
        success: function (response) {
            alert('Thêm hình ảnh thành công!');
            updateNotification('Thêm hình ảnh thành công!', 'success');
            setTimeout(() => {
                window.location.href = '/spct/hien-thi?activated=hinhAnh';
            }, 1500);
        },
        error: function (xhr) {
            const errorMessage = xhr.responseJSON?.message || 'Có lỗi xảy ra khi thêm hình ảnh.';
            updateNotification(errorMessage, 'danger');
        },
        complete: function () {
            $('#loadingIndicator').hide();
        }
    });
}

function updateNotification(message, type) {
    const notification = $('#notification');
    notification
        .removeClass()
        .addClass(`alert alert-${type}`)
        .text(message)
        .show();
}

$(document).ready(function() {
    $('#imageFile').on('change', function(event) {
        const file = event.target.files[0]; // Lấy file vừa chọn

        if (file) {
            const reader = new FileReader(); // Tạo đối tượng FileReader

            reader.onload = function(e) {
                // Khi file đã được đọc, gán dữ liệu vào thẻ img
                $('#previewImage')
                    .attr('src', e.target.result) // Gán dữ liệu base64
                    .css('display', 'block'); // Hiển thị hình ảnh
            };

            reader.readAsDataURL(file); // Đọc file dưới dạng Data URL
        } else {
            // Nếu không có file nào được chọn, ẩn hình ảnh xem trước
            $('#previewImage').css('display', 'none');
        }
    });
});