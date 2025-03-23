/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kopr.GUI;

import DatabaseConnector.Mysql;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GM.Genius
 */
public class HomeExtend_Laporan extends javax.swing.JFrame {

    /**
     * Creates new form Gods;
     */
    Mysql maria = new Mysql();

    public HomeExtend_Laporan() {
        initComponents();
        displayTableOnlyMemberData(logPemasukan, "pemasukan_log");
        displayTableOnlyMemberData(logPengeluaran, "pengeluaran_log");
        displayTableOnlyMemberData(HistoryRecord, "data_log");
        showMemberBerhutangTNA(tableShowTerlambatBayarQQ, "select * from V_penghutang_terlambat_NA", 8);
        showMemberBerhutangTNA(tabelShowPenghutang, "select * from V_penghutang_na", 6);
        showMemberBerhutangTNA(tableShowHutangKeluar, "select * from v_penghutang_noa_member_out", 5);
    }
    
    public void changeCloseFunction () {
        
    }

    public void displayTableOnlyMemberData(JTable TabelVar, String tableName) {
        /*
        disarankan membuat fungsi ini dynamic pada
        class Crud agar bisa digunakan sebagai config atau func
         */

        int que;
        String query = "SELECT * FROM " + tableName + " GROUP BY tanggal DESC";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
            que = rose.getColumnCount();

            DefaultTableModel dft = (DefaultTableModel) TabelVar.getModel();
            dft.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int c = 1; c <= que; c++) {
                    v2.add(rs.getString(2));
                    v2.add(rs.getString(3));
                    v2.add(rs.getString(4));
                    v2.add(rs.getString(5));
                }
                dft.addRow(v2);
            }
        } catch (SQLException ex) {
            System.out.println("tidak dikatakan error : " + ex.getSQLState());
            if ("S1009".equals(ex.getSQLState())) {
                try {
                    Connection conn = maria.connect();
                    PreparedStatement pst = conn.prepareStatement(query);

                    ResultSet rs = pst.executeQuery();
                    ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
                    que = rose.getColumnCount();

                    DefaultTableModel dft = (DefaultTableModel) TabelVar.getModel();
                    dft.setRowCount(0);
                    while (rs.next()) {
                        Vector v2 = new Vector();
                        for (int c = 1; c <= que; c++) {
                            v2.add(rs.getString(2));
                            v2.add(rs.getString(3));
                            v2.add(rs.getString(4));
                        }
                        dft.addRow(v2);
                    }
                } catch (SQLException e) {
                }
            }
        }
    }
    
    public void showMemberBerhutangTNA(JTable tableVar, String query, int columnL) {
        int que;
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
            que = rose.getColumnCount();

            DefaultTableModel dft = (DefaultTableModel) tableVar.getModel();
            dft.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int c = 1; c <= que; c++) {
                    int incre = 1;
                    while (columnL >= incre) {
                    v2.add(rs.getString(incre));
                    incre++;
                    }
                }
                dft.addRow(v2);
            }
        } catch (SQLException e) {
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

        Laporan = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        HistoryRecord = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        logPengeluaran = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        logPemasukan = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableShowHutangKeluar = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelShowPenghutang = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        tableShowTerlambatBayarQQ = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 153));
        setResizable(false);

        Laporan.setBackground(new java.awt.Color(204, 204, 204));
        Laporan.setLayout(null);

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 102, 102));
        jLabel48.setText("Laporan");
        Laporan.add(jLabel48);
        jLabel48.setBounds(430, 20, 239, 35);

        jLabel50.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 147, 147));
        jLabel50.setText("all pinjaman keluar");
        Laporan.add(jLabel50);
        jLabel50.setBounds(590, 430, 140, 16);

        jLabel51.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 147, 147));
        jLabel51.setText("PENGELUARAN");
        Laporan.add(jLabel51);
        jLabel51.setBounds(50, 260, 85, 16);

        jLabel52.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 147, 147));
        jLabel52.setText("HISTORY SYSTEM");
        Laporan.add(jLabel52);
        jLabel52.setBounds(50, 430, 100, 16);

        HistoryRecord.setBackground(new java.awt.Color(0, 153, 153));
        HistoryRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "catatan", "type", "tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(HistoryRecord);
        if (HistoryRecord.getColumnModel().getColumnCount() > 0) {
            HistoryRecord.getColumnModel().getColumn(0).setPreferredWidth(170);
            HistoryRecord.getColumnModel().getColumn(1).setPreferredWidth(60);
        }

        Laporan.add(jScrollPane9);
        jScrollPane9.setBounds(40, 450, 350, 127);

        logPengeluaran.setBackground(new java.awt.Color(0, 153, 153));
        logPengeluaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "catatan", "nominal", "tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(logPengeluaran);
        if (logPengeluaran.getColumnModel().getColumnCount() > 0) {
            logPengeluaran.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        Laporan.add(jScrollPane10);
        jScrollPane10.setBounds(40, 280, 350, 127);

        logPemasukan.setBackground(new java.awt.Color(0, 153, 153));
        logPemasukan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "catatan", "nominal", "tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(logPemasukan);
        if (logPemasukan.getColumnModel().getColumnCount() > 0) {
            logPemasukan.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        Laporan.add(jScrollPane5);
        jScrollPane5.setBounds(40, 120, 350, 120);

        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Laporan.add(jButton1);
        jButton1.setBounds(450, 120, 90, 27);

        jLabel53.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 147, 147));
        jLabel53.setText("PEMASUKAN");
        Laporan.add(jLabel53);
        jLabel53.setBounds(50, 100, 87, 16);

        jLabel54.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 147, 147));
        jLabel54.setText("all pernah terlambat");
        Laporan.add(jLabel54);
        jLabel54.setBounds(600, 100, 140, 16);

        jLabel55.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 147, 147));
        jLabel55.setText("all pinjaman non terlambat");
        Laporan.add(jLabel55);
        jLabel55.setBounds(590, 260, 140, 16);

        tableShowHutangKeluar.setBackground(new java.awt.Color(0, 153, 153));
        tableShowHutangKeluar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "nama", "hutang", "tanggal pinjam", "bunga", "keterlambatan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tableShowHutangKeluar);

        Laporan.add(jScrollPane8);
        jScrollPane8.setBounds(580, 460, 550, 120);

        tabelShowPenghutang.setBackground(new java.awt.Color(0, 153, 153));
        tabelShowPenghutang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "nama", "hutang", "bunga", "jangka_waktu_bayar", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tabelShowPenghutang);
        if (tabelShowPenghutang.getColumnModel().getColumnCount() > 0) {
            tabelShowPenghutang.getColumnModel().getColumn(0).setPreferredWidth(35);
            tabelShowPenghutang.getColumnModel().getColumn(1).setPreferredWidth(100);
            tabelShowPenghutang.getColumnModel().getColumn(2).setPreferredWidth(120);
            tabelShowPenghutang.getColumnModel().getColumn(3).setPreferredWidth(60);
            tabelShowPenghutang.getColumnModel().getColumn(4).setPreferredWidth(170);
        }

        Laporan.add(jScrollPane4);
        jScrollPane4.setBounds(580, 290, 550, 120);

        tableShowTerlambatBayarQQ.setBackground(new java.awt.Color(0, 153, 153));
        tableShowTerlambatBayarQQ.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "nama", "hutang", "batas_awal", "batas_baru", "bunga", "terlambat", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(tableShowTerlambatBayarQQ);
        if (tableShowTerlambatBayarQQ.getColumnModel().getColumnCount() > 0) {
            tableShowTerlambatBayarQQ.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        Laporan.add(jScrollPane11);
        jScrollPane11.setBounds(580, 120, 550, 120);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Laporan, javax.swing.GroupLayout.PREFERRED_SIZE, 1159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Laporan, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        displayTableOnlyMemberData(logPemasukan, "pemasukan_log");
        displayTableOnlyMemberData(logPengeluaran, "pengeluaran_log");
        displayTableOnlyMemberData(HistoryRecord, "data_log");
        showMemberBerhutangTNA(tableShowTerlambatBayarQQ, "select * from V_penghutang_terlambat_NA", 8);
        showMemberBerhutangTNA(tabelShowPenghutang, "select * from V_penghutang_na", 6);
        showMemberBerhutangTNA(tableShowHutangKeluar, "select * from v_penghutang_noa_member_out", 5);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(HomeExtend_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeExtend_Laporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable HistoryRecord;
    private javax.swing.JPanel Laporan;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable logPemasukan;
    private javax.swing.JTable logPengeluaran;
    private javax.swing.JTable tabelShowPenghutang;
    private javax.swing.JTable tableShowHutangKeluar;
    private javax.swing.JTable tableShowTerlambatBayarQQ;
    // End of variables declaration//GEN-END:variables
}
