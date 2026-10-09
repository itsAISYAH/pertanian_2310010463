package pertanian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Util {

    public static final String KOSONG = "(kosong)";

    public interface Aksi {
        void jalankan(Connection con) throws Exception;
    }

    public static void pesan(String teks) {
        JOptionPane.showMessageDialog(null, teks);
    }

    public static boolean konfirmasi(String teks) {
        return JOptionPane.showConfirmDialog(null, teks, "Konfirmasi",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean adaPilihan(String id) {
        if (id == null || id.trim().isEmpty()) {
            pesan("Pilih data di tabel terlebih dahulu.");
            return false;
        }
        return true;
    }

    public static String pesanSql(SQLException e) {
        if (e.getErrorCode() == 1451) {
            return "Data ini masih dipakai oleh data lain. Hapus data yang terkait terlebih dahulu.";
        }
        if (e.getErrorCode() == 1452) {
            return "ID yang dimasukkan belum ada di tabel induknya. Periksa kembali ID Petani, Lahan, Tanaman, dan sejenisnya.";
        }
        if (e.getErrorCode() == 0) {
            return e.getMessage();
        }
        return "Kesalahan database: " + e.getMessage();
    }

    private static void setParam(PreparedStatement ps, Object[] param) throws SQLException {
        for (int i = 0; i < param.length; i++) {
            if (param[i] == null) {
                ps.setNull(i + 1, java.sql.Types.NULL);
            } else {
                ps.setObject(i + 1, param[i]);
            }
        }
    }

    public static void jalankan(Connection con, String sql, Object... param) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setParam(ps, param);
            ps.executeUpdate();
        }
    }

    public static boolean eksekusi(String sql, Object... param) {
        try {
            jalankan(Koneksi.getKoneksi(), sql, param);
            return true;
        } catch (SQLException e) {
            pesan(pesanSql(e));
        } catch (NullPointerException e) {
            pesan("Tidak ada koneksi ke database. Pastikan MySQL di XAMPP sudah berjalan.");
        }
        return false;
    }

    public static boolean transaksi(Aksi aksi) {
        Connection con = Koneksi.getKoneksi();
        if (con == null) {
            pesan("Tidak ada koneksi ke database. Pastikan MySQL di XAMPP sudah berjalan.");
            return false;
        }
        try {
            con.setAutoCommit(false);
            aksi.jalankan(con);
            con.commit();
            return true;
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException ignore) {
            }
            if (e instanceof SQLException) {
                pesan(pesanSql((SQLException) e));
            } else {
                pesan(e.getMessage());
            }
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public static void isiTabel(JTable tabel, String sql, Object... param) {
        DefaultTableModel lama = (DefaultTableModel) tabel.getModel();
        int kolom = lama.getColumnCount();
        String[] judul = new String[kolom];
        for (int i = 0; i < kolom; i++) {
            judul[i] = lama.getColumnName(i);
        }
        DefaultTableModel model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try (PreparedStatement ps = Koneksi.getKoneksi().prepareStatement(sql)) {
            setParam(ps, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] baris = new Object[kolom];
                    for (int i = 0; i < kolom; i++) {
                        baris[i] = rs.getObject(i + 1);
                    }
                    model.addRow(baris);
                }
            }
        } catch (Exception e) {
            pesan("Gagal memuat data: " + e.getMessage());
        }
        tabel.setModel(model);
    }

    public static int hitung(String sql) {
        try (PreparedStatement ps = Koneksi.getKoneksi().prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String ambil(JTable tabel, int baris, int kolom) {
        Object o = tabel.getValueAt(baris, kolom);
        return o == null ? "" : o.toString();
    }

    public static String wajib(String s, String nama) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException(nama + " belum diisi.");
        }
        return s.trim();
    }

    public static int angka(String s, String nama) {
        String t = wajib(s, nama);
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " harus berupa angka bulat.");
        }
    }

    public static Integer angkaOpsional(String s, String nama) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return angka(s, nama);
    }

    public static double desimal(String s, String nama) {
        String t = wajib(s, nama).replace(',', '.');
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " harus berupa angka, contoh 2.5");
        }
    }

    public static java.sql.Date tanggal(java.util.Date d) {
        if (d == null) {
            throw new IllegalArgumentException("Tanggal belum diisi.");
        }
        return new java.sql.Date(d.getTime());
    }

    public static java.sql.Date tanggal(String s, String nama) {
        String t = wajib(s, nama);
        try {
            return java.sql.Date.valueOf(t);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(nama + " harus berformat yyyy-MM-dd, contoh 2026-10-09.");
        }
    }

    public static java.util.Date keTanggal(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        try {
            return java.sql.Date.valueOf(s.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static java.sql.Time waktu(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        String t = s.trim();
        if (t.length() == 5) {
            t += ":00";
        }
        try {
            return java.sql.Time.valueOf(t);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Waktu harus berformat HH:mm atau HH:mm:ss, contoh 07:30.");
        }
    }

    public static void isiCombo(JComboBox<String> cb, String sql, boolean opsional) {
        cb.removeAllItems();
        if (opsional) {
            cb.addItem(KOSONG);
        }
        try (PreparedStatement ps = Koneksi.getKoneksi().prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cb.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (Exception e) {
            pesan("Gagal memuat pilihan: " + e.getMessage());
        }
    }

    public static Integer idCombo(JComboBox<String> cb) {
        Object o = cb.getSelectedItem();
        if (o == null || KOSONG.equals(o.toString())) {
            return null;
        }
        String s = o.toString();
        int i = s.indexOf(" - ");
        return Integer.valueOf(i < 0 ? s.trim() : s.substring(0, i).trim());
    }

    public static void pilihCombo(JComboBox<String> cb, String id) {
        if (cb.getItemCount() == 0) {
            return;
        }
        if (id != null && !id.trim().isEmpty()) {
            for (int i = 0; i < cb.getItemCount(); i++) {
                String s = cb.getItemAt(i);
                if (s.equals(id.trim()) || s.startsWith(id.trim() + " - ")) {
                    cb.setSelectedIndex(i);
                    return;
                }
            }
        }
        cb.setSelectedIndex(0);
    }

    public static String namaBarang(int idBarang, String isian) {
        if (isian != null && !isian.trim().isEmpty()) {
            return isian.trim();
        }
        try (PreparedStatement ps = Koneksi.getKoneksi().prepareStatement(
                "SELECT nama_barang FROM persediaan_stok WHERE id_barang = ?")) {
            ps.setInt(1, idBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Gagal membaca nama barang: " + e.getMessage());
        }
        throw new IllegalArgumentException("ID Barang " + idBarang + " tidak ditemukan di persediaan stok.");
    }

    public static void ubahStok(Connection con, int idBarang, int selisih) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE persediaan_stok SET jumlah_barang = jumlah_barang + ? WHERE id_barang = ?")) {
            ps.setInt(1, selisih);
            ps.setInt(2, idBarang);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Barang dengan ID " + idBarang + " tidak ditemukan di persediaan stok.");
            }
        }
    }

    public static void cekStok(Connection con, int idBarang) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT jumlah_barang FROM persediaan_stok WHERE id_barang = ?")) {
            ps.setInt(1, idBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) < 0) {
                    throw new SQLException("Stok barang ID " + idBarang + " tidak mencukupi.");
                }
            }
        }
    }

    public static int[] barisStok(Connection con, String tabel, String kolomId, int id) throws SQLException {
        String sql = "SELECT id_barang, jumlah_barang FROM " + tabel + " WHERE " + kolomId + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new int[]{rs.getInt(1), rs.getInt(2)};
                }
            }
        }
        throw new SQLException("Data tidak ditemukan.");
    }

        public static Integer cariId(String sql, int id) {
        try (PreparedStatement ps = Koneksi.getKoneksi().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void sinkronTanaman(JComboBox<String> cmTanaman, JComboBox<String> cmLahan, JComboBox<String> cmPetani) {
        Integer idTanaman = idCombo(cmTanaman);
        if (idTanaman == null) {
            return;
        }
        if (cmLahan != null) {
            Integer idLahan = cariId("SELECT id_lahan FROM tanaman WHERE id_tanaman = ?", idTanaman);
            if (idLahan != null) {
                pilihCombo(cmLahan, String.valueOf(idLahan));
            }
        }
        if (cmPetani != null) {
            Integer idPetani = cariId("SELECT l.id_petani FROM tanaman t JOIN lahan l ON t.id_lahan = l.id_lahan WHERE t.id_tanaman = ?", idTanaman);
            if (idPetani != null) {
                pilihCombo(cmPetani, String.valueOf(idPetani));
            }
        }
    }

    
}