document.querySelectorAll('.add-to-cart').forEach(button => {
    button.addEventListener('click', function () {
        const sanPhamChiTietId = this.getAttribute('data-sanpham-id');  // Lấy ID sản phẩm chi tiết
        const soLuong = this.getAttribute('data-soLuong');  // Lấy số lượng từ data attribute

        // Kiểm tra nếu giá trị không hợp lệ
        if (isNaN(sanPhamChiTietId) || isNaN(soLuong) || soLuong <= 0) {
            alert("ID sản phẩm hoặc số lượng không hợp lệ.");
            return;
        }

        // Gửi yêu cầu POST với cả sanPhamChiTietId và soLuong
        fetch('/khach-hang/gio-hang/them-san-pham', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `sanPhamChiTietId=${sanPhamChiTietId}&soLuong=${soLuong}`  // Thêm soLuong vào body
        })
            .then(response => response.json())  // Đảm bảo nhận dữ liệu dưới dạng JSON
            .then(data => {
                alert(data.message);  // Hiển thị thông báo
                // Cập nhật số lượng giỏ hàng trên giao diện mà không cần tải lại trang
                document.querySelector('#cart-count').textContent = data.cartCount;
            })
            .catch(error => console.error('Error:', error));
    });
});
// Initialize AOS
AOS.init({
    duration: 1000,
    once: true,
});

<!-- Custom JavaScript for interactive effects -->

// Add hover effect on product cards
document.querySelectorAll('.product-card').forEach(card => {
    card.addEventListener('mouseover', () => {
        card.classList.add('shadow-lg');
    });
    card.addEventListener('mouseout', () => {
        card.classList.remove('shadow-lg');
    });
});

// Toggle icon and smooth scroll for "Xem cấu hình chi tiết" button
const toggleSpecsButton = document.querySelector('.toggle-specs');
const moreSpecs = document.getElementById('moreSpecs');

toggleSpecsButton.addEventListener('click', function () {
    // Toggle the collapse
    const bsCollapse = new bootstrap.Collapse(moreSpecs, {
        toggle: false
    });
    if (moreSpecs.classList.contains('show')) {
        bsCollapse.hide();
    } else {
        bsCollapse.show();
    }

    // Toggle the icon
    const icon = this.querySelector('i');
    icon.classList.toggle('fa-chevron-down');
    icon.classList.toggle('fa-chevron-up');

    // Smooth scroll to the detailed specs
    setTimeout(() => {
        moreSpecs.scrollIntoView({behavior: 'smooth'});
    }, 300); // Delay to allow collapse animation to start
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


