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

document.querySelector('#comment-form').addEventListener('submit', function(event) {
    event.preventDefault();  // Ngăn chặn hành động mặc định của form

    // Kiểm tra xem người dùng có đăng nhập không
    fetch('/khach-hang/check-login', {
        method: 'GET',
    })
        .then(response => response.text())  // Đọc phản hồi dưới dạng text thay vì JSON
        .then(text => {
            if (text.includes("<!DOCTYPE html>")) {  // Kiểm tra nếu phản hồi là HTML (nghĩa là chưa đăng nhập)
                const confirmLogin = confirm("Bạn chưa đăng nhập. Bạn có muốn đăng nhập không?");
                if (confirmLogin) {
                    window.location.href = '/login';  // Chuyển hướng đến trang đăng nhập
                }
                return;
            }

            try {
                const data = JSON.parse(text);  // Thử parse thành JSON nếu phản hồi không phải HTML
                if (!data.loggedIn) {  // Nếu khách hàng chưa đăng nhập
                    const confirmLogin = confirm("Bạn chưa đăng nhập. Bạn có muốn đăng nhập không?");
                    if (confirmLogin) {
                        window.location.href = '/login';  // Chuyển hướng đến trang đăng nhập
                    }
                    return;
                }

                // Nếu đã đăng nhập, gửi form bình luận
                document.querySelector('#comment-form').submit();  // Gửi form bình luận

            } catch (error) {
                console.error('Error parsing JSON:', error);
                alert("Đã có lỗi xảy ra khi xử lý dữ liệu.");
            }
        })
        .catch(error => {
            console.error('Error checking login:', error);
            alert("Đã có lỗi xảy ra khi kiểm tra đăng nhập.");
        });
});
