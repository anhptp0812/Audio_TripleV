function addHang() {
    const tenHang = $('#tenHang').val();

    $.ajax({
        url: '/admin/hang/add',
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify({ ten: tenHang }), // Chuyển đổi dữ liệu thành chuỗi JSON
        success: function(response) {
            alert('Thêm hãng thành công!');
            window.location.href = '/admin/hang/hien-thi';
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm hãng.');
        },
        complete: function() {
            $('.loading').remove();
        }
    });
}