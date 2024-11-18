
function updateSanPhamChiTiet() {
    const formData = {
        id: document.getElementById('id').value, // Lấy ID từ trường hidden
        sanPham: document.getElementById('sanPham').value, // Tên sản phẩm
        mauSac: document.getElementById('mauSac').value,
        hang: document.getElementById('hang').value,
        hinhAnh: document.getElementById('hinhAnh').value,
        loaiSanPham: document.getElementById('loaiSanPham').value,
        trangThai: document.getElementById('trangThai').value,
        ngayTao: document.getElementById('ngayTao').value,
        ngayCapNhat: document.getElementById('ngayCapNhat').value || new Date().toISOString().split('T')[0] // Gán ngày hiện tại nếu trường để trống
    };

    // Kiểm tra các trường bắt buộc
    if (!formData.sanPham || !formData.mauSac) {
        alert('Vui lòng điền tất cả các trường bắt buộc.');
        return; // Dừng hàm nếu có trường trống
    }

    // // Đảm bảo ngày cập nhật là ngày hôm nay nếu không được nhập
    // if (!formData.ngayCapNhat) {
    //     formData.ngayCapNhat = new Date().toISOString().split('T')[0]; // Gán ngày hôm nay
    // }

    $.ajax({
        url: '/spct/update', // Thay đổi thành endpoint thực tế của bạn
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert('Cập nhật sản phẩm chi tiết thành công!');
            window.location.href = '/spct/hien-thi?activated=spct'; // Chuyển hướng về trang quản lý sản phẩm
        },
        error: function(xhr) {
            alert('Có lỗi xảy ra: ' + xhr.responseText);
        }
    });
}

