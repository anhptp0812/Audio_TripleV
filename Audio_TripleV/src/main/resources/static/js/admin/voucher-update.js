// Update voucher function
function updateVoucher() {
    const formData = {
        id: document.getElementById('id').value,
        ten: document.getElementById('ten').value.trim(),
        loai: document.getElementById('loai').value,
        giaTriTien: document.getElementById('giaTriTien').value.trim(),
        giaTriPhanTram: document.getElementById('giaTriPhanTram').value.trim(),
        giaTriHoaDonToiThieu: document.getElementById('giaTriHoaDonToiThieu').value.trim(),
        trangThai: document.getElementById('trangThai').value,
        ngayBatDau: document.getElementById('ngayBatDau').value,
        ngayKetThuc: document.getElementById('ngayKetThuc').value,
        ngayCapNhat: document.getElementById('ngayCapNhat').value || new Date().toISOString().split('T')[0]
    };

    // Kiểm tra các trường bắt buộc
    if (!formData.ten) {
        alert('Vui lòng điền tên voucher.');
        return;
    }

    if (!formData.giaTriHoaDonToiThieu || parseInt(formData.giaTriHoaDonToiThieu) < 1) {
        alert('Giá trị hóa đơn tối thiểu phải lớn hơn hoặc bằng 1.');
        return;
    }

    if (formData.loai === 'GiamTien') {
        if (!formData.giaTriTien || parseInt(formData.giaTriTien) < 1) {
            alert('Giá trị tiền phải lớn hơn hoặc bằng 1.');
            return;
        }
    } else if (formData.loai === 'GiamPhanTram') {
        const giaTriPhanTram = parseInt(formData.giaTriPhanTram);
        if (!formData.giaTriPhanTram || giaTriPhanTram < 1 || giaTriPhanTram > 100) {
            alert('Giá trị phần trăm phải từ 1 đến 100.');
            return;
        }
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
