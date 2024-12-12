function addMauSac() {
    const tenMauSac = $('#tenMauSac').val(); // Lấy giá trị tên màu sắc

    $.ajax({
        url: '/mau-sac/add', // Đường dẫn đến endpoint để thêm màu sắc
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify({ ten: tenMauSac }), // Chuyển đổi dữ liệu thành chuỗi JSON
        beforeSend: function() {
            $('body').append('<div class="loading">Đang xử lý...</div>');
        },
        success: function(response) {
            alert('Thêm màu sắc thành công!');
            window.location.href = '/mau-sac/hien-thi'; // Chuyển hướng về trang danh sách màu sắc
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm màu sắc.');
        },
        complete: function() {
            $('.loading').remove();
        }
    });
}