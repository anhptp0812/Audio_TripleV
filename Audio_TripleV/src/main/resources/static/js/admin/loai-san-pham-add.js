function addLoaiSP() {
    const kieuDang = $('#kieuDangID').val();

    $.ajax({
        url: '/loai-san-pham/add',
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify({ kieuDang: kieuDang }), // Chuyển đổi dữ liệu thành chuỗi JSON
        beforeSend: function() {
            $('body').append('<div class="loading">Đang xử lý...</div>');
        },
        success: function(response) {
            alert('Thêm loại sản phẩm thành công!');
            window.location.href = '/spct/hien-thi?activated=loaisp'; // Chuyển hướng về trang danh sách màu sắc
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm loại sản phẩm.');
        },
        complete: function() {
            $('.loading').remove();
        }
    });
}