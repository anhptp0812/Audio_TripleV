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

document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const errorDiv = document.querySelector('.text-danger');

    form.addEventListener('submit', function (e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            errorDiv.textContent = "Mật khẩu mới và xác nhận mật khẩu không khớp!";
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('updateForm');
    const errorMessage = document.getElementById('error-message');

    form.addEventListener('submit', function (e) {
        errorMessage.innerHTML = ''; // Xóa thông báo lỗi trước đó
        let valid = true;

        // Lấy giá trị các trường
        const fullName = form.fullName.value.trim();
        const email = form.email.value.trim();
        const phone = form.phone.value.trim();
        const address = form.address.value.trim();

        // Kiểm tra họ và tên
        if (fullName.length < 3 || fullName.length > 50) {
            valid = false;
            errorMessage.innerHTML += 'Họ và tên phải từ 3 đến 50 ký tự.<br>';
        }

        // Kiểm tra email
        const emailPattern = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/;
        if (!emailPattern.test(email)) {
            valid = false;
            errorMessage.innerHTML += 'Email không đúng định dạng.<br>';
        }

        // Kiểm tra số điện thoại
        const phonePattern = /^0[0-9]{9}$/;
        if (!phonePattern.test(phone)) {
            valid = false;
            errorMessage.innerHTML += 'Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số.<br>';
        }

        // Kiểm tra địa chỉ
        if (address.length < 5 || address.length > 100) {
            valid = false;
            errorMessage.innerHTML += 'Địa chỉ phải từ 5 đến 100 ký tự.<br>';
        }

        // Ngăn gửi form nếu có lỗi
        if (!valid) {
            e.preventDefault();
        }
    });
});
