/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaksi.hp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.rmi.activation.Activatable.register;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author rafli
 */
public class halamanAdmin extends javax.swing.JFrame {

    /**
     * Creates new form halamanAdmin
     */
    koneksi koneksi = new koneksi();
    private DefaultTableModel model;
    
    private void autonumber(){
        try {
            Connection c = koneksi.getKoneksi();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM barang ORDER BY id_hp DESC";
            ResultSet r = s.executeQuery(sql);
            if (r.next()) {
                String NoFaktur = r.getString("id_hp").substring(2);
                String BR = "" +(Integer.parseInt(NoFaktur)+1);
                String Nol = "";
                
                if (BR.length()==1) 
                    {Nol = "00";}
                else if(BR.length()==2)
                    {Nol = "0";}
                else if(BR.length()==3)
                    {Nol = "";}
                
                txtIdHp.setText("BR" + Nol + BR);  
            }else{
                txtIdHp.setText("BR001");
            }
            r.close();
            s.close();
        } catch (Exception e) {
            System.out.println("autonumber error");
        }
    }
    
    public void clear(){
        txtCariData.setText("");
        txtIdHp.setText("");
        txtMerkHp.setText("");
        txtStok.setText("");
        txtHargaPabrik.setText("");
        txtHargaJual.setText("");
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
    
    public void cariData(){
        DefaultTableModel tabel = new DefaultTableModel();
        
        tabel.addColumn("id_hp");
        tabel.addColumn("merk_hp");
        tabel.addColumn("memori");
        tabel.addColumn("ram");
        tabel.addColumn("harga_pabrik");
        tabel.addColumn("harga_jual");
        tabel.addColumn("stok");
        
        try {
            Connection c = koneksi.getKoneksi();
            String sql = "Select * from barang where id_hp like '%" + txtCariData.getText() + "%'" +
                    "or merk_hp like '%" + txtCariData.getText() + "%'";
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {                
                tabel.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                });
            }
            jTable1.setModel(tabel);
            loadData();
        } catch (Exception e) {
            System.out.println("Cari Data Error");
        }finally{
        }
    }
    
    public halamanAdmin() {
        initComponents();
        
        this.setLocationRelativeTo(null);
        
        model = new DefaultTableModel();
        
        jTable1.setModel(model);
        
        model.addColumn("id_hp");
        model.addColumn("merk_hp");
        model.addColumn("memori");
        model.addColumn("ram");
        model.addColumn("harga_pabrik");
        model.addColumn("harga_jual");
        model.addColumn("stok");
        
        loadData();
        autonumber();
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    public void excel() throws FileNotFoundException, IOException{
        try{
        Class.forName("com.mysql.jdbc.Driver");
        com.mysql.jdbc.Connection koneksi = (com.mysql.jdbc.Connection)
        DriverManager.getConnection("jdbc:mysql://localhost:3306/db-deri","root","");;
        com.mysql.jdbc.Statement statement = (com.mysql.jdbc.Statement)
        koneksi.createStatement();
        FileOutputStream fileOut;
        // Hasil Export
        fileOut = new FileOutputStream("C:/Users/rafli/Documents/Tugas Utama/Tugas Utama Deri.xls");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("Tugas Utama Deri");
        // Nama Field
        Row row1 = worksheet.createRow((short)0);
        row1.createCell(0).setCellValue("id_hp");
        row1.createCell(1).setCellValue("merk_hp"); 
        row1.createCell(2).setCellValue("memori");
        row1.createCell(3).setCellValue("ram");
        row1.createCell(4).setCellValue("harga_pabrik");
        row1.createCell(5).setCellValue("harga_jual");
        row1.createCell(6).setCellValue("stok");
        Row row2 ;
        ResultSet rs = statement.executeQuery("select* from barang");
        while(rs.next()){
        int a = rs.getRow();
        row2 = worksheet.createRow((short)a);
        // Sesuaikan dengan Jumlah Field
        row2.createCell(0).setCellValue(rs.getString(1));
        row2.createCell(1).setCellValue(rs.getString(2));
        row2.createCell(2).setCellValue(rs.getString(3));
        row2.createCell(3).setCellValue(rs.getString(4));
        row2.createCell(4).setCellValue(rs.getString(5));
        row2.createCell(5).setCellValue(rs.getString(6));
        row2.createCell(6).setCellValue(rs.getString(7));

        }
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        rs.close();
        statement.close();
        koneksi.close();
        JOptionPane.showMessageDialog(this,"Save to Excel Success !!");
        }catch(ClassNotFoundException e){
        System.out.println(e);
        }catch(SQLException ex){
        System.out.println(ex);
        }catch(IOException ioe){
        System.out.println(ioe);
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

        txtCariData = new javax.swing.JTextField();
        cbJumlahRam = new javax.swing.JComboBox<>();
        cbJumlahMemori = new javax.swing.JComboBox<>();
        txtMerkHp = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txtStok = new javax.swing.JTextField();
        txtHargaPabrik = new javax.swing.JTextField();
        txtHargaJual = new javax.swing.JTextField();
        btnHapus = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        txtIdHp = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCariData.setBorder(null);
        txtCariData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariDataActionPerformed(evt);
            }
        });
        getContentPane().add(txtCariData, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, 410, 40));

        cbJumlahRam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "4", "8", "16" }));
        getContentPane().add(cbJumlahRam, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 340, 430, 40));

        cbJumlahMemori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "64", "128", "256", "512" }));
        getContentPane().add(cbJumlahMemori, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 280, 430, 40));

        txtMerkHp.setBorder(null);
        getContentPane().add(txtMerkHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 218, 410, 40));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jButton1.setText("Print");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(834, 851, 100, 40));

        txtStok.setBorder(null);
        getContentPane().add(txtStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 410, 410, 40));

        txtHargaPabrik.setBorder(null);
        getContentPane().add(txtHargaPabrik, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 473, 410, 40));

        txtHargaJual.setBorder(null);
        getContentPane().add(txtHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 537, 410, 40));

        btnHapus.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 851, 100, 40));

        btnSimpan.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(502, 851, 100, 40));

        txtIdHp.setBorder(null);
        getContentPane().add(txtIdHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 153, 410, 40));

        btnEdit.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(613, 851, 100, 40));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 600, 840, 240));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/transaksi/hp/gambar/halamanAdmin.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 900));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_jTable1KeyTyped

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        // TODO add your handling code here:
        int i = jTable1.getSelectedRow();
    if (i == -1) {
        return;
    }
    String id = txtIdHp.getText();
    String merk_hp = txtMerkHp.getText();
    String memori = (String) cbJumlahMemori.getSelectedItem();
    String ram = (String) cbJumlahRam.getSelectedItem();
    String stok = txtStok.getText();
    String hargaPabrik = txtHargaPabrik.getText();
    String hargaJual = txtHargaJual.getText();

    try {
        Connection c = koneksi.getKoneksi();
        String sql = "UPDATE barang SET merk_hp = ?, memori = ?, ram = ?, stok = ?, harga_pabrik = ?, harga_jual = ? WHERE id_hp = ?";
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, merk_hp);
        p.setString(2, memori);
        p.setString(3, ram);
        p.setString(4, stok);
        p.setString(5, hargaPabrik);
        p.setString(6, hargaJual);
        p.setString(7, id);

        p.executeUpdate();
        p.close();
        JOptionPane.showMessageDialog(null, "Data Terubah");
        btnSimpan.setEnabled(true);
//        btnEdit.setEnabled(false);
//        btnHapus.setEnabled(false);
        clear();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Update error: " + e.getMessage());
    } finally {
        loadData();
        autonumber();
    }
    }//GEN-LAST:event_btnEditMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        // Mendapatkan indeks baris yang dipilih
        int selectedRow = jTable1.getSelectedRow();

        // Memeriksa apakah baris dipilih
        if (selectedRow != -1) {
            // Mendapatkan nilai dari kolom-kolom yang dipilih
            String id_hp = jTable1.getValueAt(selectedRow, 0).toString();
            String merk_hp = jTable1.getValueAt(selectedRow, 1).toString();
            String memori = jTable1.getValueAt(selectedRow, 2).toString();
            String ram = jTable1.getValueAt(selectedRow, 3).toString();
            String harga_pabrik = jTable1.getValueAt(selectedRow, 4).toString();
            String harga_jual = jTable1.getValueAt(selectedRow, 5).toString();
            String stok = jTable1.getValueAt(selectedRow, 6).toString();

            // Menetapkan nilai ke komponen GUI
            txtCariData.setText(id_hp);
            txtMerkHp.setText(merk_hp);
            txtStok.setText(stok);
            txtHargaPabrik.setText(harga_pabrik);
            txtHargaJual.setText(harga_jual);
            txtIdHp.setText(id_hp);

            // Menetapkan nilai ke JComboBox
            cbJumlahRam.setSelectedItem(ram);
            cbJumlahMemori.setSelectedItem(memori);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
        // TODO add your handling code here:
        String id_hp = txtIdHp.getText();
        String merk_hp = txtMerkHp.getText();
        String memori = (String)cbJumlahMemori.getSelectedItem();
        String ram = (String)cbJumlahRam.getSelectedItem();
        String harga_pabrik = txtHargaPabrik.getText();
        String harga_jual = txtHargaJual.getText();
        String stok = txtStok.getText();
        
        
        try {
            Connection c = koneksi.getKoneksi();
            String sql = "INSERT INTO barang VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, id_hp);
            p.setString(2, merk_hp);
            p.setString(3, memori);
            p.setString(4, ram);
            p.setString(5, harga_pabrik);
            p.setString(6, harga_jual);
            p.setString(7, stok);
            p.executeUpdate();
            p.close();
            JOptionPane.showMessageDialog(null, "Data Tersimpan");
            loadData();
        } catch(NumberFormatException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "ID HP harus berupa angka");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Terjadi Kesalahan");
        }finally{
            autonumber();
            clear();
        }
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
        // TODO add your handling code here:
        int i = jTable1.getSelectedRow();
        if (i == -1) {
            return;
        }
        
        String id = (String) model.getValueAt(i, 0);
        
        int pernyataan = JOptionPane.showConfirmDialog(null, "Yakin Data Akan Dihapus","Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pernyataan== JOptionPane.OK_OPTION) {
            try {
                Connection c = koneksi.getKoneksi();
                String sql = "DELETE FROM barang WHERE id_hp = ?";
                PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, id);
                p.executeUpdate();
                p.close();
                JOptionPane.showMessageDialog(null, "Data Terhapus");
            } catch (Exception e) {
                System.out.println("Terjadi Kesalahan");
            }finally{
                btnSimpan.setEnabled(true);
//                btnEdit.setEnabled(false);
//                btnHapus.setEnabled(false);
                loadData();
                autonumber();
                clear();
            }
        }
        if (pernyataan== JOptionPane.CANCEL_OPTION) {
            
        }
    }//GEN-LAST:event_btnHapusMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        try {
            excel();
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void txtCariDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariDataActionPerformed

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
            java.util.logging.Logger.getLogger(halamanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(halamanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(halamanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(halamanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new halamanAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbJumlahMemori;
    private javax.swing.JComboBox<String> cbJumlahRam;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtCariData;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtHargaPabrik;
    private javax.swing.JTextField txtIdHp;
    private javax.swing.JTextField txtMerkHp;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
