/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pertanian;

/**
 *
 * @author Asus
 */
public class PanelBarangKeluar extends javax.swing.JPanel {

    /**
     * Creates new form PanelBarangKeluar
     */
    public PanelBarangKeluar() {
        initComponents();
        siapkan();
    }
    
        private void siapkan() {
        txtIdBarangKeluar.setText("");
        txtIdBarangKeluar.setEditable(false);
        Util.isiCombo(cmBarang, "SELECT id_barang, nama_barang FROM persediaan_stok ORDER BY nama_barang", false);
        Util.isiCombo(cmTanaman, "SELECT id_tanaman, nama_tanaman FROM tanaman ORDER BY nama_tanaman", true);
        Util.isiCombo(cmJadwalObat, "SELECT id_obat, CONCAT(nama_obat, ' (', tanggal, ')') FROM jadwal_obat_hama ORDER BY id_obat DESC", true);
        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        tabelBarangKeluar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiForm();
            }
        });
        muatData();
    }

    private String idKeluar() {
        String o = txtIdBarangKeluar.getText();
        return o == null ? "" : o;
    }

    private void muatData() {
        Util.isiTabel(tabelBarangKeluar, "SELECT id_bkeluar, id_barang, id_tanaman, id_jadwal_obat, nama_barang, tanggal, jumlah_barang FROM barang_keluar ORDER BY id_bkeluar DESC");
    }

    private void bersihkan() {
        txtIdBarangKeluar.setText("");
        Util.pilihCombo(cmBarang, "");
        Util.pilihCombo(cmTanaman, "");
        Util.pilihCombo(cmJadwalObat, "");
        jDateChooser1.setDate(null);
        txtJumlahBarang.setText("");
        tabelBarangKeluar.clearSelection();
    }

    private void isiForm() {
        int b = tabelBarangKeluar.getSelectedRow();
        if (b < 0) {
            return;
        }
        txtIdBarangKeluar.setText(Util.ambil(tabelBarangKeluar, b, 0));
        Util.pilihCombo(cmBarang, Util.ambil(tabelBarangKeluar, b, 1));
        Util.pilihCombo(cmTanaman, Util.ambil(tabelBarangKeluar, b, 2));
        Util.pilihCombo(cmJadwalObat, Util.ambil(tabelBarangKeluar, b, 3));
        jDateChooser1.setDate(Util.keTanggal(Util.ambil(tabelBarangKeluar, b, 5)));
        txtJumlahBarang.setText(Util.ambil(tabelBarangKeluar, b, 6));
    }

    private void simpan() {
        try {
            Integer pilihBarang = Util.idCombo(cmBarang);
            if (pilihBarang == null) {
                throw new IllegalArgumentException("Barang belum dipilih.");
            }
            final int idBarang = pilihBarang;
            final Integer idTanaman = Util.idCombo(cmTanaman);
            final Integer idJadwal = Util.idCombo(cmJadwalObat);
            final String nama = Util.namaBarang(idBarang, "");
            final java.sql.Date tgl = Util.tanggal(jDateChooser1.getDate());
            final int jumlah = Util.angka(txtJumlahBarang.getText(), "Jumlah barang");
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah barang harus lebih dari 0.");
            }
            boolean ok = Util.transaksi(con -> {
                Util.jalankan(con, "INSERT INTO barang_keluar (id_barang, id_tanaman, id_jadwal_obat, nama_barang, tanggal, jumlah_barang) VALUES (?, ?, ?, ?, ?, ?)",
                        idBarang, idTanaman, idJadwal, nama, tgl, jumlah);
                Util.ubahStok(con, idBarang, -jumlah);
                Util.cekStok(con, idBarang);
            });
            if (ok) {
                Util.pesan("Data berhasil disimpan. Stok barang berkurang " + jumlah + ".");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    private void ubah() {
        if (!Util.adaPilihan(idKeluar())) {
            return;
        }
        try {
            final int id = Util.angka(idKeluar(), "ID Barang Keluar");
            Integer pilihBarang = Util.idCombo(cmBarang);
            if (pilihBarang == null) {
                throw new IllegalArgumentException("Barang belum dipilih.");
            }
            final int idBarang = pilihBarang;
            final Integer idTanaman = Util.idCombo(cmTanaman);
            final Integer idJadwal = Util.idCombo(cmJadwalObat);
            final String nama = Util.namaBarang(idBarang, "");
            final java.sql.Date tgl = Util.tanggal(jDateChooser1.getDate());
            final int jumlah = Util.angka(txtJumlahBarang.getText(), "Jumlah barang");
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah barang harus lebih dari 0.");
            }
            boolean ok = Util.transaksi(con -> {
                int[] lama = Util.barisStok(con, "barang_keluar", "id_bkeluar", id);
                Util.ubahStok(con, lama[0], lama[1]);
                Util.jalankan(con, "UPDATE barang_keluar SET id_barang = ?, id_tanaman = ?, id_jadwal_obat = ?, nama_barang = ?, tanggal = ?, jumlah_barang = ? WHERE id_bkeluar = ?",
                        idBarang, idTanaman, idJadwal, nama, tgl, jumlah, id);
                Util.ubahStok(con, idBarang, -jumlah);
                Util.cekStok(con, lama[0]);
                Util.cekStok(con, idBarang);
            });
            if (ok) {
                Util.pesan("Data berhasil diubah. Stok sudah disesuaikan.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }
 
    private void hapus() {
        if (!Util.adaPilihan(idKeluar())) {
            return;
        }
        if (!Util.konfirmasi("Hapus data barang keluar ini? Stok barang akan dikembalikan.")) {
            return;
        }
        try {
            final int id = Util.angka(idKeluar(), "ID Barang Keluar");
            boolean ok = Util.transaksi(con -> {
                int[] lama = Util.barisStok(con, "barang_keluar", "id_bkeluar", id);
                Util.ubahStok(con, lama[0], lama[1]);
                Util.jalankan(con, "DELETE FROM barang_keluar WHERE id_bkeluar = ?", id);
            });
            if (ok) {
                Util.pesan("Data berhasil dihapus. Stok sudah dikembalikan.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBarangKeluar = new javax.swing.JLabel();
        lblIdBarangKeluar = new javax.swing.JLabel();
        lblTanaman = new javax.swing.JLabel();
        lblBarang = new javax.swing.JLabel();
        lblJadwalObat = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        lblJumlahBarang = new javax.swing.JLabel();
        txtJumlahBarang = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarangKeluar = new javax.swing.JTable();
        cmBarang = new javax.swing.JComboBox<>();
        cmTanaman = new javax.swing.JComboBox<>();
        cmJadwalObat = new javax.swing.JComboBox<>();
        txtIdBarangKeluar = new javax.swing.JTextField();

        lblBarangKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBarangKeluar.setText("Barang Keluar");

        lblIdBarangKeluar.setText("ID Barang Keluar");

        lblTanaman.setText("Tanaman");

        lblBarang.setText("Barang");

        lblJadwalObat.setText("Jadwal Obat");

        lblTanggal.setText("Tanggal");

        lblJumlahBarang.setText("Jumlah Barang");

        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("SIMPAN");

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setText("EDIT");

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setText("HAPUS");

        tabelBarangKeluar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Barang Keluar", "ID Barang", "ID Tanaman", "ID Jadwal Obat", "Nama Barang", "Tanggal", "Jumlah"
            }
        ));
        jScrollPane1.setViewportView(tabelBarangKeluar);

        cmBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmTanaman.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmJadwalObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtIdBarangKeluar.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmTanaman, 0, 300, Short.MAX_VALUE)
                                .addComponent(lblBarangKeluar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblIdBarangKeluar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTanaman, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtIdBarangKeluar))
                            .addComponent(lblJumlahBarang)
                            .addComponent(txtJumlahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTanggal)
                                    .addComponent(lblJadwalObat)
                                    .addComponent(lblBarang))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmJadwalObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBarangKeluar)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdBarangKeluar)
                    .addComponent(lblBarang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdBarangKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanaman)
                    .addComponent(lblJadwalObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmTanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmJadwalObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggal)
                    .addComponent(lblJumlahBarang))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtJumlahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan)
                            .addComponent(btnEdit)
                            .addComponent(btnHapus))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmBarang;
    private javax.swing.JComboBox<String> cmJadwalObat;
    private javax.swing.JComboBox<String> cmTanaman;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBarang;
    private javax.swing.JLabel lblBarangKeluar;
    private javax.swing.JLabel lblIdBarangKeluar;
    private javax.swing.JLabel lblJadwalObat;
    private javax.swing.JLabel lblJumlahBarang;
    private javax.swing.JLabel lblTanaman;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JTable tabelBarangKeluar;
    private javax.swing.JTextField txtIdBarangKeluar;
    private javax.swing.JTextField txtJumlahBarang;
    // End of variables declaration//GEN-END:variables
}
