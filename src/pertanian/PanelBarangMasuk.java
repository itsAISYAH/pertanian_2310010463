/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pertanian;

/**
 *
 * @author Asus
 */
public class PanelBarangMasuk extends javax.swing.JPanel {

    /**
     * Creates new form PanelBarangMasuk
     */
    public PanelBarangMasuk() {
        initComponents();
        siapkan();
    }
    
        private void siapkan() {
        txtIdBarangMasuk.setEditable(false);
        Util.isiCombo(cmBarang, "SELECT id_barang, nama_barang FROM persediaan_stok ORDER BY nama_barang", false);
        Util.isiCombo(cmTanaman, "SELECT id_tanaman, nama_tanaman FROM tanaman ORDER BY nama_tanaman", true);
        Util.isiCombo(cmPanen, "SELECT id_panen, nama_panen FROM panen ORDER BY id_panen DESC", true);
        cmPanen.addActionListener(e -> sinkronPanen());
        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        tabelBarangMasuk.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiForm();
            }
        });
        muatData();
    }

    private void sinkronPanen() {
        Integer idPanen = Util.idCombo(cmPanen);
        if (idPanen == null) {
            cmTanaman.setEnabled(true);
            return;
        }
        Integer idTanaman = Util.cariId("SELECT id_tanaman FROM panen WHERE id_panen = ?", idPanen);
        if (idTanaman != null) {
            Util.pilihCombo(cmTanaman, String.valueOf(idTanaman));
        }
        cmTanaman.setEnabled(false);
    }

    private void muatData() {
        Util.isiTabel(tabelBarangMasuk, "SELECT id_bmasuk, id_barang, id_tanaman, id_panen, nama_barang, tanggal, jumlah_barang FROM barang_masuk ORDER BY id_bmasuk DESC");
    }

    private void bersihkan() {
        txtIdBarangMasuk.setText("");
        Util.pilihCombo(cmBarang, "");
        Util.pilihCombo(cmPanen, "");
        Util.pilihCombo(cmTanaman, "");
        sinkronPanen();
        jDateChooser1.setDate(null);
        txtJumlahBarang.setText("");
        tabelBarangMasuk.clearSelection();
    }

    private void isiForm() {
        int b = tabelBarangMasuk.getSelectedRow();
        if (b < 0) {
            return;
        }
        txtIdBarangMasuk.setText(Util.ambil(tabelBarangMasuk, b, 0));
        Util.pilihCombo(cmBarang, Util.ambil(tabelBarangMasuk, b, 1));
        Util.pilihCombo(cmPanen, Util.ambil(tabelBarangMasuk, b, 3));
        sinkronPanen();
        if (Util.idCombo(cmPanen) == null) {
            Util.pilihCombo(cmTanaman, Util.ambil(tabelBarangMasuk, b, 2));
        }
        jDateChooser1.setDate(Util.keTanggal(Util.ambil(tabelBarangMasuk, b, 5)));
        txtJumlahBarang.setText(Util.ambil(tabelBarangMasuk, b, 6));
    }

    private void simpan() {
        try {
            Integer pilihBarang = Util.idCombo(cmBarang);
            if (pilihBarang == null) {
                throw new IllegalArgumentException("Barang belum dipilih.");
            }
            final int idBarang = pilihBarang;
            sinkronPanen();
            final Integer idTanaman = Util.idCombo(cmTanaman);
            final Integer idPanen = Util.idCombo(cmPanen);
            final String nama = Util.namaBarang(idBarang, "");
            final java.sql.Date tgl = Util.tanggal(jDateChooser1.getDate());
            final int jumlah = Util.angka(txtJumlahBarang.getText(), "Jumlah barang");
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah barang harus lebih dari 0.");
            }
            boolean ok = Util.transaksi(con -> {
                Util.jalankan(con, "INSERT INTO barang_masuk (id_barang, id_tanaman, id_panen, nama_barang, tanggal, jumlah_barang) VALUES (?, ?, ?, ?, ?, ?)",
                        idBarang, idTanaman, idPanen, nama, tgl, jumlah);
                Util.ubahStok(con, idBarang, jumlah);
            });
            if (ok) {
                Util.pesan("Data berhasil disimpan. Stok barang bertambah " + jumlah + ".");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    private void ubah() {
        if (!Util.adaPilihan(txtIdBarangMasuk.getText())) {
            return;
        }
        try {
            final int id = Util.angka(txtIdBarangMasuk.getText(), "ID Barang Masuk");
            Integer pilihBarang = Util.idCombo(cmBarang);
            if (pilihBarang == null) {
                throw new IllegalArgumentException("Barang belum dipilih.");
            }
            final int idBarang = pilihBarang;
            sinkronPanen();
            final Integer idTanaman = Util.idCombo(cmTanaman);
            final Integer idPanen = Util.idCombo(cmPanen);
            final String nama = Util.namaBarang(idBarang, "");
            final java.sql.Date tgl = Util.tanggal(jDateChooser1.getDate());
            final int jumlah = Util.angka(txtJumlahBarang.getText(), "Jumlah barang");
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah barang harus lebih dari 0.");
            }
            boolean ok = Util.transaksi(con -> {
                int[] lama = Util.barisStok(con, "barang_masuk", "id_bmasuk", id);
                Util.ubahStok(con, lama[0], -lama[1]);
                Util.jalankan(con, "UPDATE barang_masuk SET id_barang = ?, id_tanaman = ?, id_panen = ?, nama_barang = ?, tanggal = ?, jumlah_barang = ? WHERE id_bmasuk = ?",
                        idBarang, idTanaman, idPanen, nama, tgl, jumlah, id);
                Util.ubahStok(con, idBarang, jumlah);
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
        if (!Util.adaPilihan(txtIdBarangMasuk.getText())) {
            return;
        }
        if (!Util.konfirmasi("Hapus data barang masuk ini? Stok barang akan dikurangi kembali.")) {
            return;
        }
        try {
            final int id = Util.angka(txtIdBarangMasuk.getText(), "ID Barang Masuk");
            boolean ok = Util.transaksi(con -> {
                int[] lama = Util.barisStok(con, "barang_masuk", "id_bmasuk", id);
                Util.ubahStok(con, lama[0], -lama[1]);
                Util.jalankan(con, "DELETE FROM barang_masuk WHERE id_bmasuk = ?", id);
                Util.cekStok(con, lama[0]);
            });
            if (ok) {
                Util.pesan("Data berhasil dihapus. Stok sudah disesuaikan.");
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

        lblBarangMasuk = new javax.swing.JLabel();
        lblIdBarangMasuk = new javax.swing.JLabel();
        txtIdBarangMasuk = new javax.swing.JTextField();
        lblIdBarang = new javax.swing.JLabel();
        lblIdTanaman = new javax.swing.JLabel();
        lblIdPanen = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        txtJumlahBarang = new javax.swing.JTextField();
        lblJumlahBarang = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarangMasuk = new javax.swing.JTable();
        cmTanaman = new javax.swing.JComboBox<>();
        cmBarang = new javax.swing.JComboBox<>();
        cmPanen = new javax.swing.JComboBox<>();

        lblBarangMasuk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBarangMasuk.setText("Barang Masuk");

        lblIdBarangMasuk.setText("ID Barang Masuk");

        lblIdBarang.setText("ID Barang");

        lblIdTanaman.setText("ID Tanaman");

        lblIdPanen.setText("ID Panen");

        lblTanggal.setText("Tanggal");

        lblJumlahBarang.setText("Jumlah Barang");

        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("SIMPAN");

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setText("HAPUS");

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setText("EDIT");

        tabelBarangMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID BM", "ID BK", "ID Tanaman", "ID Panen", "Nama Barang", "Tanggal", "Jumlah Barang"
            }
        ));
        jScrollPane1.setViewportView(tabelBarangMasuk);

        cmTanaman.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmPanen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmTanaman, javax.swing.GroupLayout.Alignment.LEADING, 0, 301, Short.MAX_VALUE)
                            .addComponent(txtIdBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIdBarangMasuk, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIdTanaman, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJumlahBarang, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJumlahBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmBarang, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblIdBarang, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblIdPanen, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTanggal, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmPanen, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(btnSimpan)
                .addGap(38, 38, 38)
                .addComponent(btnEdit)
                .addGap(32, 32, 32)
                .addComponent(btnHapus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBarangMasuk)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdBarangMasuk)
                            .addComponent(lblIdBarang))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdTanaman)
                            .addComponent(lblIdPanen))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmTanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmPanen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTanggal)
                            .addComponent(lblJumlahBarang))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJumlahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmBarang;
    private javax.swing.JComboBox<String> cmPanen;
    private javax.swing.JComboBox<String> cmTanaman;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBarangMasuk;
    private javax.swing.JLabel lblIdBarang;
    private javax.swing.JLabel lblIdBarangMasuk;
    private javax.swing.JLabel lblIdPanen;
    private javax.swing.JLabel lblIdTanaman;
    private javax.swing.JLabel lblJumlahBarang;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JTable tabelBarangMasuk;
    private javax.swing.JTextField txtIdBarangMasuk;
    private javax.swing.JTextField txtJumlahBarang;
    // End of variables declaration//GEN-END:variables
}
