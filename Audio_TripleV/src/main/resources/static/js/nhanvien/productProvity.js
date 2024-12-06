let formDisplayed = false;
let selectedProducts = []; // Mảng lưu trữ sản phẩm đã chọn

document.addEventListener('DOMContentLoaded', function () {
    // Thêm sự kiện cho các nút "Add Product"
    addProductButtonEventListeners();

    const today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    const yyyy = today.getFullYear();

    const formattedDate = yyyy + '-' + mm + '-' + dd;

    const ngayGiaoInput = document.getElementById('ngayGiaoId');
    ngayGiaoInput.value = formattedDate; // Thiết lập giá trị mặc định là ngày hôm nay
});

// Hàm thêm sự kiện cho nút "Add Product"
function addProductButtonEventListeners() {
    const addProductButtons = document.querySelectorAll('td button');
    addProductButtons.forEach(button => {
        button.addEventListener('click', function () {
            const row = this.closest('tr');
            const spctId = row.querySelector('td:nth-child(1)').textContent.trim();
            const productName = row.querySelector('td:nth-child(2)').textContent.trim();
            const quantity = 1;
            // const quantity = parseInt(row.querySelector('td:nth-child(8)').textContent.trim(), 10) || 0;
            const price = parseFloat(row.querySelector('td:nth-child(7)').textContent.trim()) || 0;

            addProductToForm(spctId, productName, quantity, price);
        });
    });
}

// Hàm xử lý việc tìm kiếm
$(document).ready(function () {
    $('#searchForm').submit(function (event) {
        event.preventDefault(); // Ngừng reload trang

        var formAction = $(this).attr('action'); // Lấy URL từ form action
        var formData = $(this).serialize(); // Lấy dữ liệu form

        $.ajax({
            url: formAction,
            method: 'GET',
            data: formData,
            success: function (response) {
                // Cập nhật bảng với dữ liệu mới
                $('#tableBody').html($(response).find('#tableBody').html());

                // Gắn lại sự kiện cho các nút "Add Product"
                addProductButtonEventListeners();
            },
            error: function () {
                alert('Có lỗi xảy ra trong quá trình tìm kiếm!');
            }
        });
    });
});

function updateProductQuantityInTable(product) {
    const rows = document.querySelectorAll('#addedProductsTableBody tr');
    rows.forEach(row => {
        const spctId = row.querySelector('input[name="spctIds"]').value;
        if (spctId === product.spctId) {
            const quantityInput = row.querySelector('input[name="soLuong"]');

            // Cập nhật số lượng trong ô input
            quantityInput.value = product.quantity;

            // Cập nhật lại tổng tiền
            updateTotalAmount();  // Cập nhật tổng tiền sau khi thay đổi số lượng
        }
    });
}

function saveOrderDetails() {
    // Tạo đối tượng orderRequest
    event.preventDefault();
    const orderRequest = {
        products: selectedProducts,
        // Bạn có thể thêm các thông tin khác như ID đơn hàng, thông tin người dùng, v.v.
    };

    // Gửi orderRequest đến backend
    fetch('/user/ban-hang/save-details', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json' // Thêm header này
        },
        body: JSON.stringify(orderRequest)

    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Order saved successfully:', data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function removeProduct(button) {
    const row = button.closest('tr');
    const spctId = row.querySelector('input[name="spctIds"]').value;

    // Xóa sản phẩm khỏi mảng selectedProducts
    selectedProducts = selectedProducts.filter(product => product.spctId !== spctId);

    // Xóa dòng sản phẩm khỏi bảng
    row.remove();

    // Cập nhật tổng tiền sau khi xóa
    updateTotalAmount();
}

function selectCustomer(id, ten, sdt) {
    // Cập nhật thông tin khách hàng vào form đơn hàng
    document.getElementById('khachHangId').value = id; // Lưu id khách hàng
    document.getElementById('khachHangInput').value = ten; // Hiển thị tên khách hàng
    closeCustomerForm(); // Đóng form khách hàng
}

function selectProduct(id) {

}

function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
        searchInput.style.display = 'block';
        searchInput.focus();
    } else {
        const searchValue = searchInput.value.trim();
        if (searchValue) {
            window.location.href = `sang_pham.html?search=${encodeURIComponent(searchValue)}`;
        }
        searchInput.value = '';
        searchInput.style.display = 'none';
    }
}

let isOrderCreated = false; // Biến kiểm tra xem hóa đơn đã được tạo hay chưa
let viewDetail = false;




// Hàm cập nhật số lượng trong bảng khi có sự thay đổi
function updateProductQuantityInTable(product) {
    const rows = document.querySelectorAll('#addedProductsTableBody tr');

    rows.forEach(row => {
        const spctId = row.querySelector('input[name="spctIds"]').value;

        if (spctId == product.spctId) {
            const quantityInput = row.querySelector('input[name="soLuong"]');
            quantityInput.value = product.quantity;
        }
    });

    updateTotalAmount(); // Cập nhật lại tổng tiền sau khi thay đổi số lượng
}

// Hàm xử lý thanh toán
// function processPayment() {
//     const total = parseFloat(document.getElementById('totalAmount').textContent);
//     const payment = parseFloat(document.getElementById('customerPayment').value);
//
//     if (payment >= total) {
//         // Simulate backend API call
//         fetch('/user/ban-hang', {
//             method: 'POST',
//
//             headers: {
//
//                 'Content-Type': 'application/json',
//             },
//             body: JSON.stringify({
//                 products: selectedProducts,
//                 totalAmount: total,
//                 payment: payment
//             }),
//         })
//             .then(response => response.json())
//             .then(data => {
//                 alert('Thanh toán thành công!');
//
//                 // Reset the cart and UI
//                 selectedProducts = [];
//                 document.getElementById('customerPayment').value = '';
//                 document.getElementById('changeAmount').textContent = '0';
//                 updateTotalAmount();
//
//                 console.log('Success:', data);
//             })
//             .catch((error) => {
//                 console.error('Error:', error);
//             });
//
//     } else {
//         alert('Số tiền khách đưa không đủ!');
//     }
// }

function showMenu(category) {
    const sections = document.querySelectorAll('.menu-section');
    sections.forEach(section => section.style.display = 'none');
    document.getElementById(category).style.display = 'block';
}

function toggleHistoryForm() {
    const historyForm = document.querySelector('.history-form');
    historyForm.style.display = historyForm.style.display === 'block' ? 'none' : 'block';
}

function toggleCustomerForm() {
    document.querySelector('.customer-form').style.display = 'block';
}

function closeCustomerForm() {
    document.querySelector('.customer-form').style.display = 'none';
}

// function showForms() {
//     isOrderCreated = true;
//     const sanPhamForm = document.querySelector('.form-san-pham');
//     if (!formDisplayed) {
//         sanPhamForm.style.display = 'block';
//         formDisplayed = true;
//     } else {
//         alert('Không thể tạo 2 Bill cùng 1 Thanh Toán');
//     }
// }

function viewOrderDetails(spctId) {
    viewDetail = true;

    if (!spctId || isNaN(spctId)) {
        alert('ID đơn hàng không hợp lệ');
        return;
    }

    fetch(`/user/ban-hang/${spctId}/details`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Không thể tải chi tiết đơn hàng');
            }
            return response.json();
        })
        .then(data => {
            const addedProductsTableBody = document.getElementById('addedProductsTableBody');
            addedProductsTableBody.innerHTML = ''; // Xóa nội dung cũ

            // Tạo mảng sản phẩm cho selectedProducts
            selectedProducts = [];

            // Hiển thị các sản phẩm và tính tổng tiền
            data.forEach(item => {
                const newRow = document.createElement('tr');
                newRow.innerHTML = `

                    <td>${item.productName || 'Chưa có tên sản phẩm'}</td>
                    <td><input type="number" name="soLuong" value="${item.quantity}" min="1" onchange="updateProductQuantity(this)" /></td>
                    <td><input type="number" name="donGia" value="${item.price}" readonly /></td>
                    <td><button name="paymentMethod" value="xoa" onclick="removeProductinDatabase()">Xóa</button></td>
             
                `;
                addedProductsTableBody.appendChild(newRow);

                // Thêm sản phẩm vào mảng selectedProducts
                selectedProducts.push({
                    spctId: item.spctId,
                    productName: item.productName,
                    quantity: item.quantity,
                    price: item.price
                });
            });

            // Gọi hàm cập nhật tổng tiền
            updateTotalAmount();

            // Hiển thị form chi tiết đơn hàng
            document.getElementById('orderDetailsForm').style.display = 'block';

            // Gọi hàm cập nhật tiền trả lại
            calculateChange();
        })
        .catch(error => {
            console.error('Error fetching order details:', error);
            alert('Có lỗi xảy ra khi tải chi tiết đơn hàng. Vui lòng thử lại sau.');
        });
}

function removeProductinDatabase(spctId) {
    if (confirm("Are you sure you want to delete this item?")) {
        fetch(`/user/hoa-hang//${spctId}`, { method: 'POST' })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    console.error('Failed to delete order detail');
                }
            })
            .catch(error => console.error('Error:', error));
    }
}


function deleteProductRow(dhctId, button) {
    if (!dhctId || isNaN(dhctId)) {
        alert('ID chi tiết đơn hàng không hợp lệ');
        return;
    }

    fetch(`/user/ban-hang/${dhctId}`, {
        method: 'DELETE',
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Xóa dòng sản phẩm khỏi bảng
                const row = button.closest('tr');
                row.remove();

                // Cập nhật lại mảng selectedProducts sau khi xóa
                selectedProducts = selectedProducts.filter(product => product.dhctId !== dhctId);

                // Cập nhật tổng tiền sau khi xóa
                updateTotalAmount();
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Xóa sản phẩm thất bại.');
        });
}


// Hiển thị form khách hàng
function openCustomerForm() {
    document.getElementById("customerForm").style.display = "block";
}

// Ẩn form khách hàng
function closeCustomerForm() {
    document.getElementById("customerForm").style.display = "none";
}

// Giả sử có một chức năng tìm kiếm khách hàng (tạm thời là demo)
function searchCustomer() {
    const name = document.getElementById('customerName').value.trim();
    const phone = document.getElementById('customerPhone').value.trim();

    // Kiểm tra xem ít nhất một trong hai ô nhập có giá trị
    if (!name && !phone) {
        alert("Vui lòng nhập tên hoặc số điện thoại để tìm kiếm.");
        return; // Không thực hiện tìm kiếm nếu cả hai ô đều trống
    }

    // Gửi yêu cầu AJAX đến server để tìm kiếm khách hàng
    fetch(`/api/khach-hang/search?name=${encodeURIComponent(name)}&phone=${encodeURIComponent(phone)}`)
        .then(response => response.json())
        .then(data => {
            const customerList = document.getElementById('customer-list');
            customerList.innerHTML = ''; // Xóa danh sách cũ

            // Kiểm tra nếu không có khách hàng nào được tìm thấy
            if (data.length === 0) {
                customerList.innerHTML = '<tr><td colspan="3">Không tìm thấy khách hàng nào.</td></tr>';
                return;
            }

            // Thêm từng khách hàng vào bảng
            data.forEach(kh => {
                const row = document.createElement('tr');
                row.setAttribute('onclick', `selectCustomer('${kh.id}', '${kh.ten}', '${kh.sdt}')`);
                row.innerHTML = `
                    <td>${kh.ten}</td>
                    <td>${kh.sdt}</td>
                    <td>Chọn</td>
                `;
                customerList.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Có lỗi xảy ra khi tìm kiếm khách hàng:', error);
        });
}

function addCustomer(event) {
    event.preventDefault(); // Ngăn form gửi yêu cầu HTTP mặc định

    const name = document.getElementById('customerName').value.trim();
    const phone = document.getElementById('customerPhone').value.trim();

    // Kiểm tra đầu vào
    if (!name || !phone) {
        alert("Vui lòng nhập đầy đủ thông tin tên và SĐT.");
        return;
    }

    // Gửi yêu cầu thêm khách hàng
    fetch('/api/khach-hang/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            ten: name,
            sdt: phone,
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể thêm khách hàng.");
            }
            return response.json();
        })
        .then(newCustomer => {
            alert(`Đã thêm khách hàng: ${newCustomer.ten} (${newCustomer.sdt})`);

            // Tự động thêm khách hàng mới vào danh sách
            const customerList = document.getElementById('customer-list');
            const newRow = document.createElement('tr');
            newRow.onclick = () =>
                selectCustomer(newCustomer.id, newCustomer.ten, newCustomer.sdt);
            newRow.innerHTML = `
                <td>${newCustomer.ten}</td>
                <td>${newCustomer.sdt}</td>
                <td>Chọn</td>
            `;
            customerList.appendChild(newRow);

            // Xóa nội dung trong form
            document.getElementById('customerName').value = '';
            document.getElementById('customerPhone').value = '';
        })
        .catch(error => {
            console.error("Error saving customer:", error);
            alert("Có lỗi xảy ra khi thêm khách hàng.");
        });
}

window.onload = function () {
    // Lấy id từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get('id'); // Lấy id từ query string

    // Gán id vào các input
    if (orderId) {
        document.querySelector('input[name="donHang.id"]').value = orderId;
    }
}; // Gán vào input tương ứng

function updateProductQuantity(inputElement) {
    const row = inputElement.closest('tr');
    const price = parseFloat(row.querySelector('input[name="donGia"]').value) || 0;
    const spctId = row.querySelector('input[name="spctIds"]').value;  // Lấy spctId của sản phẩm
    const newQuantity = parseInt(inputElement.value, 10);  // Lấy số lượng mới từ input

    // Kiểm tra số lượng hợp lệ (lớn hơn 0)
    if (newQuantity <= 0) {
        alert("Số lượng phải lớn hơn 0");
        inputElement.value = 1;  // Đặt lại số lượng về 1 nếu không hợp lệ
        return;
    }

    // Tìm sản phẩm trong mảng selectedProducts
    const product = selectedProducts.find(item => item.spctId === spctId);
    if (product) {
        product.quantity = newQuantity;  // Cập nhật số lượng trong mảng
    }

    // Cập nhật tổng tiền sau khi thay đổi số lượng
    updateTotalAmount();

    // Cập nhật lại số lượng trong bảng (nếu cần thiết)
    row.querySelector('input[name="soLuong"]').value = newQuantity;  // Cập nhật lại bảng
}

// Hàm tính toán số tiền trả lại
function calculateChange() {
    const payment = parseFloat(document.getElementById('customerPayment').value) || 0; // Lấy giá trị tiền khách đưa
    const total = parseFloat(document.getElementById('totalAmount').textContent); // Lấy tổng tiền
    const change = payment - total; // Tính số tiền trả lại

    // Cập nhật số tiền trả lại
    document.getElementById('changeAmount').textContent = change >= 0 ? change.toFixed(2) : '0';
}

// Hàm cập nhật tổng tiền
function updateTotalAmount() {
    const total = selectedProducts.reduce((sum, product) => sum + (product.quantity * product.price), 0);
    document.getElementById('totalAmount').textContent = total.toFixed(2); // Hiển thị tổng với 2 chữ số thập phân
    calculateChange(); // Cập nhật tiền trả lại mỗi khi tổng tiền thay đổi
}

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