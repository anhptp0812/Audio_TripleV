function selectCustomer(id, name, phone) {
    // Set the customer name and ID in the form fields
    document.getElementById("khachHangName").value = name;
    document.getElementById("khachHangId").value = id;

    // Example: assume we are passing a productId and quantity to create HoaDonChiTiet
    const productId = 1;  // Example product ID
    const quantity = 1;   // Example quantity

    closeForm();

    // Make a POST request to create an invoice for the selected customer
    $.post("/user/ban-hang/create", {
        idKhachHang: id,
        tenKhachHang: name,  // Add this line to send 'tenKhachHang'
        productId: productId,
        quantity: quantity
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

function handleSearch() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
        function handleSearch() {
        }
    }
}

function openCustomerForm() {
    const form = document.getElementById('customerForm');
    const overlay = document.getElementById('overlay');

    // Đảm bảo hiển thị từ giữa ngay lập tức
    form.classList.add('show');
    overlay.classList.add('show');
}

function closeForm() {
    const form = document.getElementById('customerForm');
    const overlay = document.getElementById('overlay');

    // Ẩn form
    form.classList.remove('show');
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

function updateTotalAmount(totalAmount) {
    document.getElementById("totalAmount").innerText = totalAmount.toLocaleString() + " đ";
}

document.addEventListener("click", function (event) {
    if (event.target.classList.contains("add-product-btn")) {
        const button = event.target;
        console.log("Button data attributes:", {
            id: button.getAttribute("data-id"),
            price: button.getAttribute("data-price"),
            quantity: button.getAttribute("data-quantity"),
            name: button.getAttribute("data-name"),
        });

        const productId = button.getAttribute("data-id");
        const price = parseFloat(button.getAttribute("data-price")) || 0;
        const quantity = parseInt(button.getAttribute("data-quantity")) || 1;
        const productName = button.getAttribute("data-name");

        if (!productId || isNaN(productId)) {
            alert("Không tìm thấy ID sản phẩm hợp lệ. Vui lòng kiểm tra lại.");
            return;
        }

        addProductToForm(productId, price, quantity, productName);
    }
});

function addProductToForm(productId, price, quantity, productName) {
    if (isNaN(productId) || isNaN(price) || isNaN(quantity)) {
        alert("Dữ liệu không hợp lệ. Vui lòng kiểm tra lại!");
        return;
    }

    const hoaDonId = document.getElementById("hoaDonId").value;
    if (!hoaDonId) {
        alert("Chưa tạo hóa đơn. Vui lòng tạo hóa đơn trước khi thêm sản phẩm!");
        return;
    }

    $.post(`/user/ban-hang/${hoaDonId}/add-product`, {
        spctId: productId,
        quantity: quantity
    })
        .done(function (response) {
            const tableBody = document.getElementById("addedProductsTableBody");
            const existingRow = tableBody.querySelector(`tr[data-id="${response.id}"]`);

            if (response.isUpdated && existingRow) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng và giá
                const quantityCell = existingRow.querySelector("td:nth-child(2)");
                const priceCell = existingRow.querySelector("td:nth-child(3)");

                // Cập nhật số lượng và tổng giá
                quantityCell.textContent = response.quantity;
                priceCell.textContent = (response.price * response.quantity).toLocaleString() + " đ";
            } else {
                // Nếu sản phẩm chưa tồn tại, thêm hàng mới
                const newRow = `
        <tr data-id="${response.id}">
            <td>${response.productName}</td>
            <td>${response.quantity}</td>
            <td>${(response.price * response.quantity).toLocaleString()} đ</td>
            <td>
                <button class="btn btn-danger" onclick="removeProduct(${response.id})">Xóa</button>
            </td>
        </tr>
        `;
                tableBody.insertAdjacentHTML("beforeend", newRow);
            }

            // Cập nhật tổng tiền hóa đơn
            updateTotalAmount(response.totalAmount);
        })
        .fail(function (error) {
            alert("Lỗi khi thêm sản phẩm vào hóa đơn: " + (error.responseJSON?.message || error.responseText));
        });
}

function loadProducts() {
    const hoaDonId = document.getElementById("hoaDonId").value;
    if (!hoaDonId) {
        console.error("Không tìm thấy hóa đơn ID.");
        return;
    }

    $.get(`/user/ban-hang/${hoaDonId}/products`)
        .done(function (response) {
            const tableBody = document.getElementById("addedProductsTableBody");
            tableBody.innerHTML = ""; // Xóa dữ liệu cũ

            response.forEach(product => {
                const newRow = `
                <tr data-id="${product.id}">
                    <td>${product.productName}</td>
                    <td>${product.quantity}</td>
                    <td>${product.totalPrice.toLocaleString()} đ</td>
                    <td>
                        <button class="btn btn-danger" onclick="removeProduct(${product.id})">Xóa</button>
                    </td>
                </tr>
                `;
                tableBody.insertAdjacentHTML("beforeend", newRow);
            });

            // Cập nhật tổng tiền hóa đơn
            const totalAmount = response.reduce((sum, product) => sum + product.totalPrice, 0);
            updateTotalAmount(totalAmount);
        })
        .fail(function (error) {
            console.error("Không thể tải danh sách sản phẩm:", error);
        });
}

document.addEventListener("DOMContentLoaded", function () {
    loadProducts();
});

function removeProduct(productId) {
    const hoaDonId = document.getElementById("hoaDonId").value;

    if (!hoaDonId) {
        alert("Không tìm thấy hóa đơn.");
        return;
    }

    if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
        return;
    }

    $.ajax({
        url: `/user/ban-hang/${hoaDonId}/remove-product/${productId}`,
        type: "DELETE"
    })
        .done(function (response) {
            // Xóa hàng trong bảng
            const row = document.querySelector(`tr[data-id="${productId}"]`);
            if (row) {
                row.remove();
            }

            // Cập nhật tổng tiền
            updateTotalAmount(response.totalAmount);
            alert(response.message);
        })
        .fail(function (error) {
            alert("Lỗi khi xóa sản phẩm: " + (error.responseJSON?.message || error.responseText));
        });
}

document.addEventListener("DOMContentLoaded", function () {
    // Kiểm tra xem URL có chứa "/user/ban-hang/{hoaDonId}" không
    const currentPath = window.location.pathname;
    const match = currentPath.match(/\/user\/ban-hang\/(\d+)/);

    if (match && match[1]) {
        // Lấy ID hóa đơn từ URL
        const hoaDonId = match[1];
        const hoaDonIdElement = document.getElementById("hoaDonId");

        // Cập nhật giá trị của input #hoaDonId
        if (hoaDonIdElement) {
            hoaDonIdElement.value = hoaDonId;
        }

        // Gọi hàm loadProducts để tải sản phẩm
        loadProducts();
    }
});