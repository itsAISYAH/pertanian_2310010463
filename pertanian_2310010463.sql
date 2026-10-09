-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 09 Okt 2026 pada 12.02
-- Versi server: 10.4.25-MariaDB
-- Versi PHP: 7.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pertanian_2310010463`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_keluar`
--

CREATE TABLE `barang_keluar` (
  `id_bkeluar` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `id_tanaman` int(11) DEFAULT NULL,
  `id_jadwal_obat` int(11) DEFAULT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `tanggal` date NOT NULL,
  `jumlah_barang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_masuk`
--

CREATE TABLE `barang_masuk` (
  `id_bmasuk` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `id_tanaman` int(11) DEFAULT NULL,
  `id_panen` int(11) DEFAULT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `tanggal` date NOT NULL,
  `jumlah_barang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal_obat_hama`
--

CREATE TABLE `jadwal_obat_hama` (
  `id_obat` int(11) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `jenis_obat` varchar(100) DEFAULT NULL,
  `tanggal` date NOT NULL,
  `waktu` time DEFAULT NULL,
  `id_petani` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal_pemupukan`
--

CREATE TABLE `jadwal_pemupukan` (
  `id_pemupukan` int(11) NOT NULL,
  `id_petani` int(11) NOT NULL,
  `id_lahan` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `waktu` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `lahan`
--

CREATE TABLE `lahan` (
  `id_lahan` int(11) NOT NULL,
  `nama_lahan` varchar(100) NOT NULL,
  `luas_lahan` decimal(10,2) DEFAULT NULL,
  `id_petani` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `login`
--

CREATE TABLE `login` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `panen`
--

CREATE TABLE `panen` (
  `id_panen` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `nama_panen` varchar(100) NOT NULL,
  `jumlah_panen` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `waktu` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `penanaman`
--

CREATE TABLE `penanaman` (
  `no_penanaman` int(11) NOT NULL,
  `id_petani` int(11) NOT NULL,
  `id_lahan` int(11) NOT NULL,
  `id_tanaman` int(11) NOT NULL,
  `jumlah_tanam` int(11) NOT NULL,
  `tanggal_penanaman` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `persediaan_stok`
--

CREATE TABLE `persediaan_stok` (
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `jumlah_barang` int(11) NOT NULL DEFAULT 0,
  `id_petani` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `petani`
--

CREATE TABLE `petani` (
  `id_petani` int(11) NOT NULL,
  `nama_petani` varchar(100) NOT NULL,
  `alamat_petani` varchar(255) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tanaman`
--

CREATE TABLE `tanaman` (
  `id_tanaman` int(11) NOT NULL,
  `nama_tanaman` varchar(100) NOT NULL,
  `id_lahan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD PRIMARY KEY (`id_bkeluar`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_tanaman` (`id_tanaman`),
  ADD KEY `id_jadwal_obat` (`id_jadwal_obat`);

--
-- Indeks untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD PRIMARY KEY (`id_bmasuk`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_tanaman` (`id_tanaman`),
  ADD KEY `id_panen` (`id_panen`);

--
-- Indeks untuk tabel `jadwal_obat_hama`
--
ALTER TABLE `jadwal_obat_hama`
  ADD PRIMARY KEY (`id_obat`),
  ADD KEY `id_petani` (`id_petani`),
  ADD KEY `id_tanaman` (`id_tanaman`);

--
-- Indeks untuk tabel `jadwal_pemupukan`
--
ALTER TABLE `jadwal_pemupukan`
  ADD PRIMARY KEY (`id_pemupukan`),
  ADD KEY `id_petani` (`id_petani`),
  ADD KEY `id_lahan` (`id_lahan`),
  ADD KEY `id_tanaman` (`id_tanaman`);

--
-- Indeks untuk tabel `lahan`
--
ALTER TABLE `lahan`
  ADD PRIMARY KEY (`id_lahan`),
  ADD KEY `id_petani` (`id_petani`);

--
-- Indeks untuk tabel `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`username`);

--
-- Indeks untuk tabel `panen`
--
ALTER TABLE `panen`
  ADD PRIMARY KEY (`id_panen`),
  ADD KEY `id_tanaman` (`id_tanaman`);

--
-- Indeks untuk tabel `penanaman`
--
ALTER TABLE `penanaman`
  ADD PRIMARY KEY (`no_penanaman`),
  ADD KEY `id_petani` (`id_petani`),
  ADD KEY `id_lahan` (`id_lahan`),
  ADD KEY `id_tanaman` (`id_tanaman`);

--
-- Indeks untuk tabel `persediaan_stok`
--
ALTER TABLE `persediaan_stok`
  ADD PRIMARY KEY (`id_barang`),
  ADD KEY `id_petani` (`id_petani`);

--
-- Indeks untuk tabel `petani`
--
ALTER TABLE `petani`
  ADD PRIMARY KEY (`id_petani`);

--
-- Indeks untuk tabel `tanaman`
--
ALTER TABLE `tanaman`
  ADD PRIMARY KEY (`id_tanaman`),
  ADD KEY `id_lahan` (`id_lahan`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  MODIFY `id_bkeluar` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  MODIFY `id_bmasuk` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `jadwal_obat_hama`
--
ALTER TABLE `jadwal_obat_hama`
  MODIFY `id_obat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `jadwal_pemupukan`
--
ALTER TABLE `jadwal_pemupukan`
  MODIFY `id_pemupukan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `lahan`
--
ALTER TABLE `lahan`
  MODIFY `id_lahan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `panen`
--
ALTER TABLE `panen`
  MODIFY `id_panen` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `penanaman`
--
ALTER TABLE `penanaman`
  MODIFY `no_penanaman` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `persediaan_stok`
--
ALTER TABLE `persediaan_stok`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `petani`
--
ALTER TABLE `petani`
  MODIFY `id_petani` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `tanaman`
--
ALTER TABLE `tanaman`
  MODIFY `id_tanaman` int(11) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD CONSTRAINT `barang_keluar_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `persediaan_stok` (`id_barang`) ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_keluar_ibfk_2` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_keluar_ibfk_3` FOREIGN KEY (`id_jadwal_obat`) REFERENCES `jadwal_obat_hama` (`id_obat`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD CONSTRAINT `barang_masuk_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `persediaan_stok` (`id_barang`) ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_masuk_ibfk_2` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_masuk_ibfk_3` FOREIGN KEY (`id_panen`) REFERENCES `panen` (`id_panen`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `jadwal_obat_hama`
--
ALTER TABLE `jadwal_obat_hama`
  ADD CONSTRAINT `jadwal_obat_hama_ibfk_1` FOREIGN KEY (`id_petani`) REFERENCES `petani` (`id_petani`) ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_obat_hama_ibfk_2` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `jadwal_pemupukan`
--
ALTER TABLE `jadwal_pemupukan`
  ADD CONSTRAINT `jadwal_pemupukan_ibfk_1` FOREIGN KEY (`id_petani`) REFERENCES `petani` (`id_petani`) ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_pemupukan_ibfk_2` FOREIGN KEY (`id_lahan`) REFERENCES `lahan` (`id_lahan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_pemupukan_ibfk_3` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `lahan`
--
ALTER TABLE `lahan`
  ADD CONSTRAINT `lahan_ibfk_1` FOREIGN KEY (`id_petani`) REFERENCES `petani` (`id_petani`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `panen`
--
ALTER TABLE `panen`
  ADD CONSTRAINT `panen_ibfk_1` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `penanaman`
--
ALTER TABLE `penanaman`
  ADD CONSTRAINT `penanaman_ibfk_1` FOREIGN KEY (`id_petani`) REFERENCES `petani` (`id_petani`) ON UPDATE CASCADE,
  ADD CONSTRAINT `penanaman_ibfk_2` FOREIGN KEY (`id_lahan`) REFERENCES `lahan` (`id_lahan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `penanaman_ibfk_3` FOREIGN KEY (`id_tanaman`) REFERENCES `tanaman` (`id_tanaman`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `persediaan_stok`
--
ALTER TABLE `persediaan_stok`
  ADD CONSTRAINT `persediaan_stok_ibfk_1` FOREIGN KEY (`id_petani`) REFERENCES `petani` (`id_petani`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tanaman`
--
ALTER TABLE `tanaman`
  ADD CONSTRAINT `tanaman_ibfk_1` FOREIGN KEY (`id_lahan`) REFERENCES `lahan` (`id_lahan`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
