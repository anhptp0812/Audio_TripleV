document.getElementById('user-icon').addEventListener('click', function (event) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của liên kết

    const dropdown = document.getElementById('user-dropdown');
    dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
});

// Ẩn menu khi click bên ngoài
document.addEventListener('click', function (event) {
    const userIcon = document.getElementById('user-icon');
    const dropdown = document.getElementById('user-dropdown');

    if (!userIcon.contains(event.target) && !dropdown.contains(event.target)) {
        dropdown.style.display = 'none';
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");

    form.addEventListener("submit", function (event) {
        let isValid = true;
        const errors = [];

        // Lấy giá trị các trường
        const fullName = document.getElementById("fullName").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const address = document.getElementById("address").value.trim();
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked')?.value;

        // Kiểm tra Họ và Tên
        if (fullName === "") {
            isValid = false;
            errors.push("Họ và tên không được để trống.");
        }

        // Kiểm tra Email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email === "") {
            isValid = false;
            errors.push("Email không được để trống.");
        } else if (!emailRegex.test(email)) {
            isValid = false;
            errors.push("Email không đúng định dạng.");
        }

        // Kiểm tra Số điện thoại
        const phoneRegex = /^[0-9]{10,11}$/; // Chỉ cho phép 10-11 chữ số
        if (phone === "") {
            isValid = false;
            errors.push("Số điện thoại không được để trống.");
        } else if (!phoneRegex.test(phone)) {
            isValid = false;
            errors.push("Số điện thoại không hợp lệ.");
        }

        // Kiểm tra Địa chỉ
        if (address === "") {
            isValid = false;
            errors.push("Địa chỉ giao hàng không được để trống.");
        }

        // Kiểm tra Phương thức thanh toán
        // if (paymentMethod === "card") {
        //     isValid = false;
        //     errors.push("Phương thức thanh toán bằng ví VnPay hiện chưa hỗ trợ.");
        // }

        // Nếu không hợp lệ, ngăn form submit và hiển thị lỗi
        if (!isValid) {
            event.preventDefault(); // Ngăn form gửi dữ liệu
            alert(errors.join("\n")); // Hiển thị lỗi dưới dạng popup
            return;
        }

        // Xác nhận đặt hàng
        const confirmOrder = confirm("Xác nhận đặt hàng? Bạn có chắc muốn hoàn tất đơn hàng này không?");
        if (!confirmOrder) {
            event.preventDefault(); // Ngăn form nếu người dùng chọn 'Hủy'
        }
    });
});