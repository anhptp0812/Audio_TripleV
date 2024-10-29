const express = require('express');
const sql = require('mssql');

const app = express();

const config = {
    user: 'sa',
    password: '123456',
    server: '8080', // địa chỉ máy chủ SQL Server
    database: 'Audio_tripleV',
    options: {
        encrypt: true, // sử dụng mã hóa nếu cần
        trustServerCertificate: true // chỉ dùng trong môi trường phát triển
    }
};

app.get('/admin/nhan-vien', async (req, res) => {
    try {
        await sql.connect(config);
        const result = await sql.query('SELECT * FROM nhanvien');
        res.json(result.recordset);
    } catch (err) {
        console.error('SQL error', err);
        res.status(500).send(err);
    }
});

app.listen(8080, () => {
    console.log('Server is running on port 8080');
});
