// Update voucher function
function updateVoucher() {
    const formData = {
        id: document.getElementById('id').value,
        ten: document.getElementById('ten').value,
        loai: document.getElementById('loai').value,
        giaTriTien: document.getElementById('giaTriTien').value,
        giaTriPhanTram: document.getElementById('giaTriPhanTram').value,
        giaTriHoaDonToiThieu: document.getElementById('giaTriHoaDonToiThieu').value,
        trangThai: document.getElementById('trangThai').value,
        ngayBatDau: document.getElementById('ngayBatDau').value,
        ngayKetThuc: document.getElementById('ngayKetThuc').value,
        ngayCapNhat: document.getElementById('ngayCapNhat').value || new Date().toISOString().split('T')[0] // Gán ngày hiện tại nếu trường để trống
    };

    // Kiểm tra các trường bắt buộc
    if (!formData.ten) {
        alert('Vui lòng điền tên voucher.');
        return;
    }

    // Kiểm tra xem có ít nhất một giá trị cho loại voucher
    if (formData.loai === 'GiamTien' && !formData.giaTriTien) {
        alert('Vui lòng điền giá trị tiền cho voucher.');
        return;
    } else if (formData.loai === 'GiamPhanTram' && !formData.giaTriPhanTram) {
        alert('Vui lòng điền giá trị phần trăm cho voucher.');
        return;
    }

    // Kiểm tra giá trị hóa đơn tối thiểu
    if (!formData.giaTriHoaDonToiThieu) {
        alert('Vui lòng điền giá trị hóa đơn tối thiểu.');
        return;
    }

    $.ajax({
        url: '/admin/voucher/update',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert('Cập nhật voucher thành công!');
            window.location.href = '/admin/voucher/hien-thi';
        },
        error: function(xhr) {
            alert('Có lỗi xảy ra: ' + xhr.responseText);
        }
    });
}

// Function to toggle fields based on voucher type (updated)
function toggleVoucherTypeFields() {
    var loai = document.getElementById("loai").value;
    if (loai === "GiamTien") {
        document.getElementById("giaTriTienField").style.display = "block";
        document.getElementById("giaTriPhanTramField").style.display = "none";
    } else if (loai === "GiamPhanTram") {
        document.getElementById("giaTriTienField").style.display = "none";
        document.getElementById("giaTriPhanTramField").style.display = "block";
    }
}

// Call toggle function on page load to set the initial visibility based on current selection
document.addEventListener("DOMContentLoaded", function() {
    toggleVoucherTypeFields(); // Ensure initial visibility
    document.getElementById('loai').addEventListener('change', toggleVoucherTypeFields); // Add listener to change event
});
