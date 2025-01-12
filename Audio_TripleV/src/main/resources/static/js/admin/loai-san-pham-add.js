function addLoaiSP() {
    const kieuDang = $('#kieuDangID').val();

    $.ajax({
        url: '/admin/loai-san-pham/add',
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify({ kieuDang: kieuDang }), // Chuyển đổi dữ liệu thành chuỗi JSON
        success: function(response) {
            alert('Thêm loại sản phẩm thành công!');
            window.location.href = '/admin/loai-san-pham/hien-thi'; // Chuyển hướng về trang danh sách màu sắc
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm loại sản phẩm.');
        },
        complete: function() {
            $('.loading').remove();
        }
    });
}