-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 04, 2021 lúc 06:49 PM
-- Phiên bản máy phục vụ: 10.4.11-MariaDB
-- Phiên bản PHP: 7.4.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `appteams`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `listuser`
--

CREATE TABLE `listuser` (
  `id` int(11) NOT NULL,
  `iduser` varchar(225) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `listuser`
--

INSERT INTO `listuser` (`id`, `iduser`) VALUES
(0, '3'),
(1, '1-2-3'),
(2, '1-2-3-4');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `mess`
--

CREATE TABLE `mess` (
  `iduser` int(11) NOT NULL,
  `content` text NOT NULL,
  `idroom` int(11) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `mess`
--

INSERT INTO `mess` (`iduser`, `content`, `idroom`, `date`) VALUES
(3, 'hello', 2, '0000-00-00 00:00:00'),
(1, 'không nhận đc', 2, '0000-00-00 00:00:00'),
(3, 'vậy a', 2, '0000-00-00 00:00:00'),
(1, 'what', 2, '0000-00-00 00:00:00'),
(1, 'ok nhes', 2, '0000-00-00 00:00:00'),
(3, 'hả', 2, '0000-00-00 00:00:00'),
(1, 'chao tuan', 2, '0000-00-00 00:00:00'),
(2, 'minh khong nhan duoc', 2, '0000-00-00 00:00:00'),
(1, 'di nau cơm đax', 2, '0000-00-00 00:00:00'),
(2, 'ok bác', 2, '0000-00-00 00:00:00'),
(3, 'dung distra', 2, '0000-00-00 00:00:00'),
(1, 'ap dung gi cho doi song', 2, '0000-00-00 00:00:00'),
(1, 'alo thanh do khong', 2, '0000-00-00 00:00:00'),
(3, 'aloooolllll', 2, '0000-00-00 00:00:00'),
(1, 'ahihi', 2, '0000-00-00 00:00:00'),
(1, 'hello', 5, '0000-00-00 00:00:00'),
(2, 'hello 12345', 9, '0000-00-00 00:00:00'),
(1, 'chao tuan', 9, '0000-00-00 00:00:00'),
(1, 'chao thanh', 9, '0000-00-00 00:00:00'),
(2, 'minh la phu', 9, '0000-00-00 00:00:00'),
(1, 'ok mình nhận đc rồi', 9, '0000-00-00 00:00:00'),
(2, 'thế tối ăn gì?', 9, '0000-00-00 00:00:00'),
(1, 'toói ăn bún cá as?', 9, '0000-00-00 00:00:00'),
(1, 'hello world', 9, '0000-00-00 00:00:00'),
(1, 'minh đang ở đây nef', 9, '0000-00-00 00:00:00'),
(1, 'dang o 8', 9, '0000-00-00 00:00:00'),
(1, 'minh dang ở 2', 9, '0000-00-00 00:00:00'),
(1, 'phu dang o 11', 9, '0000-00-00 00:00:00'),
(1, 'haha o day khong co Oanh', 9, '0000-00-00 00:00:00'),
(1, 'oh oanh oi', 9, '0000-00-00 00:00:00'),
(3, 'oanh dang o 11', 9, '0000-00-00 00:00:00'),
(1, 'sao khong lu thong tin nhi', 9, '0000-00-00 00:00:00'),
(1, 'hi oanh', 9, '0000-00-00 00:00:00'),
(3, 'what', 9, '0000-00-00 00:00:00'),
(1, 'hi oanh nha', 12, '0000-00-00 00:00:00'),
(3, 'vang\n', 12, '0000-00-00 00:00:00'),
(3, 'chafo fffnalkd snsksla d\nfjfkdo\nfdkfn ', 12, '0000-00-00 00:00:00'),
(1, 'chatr ok', 12, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `newfeed`
--

CREATE TABLE `newfeed` (
  `idroom` int(11) NOT NULL,
  `title` text NOT NULL,
  `content` text NOT NULL,
  `auth` varchar(225) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `newfeed`
--

INSERT INTO `newfeed` (`idroom`, `title`, `content`, `auth`) VALUES
(2, 'thong bao lich hoc', '        @OverrideonItemClick(AdapterView<?> adapterView, View view, int i, long l) {\r\n Movie selectedMovie = movieList.get(i);\r\nIntent detailIntent = new Intent(mContext, MovieDetailActivity.class);\r\n  detailIntent.putExtra(\"title\", selectedMovie.title);\r\n detailIntent.putExtra(\"description\", selectedMovie.description);\r\n detailIntent.putExtra(\"poster\", ', '1'),
(2, 'Sang mai di caffe', 'okokokkokokokok', ''),
(2, '123', 'fffffffffffff', ':)) '),
(2, '12345', 'fffffffffffff', ':)) '),
(2, '123456', 'fffffffffffff', 'Tran Hoang Phuc'),
(2, '123456', 'fffffffffffff', 'Tran Hoang Phuc'),
(2, '123456', 'ffffffffffffftttt', 'Tran Hoang Phuc'),
(2, '123456', 'ffffffffffffftttt', 'Tran Hoang Phuc'),
(2, 'hahaha', 'hahahahahahahh123', 'Tran Hoang Phuc'),
(2, 'hahaha', 'hahahahahahahh123', 'Tran Hoang Phuc'),
(2, 'hihihihih', 'okokokkoko', 'Tran Thi Kieu Oanh'),
(2, 'hihihihih', 'okokokkoko', 'Tran Thi Kieu Oanh'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hihi', 'thanh coong nhes\nalo alo', 'Tran Hoang Phuc'),
(-1, 'hello 1h sang', '1111', 'Tran Hoang Phuc'),
(-1, 'hello 1h sang', '1111', 'Tran Hoang Phuc'),
(-1, 'hello 1h sang', '1111', 'Tran Hoang Phuc');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room`
--

CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `idadmin` int(11) NOT NULL,
  `idlistuser` varchar(225) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `room`
--

INSERT INTO `room` (`id`, `idadmin`, `idlistuser`) VALUES
(2, 1, '1-2-3'),
(5, 1, '1-2'),
(8, 1, '1-2-3-4'),
(9, 3, '1-2-3'),
(10, 1, '1-4'),
(11, 4, '4-1-3'),
(12, 1, '1-3');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(225) NOT NULL,
  `password` varchar(225) NOT NULL,
  `fullname` varchar(225) NOT NULL,
  `gender` varchar(225) NOT NULL,
  `address` varchar(225) NOT NULL,
  `level` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `fullname`, `gender`, `address`, `level`) VALUES
(1, 'phu', 'phu123', 'Tran Hoang Phuc', 'Nam', 'Viet Nam', 1),
(2, 'Tuan', 'tuan123', 'Ho Tran Tuan', 'Nam', 'Hoan Kiem - Ha Noi', 1),
(3, 'Oanh', 'oanh123', 'Tran Thi Kieu Oanh', 'Nu', 'Trung Vuong - HCM - Viet Nam', 0),
(4, 'Vy', 'vy123', 'Tran Thi Vy', 'Nu', 'Hoa Khanh - Da Nang - viet Nam', 0),
(5, 'thanh', 'thanh123', 'Nguyen Trong Thanh', 'Nam', 'Gia Lai - Viet Nam', 0);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `listuser`
--
ALTER TABLE `listuser`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`iduser`);

--
-- Chỉ mục cho bảng `mess`
--
ALTER TABLE `mess`
  ADD KEY `iduser` (`iduser`),
  ADD KEY `idroom` (`idroom`);

--
-- Chỉ mục cho bảng `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idadmin` (`idadmin`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `mess`
--
ALTER TABLE `mess`
  ADD CONSTRAINT `mess_ibfk_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `mess_ibfk_2` FOREIGN KEY (`idroom`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_2` FOREIGN KEY (`idadmin`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
