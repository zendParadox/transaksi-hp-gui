/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaksi.hp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author rafli
 */
public class halamanUser extends javax.swing.JFrame {

    /**
     * Creates new form halamanUser
     */
    private DefaultTableModel model;
    
    public halamanUser() {
        initComponents();
        
//        btnProses.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                btnProsesActionPerformed(e);
//            }
//        });
        
        // Menetapkan model untuk jTable1
        model = new DefaultTableModel();
        jTable1.setModel(model);

        // Menambahkan kolom ke model
        model.addColumn("ID HP");
        model.addColumn("Merk HP");
        model.addColumn("Memori");
        model.addColumn("RAM");
        model.addColumn("Harga Pabrik");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");

        // Memuat data dari database ke jTable1
        loadData();
        
        
    }
    
    private void clearForm() {
        txtNamaCustomer.setText("");
        txtMerkHp.setText("");
        txtRam.setText("");
        txtMemori.setText("");
        txtHarga.setText("");
        txtBayar.setText("");
        txtNoTransaksi.setText("");
    }
    
        private void simpanTransaksi(String idCustomer, String totalHarga) {
        try {
            Connection connection = koneksi.getKoneksi();
            String sql = "INSERT INTO transaksi (username_user, total_harga) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, idCustomer);
            preparedStatement.setString(2, totalHarga);
            preparedStatement.executeUpdate();
            
            // Mendapatkan ID transaksi yang baru saja disimpan
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idTransaksi = generatedKeys.getInt(1);
                
                // Menetapkan ID transaksi ke txtNoTransaksi
                txtNoTransaksi.setText(String.valueOf(idTransaksi));
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Simpan transaksi error");
        }
    }

    private void simpanRincianTransaksi() {
        try {
            Connection connection = koneksi.getKoneksi();
            int baris = jTable1.getRowCount();

            for (int i = 0; i < baris; i++) {
                String sql = "INSERT INTO transaksi_rinci (id_transaksi, id_hp, jumlah_barang, harga) VALUES(?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, txtNoTransaksi.getText()); // Gunakan ID transaksi yang baru saja disimpan
                preparedStatement.setString(2, jTable1.getValueAt(i, 0).toString());
                preparedStatement.setString(3, jTable1.getValueAt(i, 3).toString());
                preparedStatement.setString(4, jTable1.getValueAt(i, 4).toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Simpan transaksi_rinci error");
        }
    }
    
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();
            
            String sql = "SELECT * FROM barang";
            ResultSet r = s.executeQuery(sql);
            
            while (r.next()) {
                Object[] o = new Object[7];
                o [0] = r.getString("id_hp");
                o [1] = r.getString("merk_hp");
                o [2] = r.getString("memori");
                o [3] = r.getString("ram");
                o [4] = r.getString("harga_pabrik");
                o [5] = r.getString("harga_jual");
                o [6] = r.getString("stok");
                
                model.addRow(o);
            }
            r.close();
            s.close();
        } catch (Exception e) {
            System.out.println("terjadi kesalahan");
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

        txtTanggal = new javax.swing.JTextField();
        txtNamaCustomer = new javax.swing.JTextField();
        txtMerkHp = new javax.swing.JTextField();
        txtRam = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        txtMemori = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtBayar = new javax.swing.JTextField();
        txtNoTransaksi = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtIDCustomer = new javax.swing.JTextField();
        btnProses = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTanggal.setBorder(null);
        txtTanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalActionPerformed(evt);
            }
        });
        getContentPane().add(txtTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 200, 40));

        txtNamaCustomer.setBorder(null);
        getContentPane().add(txtNamaCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 400, 40));

        txtMerkHp.setBorder(null);
        getContentPane().add(txtMerkHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 568, 270, 40));

        txtRam.setBorder(null);
        getContentPane().add(txtRam, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 648, 270, 40));

        txtKembalian.setToolTipText("");
        txtKembalian.setBorder(null);
        getContentPane().add(txtKembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 825, 270, 40));

        txtMemori.setBorder(null);
        getContentPane().add(txtMemori, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 568, 270, 40));

        txtHarga.setBorder(null);
        getContentPane().add(txtHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 648, 270, 40));

        txtBayar.setBorder(null);
        getContentPane().add(txtBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 825, 270, 40));

        txtNoTransaksi.setText("nomor transaksi");
        getContentPane().add(txtNoTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 150, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 880, 260));

        txtIDCustomer.setText("id customer");
        getContentPane().add(txtIDCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 100, -1));

        btnProses.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnProses.setText("Proses");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });
        getContentPane().add(btnProses, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 750, 120, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/transaksi/hp/gambar/halamanUser.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 900));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        // Mendapatkan indeks baris yang dipilih
        int selectedRow = jTable1.getSelectedRow();

        // Memeriksa apakah baris dipilih
        if (selectedRow != -1) {
            // Mendapatkan nilai dari kolom-kolom yang dipilih
            String merk_hp = jTable1.getValueAt(selectedRow, 1).toString();
            String memori = jTable1.getValueAt(selectedRow, 2).toString();
            String ram = jTable1.getValueAt(selectedRow, 3).toString();
            String harga_jual = jTable1.getValueAt(selectedRow, 5).toString();

            // Menetapkan nilai ke komponen GUI
            txtMerkHp.setText(merk_hp);
            txtHarga.setText(harga_jual);

            txtRam.setText(ram);
            txtMemori.setText(memori);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        // TODO add your handling code here:
//        cariData();
    }//GEN-LAST:event_jTable1KeyTyped

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        // TODO add your handling code here:
        try {
            // Mendapatkan nilai dari txtBayar dan txtIDCustomer
            String totalHarga = txtBayar.getText();
            String idCustomer = txtIDCustomer.getText();

            // Menyimpan transaksi ke tabel transaksi
            simpanTransaksi(idCustomer, totalHarga);

            // Menyimpan rincian transaksi ke tabel transaksi_rinci
            simpanRincianTransaksi();

            // Menampilkan pesan sukses
            JOptionPane.showMessageDialog(this, "Transaksi Berhasil", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Memuat kembali data ke jTable1 setelah transaksi
            loadData();

            // Clear dan reset form atau lakukan operasi lainnya
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan nilai numerik yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(halamanUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(halamanUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(halamanUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(halamanUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new halamanUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProses;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIDCustomer;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtMemori;
    private javax.swing.JTextField txtMerkHp;
    private javax.swing.JTextField txtNamaCustomer;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JTextField txtRam;
    private javax.swing.JTextField txtTanggal;
    // End of variables declaration//GEN-END:variables
}
