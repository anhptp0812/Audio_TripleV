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

function handleFormSubmission(event) {
    event.preventDefault(); // Chặn form gửi mặc định

    const ten = document.getElementById('ten').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const address = document.getElementById('address').value.trim();
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');

    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const phonePattern = /^(\+84|0)\d{9,10}$/;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
        alert('Vui lòng nhập họ và tên.');
        return;
    }

    if (!email || !emailPattern.test(email)) {
        alert('Vui lòng nhập email hợp lệ.');
        return;
    }

    if (!phone || !phonePattern.test(phone)) {
        alert('Vui lòng nhập số điện thoại hợp lệ (bắt đầu bằng +84 hoặc 0, 9-10 chữ số).');
        return;
    }

    if (!address) {
        alert('Vui lòng nhập địa chỉ giao hàng.');
        return;
    }

    if (!paymentMethod) {
        alert('Vui lòng chọn phương thức thanh toán.');
        return;
    }

    // Dữ liệu để gửi lên server
    const orderData = {
        fullName: ten,
        email: email,
        phone: phone,
        address: address,
        paymentMethod: paymentMethod.value
    };

    // Xử lý theo phương thức thanh toán
    if (paymentMethod.value === 'cash') {
        // Gửi dữ liệu thanh toán qua fetch
        fetch('/khach-hang/thanh-toan/dat-hang', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
            })
            .catch(error => console.error('Error:', error));
    } else if (paymentMethod.value === 'card') {
        alert('Chức năng thanh toán bằng ví VnPay hiện chưa hỗ trợ.');
    }
}

// Lắng nghe sự kiện submit của form
document.getElementById('payment-form').addEventListener('submit', handleFormSubmission);