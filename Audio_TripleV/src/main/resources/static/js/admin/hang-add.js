function addHang() {
    const tenHang = $('#tenHang').val();

    $.ajax({
        url: '/hang/add',
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify({ ten: tenHang }), // Chuyển đổi dữ liệu thành chuỗi JSON
        beforeSend: function() {
            $('body').append('<div class="loading">Đang xử lý...</div>');
        },
        success: function(response) {
            alert('Thêm hãng thành công!');
            window.location.href = '/spct/hien-thi?activated=hang';
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm hãng.');
        },
        complete: function() {
            $('.loading').remove();
        }
    });
}