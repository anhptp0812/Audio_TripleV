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
        const errors = {};

        // Lấy giá trị các trường
        const fullName = document.getElementById("fullName").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const address = document.getElementById("address").value.trim();
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked')?.value;

        // Xóa các lỗi trước đó
        document.querySelectorAll(".error-message").forEach(el => el.textContent = "");

        // Kiểm tra Họ và Tên
        if (fullName === "") {
            isValid = false;
            errors.fullName = "Họ và tên không được để trống.";
        }

        // Kiểm tra Email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email === "") {
            isValid = false;
            errors.email = "Email không được để trống.";
        } else if (!emailRegex.test(email)) {
            isValid = false;
            errors.email = "Email không đúng định dạng.";
        }

        // Kiểm tra Số điện thoại
        const phoneRegex = /^[0-9]{10,11}$/; // Chỉ cho phép 10-11 chữ số
        if (phone === "") {
            isValid = false;
            errors.phone = "Số điện thoại không được để trống.";
        } else if (!phoneRegex.test(phone)) {
            isValid = false;
            errors.phone = "Số điện thoại không hợp lệ.";
        }

        // Kiểm tra Địa chỉ
        if (address === "") {
            isValid = false;
            errors.address = "Địa chỉ giao hàng không được để trống.";
        }

        // Kiểm tra Phương thức thanh toán
        if (paymentMethod === "card") {
            isValid = false;
            alert("Chưa làm");
        }

        // Hiển thị lỗi nếu có
        for (const [field, message] of Object.entries(errors)) {
            const errorElement = document.querySelector(`#${field} + .error-message`);
            if (errorElement) {
                errorElement.textContent = message;
            }
        }

        // Nếu không hợp lệ, ngăn form submit
        if (!isValid) {
            event.preventDefault();
            return;
        }

        // Xác nhận đặt hàng
        const confirmOrder = confirm("Xác nhận đặt hàng? Bạn có chắc muốn hoàn tất đơn hàng này không?");
        if (!confirmOrder) {
            event.preventDefault();
        }
    });
});
