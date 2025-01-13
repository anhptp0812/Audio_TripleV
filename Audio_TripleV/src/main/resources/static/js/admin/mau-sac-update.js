
function updateMauSac() {
    const formData = {
        id: document.getElementById('id').value,
        ten: document.getElementById('ten').value,
        ngayTao: document.getElementById('ngayTao').value,
        ngayCapNhat: document.getElementById('ngayCapNhat').value || new Date().toISOString().split('T')[0] // Gán ngày hiện tại nếu trường để trống
    };

    // Kiểm tra các trường bắt buộc
    if (!formData.ten) {
        alert('Vui lòng điền tên màu sắc.');
        return;
    }

    $.ajax({
        url: '/admin/mau-sac/update',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert('Cập nhật màu sắc thành công!');
            window.location.href = '/admin/mau-sac/hien-thi';
        },
        error: function(xhr) {
            alert('Có lỗi xảy ra: ' + xhr.responseText);
        }
    });
}
