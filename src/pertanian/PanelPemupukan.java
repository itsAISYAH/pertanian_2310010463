/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pertanian;

/**
 *
 * @author Asus
 */
public class PanelPemupukan extends javax.swing.JPanel {

    /**
     * Creates new form PanelPemupukan
     */
    public PanelPemupukan() {
        initComponents();
        siapkan();
    }
    
        private void siapkan() {
        txtIdPemupukan.setEditable(false);
        Util.isiCombo(cmPetani, "SELECT id_petani, nama_petani FROM petani ORDER BY nama_petani", false);
        Util.isiCombo(cmLahan, "SELECT id_lahan, nama_lahan FROM lahan ORDER BY nama_lahan", false);
        Util.isiCombo(cmTanaman, "SELECT id_tanaman, nama_tanaman FROM tanaman ORDER BY nama_tanaman", false);
        cmPetani.setEnabled(false);
        cmLahan.setEnabled(false);
        cmTanaman.addActionListener(e -> Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani));
        Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
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
        Util.isiTabel(jTable1, "SELECT id_pemupukan, id_petani, id_lahan, id_tanaman, tanggal, waktu FROM jadwal_pemupukan ORDER BY tanggal DESC, id_pemupukan DESC");
    }

    private void bersihkan() {
        txtIdPemupukan.setText("");
        Util.pilihCombo(cmTanaman, "");
        Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
        tanggalPemupukan.setDate(null);
        txtWaktu.setText("");
        jTable1.clearSelection();
    }

    private void isiForm() {
        int b = jTable1.getSelectedRow();
        if (b < 0) {
            return;
        }
        txtIdPemupukan.setText(Util.ambil(jTable1, b, 0));
        Util.pilihCombo(cmTanaman, Util.ambil(jTable1, b, 3));
        Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
        tanggalPemupukan.setDate(Util.keTanggal(Util.ambil(jTable1, b, 4)));
        txtWaktu.setText(Util.ambil(jTable1, b, 5));
    }

    private void simpan() {
        try {
            Integer idTanaman = Util.idCombo(cmTanaman);
            if (idTanaman == null) {
                throw new IllegalArgumentException("Tanaman belum dipilih.");
            }
            Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
            Integer idLahan = Util.idCombo(cmLahan);
            Integer idPetani = Util.idCombo(cmPetani);
            java.sql.Date tgl = Util.tanggal(tanggalPemupukan.getDate());
            java.sql.Time wkt = Util.waktu(txtWaktu.getText());
            if (Util.eksekusi("INSERT INTO jadwal_pemupukan (id_petani, id_lahan, id_tanaman, tanggal, waktu) VALUES (?, ?, ?, ?, ?)",
                    idPetani, idLahan, idTanaman, tgl, wkt)) {
                Util.pesan("Data berhasil disimpan.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    private void ubah() {
        if (!Util.adaPilihan(txtIdPemupukan.getText())) {
            return;
        }
        try {
            int id = Util.angka(txtIdPemupukan.getText(), "ID Pemupukan");
            Integer idTanaman = Util.idCombo(cmTanaman);
            if (idTanaman == null) {
                throw new IllegalArgumentException("Tanaman belum dipilih.");
            }
            Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
            Integer idLahan = Util.idCombo(cmLahan);
            Integer idPetani = Util.idCombo(cmPetani);
            java.sql.Date tgl = Util.tanggal(tanggalPemupukan.getDate());
            java.sql.Time wkt = Util.waktu(txtWaktu.getText());
            if (Util.eksekusi("UPDATE jadwal_pemupukan SET id_petani = ?, id_lahan = ?, id_tanaman = ?, tanggal = ?, waktu = ? WHERE id_pemupukan = ?",
                    idPetani, idLahan, idTanaman, tgl, wkt, id)) {
                Util.pesan("Data berhasil diubah.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }
 
    private void hapus() {
        if (!Util.adaPilihan(txtIdPemupukan.getText())) {
            return;
        }
        if (Util.konfirmasi("Hapus jadwal pemupukan ini?")
                && Util.eksekusi("DELETE FROM jadwal_pemupukan WHERE id_pemupukan = ?", Integer.parseInt(txtIdPemupukan.getText().trim()))) {
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

        lblJadwalPemupukan = new javax.swing.JLabel();
        lblIdPemupukan = new javax.swing.JLabel();
        txtIdPemupukan = new javax.swing.JTextField();
        lblIdPetani = new javax.swing.JLabel();
        lblIdLahan = new javax.swing.JLabel();
        lblIdTanaman = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        lblWaktu = new javax.swing.JLabel();
        txtWaktu = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tanggalPemupukan = new com.toedter.calendar.JDateChooser();
        cmPetani = new javax.swing.JComboBox<>();
        cmLahan = new javax.swing.JComboBox<>();
        cmTanaman = new javax.swing.JComboBox<>();

        lblJadwalPemupukan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblJadwalPemupukan.setText("Jadwal Pemupukan");

        lblIdPemupukan.setText("ID Pemupukan");

        lblIdPetani.setText("ID Petani");

        lblIdLahan.setText("ID Lahan");

        lblIdTanaman.setText("ID Tanaman");

        lblTanggal.setText("Tanggal");

        lblWaktu.setText("Waktu");

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setText("EDIT");

        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("SIMPAN");

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setText("HAPUS");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Pemupukan", "ID Petani", "ID Lahan", "ID Tanaman", "Tanggal", "Waktu"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        cmPetani.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmLahan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmTanaman.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(btnEdit)
                .addGap(18, 18, 18)
                .addComponent(btnHapus)
                .addGap(190, 190, 190))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmLahan, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtIdPemupukan)
                            .addComponent(lblJadwalPemupukan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIdPemupukan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIdLahan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTanggal, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tanggalPemupukan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblWaktu)
                            .addComponent(lblIdPetani)
                            .addComponent(lblIdTanaman)
                            .addComponent(txtWaktu)
                            .addComponent(cmPetani, 0, 299, Short.MAX_VALUE)
                            .addComponent(cmTanaman, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJadwalPemupukan)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdPemupukan)
                    .addComponent(lblIdPetani))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdPemupukan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmPetani, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdLahan)
                    .addComponent(lblIdTanaman))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmLahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmTanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggal)
                    .addComponent(lblWaktu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggalPemupukan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnSimpan)
                    .addComponent(btnHapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmLahan;
    private javax.swing.JComboBox<String> cmPetani;
    private javax.swing.JComboBox<String> cmTanaman;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblIdLahan;
    private javax.swing.JLabel lblIdPemupukan;
    private javax.swing.JLabel lblIdPetani;
    private javax.swing.JLabel lblIdTanaman;
    private javax.swing.JLabel lblJadwalPemupukan;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblWaktu;
    private com.toedter.calendar.JDateChooser tanggalPemupukan;
    private javax.swing.JTextField txtIdPemupukan;
    private javax.swing.JTextField txtWaktu;
    // End of variables declaration//GEN-END:variables
}
