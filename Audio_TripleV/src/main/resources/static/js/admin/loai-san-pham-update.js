
    function updateLoaiSP() {
    const formData = {
    id: document.getElementById('id').value,
    kieuDang: document.getElementById('kieuDang').value,
    ngayTao: document.getElementById('ngayTao').value,
    ngayCapNhat: document.getElementById('ngayCapNhat').value || new Date().toISOString().split('T')[0] // Gán ngày hiện tại nếu trường để trống
};

    // Kiểm tra các trường bắt buộc
    if (!formData.kieuDang) {
    alert('Vui lòng điền tên Loại.');
    return;
}

    $.ajax({
    url: '/loai-san-pham/update',
    type: 'PUT',
    contentType: 'application/json',
    data: JSON.stringify(formData),
    success: function(response) {
    alert('Cập nhật Loại thành công!');
    window.location.href = '/spct/hien-thi?activated=loaisp';
},
    error: function(xhr) {
    alert('Có lỗi xảy ra: ' + xhr.responseText);
}
});
}
