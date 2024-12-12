function toggleSidebar() {
    var sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('expanded');
}

function handleSearch() {
    var searchQuery = document.getElementById('searchInput').value;
    alert("Tìm kiếm: " + searchQuery);
}

function toggleSearchInput() {
    var searchInput = document.getElementById('searchInput');
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
        searchInput.style.display = 'block';
        searchInput.focus();
    } else {
        searchInput.style.display = 'none';
    }
}

document.getElementById('manageMenuToggle').addEventListener('click', function (e) {
    e.preventDefault(); // Ngăn điều hướng mặc định
    const submenu = document.getElementById('manageSubmenu');
    const arrow = this.querySelector('.arrow'); // Mũi tên

    // Thay đổi trạng thái hiển thị
    if (submenu.classList.contains('show')) {
        submenu.classList.remove('show');
        this.classList.remove('active');
    } else {
        submenu.classList.add('show');
        this.classList.add('active');
    }
});
