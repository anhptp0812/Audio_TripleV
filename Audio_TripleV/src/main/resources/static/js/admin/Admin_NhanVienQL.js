function addRow() {
    // Add row functionality
    alert('Thêm chức năng chưa được triển khai.');
    logHistory('Thêm', 'Thêm nhân viên mới');
}

function deleteRow() {
    // Delete row functionality
    alert('Xóa chức năng chưa được triển khai.');
    logHistory('Xóa', 'Xóa nhân viên');
}

function editRow() {
    // Edit row functionality
    alert('Sửa chức năng chưa được triển khai.');
    logHistory('Sửa', 'Sửa thông tin nhân viên');
}

function logHistory(action, details) {
    const historyTable = document.getElementById('historyTable').getElementsByTagName('tbody')[0];
    const newRow = historyTable.insertRow();
    const idCell = newRow.insertCell(0);
    const actionCell = newRow.insertCell(1);
    const detailsCell = newRow.insertCell(2);
    const dateCell = newRow.insertCell(3);

    idCell.innerHTML = historyTable.rows.length + 1;
    actionCell.innerHTML = action;
    detailsCell.innerHTML = details;
    dateCell.innerHTML = new Date().toLocaleString();
}