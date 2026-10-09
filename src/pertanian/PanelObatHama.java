/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pertanian;

/**
 *
 * @author Asus
 */
public class PanelObatHama extends javax.swing.JPanel {

    /**
     * Creates new form PanelObatHama
     */
    public PanelObatHama() {
        initComponents();
        siapkan();
    }
    
        private void siapkan() {
        txtIdObat.setEditable(false);
        Util.isiCombo(cmPetani, "SELECT id_petani, nama_petani FROM petani ORDER BY nama_petani", false);
        Util.isiCombo(cmTanaman, "SELECT id_tanaman, nama_tanaman FROM tanaman ORDER BY nama_tanaman", false);
        cmPetani.setEnabled(false);
        cmTanaman.addActionListener(e -> Util.sinkronTanaman(cmTanaman, null, cmPetani));
        Util.sinkronTanaman(cmTanaman, null, cmPetani);
        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiForm();
            }
        });
        muatData();
    }

    private void muatData() {
        Util.isiTabel(jTable1, "SELECT id_obat, nama_obat, jenis_obat, tanggal, waktu, id_tanaman, id_petani FROM jadwal_obat_hama ORDER BY tanggal DESC, id_obat DESC");
    }

    private void bersihkan() {
        txtIdObat.setText("");
        txtNamaObat.setText("");
        cbJenisObat.setSelectedIndex(0);
        tanggalJadwalObat.setDate(null);
        txtWaktu.setText("");
        Util.pilihCombo(cmTanaman, "");
        Util.sinkronTanaman(cmTanaman, null, cmPetani);
        jTable1.clearSelection();
    }

    private void isiForm() {
        int b = jTable1.getSelectedRow();
        if (b < 0) {
            return;
        }
        txtIdObat.setText(Util.ambil(jTable1, b, 0));
        txtNamaObat.setText(Util.ambil(jTable1, b, 1));
        cbJenisObat.setSelectedItem(Util.ambil(jTable1, b, 2));
        tanggalJadwalObat.setDate(Util.keTanggal(Util.ambil(jTable1, b, 3)));
        txtWaktu.setText(Util.ambil(jTable1, b, 4));
        Util.pilihCombo(cmTanaman, Util.ambil(jTable1, b, 5));
        Util.sinkronTanaman(cmTanaman, null, cmPetani);
    }

    private void simpan() {
        try {
            String nama = Util.wajib(txtNamaObat.getText(), "Nama obat");
            String jenis = String.valueOf(cbJenisObat.getSelectedItem());
            java.sql.Date tgl = Util.tanggal(tanggalJadwalObat.getDate());
            java.sql.Time wkt = Util.waktu(txtWaktu.getText());
            Integer idTanaman = Util.idCombo(cmTanaman);
            if (idTanaman == null) {
                throw new IllegalArgumentException("Tanaman belum dipilih.");
            }
            Util.sinkronTanaman(cmTanaman, null, cmPetani);
            Integer idPetani = Util.idCombo(cmPetani);
            if (Util.eksekusi("INSERT INTO jadwal_obat_hama (nama_obat, jenis_obat, tanggal, waktu, id_petani, id_tanaman) VALUES (?, ?, ?, ?, ?, ?)",
                    nama, jenis, tgl, wkt, idPetani, idTanaman)) {
                Util.pesan("Data berhasil disimpan.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    private void ubah() {
        if (!Util.adaPilihan(txtIdObat.getText())) {
            return;
        }
        try {
            int id = Util.angka(txtIdObat.getText(), "ID Obat");
            String nama = Util.wajib(txtNamaObat.getText(), "Nama obat");
            String jenis = String.valueOf(cbJenisObat.getSelectedItem());
            java.sql.Date tgl = Util.tanggal(tanggalJadwalObat.getDate());
            java.sql.Time wkt = Util.waktu(txtWaktu.getText());
            Integer idTanaman = Util.idCombo(cmTanaman);
            if (idTanaman == null) {
                throw new IllegalArgumentException("Tanaman belum dipilih.");
            }
            Util.sinkronTanaman(cmTanaman, null, cmPetani);
            Integer idPetani = Util.idCombo(cmPetani);
            if (Util.eksekusi("UPDATE jadwal_obat_hama SET nama_obat = ?, jenis_obat = ?, tanggal = ?, waktu = ?, id_petani = ?, id_tanaman = ? WHERE id_obat = ?",
                    nama, jenis, tgl, wkt, idPetani, idTanaman, id)) {
                Util.pesan("Data berhasil diubah.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }
 
    private void hapus() {
        if (!Util.adaPilihan(txtIdObat.getText())) {
            return;
        }
        if (Util.konfirmasi("Hapus jadwal obat hama ini?")
                && Util.eksekusi("DELETE FROM jadwal_obat_hama WHERE id_obat = ?", Integer.parseInt(txtIdObat.getText().trim()))) {
            Util.pesan("Data berhasil dihapus.");
            muatData();
            bersihkan();
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

        lblJadwalObat = new javax.swing.JLabel();
        lblIdObat = new javax.swing.JLabel();
        txtIdObat = new javax.swing.JTextField();
        lblJenisObat = new javax.swing.JLabel();
        txtNamaObat = new javax.swing.JTextField();
        lblNamaObat = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        lblIdPetani = new javax.swing.JLabel();
        lblIdTanaman = new javax.swing.JLabel();
        lblWaktu = new javax.swing.JLabel();
        txtWaktu = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tanggalJadwalObat = new com.toedter.calendar.JDateChooser();
        cbJenisObat = new javax.swing.JComboBox<>();
        cmTanaman = new javax.swing.JComboBox<>();
        cmPetani = new javax.swing.JComboBox<>();

        lblJadwalObat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblJadwalObat.setText("Jadwal Obat Hama");

        lblIdObat.setText("ID Obat");

        lblJenisObat.setText("Jenis Obat");

        lblNamaObat.setText("Nama Obat");

        lblTanggal.setText("Tanggal");

        lblIdPetani.setText("ID Petani");

        lblIdTanaman.setText("ID Tanaman");

        lblWaktu.setText("Waktu");

        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("SIMPAN");

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setText("HAPUS");

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setText("EDIT");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Obat", "Nama Obat", "Jenis Obat", "Tanggal", "Waktu", "ID Tanaman", "ID Petani"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        cbJenisObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Insektisida", "Fungisida", "Herbisida", "ZPT" }));

        cmTanaman.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmPetani.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblJadwalObat)
                            .addComponent(lblIdObat)
                            .addComponent(lblJenisObat)
                            .addComponent(txtIdObat)
                            .addComponent(lblWaktu)
                            .addComponent(txtWaktu)
                            .addComponent(cbJenisObat, 0, 300, Short.MAX_VALUE)
                            .addComponent(cmPetani, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamaObat)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSimpan)
                                .addGap(38, 38, 38)
                                .addComponent(btnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHapus))
                            .addComponent(tanggalJadwalObat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblIdTanaman)
                                    .addComponent(lblNamaObat)
                                    .addComponent(lblTanggal))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmTanaman, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIdPetani)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJadwalObat)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdObat)
                            .addComponent(lblNamaObat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblJenisObat)
                            .addComponent(lblTanggal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbJenisObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tanggalJadwalObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdTanaman)
                    .addComponent(lblWaktu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmTanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblIdPetani)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnHapus)
                    .addComponent(btnEdit)
                    .addComponent(cmPetani, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbJenisObat;
    private javax.swing.JComboBox<String> cmPetani;
    private javax.swing.JComboBox<String> cmTanaman;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblIdObat;
    private javax.swing.JLabel lblIdPetani;
    private javax.swing.JLabel lblIdTanaman;
    private javax.swing.JLabel lblJadwalObat;
    private javax.swing.JLabel lblJenisObat;
    private javax.swing.JLabel lblNamaObat;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblWaktu;
    private com.toedter.calendar.JDateChooser tanggalJadwalObat;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JTextField txtNamaObat;
    private javax.swing.JTextField txtWaktu;
    // End of variables declaration//GEN-END:variables
}
