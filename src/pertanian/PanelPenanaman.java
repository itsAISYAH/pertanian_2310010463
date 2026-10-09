/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pertanian;

/**
 *
 * @author Asus
 */
public class PanelPenanaman extends javax.swing.JPanel {

    /**
     * Creates new form PanelPenanaman
     */
    public PanelPenanaman() {
        initComponents();
        siapkan();
    }
    
        private void siapkan() {
        txtNoPenanaman.setEditable(false);
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
        tabelPenanaman.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiForm();
            }
        });
        muatData();
    }

    private void muatData() {
        Util.isiTabel(tabelPenanaman, "SELECT no_penanaman, id_petani, id_lahan, id_tanaman, jumlah_tanam, tanggal_penanaman FROM penanaman ORDER BY no_penanaman");
    }

    private void bersihkan() {
        txtNoPenanaman.setText("");
        Util.pilihCombo(cmTanaman, "");
        Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
        txtJumlahTanam.setText("");
        tanggalPenanaman.setDate(null);
        tabelPenanaman.clearSelection();
    }

    private void isiForm() {
        int b = tabelPenanaman.getSelectedRow();
        if (b < 0) {
            return;
        }
        txtNoPenanaman.setText(Util.ambil(tabelPenanaman, b, 0));
        Util.pilihCombo(cmTanaman, Util.ambil(tabelPenanaman, b, 3));
        Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
        txtJumlahTanam.setText(Util.ambil(tabelPenanaman, b, 4));
        tanggalPenanaman.setDate(Util.keTanggal(Util.ambil(tabelPenanaman, b, 5)));
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
            int jumlah = Util.angka(txtJumlahTanam.getText(), "Jumlah tanam");
            java.sql.Date tgl = Util.tanggal(tanggalPenanaman.getDate());
            if (Util.eksekusi("INSERT INTO penanaman (id_petani, id_lahan, id_tanaman, jumlah_tanam, tanggal_penanaman) VALUES (?, ?, ?, ?, ?)",
                    idPetani, idLahan, idTanaman, jumlah, tgl)) {
                Util.pesan("Data berhasil disimpan.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }

    private void ubah() {
        if (!Util.adaPilihan(txtNoPenanaman.getText())) {
            return;
        }
        try {
            int no = Util.angka(txtNoPenanaman.getText(), "No Penanaman");
            Integer idTanaman = Util.idCombo(cmTanaman);
            if (idTanaman == null) {
                throw new IllegalArgumentException("Tanaman belum dipilih.");
            }
            Util.sinkronTanaman(cmTanaman, cmLahan, cmPetani);
            Integer idLahan = Util.idCombo(cmLahan);
            Integer idPetani = Util.idCombo(cmPetani);
            int jumlah = Util.angka(txtJumlahTanam.getText(), "Jumlah tanam");
            java.sql.Date tgl = Util.tanggal(tanggalPenanaman.getDate());
            if (Util.eksekusi("UPDATE penanaman SET id_petani = ?, id_lahan = ?, id_tanaman = ?, jumlah_tanam = ?, tanggal_penanaman = ? WHERE no_penanaman = ?",
                    idPetani, idLahan, idTanaman, jumlah, tgl, no)) {
                Util.pesan("Data berhasil diubah.");
                muatData();
                bersihkan();
            }
        } catch (IllegalArgumentException ex) {
            Util.pesan(ex.getMessage());
        }
    }
 
    private void hapus() {
        if (!Util.adaPilihan(txtNoPenanaman.getText())) {
            return;
        }
        if (Util.konfirmasi("Hapus data penanaman ini?")
                && Util.eksekusi("DELETE FROM penanaman WHERE no_penanaman = ?", Integer.parseInt(txtNoPenanaman.getText().trim()))) {
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

        lblPenanaman = new javax.swing.JLabel();
        lblNoPenanaman = new javax.swing.JLabel();
        txtNoPenanaman = new javax.swing.JTextField();
        lblIdPetani = new javax.swing.JLabel();
        lblIdLahan = new javax.swing.JLabel();
        lblJumlahTanam = new javax.swing.JLabel();
        txtJumlahTanam = new javax.swing.JTextField();
        lblIdTanaman = new javax.swing.JLabel();
        lblTanggalPenanaman = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPenanaman = new javax.swing.JTable();
        cmPetani = new javax.swing.JComboBox<>();
        cmLahan = new javax.swing.JComboBox<>();
        cmTanaman = new javax.swing.JComboBox<>();
        tanggalPenanaman = new com.toedter.calendar.JDateChooser();

        lblPenanaman.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPenanaman.setText("Penanaman");

        lblNoPenanaman.setText("No Penanaman");

        lblIdPetani.setText("ID Petani");

        lblIdLahan.setText("ID Lahan");

        lblJumlahTanam.setText("Jumlah Tanam");

        lblIdTanaman.setText("ID Tanaman");

        lblTanggalPenanaman.setText("Tanggal Penanaman");

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setText("EDIT");

        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("SIMPAN");

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setText("HAPUS");

        tabelPenanaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No Penanaman", "ID Petani", "ID Lahan", "ID Tanaman", "Jumlah Tanam", "Tanggal Penanaman"
            }
        ));
        jScrollPane1.setViewportView(tabelPenanaman);

        cmPetani.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmLahan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmTanaman.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(btnSimpan)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtJumlahTanam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                        .addComponent(txtNoPenanaman)
                                        .addComponent(lblPenanaman, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNoPenanaman, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(lblIdLahan)
                                    .addComponent(lblJumlahTanam))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmLahan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(35, 35, 35)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblIdPetani)
                            .addComponent(lblIdTanaman)
                            .addComponent(lblTanggalPenanaman)
                            .addComponent(cmPetani, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmTanaman, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tanggalPenanaman, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPenanaman)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNoPenanaman)
                            .addComponent(lblIdPetani))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNoPenanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(lblJumlahTanam)
                            .addComponent(lblTanggalPenanaman))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJumlahTanam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tanggalPenanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnSimpan)
                    .addComponent(btnHapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
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
    private javax.swing.JLabel lblIdLahan;
    private javax.swing.JLabel lblIdPetani;
    private javax.swing.JLabel lblIdTanaman;
    private javax.swing.JLabel lblJumlahTanam;
    private javax.swing.JLabel lblNoPenanaman;
    private javax.swing.JLabel lblPenanaman;
    private javax.swing.JLabel lblTanggalPenanaman;
    private javax.swing.JTable tabelPenanaman;
    private com.toedter.calendar.JDateChooser tanggalPenanaman;
    private javax.swing.JTextField txtJumlahTanam;
    private javax.swing.JTextField txtNoPenanaman;
    // End of variables declaration//GEN-END:variables
}
