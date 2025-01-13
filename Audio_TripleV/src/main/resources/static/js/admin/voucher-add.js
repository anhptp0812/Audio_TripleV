function addVoucher() {
    const tenVoucher = $('#tenVoucher').val();
    const loaiVoucher = $('#loaiVoucher').val();
    const giaTriTien = $('#giaTriTien').val();
    const giaTriPhanTram = $('#giaTriPhanTram').val();
    const giaTriHoaDonToiThieu = $('#giaTriHoaDonToiThieu').val();
    const trangThai = $('#trangThai').val();
    const ngayBatDau = $('#ngayBatDau').val();
    const ngayKetThuc = $('#ngayKetThuc').val();

    // Dữ liệu cần gửi đi dưới dạng JSON
    const voucherData = {
        ten: tenVoucher,
        loai: loaiVoucher,
        giaTriTien: loaiVoucher === 'GiamTien' ? giaTriTien : null,
        giaTriPhanTram: loaiVoucher === 'GiamPhanTram' ? giaTriPhanTram : null,
        giaTriHoaDonToiThieu: giaTriHoaDonToiThieu,
        trangThai: trangThai,
        ngayBatDau: ngayBatDau,
        ngayKetThuc: ngayKetThuc
    };

    $.ajax({
        url: '/admin/voucher/add', // Đảm bảo đường dẫn đúng với yêu cầu của bạn
        type: 'POST',
        contentType: 'application/json', // Đặt type content là JSON
        data: JSON.stringify(voucherData), // Chuyển đổi dữ liệu thành chuỗi JSON
        success: function(response) {
            alert('Thêm voucher thành công!');
            window.location.href = '/admin/voucher/hien-thi'; // Chuyển hướng đến trang hiển thị voucher
        },
        error: function() {
            alert('Có lỗi xảy ra khi thêm voucher.');
        },
        complete: function() {
            $('.loading').remove(); // Nếu có thể dùng cho việc xử lý loading
        }
    });
}

// Function to toggle fields based on voucher type
function toggleVoucherTypeFields() {
    var loaiVoucher = document.getElementById("loaiVoucher").value;
    if (loaiVoucher === "GiamTien") {
        document.getElementById("giaTriTienField").style.display = "block";
        document.getElementById("giaTriPhanTramField").style.display = "none";
    } else if (loaiVoucher === "GiamPhanTram") {
        document.getElementById("giaTriTienField").style.display = "none";
        document.getElementById("giaTriPhanTramField").style.display = "block";
    }
}

// Call the function on page load to set the initial visibility
document.addEventListener("DOMContentLoaded", function() {
    toggleVoucherTypeFields();
});