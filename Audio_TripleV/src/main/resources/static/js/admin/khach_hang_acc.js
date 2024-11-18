
$(document).ready(function () {
    // Sự kiện cho các nút Hồ Sơ, Địa Chỉ và Đổi Mật Khẩu
    $('#showProfileBtn').on('click', function () {
        $('#profileForm').collapse('show');
        $('#addressForm').collapse('hide');
        $('#passwordForm').collapse('hide');
        $('#orderHistoryForm').collapse('hide'); // Ẩn lịch sử mua hàng
        $('#profileTitle').text('Hồ Sơ Của Tôi');
    });

    $('#showAddressBtn').on('click', function () {
        $('#addressForm').collapse('show');
        $('#profileForm').collapse('hide');
        $('#passwordForm').collapse('hide');
        $('#orderHistoryForm').collapse('hide'); // Ẩn lịch sử mua hàng
        $('#profileTitle').text('Địa Chỉ Của Tôi');
    });

    $('#showPasswordBtn').on('click', function () {
        $('#passwordForm').collapse('show');
        $('#profileForm').collapse('hide');
        $('#addressForm').collapse('hide');
        $('#orderHistoryForm').collapse('hide'); // Ẩn đổi mật khẩu
        $('#profileTitle').text('Đổi Mật Khẩu');
    });

    // Sự kiện cho nút Đơn Mua
    $('#showOrderHistoryBtn').on('click', function () {
        $('#orderHistoryForm').collapse('show');
        $('#profileForm').collapse('hide');
        $('#addressForm').collapse('hide');
        $('#passwordForm').collapse('hide'); // Ẩn đổi mật khẩu
        $('#profileTitle').text('Lịch Sử Mua Hàng');
    });
});

function handleSearch() {
    const searchInput = document.getElementById('searchInput');

    // Check if the input is visible
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
        searchInput.style.display = 'block'; // Show the input field
        searchInput.focus();  // Automatically focus the input field
    } else {
        // If input is visible, proceed with search
        const searchValue = searchInput.value.trim();  // Get the trimmed value
        if (searchValue) {
            // Redirect to sang_pham.html with the search term
            window.location.href = `sang_pham.html?search=${encodeURIComponent(searchValue)}`;
        }
        // Clear the input field after search
        searchInput.value = '';
        searchInput.style.display = 'none';  // Hide the input field again
    }
}
