﻿create database Audio_tripleV_final_V1
go
use Audio_tripleV_final_V1
go

CREATE TABLE SanPham (

                         ID INT PRIMARY KEY identity,
                         TenSP NVARCHAR(255),
                         NgayTao datetime,
                         NgayCapNhat datetime,


);


CREATE TABLE LoaiSanPham (

                             ID INT PRIMARY KEY identity,
                             KieuDang NVARCHAR(255),
                             NgayTao datetime,
                             NgayCapNhat datetime

);
CREATE TABLE MauSac (

                        ID INT PRIMARY KEY identity,
                        ten NVARCHAR(255),
                        NgayTao datetime,
                        NgayCapNhat datetime

);
CREATE TABLE HinhAnh  (

                          ID INT PRIMARY KEY identity,
                          TenUrl NVARCHAR(255),
                          NgayTao datetime,
                          NgayCapNhat datetime

);
CREATE TABLE Hang  (

                       ID INT PRIMARY KEY identity,
                       Ten NVARCHAR(255),
                       NgayTao datetime,
                       NgayCapNhat datetime

);

CREATE TABLE SanPhamChiTiet  (

                                 ID INT PRIMARY KEY identity,
                                 ID_San_Pham INT ,
                                 ID_MauSac INT ,
                                 ID_Hang INT ,
                                 ID_HinhAnh INT ,
                                 ID_LoaiSanPham  INT ,
                                 DonGia decimal(18, 2),
                                 soLuong int,
                                 ThoiGianBaoHanh INT,
                                 TrangThai NVARCHAR(255),
                                 MoTa NVARCHAR(max),
                                 NgayTao datetime,
                                 NgayCapNhat datetime
                                     foreign key(ID_San_Pham)
                                         References SanPham (ID) ,
                                 foreign key(ID_MauSac)
                                     References MauSac (ID) ,
                                 foreign key(ID_Hang)
                                     References Hang (ID) ,
                                 foreign key(ID_HinhAnh)
                                     References HinhAnh (ID) ,
                                 foreign key(ID_LoaiSanPham)
                                     References LoaiSanPham (ID)

);
CREATE TABLE KhachHang (

                           ID INT PRIMARY KEY identity,
                           Ten NVARCHAR(255),
                           sdt NVARCHAR(11),
                           DiaChi NVARCHAR(255),
                           Email NVARCHAR(255),
                           TaiKhoan NVARCHAR(255),
                           MatKhau NVARCHAR(255),
                           role NVARCHAR(255),
                           isRegitered bit ,
                           NgayDangKy datetime,

);


CREATE TABLE NhanVien (

                          ID INT PRIMARY KEY identity,
                          Ten NVARCHAR(255),
                          NgaySinh DateTime,
                          GioiTinh NVARCHAR(15),
                          DiaChi NVARCHAR(255),
                          Email NVARCHAR(255),
                          SoDienThoai NVARCHAR(15),
                          role NVARCHAR(255),
                          username NVARCHAR(255),
                          password NVARCHAR(255)

);
CREATE TABLE LichSuHoatDong (

                                ID INT PRIMARY KEY identity,
                                ID_NhanVien INT,
                                ThoiGian date,
                                MoTa NVARCHAR(255),
                                TrangThai NVARCHAR(255),
                                NgayTao datetime,
                                NgayCapNhat datetime,
                                NguoiTao NVARCHAR(255),
                                NguoiCapNhat NVARCHAR(255),
                                foreign key(ID_NhanVien)
                                    References NhanVien (ID) ,

);

CREATE TABLE GioHang (

                         ID INT PRIMARY KEY identity,
                         ID_Khach_Hang INT,
                         TongGia decimal(18, 2),
                         NgayThem datetime,
                         TrangThai NVARCHAR(255),
                         foreign key(ID_Khach_Hang)
                             References KhachHang (ID) ,

);

CREATE TABLE GioHangChiTiet (

                                ID INT PRIMARY KEY identity,
                                ID_GioHang INT,
                                ID_SPCT INT ,
                                SoLuong INT,
                                foreign key(ID_GioHang)
                                    References GioHang (ID) ,
                                foreign key(ID_SPCT)
                                    References SanPhamChiTiet (ID) ,

);
CREATE TABLE Voucher (
                         Id INT PRIMARY KEY IDENTITY,
                         Ten NVARCHAR(255) NOT NULL,
                         Loai NVARCHAR(50) NOT NULL, -- 'GiamTien' hoặc 'GiamPhanTram'
                         GiaTriTien DECIMAL(18, 2),
                         GiaTriPhanTram DECIMAL(5, 2),
                         GiaTriHoaDonToiThieu FLOAT(53), -- (Tùy chọn) Giá trị hóa đơn tối thiểu
                         TrangThai NVARCHAR(50),
                         NgayBatDau DATE NOT NULL,
                         NgayKetThuc DATE NOT NULL,
                         NgayTao DATETIME,
                         NgayCapNhat DATETIME
);

CREATE TABLE DonHang (

                         ID INT PRIMARY KEY identity,
                         ID_Khach_Hang INT ,
                         ID_Voucher INT,
                         TongGia decimal(18, 2),
                         TrangThai NVARCHAR(255),
                         TrangThaiPayment NVARCHAR(255),
                         NgayGiao datetime,
                         NgayTao datetime,
                         NgayCapNhat datetime,
                         foreign key(ID_Khach_Hang)
                             References KhachHang (ID) ,
                         foreign key(ID_Voucher)
                             References Voucher(ID)
);
CREATE TABLE DonHangChiTiet (

                                ID INT PRIMARY KEY identity,
                                ID_Don_Hang INT ,
                                ID_SPCT INT,
                                SoLuong INT,
                                DonGia decimal(18, 2),
                                ThoiGianKetThucBH datetime,
                                NgayTao datetime,
                                NgayCapNhat datetime,
                                foreign key(ID_Don_Hang)
                                    References DonHang (ID),
                                foreign key(ID_SPCT)
                                    References SanPhamChiTiet (ID)

);

CREATE TABLE HoaDon (

                        ID INT PRIMARY KEY identity,
                        ID_Nhan_Vien INT,
                        ID_Khach_Hang INT ,
                        ID_Voucher INT,
                        TongGia decimal(18, 2),
                        SoTienPhaiTra decimal(18, 2),
                        TienKhachDua decimal(18, 2),
                        TienThua decimal(18, 2),
                        TrangThai NVARCHAR(255),
                        NgayTao datetime,
                        NgayCapNhat datetime,
                        foreign key(ID_Nhan_Vien)
                            References NhanVien (ID) ,
                        foreign key(ID_Khach_Hang)
                            References KhachHang (ID) ,
                        foreign key(ID_Voucher)
                            References Voucher(ID)

);
CREATE TABLE HoaDonChiTiet (

                               ID INT PRIMARY KEY identity,
                               ID_Hoa_Don INT ,
                               ID_Spct INT,
                               DonGia decimal(18, 2),
                               SoLuong INT,
                               ThoiGianKetThucBH datetime,
                               NgayTao datetime,
                               NgayCapNhat datetime,
                               foreign key(ID_Hoa_Don)
                                   References HoaDon (ID),
                               foreign key(ID_Spct)
                                   References SanPhamChiTiet(ID),
);


CREATE TABLE articles (
                          id INT IDENTITY(1,1) PRIMARY KEY,  -- Mã bài viết
                          title NVARCHAR(255) NOT NULL,      -- Tiêu đề bài viết
                          imageUrl NVARCHAR(255),            -- Thêm trường URL ảnh
                          content NVARCHAR(MAX),             -- Nội dung bài viết
                          created_at DATETIME DEFAULT GETDATE(),  -- Ngày giờ tạo bài viết
                          updated_at DATETIME DEFAULT GETDATE()  -- Ngày giờ cập nhật
);


CREATE TABLE comments (
                          id INT IDENTITY(1,1) PRIMARY KEY,  -- Mã bình luận
                          article_id INT NOT NULL,           -- Mã bài viết liên kết với bình luận
                          ID_Khach_Hang INT NULL,                 -- Mã khách hàng (tác giả bình luận)
                          content NVARCHAR(MAX),             -- Nội dung bình luận
                          created_at DATETIME DEFAULT GETDATE(),  -- Ngày giờ tạo bình luận
                          FOREIGN KEY (article_id) REFERENCES articles(id),  -- Liên kết với bảng bài viết
                          FOREIGN KEY (ID_Khach_Hang) REFERENCES KhachHang(ID)  -- Liên kết với bảng khách hàng
);

INSERT INTO LoaiSanPham (KieuDang, NgayTao, NgayCapNhat)
VALUES
(N'Tai nghe true wireless', GETDATE(), GETDATE()),
(N'Tai nghe dây', GETDATE(), GETDATE()),
(N'Onear', GETDATE(), GETDATE()),
(N'Inear', GETDATE(), GETDATE()),
(N'Head phone', GETDATE(), GETDATE());



INSERT INTO MauSac (ten, ngayTao, ngayCapNhat)
VALUES
('Red', GETDATE(), GETDATE()),
('Green', GETDATE(), GETDATE()),
('Blue', GETDATE(), GETDATE()),
('Yellow', GETDATE(), GETDATE()),
('Orange', GETDATE(), GETDATE()),
('Purple', GETDATE(), GETDATE()),
('Brown', GETDATE(), GETDATE()),
('Black', GETDATE(), GETDATE()),
('White', GETDATE(), GETDATE()),
('Gray', GETDATE(), GETDATE()),
('Cyan', GETDATE(), GETDATE()),
('Magenta', GETDATE(), GETDATE()),
('Pink', GETDATE(), GETDATE()),
('Lime', GETDATE(), GETDATE()),
('Maroon', GETDATE(), GETDATE()),
('Navy', GETDATE(), GETDATE()),
('Teal', GETDATE(), GETDATE()),
('Olive', GETDATE(), GETDATE()),
('Beige', GETDATE(), GETDATE()),
('Coral', GETDATE(), GETDATE()),
('Gold', GETDATE(), GETDATE()),
('Silver', GETDATE(), GETDATE()),
('Crimson', GETDATE(), GETDATE()),
('Ivory', GETDATE(), GETDATE()),
('Lavender', GETDATE(), GETDATE()),
('Mint', GETDATE(), GETDATE()),
('Salmon', GETDATE(), GETDATE()),
('Turquoise', GETDATE(), GETDATE()),
('Violet', GETDATE(), GETDATE()),
('Indigo', GETDATE(), GETDATE()),
('Chocolate', GETDATE(), GETDATE()),
('Sky Blue', GETDATE(), GETDATE()),
('Slate Gray', GETDATE(), GETDATE()),
('Sienna', GETDATE(), GETDATE()),
('Sea Green', GETDATE(), GETDATE()),
('Khaki', GETDATE(), GETDATE()),
('Azure', GETDATE(), GETDATE()),
('Aquamarine', GETDATE(), GETDATE()),
('Chartreuse', GETDATE(), GETDATE()),
('Cinnamon', GETDATE(), GETDATE()),
('Forest Green', GETDATE(), GETDATE()),
('Fuchsia', GETDATE(), GETDATE()),
('Lavender Blush', GETDATE(), GETDATE()),
('Mauve', GETDATE(), GETDATE()),
('Pale Green', GETDATE(), GETDATE()),
('Peach', GETDATE(), GETDATE()),
('Periwinkle', GETDATE(), GETDATE()),
('Plum', GETDATE(), GETDATE()),
('Rose', GETDATE(), GETDATE());


insert into HinhAnh
VALUES
    ----headphone
    ('headphone1.jpg', GETDATE(), GETDATE()),
    ('headphone2.jpg', GETDATE(), GETDATE()),
    ('headphone3.jpg', GETDATE(), GETDATE()),
    ('headphone4.jpg', GETDATE(), GETDATE()),
    ('headphone5.jpg', GETDATE(), GETDATE()),
    ('headphone6.jpg', GETDATE(), GETDATE()),
    ('headphone7.jpg', GETDATE(), GETDATE()),
    ('headphone8.jpg', GETDATE(), GETDATE()),
    ('headphone9.jpg', GETDATE(), GETDATE()),
    ('headphone10.jpg', GETDATE(), GETDATE()),
    ('headphone11.jpg', GETDATE(), GETDATE()),
    ('headphone12.jpg', GETDATE(), GETDATE()),
    ('headphone13.jpg', GETDATE(), GETDATE()),
    -----
    ('inear1.jpg', GETDATE(), GETDATE()),
    ('inear2.jpg', GETDATE(), GETDATE()),
    ('inear3.jpg', GETDATE(), GETDATE()),
    ('inear4.jpg', GETDATE(), GETDATE()),
    ('inear5.jpg', GETDATE(), GETDATE()),
    ('inear6.jpg', GETDATE(), GETDATE()),
    ('inear7.jpg', GETDATE(), GETDATE()),
    ('inear8.jpg', GETDATE(), GETDATE()),
    ('inear9.jpg', GETDATE(), GETDATE()),
    ('inear10.jpg', GETDATE(), GETDATE()),
    ('inear11.jpg', GETDATE(), GETDATE()),
    ('inear12.jpg', GETDATE(), GETDATE()),
    ('inear13.jpg', GETDATE(), GETDATE()),
    ('inear14.jpg', GETDATE(), GETDATE()),
    ('inear15.jpg', GETDATE(), GETDATE()),
    ('inear16.jpg', GETDATE(), GETDATE()),
    ('inear17.jpg', GETDATE(), GETDATE()),
    ('inear18.jpg', GETDATE(), GETDATE()),
    ('inear19.jpg', GETDATE(), GETDATE()),
    ('inear20.jpg', GETDATE(), GETDATE()),
    ('inear21.jpg', GETDATE(), GETDATE()),
    -----
    ('onear1.jpg', GETDATE(), GETDATE()),
    ('onear2.jpg', GETDATE(), GETDATE()),
    ('onear3.jpg', GETDATE(), GETDATE()),
    ('onear4.jpg', GETDATE(), GETDATE()),
    ('onear5.jpg', GETDATE(), GETDATE()),
    ('onear6.jpg', GETDATE(), GETDATE()),
    ('onear7.jpg', GETDATE(), GETDATE()),
    ('onear8.jpg', GETDATE(), GETDATE()),
    ('onear9.jpg', GETDATE(), GETDATE()),
    ('onear10.jpg', GETDATE(), GETDATE()),
    ('onear11.jpg', GETDATE(), GETDATE()),
    ('onear12.jpg', GETDATE(), GETDATE()),
    -----
    ('taingheday1.jpg', GETDATE(), GETDATE()),
    ('taingheday2.jpg', GETDATE(), GETDATE()),
    ('taingheday3.jpg', GETDATE(), GETDATE()),
    ('taingheday4.jpg', GETDATE(), GETDATE()),
    ('taingheday5.jpg', GETDATE(), GETDATE()),
    ('taingheday6.jpg', GETDATE(), GETDATE()),
    ('taingheday7.jpg', GETDATE(), GETDATE()),
    ('taingheday8.jpg', GETDATE(), GETDATE()),
    ('taingheday9.jpg', GETDATE(), GETDATE()),
    ('taingheday10.jpg', GETDATE(), GETDATE()),
    ('taingheday11.jpg', GETDATE(), GETDATE()),
    ('taingheday12.jpg', GETDATE(), GETDATE()),
    ('taingheday13.jpg', GETDATE(), GETDATE()),
    ('taingheday14.jpg', GETDATE(), GETDATE()),
    ('taingheday15.jpg', GETDATE(), GETDATE()),
    ('taingheday16.jpg', GETDATE(), GETDATE()),
    ('taingheday17.jpg', GETDATE(), GETDATE()),
    ('taingheday18.jpg', GETDATE(), GETDATE()),
    ('taingheday19.jpg', GETDATE(), GETDATE()),
    ('taingheday20.jpg', GETDATE(), GETDATE()),
    ('taingheday21.jpg', GETDATE(), GETDATE()),
    ('taingheday22.jpg', GETDATE(), GETDATE()),
    ('taingheday23.jpg', GETDATE(), GETDATE()),
    ('taingheday24.jpg', GETDATE(), GETDATE()),
    ('taingheday25.jpg', GETDATE(), GETDATE()),
    ('taingheday26.jpg', GETDATE(), GETDATE()),
    -----
    ('truewireless1.jpg', GETDATE(), GETDATE()),
    ('truewireless2.jpg', GETDATE(), GETDATE()),
    ('truewireless3.jpg', GETDATE(), GETDATE()),
    ('truewireless4.jpg', GETDATE(), GETDATE()),
    ('truewireless5.jpg', GETDATE(), GETDATE()),
    ('truewireless6.jpg', GETDATE(), GETDATE()),
    ('truewireless7.jpg', GETDATE(), GETDATE()),
    ('truewireless8.jpg', GETDATE(), GETDATE()),
    ('truewireless9.jpg', GETDATE(), GETDATE()),
    ('truewireless10.jpg', GETDATE(), GETDATE()),
    ('truewireless11.jpg', GETDATE(), GETDATE()),
    ('truewireless12.jpg', GETDATE(), GETDATE()),
    ('truewireless13.jpg', GETDATE(), GETDATE()),
    ('truewireless14.jpg', GETDATE(), GETDATE()),
    ('truewireless15.jpg', GETDATE(), GETDATE()),
    ('truewireless16.jpg', GETDATE(), GETDATE()),
    ('truewireless17.jpg', GETDATE(), GETDATE()),
    ('truewireless18.jpg', GETDATE(), GETDATE()),
    ('truewireless19.jpg', GETDATE(), GETDATE()),
    ('truewireless20.jpg', GETDATE(), GETDATE());

-- Thêm dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (Ten, SDT, DiaChi, Email, TaiKhoan, MatKhau, role, isRegitered,NgayDangKy) VALUES
(N'Minh Đức ', '0123456789', N'Xóm 7 Xuân Tiến,Khánh Vân, Yên Khánh,Ninh Binh', 'minhduc@gmail.com', 'minhduc', '$2y$10$7lr1KdaHUXfboqAlpoYMi.veiBkdmUyJEvlrnDmvgVFXRKG6t8IlG','KHACHHANG', 1, GETDATE()),
(N'Hoàng Nam', '0123456788', N'Số 07,Thị Trấn Si Ma Cai,Si Ma Cai,Lào Cai ', 'hoangnam@gmail.com', 'hoangnam', '$2y$10$O2VwsuqS829atSXlQoRwRudI/c9KS4Hl4CoBJlaFcjN/gGKEfYHES', 'KHACHHANG', 1, GETDATE()),
(N'Khánh Ly', '0123456787', N'Thôn 8,Tống Trân,Phù Cừ,Hưng Yên', 'khanhly@gmail.com', 'khanhly', '$2y$10$H0kmrNQ5nuwPkgLP9pcEBeiUEjjMC8iBFmV0g46rUCqBfD.J3pswS', 'KHACHHANG', 1, GETDATE()),
(N'Hoài Thương', '0123456786', N'Số 3 Tân Mỹ,Mĩ Đình 2,Nam Từ Liêm,Hà Nội', 'hoaithuong@gmail.com', 'hoaithuong', '$2y$10$H0kmrNQ5nuwPkgLP9pcEBeiUEjjMC8iBFmV0g46rUCqBfD.J3pswS', 'KHACHHANG', 1, GETDATE()),
(N'Vũ Tuấn', '0123456785', N'Số 20,An Khánh,Thủ Đức,Hồ Chí Minh', 'vutuan@gmail.com', 'vutuan', '$2y$10$nzL3M/i3JVlcso/QSvKcC.w8JF3WkNYwozG.ZDfEupiM7HG4F82s6', 'KHACHHANG', 1, GETDATE());



-- Thêm dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (Ten, NgaySinh, GioiTinh, DiaChi, Email, SoDienThoai, role, username, password)
VALUES
(N'Phạm Lưu', '2002-02-21', 'Nam', N'Ninh Bình', 'luupvph39811@fpt.edu.vn', '0987654321', 'ADMIN', 'ad1', '$2a$10$hnBoIWef0XPTPpXdYKBWAe2r8E1cftkE1XHOEix8Bq1Ydaw.JJOsq'),
( N'Minh Thành', '2004-12-02', 'Nam', N'Hà Nội', 'thanhhnmph39897@fpt.edu.vn', '0987654322', 'ADMIN', 'ad2', '$2a$10$JwQZXenp4rajR0DxxsNvbOZULt/3Vnc/mnenlp1M6A2jj12uwwMWe'),
(N'Ngọc Thắng', '2004-10-10', 'Nam', N'Thanh Hóa', 'thangpnph39814@fpt.edu.vn', '0987654323', 'ADMIN', 'ad3', '$2a$10$tkLEHOkectv0I/ILvT3ikuMcsIE0cun9dkK9ivY3ondoLqkLzGyqK'),
( N'Đức Việt', '2004-09-29', 'Nam', N'Thái Bình', 'vietndph39784@fpt.edu.vn', '0987654324', 'ADMIN', 'ad4', '$2a$10$KRIxanJvyfYH832CHO2zKeRF0VXmHdHLrFFs7qgpg/8fbDG1daHf6'),
( N'Phúc Anh', '2004-10-15', 'Nam', N'Hải Phòng', 'anhptpph39488@fpt.edu.vn', '0365217002', 'ADMIN', 'ad5', '$2a$10$JdkD3l53jrgGi9kGM02XpOTGSzQYs8faJVUx4.DwLhtaeqKpS28Hi'),
( N'Minh Long', '2004-10-10', 'Nam', N'Thanh Hóa', 'longpmph39814@fpt.edu.vn', '0987654323', 'USER', 'nv1', '$2a$10$ELPY6GTdudeNKPLtfkV0B.4OtIdIcJ7D/zl7KHmV0Ft6OYPQ6euDe'),
( N'Hà Trang', '2004-09-29', N'Nữ', N'Thái Bình', 'trangthph39784@fpt.edu.vn', '0987654324', 'USER', 'nv2', '$2a$10$p7210JkDlJ9fIFlVlFSedOgEfgA9ACjZiS6ldtFs2bV8R7y4Fbgoe'),
( N'Vũ Mai', '2004-10-15', N'Nữ', N'Hải Phòng', 'maivtph39488@fpt.edu.vn', '0365217002', 'USER', 'nv3', '$2a$10$KTu12pDQwkjyaRwkEMltHOZfGeARdQOzz5qgLQl8UH4kPgGqzN87q');



INSERT INTO Hang (Ten, NgayTao, NgayCapNhat)
VALUES
('Sony', GETDATE(), GETDATE()),
('Sennheiser', GETDATE(), GETDATE()),
('Bowers & Wilkins', GETDATE(), GETDATE()),
('EarFun', GETDATE(), GETDATE()),
('Razer', GETDATE(), GETDATE()),
('Beyerdynamic', GETDATE(), GETDATE()),
('V-Moda', GETDATE(), GETDATE()),
('Audiotechnica', GETDATE(), GETDATE()),
('Soundpeats', GETDATE(), GETDATE()),
('Bang & Olufsen', GETDATE(), GETDATE()),
('Noble', GETDATE(), GETDATE()),
('FiiO', GETDATE(), GETDATE()),
('Astell & Kern', GETDATE(), GETDATE()),
('AG Final Audio', GETDATE(), GETDATE()),
('Wiwu', GETDATE(), GETDATE()),
('Kinera', GETDATE(), GETDATE()),
('Cambridge Audio', GETDATE(), GETDATE()),
('JBL', GETDATE(), GETDATE()),
('Pioneer', GETDATE(), GETDATE()),
('Technica', GETDATE(), GETDATE()),
('Earldom', GETDATE(), GETDATE()),
('Marshall', GETDATE(), GETDATE()),
('Skullcandy', GETDATE(), GETDATE()),
('Philips', GETDATE(), GETDATE()),
('Beats', GETDATE(), GETDATE()),
('Koss', GETDATE(), GETDATE()),
('Audeze', GETDATE(), GETDATE()),
('Moondrop', GETDATE(), GETDATE()),
('Apple', GETDATE(), GETDATE()),
('Ultrasone', GETDATE(), GETDATE()),
('Bose', GETDATE(), GETDATE()),
('Cleer', GETDATE(), GETDATE()),
('Kinera', GETDATE(), GETDATE());


INSERT INTO SanPham
VALUES ('Sony WH-1000XM4', GETDATE(), GETDATE()),
       ('Sennheiser Momentum 4', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PX5', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PX7', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PX7 s2', GETDATE(), GETDATE()),
       ('EarFun Wave Pro', GETDATE(), GETDATE()),
       ('Razer BlackShark V2 Pro Gen 2', GETDATE(), GETDATE()),
       ('Beyerdynamic DT 770 Pro X', GETDATE(), GETDATE()),
       ('Beyerdynamic MMX 200 Wireless', GETDATE(), GETDATE()),
       ('V-Moda Crossfade 3 Wireless', GETDATE(), GETDATE()),
       ('Audiotechnica ATH-M50x', GETDATE(), GETDATE()),
       ('Soundpeats Space Ceramic', GETDATE(), GETDATE()),
       ('Bang & Olufsen Beoplay H95', GETDATE(), GETDATE()),
       ('Noble Fokus Tri-umph', GETDATE(), GETDATE()),
       ('Noble XM1 SO', GETDATE(), GETDATE()),
       ('Noble Stage 3', GETDATE(), GETDATE()),
       ('Noble Onyx', GETDATE(), GETDATE()),
       ('FiiO JD1 Black Type-C SO', GETDATE(), GETDATE()),
       ('Astell & Kern AK UW100 MKII', GETDATE(), GETDATE()),
       ('AG Final Audio Cotsubu Black-S', GETDATE(), GETDATE()),
       ('Audio Technica ATH-SQ1TW2', GETDATE(), GETDATE()),
       ('AG Final Audio Cotsubu MK2', GETDATE(), GETDATE()),
       ('FiiO FH19', GETDATE(), GETDATE()),
       ('Wiwu T22', GETDATE(), GETDATE()),
       ('kinera celest wyvern abyss', GETDATE(), GETDATE()),
       ('AG Final Audio Cotsubu ASMR MK2', GETDATE(), GETDATE()),
       ('AG Final Audio Cotsubu ASMR 3D', GETDATE(), GETDATE()),
       ('Earfun Air Pro 4', GETDATE(), GETDATE()),
       ('Soundpeats Capsule 3 Pro Plus', GETDATE(), GETDATE()),
       ('Sennheiser Momentum True Wireless', GETDATE(), GETDATE()),
       ('Sennheiser Momentum Sport Bluetooth', GETDATE(), GETDATE()),
       ('Cambridge Audio Melomania 100', GETDATE(), GETDATE()),
       ('JBL Everest 310', GETDATE(), GETDATE()),
       ('Pioneer SE-S3BT', GETDATE(), GETDATE()),
       ('Technica ATH-S200BT', GETDATE(), GETDATE()),
       ('Earldom ET-BH52', GETDATE(), GETDATE()),
       ('Marshall Major IV', GETDATE(), GETDATE()),
       ('Skullcandy Riff Wireless 2', GETDATE(), GETDATE()),
       ('Philips TAH420', GETDATE(), GETDATE()),
       ('Sony WH-CH520', GETDATE(), GETDATE()),
       ('JBL Tune 520BT', GETDATE(), GETDATE()),
       ('Beats Solo 4', GETDATE(), GETDATE()),
       ('Marshall Major V', GETDATE(), GETDATE()),

       ('Koss Porta Pro', GETDATE(), GETDATE()),
       ('Fiio FA19', GETDATE(), GETDATE()),
       ('Sennheiser HD 490 Pro', GETDATE(), GETDATE()),
       ('JBL Tune 310C', GETDATE(), GETDATE()),
       ('Noble Audio XM1', GETDATE(), GETDATE()),
       ('Noble Audio Stage 3', GETDATE(), GETDATE()),
       ('Audeze MM-50', GETDATE(), GETDATE()),
       ('Moondrop U2', GETDATE(), GETDATE()),
       ('Fiio JD1', GETDATE(), GETDATE()),
       ('Fiio FD11', GETDATE(), GETDATE()),
       ('Koss KS C35', GETDATE(), GETDATE()),
       ('Fiio JD15', GETDATE(), GETDATE()),
       ('Fiio fh19', GETDATE(), GETDATE()),
       ('Wiwu ktd 01', GETDATE(), GETDATE()),
       ('Apple Earpods', GETDATE(), GETDATE()),
       ('Fiio FT1', GETDATE(), GETDATE()),
       ('Razer Kraken Kitty v2', GETDATE(), GETDATE()),
       ('Koss UR20', GETDATE(), GETDATE()),
       ('wiwu eb314', GETDATE(), GETDATE()),
       ('Ultrasone Signature', GETDATE(), GETDATE()),
       ('Sony MDR-M1', GETDATE(), GETDATE()),
       ('Ultrasone Edition 1', GETDATE(), GETDATE()),

       ('Bowers & Wilkins PI5', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PI5 S2', GETDATE(), GETDATE()),
       ('Sennheiser Momentum True Wireless 4', GETDATE(), GETDATE()),
       ('Bose QuietComfort Earbuds', GETDATE(), GETDATE()),
       ('Beats Solo Buds', GETDATE(), GETDATE()),
       ('AG Final Audio', GETDATE(), GETDATE()),
       ('Earfun Air Pro 4', GETDATE(), GETDATE()),
       ('Soundpeats Air 5', GETDATE(), GETDATE()),
       ('Noble Fokus', GETDATE(), GETDATE()),
       ('WF-C510', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PI6', GETDATE(), GETDATE()),
       ('Bowers & Wilkins PI8', GETDATE(), GETDATE()),
       ('Apple AirPods 4', GETDATE(), GETDATE()),
       ('Wiwu T28', GETDATE(), GETDATE()),
       ('Cleer Arc 3 Sport', GETDATE(), GETDATE()),
       ('Cambridge Audio Melomania 100', GETDATE(), GETDATE()),
       ('Cleer Arc 3', GETDATE(), GETDATE());

INSERT INTO SanPhamChiTiet
(ID_San_Pham, ID_MauSac, ID_Hang, ID_HinhAnh, ID_LoaiSanPham, DonGia, SoLuong, ThoiGianBaoHanh, TrangThai, NgayTao, NgayCapNhat)
VALUES
(1, 9, 1, 1, 5, 5000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()),  -- Sony WH-1000XM4 (12 tháng bảo hành)
(2, 9, 2, 3, 5, 6000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()),  -- Sennheiser Momentum 4 (12 tháng bảo hành)
(3, 11, 3, 3, 5, 8000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()),  -- Bowers & Wilkins PX5 (12 tháng bảo hành)
(4, 9, 3, 4, 5, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()),  -- Bowers & Wilkins PX7 (12 tháng bảo hành)
(5, 9, 3, 5, 5, 8000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()),  -- Bowers & Wilkins PX7 s2 (12 tháng bảo hành)
(6, 9, 4, 6, 5, 7000000 , 50, 6, N'Còn hàng', GETDATE(), GETDATE()),  -- EarFun Wave Pro (6 tháng bảo hành)
(7, 9, 5, 7, 5, 6000000 , 50, 6, N'Còn hàng', GETDATE(), GETDATE()),  -- Razer BlackShark V2 Pro Gen 2 (6 tháng bảo hành)
(8, 11, 6, 8, 5, 5000000 , 0, 6, N'Hết hàng', GETDATE(), GETDATE()),  -- Beyerdynamic DT 770 Pro X (6 tháng bảo hành)
(9, 9, 6, 9, 5, 4000000 , 50, 6, N'Còn hàng', GETDATE(), GETDATE()),  -- Beyerdynamic MMX 200 Wireless (6 tháng bảo hành)
(10, 9, 7, 10, 5, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- V-Moda Crossfade 3 Wireless (12 tháng bảo hành)
(11, 3, 8, 11, 5, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Audiotechnica ATH-M50x (12 tháng bảo hành)
(12, 7, 9, 12, 5, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Soundpeats Space Ceramic (12 tháng bảo hành)
(13, 20, 10, 13, 5, 9000000 , 50, 6, N'Còn hàng', GETDATE(), GETDATE()), -- Bang & Olufsen Beoplay H95 (6 tháng bảo hành)

-----inear
(14, 7, 11, 14, 4,  9000000 , 50, 12,  N'Còn hàng', GETDATE(), GETDATE()), -- Noble Fokus Tri-umph
(15, 8, 11, 15, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble XM1 SO
(16, 8, 11, 16, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble Stage 3
(17, 8, 11, 17, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble Onyx
(18, 8, 12, 19, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- FiiO JD1 Black Type-C SO
(19, 11, 13, 18, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Astell & Kern AK UW100 MKII
(20, 8, 14, 33, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- AG Final Audio Cotsubu Black-S
(21, 2, 8, 32, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Audio Technica ATH-SQ1TW2
(27, 20, 13, 25, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- AG Final Audio Cotsubu ASMR 3D
(23, 11, 12, 29, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- FiiO FH19
(24, 9, 15, 28, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Wiwu T22
(25, 6, 33, 27, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Kinera Celest Wyvern Abyss
(26, 6, 14, 26, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- AG Final Audio Cotsubu ASMR MK2
(28, 8, 4, 21, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Earfun Air Pro 4
(29, 8, 9, 24, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Soundpeats Capsule 3 Pro Plus
(30, 8, 2, 34, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sennheiser Momentum True Wireless
(31, 8, 2, 23, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sennheiser Momentum Sport Bluetooth
(32, 8, 17, 20, 4, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Cambridge Audio Melomania 100
-----onear
(33, 10, 18, 45, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- JBL Everest 310
(34, 3, 19, 44, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Pioneer SE-S3BT
(35, 8, 8, 43, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Technica ATH-S200BT
(36, 10, 21, 42, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Earldom ET-BH52
(37, 8, 22, 41, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Marshall Major IV
(38, 8, 23, 40, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Skullcandy Riff Wireless 2
(39, 8, 24, 39, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Philips TAH420
(40, 8, 1, 38, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sony WH-CH520
(41, 8, 18, 37, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- JBL Tune 520BT
(42, 3, 25, 36, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Beats Solo 4
(43, 8, 22, 35, 3, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Marshall Major V
--tai nghe day
(44, 8, 26, 47, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Koss Porta Pro
(45, 10, 12, 48, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio FA19
(46, 8, 2, 50, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sennheiser HD 490 Pro
(47, 8, 18, 51, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- JBL Tune 310C
(48, 8, 11, 52, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble Audio XM1
(49, 8, 11, 53, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble Audio Stage 3
(50, 22, 27, 54, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Audeze MM-50
(51, 22, 28, 55, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Moondrop U2
(52, 8, 12, 56, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio JD1
(53, 22, 12, 57, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio FD11
(54, 8, 26, 58, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Koss KS C35
(55, 22, 12, 59, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio JD15
(56, 10, 12, 61, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio FH19
(57, 13, 15, 63, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Wiwu KTD 01
(58, 7, 12, 65, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Fiio FT1
(59, 9, 29, 66, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Apple Earpods
(60, 13, 5, 67, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Razer Kraken Kitty V2
(61, 8, 26, 68, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Koss UR20
(62, 8, 15, 69, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Wiwu EB314
(63, 8, 30, 70, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Ultrasone Signature
(64, 8, 1, 71, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sony MDR-M1
(65, 22, 30, 72, 2, 9000000 , 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Ultrasone Edition 1
-----true wireless
(66, 8, 3, 92, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Bowers & Wilkins PI5
(67, 10, 3, 91, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Bowers & Wilkins PI5 S2
(68, 21, 2, 90, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Sennheiser Momentum True Wireless 4
(69, 8, 31, 89, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Bose QuietComfort Earbuds
(70, 1, 25, 88, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Beats Solo Buds
(71, 25, 14, 87, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- AG Final Audio
(72, 8, 4, 85, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Earfun Air Pro 4
(73, 8, 9, 84, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Soundpeats Air 5
(74, 3, 11, 82, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Noble Fokus
(75, 4, 1, 81, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- sony WF-C510
(76, 8, 3, 80, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Bowers & Wilkins PI6
(77, 8, 3, 79, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Bowers & Wilkins PI8
(78, 9, 29, 78, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Apple AirPods 4
(79, 8, 15, 76, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Wiwu T28
(80, 8, 32, 75, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Cleer Arc 3 Sport
(81, 8, 17, 73, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()), -- Cambridge Audio Melomania 100
(82, 8, 32, 74, 1, 9000000, 50, 12, N'Còn hàng', GETDATE(), GETDATE()); -- Cleer Arc 3

-- Voucher 1: Giảm 10% cho hóa đơn trên 10 triệu
INSERT INTO Voucher (Ten, Loai, GiaTriPhanTram, GiaTriHoaDonToiThieu, NgayBatDau, NgayKetThuc, NgayTao, NgayCapNhat, TrangThai)
VALUES (N'Voucher 10% cho đơn hàng trên 10 triệu', 'GiamPhanTram', 10.00, 10000000, GETDATE(), '2025-12-31', GETDATE(), GETDATE(), N'Active');

-- Voucher 2: Giảm 500.000 đồng cho hóa đơn trên 7 triệu
INSERT INTO Voucher (Ten, Loai, GiaTriTien, GiaTriHoaDonToiThieu, NgayBatDau, NgayKetThuc, NgayTao, NgayCapNhat, TrangThai)
VALUES (N'Voucher giảm 500K cho đơn hàng trên 7 triệu', 'GiamTien', 500000, 7000000, GETDATE(), '2025-12-31', GETDATE(), GETDATE(), N'Active');

-- Voucher 3: Giảm 200.000 đồng cho hóa đơn từ 3 triệu đến dưới 5 triệu
INSERT INTO Voucher (Ten, Loai, GiaTriTien, GiaTriHoaDonToiThieu, NgayBatDau, NgayKetThuc, NgayTao, NgayCapNhat, TrangThai)
VALUES (N'Voucher giảm 200K cho đơn hàng trên 3 triệu', 'GiamTien', 200000, 3000000, GETDATE(), '2025-12-31', GETDATE(), GETDATE(), N'Active');

-- Bài viết
INSERT INTO articles (title, imageUrl, content)
VALUES
(N'Tai Nghe Không Dây – Trải Nghiệm Âm Thanh Tự Do', 'tainghekhongday.jpg', N'Tai nghe không dây đang ngày càng trở thành lựa chọn hàng đầu của người dùng yêu thích sự tiện lợi và không bị ràng buộc. Với thiết kế nhỏ gọn, không dây, bạn có thể dễ dàng di chuyển, tập thể dục hay tham gia các hoạt động thể thao mà không bị vướng víu. Những mẫu tai nghe này cung cấp chất lượng âm thanh tuyệt vời và khả năng kết nối nhanh chóng với các thiết bị di động, laptop. Dù không có dây nối, tai nghe không dây vẫn giữ được sự ổn định và chất lượng âm thanh cao, mang đến những trải nghiệm âm nhạc tuyệt vời và giúp bạn tập trung hơn vào công việc hoặc sở thích cá nhân.'),
(N'Tai Nghe Over-Ear – Trải Nghiệm Âm Thanh Sống Động', 'taingheonear.jpg', N'Tai nghe over-ear (tai nghe ôm trọn tai) mang đến một trải nghiệm âm thanh vượt trội với chất lượng âm thanh chi tiết và sâu lắng. Với thiết kế bao phủ toàn bộ tai, loại tai nghe này giúp giảm thiểu tiếng ồn bên ngoài, cho phép người dùng đắm chìm vào không gian âm nhạc riêng biệt. Bên cạnh đó, với lớp đệm mút dày dặn, tai nghe over-ear rất thoải mái khi sử dụng trong thời gian dài, phù hợp cho những người làm việc lâu giờ hoặc đam mê âm nhạc.'),
(N'Tai Nghe In-Ear – Nhỏ Gọn và Tiện Lợi', 'taingheinaer.jpg', N'Tai nghe inear có thiết kế nhỏ gọn, dễ dàng mang theo và sử dụng trong mọi hoàn cảnh. Với khả năng cách âm tốt, tai nghe in-ear giúp bạn thưởng thức âm nhạc chất lượng cao, ngay cả khi di chuyển trong môi trường ồn ào như xe buýt hay tàu điện. Hầu hết các mẫu tai nghe in-ear hiện nay được trang bị công nghệ chống ồn, giúp giảm thiểu sự phiền toái từ các yếu tố xung quanh. Điều này khiến tai nghe in-ear trở thành lựa chọn lý tưởng cho những ai ưa thích sự tiện lợi và âm thanh chất lượng.'),
(N'Tai Nghe Gaming – Trải Nghiệm Âm Thanh Tối Ưu Cho Game Thủ', 'tainghegameminh.jpg', N'Tai nghe gaming là một phần không thể thiếu đối với các game thủ chuyên nghiệp. Với chất lượng âm thanh vòm 7.1, các tai nghe này mang đến những trải nghiệm game sống động, chân thật, giúp người chơi nhận diện tốt hơn các âm thanh xung quanh như bước chân của đối thủ hay tiếng súng trong game. Tai nghe gaming còn được trang bị mic chống ồn, giúp giao tiếp rõ ràng và hiệu quả trong các trận đấu. Đặc biệt, với thiết kế đặc biệt và đệm tai êm ái, tai nghe gaming không chỉ mang lại sự thoải mái mà còn giúp bạn chơi game lâu mà không bị mỏi tai.'),
(N'Cách Chọn Tai Nghe Phù Hợp Với Nhu Cầu Của Bạn', 'tuychon.jpg', N'Việc lựa chọn một chiếc tai nghe phù hợp với nhu cầu của mình không phải là điều dễ dàng. Đầu tiên, bạn cần xác định mục đích sử dụng: nghe nhạc, chơi game, tập thể thao hay làm việc. Với mỗi nhu cầu, có những đặc điểm và tính năng khác nhau. Tai nghe không dây phù hợp cho những ai yêu thích sự tiện lợi và di động, trong khi tai nghe over-ear lại thích hợp cho những ai tìm kiếm chất lượng âm thanh cao và trải nghiệm âm thanh sống động. Ngoài ra, bạn cũng cần chú ý đến các yếu tố như khả năng chống ồn, thời gian sử dụng, và độ bền của tai nghe để lựa chọn sản phẩm tốt nhất.');

-- Bình luận
INSERT INTO comments (article_id, ID_Khach_Hang, content)
VALUES
(1, 1, N'Bài viết rất hay! Cảm ơn tác giả.'),
(2, 2, N'Tai nghe không dây đúng là một lựa chọn tuyệt vời cho người hay di chuyển, mình đã mua một chiếc và rất hài lòng.'),
(3, 3, N'Tai nghe over-ear rất thích hợp để nghe nhạc chất lượng cao, mình đã thử và cảm thấy âm thanh rất chi tiết.'),
(4, 4, N'Tai nghe inear tiện lợi, nhưng nếu dùng lâu sẽ hơi đau tai. Tuy nhiên, chất lượng âm thanh khá ổn.'),
(5, 5, N'Mình là game thủ, tai nghe gaming thực sự nâng cao trải nghiệm của mình trong các trận đấu, rất khuyến khích mọi người thử!');


select * from SanPham
select * from SanPhamChiTiet
select * from MauSac
select * from Hang
select * from HinhAnh
select * from LoaiSanPham
select * from SanPhamChiTiet
select * from donHang
select * from DonHangChiTiet
select * from GioHang
select * from GioHangChiTiet
select * from KhachHang
select * from NhanVien
select * from HoaDon
select * from HoaDonChiTiet
select * from Voucher
select * from articles
select * from comments

