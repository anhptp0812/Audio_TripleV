// let formDisplayed = false;
// let selectedProducts = []; // Mảng lưu trữ sản phẩm đã chọn
//
// // document.addEventListener('DOMContentLoaded', function() {
// //     const addProductButtons = document.querySelectorAll('td button');
// //     addProductButtons.forEach(button => {
// //         button.addEventListener('click', function() {
// //             const row = this.closest('tr');
// //             const productName = row.querySelector('td:nth-child(1)').textContent;
// //             const quantity = parseFloat(row.querySelector('td:nth-child(7)').textContent) || 0; // Chuyển đổi số lượng sang số
// //             const price = parseFloat(row.querySelector('td:nth-child(6)').textContent) || 0; // Chuyển đổi giá sang số
// //
// //             addProductToForm(productName, quantity, price); // Thêm sản phẩm vào giỏ hàng
// //         });
// //     });
// // });
//
// function handleSearch() {
//     const searchInput = document.getElementById('searchInput');
//     if (searchInput.style.display === 'none' || searchInput.style.display === '') {
//         searchInput.style.display = 'block';
//         searchInput.focus();
//     } else {
//         const searchValue = searchInput.value.trim();
//         if (searchValue) {
//             window.location.href = `sang_pham.html?search=${encodeURIComponent(searchValue)}`;
//         }
//         searchInput.value = '';
//         searchInput.style.display = 'none';
//     }
// }
//
// function addProductToForm(productName, availableStock, price) {
//     let quantity = 1; // Default quantity is 1
//
//     // Create a product object
//     const product = {
//         name: productName,
//         quantity: quantity,
//         availableStock: availableStock, // Store available stock
//         price: price,
//         total: price // Initially, total = price * quantity (which is 1)
//     };
//
//     // Add the product to the list
//     selectedProducts.push(product);
//
//     // Update the product table and total amount
//     updateProductTable();
//     updateTotalAmount();
// }
//
// function updateTotalAmount() {
//     const total = selectedProducts.reduce((sum, product) => sum + product.total, 0);
//     document.getElementById('totalAmount').textContent = total;
//     calculateChange(); // Cập nhật tiền trả lại
// }
//
// // Hàm tính toán số tiền trả lại
// function calculateChange() {
//     const payment = parseFloat(document.getElementById('customerPayment').value) || 0; // Lấy giá trị tiền khách đưa
//     const total = parseFloat(document.getElementById('totalAmount').textContent); // Lấy tổng tiền
//     const change = payment - total; // Tính số tiền trả lại
//
//     // Cập nhật số tiền trả lại
//     document.getElementById('changeAmount').textContent = change >= 0 ? change : 0;
// }
//
// // Hàm xử lý thanh toán
// function processPayment() {
//     const total = parseFloat(document.getElementById('totalAmount').textContent);
//     const payment = parseFloat(document.getElementById('customerPayment').value);
//
//     if (payment >= total) {
//         // Simulate backend API call
//         fetch('/api/process-payment', {
//             method: 'POST',
//             headers: {
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
//
// // function showMenu(category) {
// //     const sections = document.querySelectorAll('.menu-section');
// //     sections.forEach(section => section.style.display = 'none');
// //     document.getElementById(category).style.display = 'block';
// // }
// //
// // function toggleHistoryForm() {
// //     const historyForm = document.querySelector('.history-form');
// //     historyForm.style.display = historyForm.style.display === 'block' ? 'none' : 'block';
// // }
// //
// // function toggleCustomerForm() {
// //     document.querySelector('.customer-form').style.display = 'block';
// // }
// //
// // function closeCustomerForm() {
// //     document.querySelector('.customer-form').style.display = 'none';
// // }
//
// // function showForms() {
// //     const sanPhamForm = document.querySelector('.form-san-pham');
// //     if (!formDisplayed) {
// //         sanPhamForm.style.display = 'block';
// //         formDisplayed = true;
// //     } else {
// //         alert('Không thể tạo 2 Bill cùng 1 Thanh Toán');
// //     }
// // }
//
// // function updateProductTable() {
// //     // Lấy thân bảng
// //     const tableBody = document.getElementById('selectedProductsBody');
// //
// //     // Xóa các hàng hiện có
// //     tableBody.innerHTML = '';
// //
// //     // Lặp qua các sản phẩm đã chọn và thêm chúng vào bảng
// //     selectedProducts.forEach((product, index) => {
// //         const row = document.createElement('tr');
// //
// //         row.innerHTML = `
// //                 <td>${product.name}</td>
// //                 <td><input type="number" value="${product.quantity}" onchange="updateQuantity(${index}, this.value)" /></td>
// //                 <td>${product.price}</td>
// //                 <td>${product.total}</td>
// //                 <td><button onclick="removeProduct(${index})">Xóa</button></td>
// //             `;
// //
// //         tableBody.appendChild(row);
// //     });
// // }
//
// // function updateQuantity(index, newQuantity) {
// //     newQuantity = parseInt(newQuantity);
// //
// //     // Ensure quantity is within valid bounds
// //     if (newQuantity > selectedProducts[index].availableStock) {
// //         alert("Số lượng vượt quá số lượng trong kho!");
// //         newQuantity = selectedProducts[index].availableStock;
// //     } else if (newQuantity < 1) {
// //         alert("Số lượng phải lớn hơn 0");
// //         newQuantity = 1;
// //     }
// //
// //     // Update product quantity and total price
// //     selectedProducts[index].quantity = newQuantity;
// //     selectedProducts[index].total = newQuantity * selectedProducts[index].price;
// //
// //     // Update the product table and total amount
// //     updateProductTable();
// //     updateTotalAmount();
// // }
// //
// // function removeProduct(index) {
// //     // Xóa sản phẩm khỏi danh sách
// //     selectedProducts.splice(index, 1);
// //
// //     // Cập nhật bảng để phản ánh sự thay đổi
// //     updateProductTable();
// //     updateTotalAmount(); // Cập nhật tổng tiền
// // }
// //
// // function submitAllProducts() {
// //     selectedProducts.forEach(product => {
// //         // Decrease the stock for each product
// //         product.availableStock -= product.quantity;
// //
// //         if (product.availableStock < 0) {
// //             alert(`Không đủ số lượng cho sản phẩm: ${product.name}`);
// //             return;
// //         }
// //     });
// //
// //     console.log("Confirmed products:", selectedProducts);
// //     alert('Sản phẩm đã được xác nhận');
// //
// //     // You can make an API call to update the backend database here
// //     // For example, send selectedProducts to your backend for processing
// // }