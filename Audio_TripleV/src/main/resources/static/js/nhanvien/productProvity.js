function selectCustomer(id, name, phone) {
    // Set the customer name and ID in the form fields
    document.getElementById("khachHangName").value = name;
    document.getElementById("khachHangId").value = id;

    closeForm();

    // Make a POST request to create an invoice for the selected customer
    $.post("/user/ban-hang/create", {
        idKhachHang: id,
        tenKhachHang: name,  // Add this line to send 'tenKhachHang'
    })
        .done(function (response) {
            // Assuming the server returns the ID of the created invoice
            const invoiceId = response.id;

            // Redirect to the invoice page
            window.location.href = "/user/ban-hang/" + invoiceId;
        })
        .fail(function (error) {
            alert("Lỗi khi tạo hóa đơn: " + error.responseText);
        });
}

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

function openCustomerForm() {
    const form = document.getElementById('customerForm');
    const overlay = document.getElementById('overlay');

    // Đảm bảo hiển thị từ giữa ngay lập tức
    form.classList.add('show');
    overlay.classList.add('show');
}

function openVoucherForm() {
    const form = document.getElementById('voucherForm');
    const overlay = document.getElementById('overlay');

    // Đảm bảo hiển thị từ giữa ngay lập tức
    form.classList.add('show');
    overlay.classList.add('show');
}

function closeForm() {
    const form = document.getElementById('customerForm');
    const form1 = document.getElementById('voucherForm');
    const overlay = document.getElementById('overlay');

    // Ẩn form
    form.classList.remove('show');
    form1.classList.remove('show');
    overlay.classList.remove('show');
}

function searchCustomer() {
    const name = document.getElementById("customerName").value.trim();
    const phone = document.getElementById("customerPhone").value.trim();

    fetch(`/user/khach-hang/tim-kiem?ten=${encodeURIComponent(name)}&sdt=${encodeURIComponent(phone)}`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("customer-list");
            tableBody.innerHTML = ""; // Xóa danh sách cũ
            if (data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="3" class="text-center">Không tìm thấy khách hàng</td></tr>`;
            } else {
                data.forEach(khachHang => {
                    const row = `<tr>
                    <td>${khachHang.ten}</td>
                    <td>${khachHang.sdt}</td>
                    <td>
                        <button class="btn btn-success" onclick="selectCustomer(${khachHang.id}, '${khachHang.ten}', '${khachHang.sdt}')">Chọn</button>
                    </td>
                </tr>`;
                    tableBody.insertAdjacentHTML("beforeend", row);
                });
            }
        })
        .catch(error => console.error("Error fetching customers:", error));
}

function addCustomer() {
    // Kiểm tra dữ liệu trước
    if (!validateCustomerForm()) return;

    const nameInput = document.getElementById("customerName");
    const phoneInput = document.getElementById("customerPhone");
    const name = nameInput.value.trim();
    const phone = phoneInput.value.trim();

    // Xác nhận hành động thêm khách hàng
    const confirmAdd = confirm(`Bạn có chắc chắn muốn thêm khách hàng với thông tin sau?\nTên: ${name}\nSĐT: ${phone}`);
    if (!confirmAdd) {
        return; // Nếu người dùng không xác nhận, dừng lại
    }

    fetch("/user/khach-hang/them", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `tenKhachHang=${encodeURIComponent(name)}&soDienThoai=${encodeURIComponent(phone)}`
    })
        .then(response => {
            if (response.ok) {
                alert("Thêm khách hàng thành công!");
                searchCustomer();
                nameInput.value = "";
                phoneInput.value = "";
            } else {
                response.text().then(text => alert(text)); // Hiển thị thông báo lỗi từ server
            }
        })
        .catch(error => {
            console.error("Lỗi khi thêm khách hàng:", error);
            alert("Đã xảy ra lỗi. Vui lòng thử lại sau!");
        });
}

function validateCustomerForm() {
    const nameInput = document.getElementById("customerName");
    const phoneInput = document.getElementById("customerPhone");
    const nameError = document.getElementById("nameError");
    const phoneError = document.getElementById("phoneError");

    let isValid = true;

    // Kiểm tra tên khách hàng
    if (!nameInput.value.trim()) {
        nameError.textContent = "Tên khách hàng không được để trống.";
        isValid = false;
    } else {
        nameError.textContent = "";
    }

    // Kiểm tra số điện thoại
    const phoneRegex = /^[0-9]{10,11}$/; // Kiểm tra số điện thoại với 10-11 chữ số
    if (!phoneInput.value.trim()) {
        phoneError.textContent = "Số điện thoại không được để trống.";
        isValid = false;
    } else if (!phoneRegex.test(phoneInput.value.trim())) {
        phoneError.textContent = "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng.";
        isValid = false;
    } else {
        phoneError.textContent = "";
    }

    return isValid;
}

const hoaDonId = document.getElementById('hoaDonId') ? document.getElementById('hoaDonId').value : null;

// Xử lý thêm sản phẩm vào hóa đơn
function addProductToOrder(productId, quantity) {
    if (!hoaDonId) {
        alert('Vui lòng tạo hóa đơn trước!');
        return;
    }

    fetch(`/user/ban-hang/${hoaDonId}/add-product`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `spctId=${productId}&soLuong=${quantity}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                // Nếu sản phẩm đã có trong hóa đơn, hiển thị thông báo và không thêm vào bảng
                if (data.message === "Sản phẩm đã có trong giỏ hàng!") {
                    alert(data.message);
                } else {
                    updateProductQuantityInTable(productId, data.remainingQuantity); // Cập nhật số lượng trong kho
                    addProductToOrderTable(data); // Cập nhật bảng chi tiết hóa đơn
                    updateTotalAmount(data.totalAmount); // Cập nhật tổng giá
                }
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Có lỗi xảy ra khi thêm sản phẩm');
        });
}


// Thêm sản phẩm vào bảng chi tiết hóa đơn
function addProductToOrderTable(productData) {
    const tableBody = document.getElementById('addedProductsTableBody');
    let existingRow = document.querySelector(`tr[data-product-id="${productData.id}"]`);

    if (existingRow) {
        // Cập nhật số lượng và tổng giá nếu sản phẩm đã tồn tại
        const quantityInput = existingRow.querySelector('.quantity-input');
        const totalCell = existingRow.querySelector('.total-price');
        quantityInput.value = productData.quantity;
        totalCell.textContent = formatCurrency(productData.quantity * productData.price);
    } else {
        // Thêm sản phẩm mới vào bảng
        const newRow = document.createElement('tr');
        newRow.setAttribute('data-product-id', productData.id);

        newRow.innerHTML = `
            <td>${productData.productName}</td>
            <td>
                <input type="number" 
                       class="form-control quantity-input" 
                       value="${productData.quantity}" 
                       min="1" 
                       data-product-id="${productData.id}"
                       onchange="updateProductQuantity(this)"
                />
            </td>
            <td>${formatCurrency(productData.price)}</td>
            <td class="total-price">${formatCurrency(productData.quantity * productData.price)}</td>
            <td>
                <button class="btn btn-danger btn-sm" 
                        onclick="removeProductFromOrder(${productData.id})">
                    Xóa
                </button>
            </td>
        `;

        tableBody.appendChild(newRow);
    }
}

function updateProductQuantityInTable(productId, newQuantity) {
    const stockCell = document.getElementById(`stock-${productId}`);
    if (stockCell) {
        stockCell.textContent = newQuantity;
    }
}

// Xóa sản phẩm khỏi hóa đơn
function removeProductFromOrder(productId) {
    fetch(`/user/ban-hang/${hoaDonId}/remove-product/${productId}`, {
        method: 'DELETE'
    })
        .then(response => response.json())
        .then(data => {
            // Xóa dòng sản phẩm khỏi bảng
            const rowToRemove = document.querySelector(`input[data-product-id="${productId}"]`).closest('tr');
            if (rowToRemove) {
                rowToRemove.remove();
            }

            // Cập nhật số lượng sản phẩm trong kho
            updateProductQuantityInTable(productId, data.remainingQuantity);

            // Cập nhật tổng giá
            updateTotalAmount(data.totalAmount);
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Có lỗi xảy ra khi xóa sản phẩm');
        });
}

// Cập nhật số lượng sản phẩm
function updateProductQuantity(inputElement) {
    const productId = inputElement.dataset.productId;
    const newQuantity = parseInt(inputElement.value);

    // Kiểm tra số lượng hợp lệ
    if (isNaN(newQuantity) || newQuantity <= 0) {
        alert('Số lượng không hợp lệ');
        return;
    }

    fetch('/user/cap-nhat-so-luong', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            productId: productId,
            quantity: newQuantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Cập nhật lại dòng sản phẩm
                const row = inputElement.closest('tr');
                const priceCell = row.querySelector('td:nth-child(3)');
                const totalPriceCell = row.querySelector('td:nth-child(4)');

                const price = parseFloat(priceCell.textContent.replace(/[^0-9.-]+/g, ""));
                const totalPrice = price * newQuantity;

                totalPriceCell.textContent = formatCurrency(totalPrice);

                // Tải lại tổng giá
                loadOrderDetails();
            } else {
                alert(data.message || 'Không thể cập nhật số lượng');
                // Khôi phục số lượng ban đầu
                inputElement.value = inputElement.defaultValue;
            }
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert('Có lỗi xảy ra khi cập nhật số lượng');
        });
}


function loadOrderDetails() {
    if (!hoaDonId) return;

    fetch(`/user/ban-hang/${hoaDonId}/products`)
        .then(response => response.json())
        .then(products => {
            const tableBody = document.getElementById('addedProductsTableBody');
            tableBody.innerHTML = ''; // Xóa các dòng cũ

            let totalAmount = 0;
            products.forEach(product => {
                const newRow = document.createElement('tr');
                newRow.innerHTML = `
                    <td>${product.productName}</td>
                    <td>
                        <input type="number" 
                               class="form-control quantity-input" 
                               value="${product.quantity}" 
                               min="1" 
                               data-product-id="${product.id}"
                               onchange="updateProductQuantity(this)"
                        />
                    </td>
                    <td>${formatCurrency(product.price)}</td>
                    <td>${formatCurrency(product.totalPrice)}</td>
                    <td>
                        <button class="btn btn-danger btn-sm" 
                                onclick="removeProductFromOrder(${product.id})">
                            Xóa
                        </button>
                    </td>
                `;

                tableBody.appendChild(newRow);
                totalAmount += product.totalPrice;
            });

            // Cập nhật tổng giá
            updateTotalAmount(totalAmount);
        })
        .catch(error => {
            console.error('Lỗi:', error);
        });
}

// Xử lý các nút thêm sản phẩm
const addProductButtons = document.querySelectorAll('.add-product-btn');
addProductButtons.forEach(button => {
    button.addEventListener('click', function () {
        const productId = this.getAttribute('data-id');
        const maxQuantity = parseInt(this.getAttribute('data-max-quantity'));
        const productName = this.getAttribute('data-name');

        if (!hoaDonId) {
            alert('Vui lòng tạo hóa đơn trước!');
            return;
        } else {

            // Hiển thị modal nhập số lượng
            const quantity = prompt(`Nhập số lượng cho sản phẩm ${productName} (Tối đa: ${maxQuantity}):`, '1');

            if (quantity !== null) {
                const parsedQuantity = parseInt(quantity);

                if (isNaN(parsedQuantity) || parsedQuantity <= 0) {
                    alert('Số lượng không hợp lệ');
                    return;
                }

                if (parsedQuantity > maxQuantity) {
                    alert(`Số lượng vượt quá số lượng tồn kho (${maxQuantity})`);
                    return;
                }

                addProductToOrder(productId, parsedQuantity);
            }
        }
    });
});

// Cập nhật tổng giá
function updateTotalAmount(amount) {
    const totalAmountElement = document.getElementById('totalAmount');
    if (totalAmountElement) {
        totalAmountElement.textContent = formatCurrency(amount);
    }

    // Cập nhật input tổng giá
    const tongGiaInput = document.getElementById('tongGia');
    if (tongGiaInput) {
        // Loại bỏ ký tự "₫" khi gán giá trị vào input
        tongGiaInput.value = amount.toLocaleString('vi-VN');  // Định dạng số theo kiểu Việt Nam
    }

    validatePaymentAmount();
}

// Hàm validatePaymentAmount để kiểm tra số tiền khách đưa và tính số tiền trả lại
function validatePaymentAmount() {
    // Lấy giá trị từ giao diện
    const customerPaymentInput = document.getElementById("customerPayment");
    const totalAmountElement = document.getElementById("totalAmount");
    const changeAmountElement = document.getElementById("changeAmount");
    const paymentErrorElement = document.getElementById("paymentError");
    const paymentButton = document.getElementById("paymentButton");
    const paymentVnPayButton = document.getElementById("paymentVnPayButton");

    // Xử lý giá trị tiền khách đưa và tổng tiền
    const customerPayment = parseFloat(customerPaymentInput.value.replace(/\D/g, "")) || 0;
    const totalAmount = parseFloat(totalAmountElement.textContent.replace(/\D/g, "")) || 0;

    // Kiểm tra giá trị hợp lệ
    if (isNaN(totalAmount) || totalAmount <= 0) {
        paymentErrorElement.style.display = "block";
        paymentErrorElement.textContent = "Vui lòng nhập số tiền hợp lệ!";
        paymentButton.disabled = true;
        paymentVnPayButton.disabled = true;
        changeAmountElement.textContent = formatCurrency(0);
    } else if (customerPayment < totalAmount) {
        paymentErrorElement.style.display = "block";
        paymentErrorElement.textContent = "Số tiền khách đưa không đủ!";
        paymentButton.disabled = true;
        paymentVnPayButton.disabled = true;
        changeAmountElement.textContent = formatCurrency(0);
    } else {
        const changeAmount = (customerPayment - totalAmount).toFixed(2);
        changeAmountElement.textContent = formatCurrency(parseFloat(changeAmount));
        paymentErrorElement.style.display = "none";
        paymentButton.disabled = false;
        paymentVnPayButton.disabled = false;
    }
}

// Hàm formatCurrency để hiển thị số tiền theo định dạng Việt Nam
function formatCurrency(amount) {
    return amount.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
}

function confirmPayment(event) {
    event.preventDefault(); // Ngừng hành động submit mặc định

    // Lấy giá trị tiền khách đưa từ giao diện
    const tienKhachDua = document.getElementById("customerPayment").value;
    const hoaDonId = document.getElementById("hoaDonId").value; // ID hóa đơn từ giao diện

    if (confirm("Bạn có muốn thanh toán không?")) {
        // Gửi yêu cầu thanh toán
        fetch(`/user/ban-hang/${hoaDonId}/thanh-toan?customerPayment=${tienKhachDua}`, {
            method: 'POST'
        })
            .then(response => response.json()) // Chuyển đổi response thành JSON
            .then(data => {
                if (data.message) {
                    alert(data.message);  // Hiển thị thông báo thành công

                    // Cập nhật giao diện với số tiền thừa
                    document.getElementById("changeAmount").textContent = data.changeAmount;

                    // Sau khi thanh toán thành công, yêu cầu in hóa đơn
                    if (confirm("Bạn có muốn in hóa đơn không?")) {
                        // Chuyển hướng đến in hóa đơn
                        window.location.href = "/user/ban-hang/in-hoa-don/" + hoaDonId;

                        // Sau khi tải xong file, quay lại trang bán hàng
                        setTimeout(function() {
                            window.location.href = "/user/ban-hang";
                        }, 2000); // Sau 2 giây (hoặc tùy theo thời gian tải)
                    } else {
                        // Nếu không in hóa đơn, quay lại trang bán hàng
                        window.location.href = "/user/ban-hang";
                    }
                } else {
                    alert("Thanh toán không thành công.");
                }
            })
            .catch(error => {
                alert("Có lỗi xảy ra khi thanh toán.");
            });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    loadOrderDetails();
    searchCustomer();
});