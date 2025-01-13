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

const ctx = document.getElementById('statisticsChart').getContext('2d');
let chart;

function loadData(timeFrame) {
    let url;
    if (timeFrame === 'day') {
        const date = document.getElementById('dateInput').value;
        if (!isValidDate(date)) {
            alert('Vui lòng nhập ngày hợp lệ (YYYY-MM-DD).');
            return;
        }
        url = `/api/thong-ke/ngay?date=${date}`;
    } else if (timeFrame === 'month') {
        const month = document.getElementById('monthInput').value;
        const year = document.getElementById('yearInputMonth').value;
        if (!isValidMonth(month) || !isValidYear(year)) {
            alert('Vui lòng nhập tháng và năm hợp lệ.');
            return;
        }
        url = `/api/thong-ke/thang?month=${month}&year=${year}`;
    } else if (timeFrame === 'year') {
        const year = document.getElementById('yearInput').value;
        if (!isValidYear(year)) {
            alert('Vui lòng nhập năm hợp lệ.');
            return;
        }
        url = `/api/thong-ke/nam?year=${year}`;
    }

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Không thể tải dữ liệu từ API.');
            return response.json();
        })
        .then(data => {
            const labels = [data.thoiGian];  // Đảm bảo rằng dữ liệu trả về có thuộc tính thoiGian
            const revenueData = [data.tongDoanhThu];  // Đảm bảo rằng dữ liệu trả về có thuộc tính tongDoanhThu
            renderChart('bar', labels, revenueData); // Vẽ biểu đồ
            updateTable([data]); // Cập nhật bảng dữ liệu
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Đã xảy ra lỗi khi tải dữ liệu: ' + error.message);
        });
}

function renderChart(type, labels = [], data = []) {
    if (chart) chart.destroy();
    chart = new Chart(ctx, {
        type: type,
        data: {
            labels: labels,
            datasets: [{
                label: 'Tổng Doanh Thu',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'top' },
                title: { display: true, text: 'Biểu Đồ Thống Kê' }
            }
        }
    });
}

function updateTable(data) {
    const tableBody = document.getElementById('statisticsTableBody');
    tableBody.innerHTML = '';
    data.forEach(item => {
        const row = `
                <tr>
                    <td>${item.thoiGian}</td>
                    <td>${item.soLuongDonHang}</td>
                    <td>${item.tongDoanhThu.toLocaleString()} ₫</td>
                    <td>${item.loiNhuan.toLocaleString()} ₫</td>
                </tr>
            `;
        tableBody.innerHTML += row;
    });
}

function isValidDate(date) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    return regex.test(date);
}

function isValidMonth(month) {
    return month >= 1 && month <= 12;
}

function isValidYear(year) {
    return year >= 1900 && year <= new Date().getFullYear();
}