document.querySelectorAll('.add-to-cart').forEach(button => {
    button.addEventListener('click', function () {
        const sanPhamChiTietId = this.getAttribute('data-sanpham-id');  // Lấy ID sản phẩm chi tiết
        const soLuong = this.getAttribute('data-soLuong');  // Lấy số lượng từ data attribute

        // Kiểm tra nếu giá trị không hợp lệ
        if (isNaN(sanPhamChiTietId) || isNaN(soLuong) || soLuong <= 0) {
            alert("ID sản phẩm hoặc số lượng không hợp lệ.");
            return;
        }

        // Kiểm tra xem khách hàng đã đăng nhập chưa
        fetch('/khach-hang/check-login', {  // Tạo một yêu cầu GET để kiểm tra trạng thái đăng nhập
            method: 'GET',
        })
            .then(response => response.text())  // Đọc phản hồi dưới dạng text thay vì JSON
            .then(text => {
                if (text.includes("<!DOCTYPE html>")) {  // Kiểm tra nếu phản hồi là HTML
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

                    // Gửi yêu cầu POST nếu đã đăng nhập
                    fetch('/khach-hang/gio-hang/them-san-pham', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: `sanPhamChiTietId=${sanPhamChiTietId}&soLuong=${soLuong}`
                    })
                        .then(response => response.json())
                        .then(data => {
                            alert(data.message);
                            document.querySelector('#cart-count').textContent = data.cartCount;
                        })
                        .catch(error => console.error('Error:', error));
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                }
            })
            .catch(error => {
                console.error('Error checking login:', error);
                alert("Đã có lỗi xảy ra khi kiểm tra đăng nhập.");
            });
    });
});

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
function changeVideo(videoId) {
    var iframe = document.getElementById("ifram-play");
    iframe.src = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
}
