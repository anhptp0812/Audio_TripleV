
    // Hàm để thêm sản phẩm chi tiết
    function addSanPhamChiTiet() {
    const formData = new FormData(document.getElementById('addForm')); // Sử dụng FormData để xử lý file upload

    $.ajax({
    url: '/spct/add', // Thay đổi thành endpoint thực tế của bạn
    type: 'POST',
    processData: false, // Để jQuery không xử lý dữ liệu
    contentType: false, // Để jQuery không thiết lập contentType
    data: formData,
    beforeSend: function () {
    // Hiển thị loader trước khi gửi
    $('body').append('<div class="loading">Đang xử lý...</div>');
},
    success: function (response) {
    alert('Thêm sản phẩm chi tiết thành công!');
    window.location.href = '/spct/hien-thi?activated=spct'; // Chuyển hướng về trang quản lý sản phẩm
},
    error: function () {
    alert('Có lỗi xảy ra khi thêm sản phẩm chi tiết.');
},
    complete: function () {
    // Ẩn loader sau khi hoàn thành
    $('.loading').remove();
}
});
}
