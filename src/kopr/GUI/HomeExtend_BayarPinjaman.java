/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kopr.GUI;

import DatabaseConnector.Mysql;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GM.Genius
 */
public class HomeExtend_BayarPinjaman extends javax.swing.JFrame {

    /**
     * Creates new form bayarPinjaman
     */
    Mysql maria = new Mysql();

    private int idTarget = 0;
    private boolean succesSend;

    private boolean findButtSync = false;

    public HomeExtend_BayarPinjaman() {
        initComponents();
        //this.idTarget = 21; // change to targetID
    }
    public boolean runNConstruct(int targetID) {
        idTarget = targetID;
        setVisible(true);
        setLocationRelativeTo(null);
        advancedOptionQQ.setVisible(false);
        resetFAdvanced.setVisible(false);
        showTableDataSPenghutang(idTarget);
        findDataMemberSPenghutang(idTarget);
        setUsersProfile(targetID);
        return true;
    }
    public void updateFrame () {
        showTableDataSPenghutang(idTarget);
        findDataMemberSPenghutang(idTarget);
        
    }

    
    public void setUsersProfile(int targetID) {
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("select nama_anggota from anggota where id_anggota = ? ");
            pst.setInt(1, targetID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                nameUserH.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("something wrong in bayarPinjaman class err: " + e);
        }
    }

    public void displayFindSyncOrD(int type, int idTargetS) {
        if (type == 0) {
            Connection conn = maria.connect();
            PreparedStatement pst;
            try {
                pst = conn.prepareStatement("select jumlah_pinjaman from pinjaman where id_pinjaman = ? ");
                pst.setInt(1, idTargetS);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    nominalValidation.setText(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(HomeExtend_BayarPinjaman.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type == 1) {
            nominalValidation.setText("");
            nominalBayar.setText("");
        }
    }

    public void showTableDataSPenghutang(int idT) {
        int que;

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareCall("{call transaction_hutang_bayar(?)}");
            pst.setInt(1, idT);

            DefaultTableModel dft = (DefaultTableModel) tableShowHutangSMember.getModel();
            dft.setRowCount(0);

            boolean hasResult = pst.execute();
            int loop = 0;
            while (hasResult) {
                //System.out.println("hei: " + (loop + 1));
                ResultSet rs = pst.getResultSet();
                ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
                que = rose.getColumnCount();
                while (rs.next()) {
                    Vector v2 = new Vector();
                    for (int c = 1; c <= que; c++) {
                        v2.add(rs.getString("id_pinjman"));
                        v2.add(rs.getString("nama"));
                        v2.add("Rp" + rs.getString("hutang"));
                        v2.add("Rp" + rs.getString("tanggalBatasAwal"));
                        v2.add("Rp" + rs.getString("tanggalBatasBaru"));
                        v2.add((Double.parseDouble(rs.getString("bunga")) * 100) + "%");
                        v2.add(rs.getString("keterlambatan"));
                    }
                    dft.addRow(v2);
                }
                hasResult = pst.getMoreResults();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass());
        }
    }

    public void findDataMemberSPenghutang(int idT) {// digunakan untuk mencari data limiter atau total data
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("SELECT ID_pinjaman FROM pinjaman WHERE id_anggota = ? AND status_pinjaman = ? ");
            pst.setInt(1, idT);
            pst.setString(2, "aktif");
            ResultSet rs = pst.executeQuery();
            findIDPinjam.removeAllItems();
            while (rs.next()) {
                findIDPinjam.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
        }
    }

    public boolean showSimpananExtend() {
        this.setVisible(true); // Menampilkan JFrame Login
        this.setLocationRelativeTo(null);

        // Menunggu sampai JFrame ditutup
        while (isVisible()) {
            try {
                //Thread.sleep(100); // Memberi jeda agar aplikasi tidak freeze
                //System.out.println("number: " + c);
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return succesSend;
        }

        return true; // Mengembalikan hasil login
    }

    public boolean handleBayarHutangSYNC() {

        int pinjamanCID = Integer.parseInt((String) findIDPinjam.getSelectedItem());
        int localAID = idTarget;
        String duitBayarNVL = nominalBayar.getText();
        
        int duitBayar = 0;
        
        try {
            if (!duitBayarNVL.isEmpty()) {
                duitBayar = Math.abs(Integer.parseInt(duitBayarNVL));
            } else {
                //if (!selectMethodeT.getSelectedItem().equals("bayar hoetang")) {
                    JOptionPane.showMessageDialog(null, "jangan biarkan input kosong");
                    return false;
                //}
            }
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "masukkan format yang benar");
            System.out.println(numberFormatException);
            return false;
        }
        
        if (JOptionPane.showConfirmDialog(null, "apakah anda yakin semua sudah benar!", "confirmation,", JOptionPane.YES_NO_OPTION) != 0) {
            return false;
        }

        Connection conn;
        CallableStatement pst;
        ResultSet rs;

        int batasBayar = 1;
        int hutangRaw = 0;
        double bunga = 0.0;
        int timeHDay = 0;
        int hutangCalc = 0;
        int kembalian = 0;

        try {
            conn = maria.connect();
            pst = conn.prepareCall("{call get_nominal_and_flower_Spenghutang(?)}");
            pst.setInt(1, pinjamanCID);
            rs = pst.executeQuery();
            while (rs.next()) {
                hutangRaw = Integer.parseInt(rs.getString(1));
                bunga = Double.parseDouble(rs.getString(2));
                timeHDay = Integer.parseInt(rs.getString(3));
            }
        } catch (SQLException e) {
            System.err.println("something wrong line 165 class bayar pinjaman methode handleBayarHutangSYNC error: " + e);
        }

        // kalkulasi kecil
        if (bungaInclude) {
            JOptionPane.showMessageDialog(null, "methode menggunakan bunga \nyang sudah dikalkulasi ke hutang", "advancedOperand", JOptionPane.PLAIN_MESSAGE);
            hutangCalc = (int) Math.round(hutangRaw * (bunga / 365) * timeHDay);
        } else {
            hutangCalc = hutangRaw; //without bunga
        }

        try {
            conn = maria.connect();
            pst = conn.prepareCall("{call handler_bayar_hutang(?,?,?,?,?,?)}");
            
            //i forgot its debuging
            pst.setInt(1, 7);
            pst.setInt(2, 0);
            pst.setInt(3, 0);
            pst.setInt(4, 0);
            
            if (duitBayar >= hutangCalc) {
                if (duitBayar == hutangRaw) {
                    pst.setInt(1, 1);
                    pst.setInt(2, localAID);
                    pst.setInt(3, pinjamanCID);
                    pst.setInt(4, duitBayar);
                } else { // duek e lebih
                    kembalian = duitBayar - hutangCalc;
                    int handlerUangLebih = duitBayar - kembalian;
                    JOptionPane.showMessageDialog(null, "uang anda kelebihan, \nkembalian anda: " + kembalian);
                    pst.setInt(1, 1);
                    pst.setInt(2, localAID);
                    pst.setInt(3, pinjamanCID);
                    pst.setInt(4, handlerUangLebih);
                }
            } else if (duitBayar >= batasBayar) {
                pst.setInt(1, 3);
                pst.setInt(2, localAID);
                pst.setInt(3, pinjamanCID);
                pst.setInt(4, duitBayar);
            } else {
                JOptionPane.showMessageDialog(null, "proses tidak bisa dilakukan");
            }
            
            
            // Query Execute
            pst.registerOutParameter(5, Types.VARCHAR);
            pst.registerOutParameter(6, Types.TINYINT);
            pst.execute();
            
            if (pst.getInt(6) == 1) {
                updateFrame();
            }
            
            System.out.println(pst.getString(5));
            System.out.println(pst.getInt(6));
            
        } catch (SQLException e) {
            System.out.println("gagal eksekusi query");
            System.err.println("something wrong line 213 class bayar pinjaman methode handleBayarHutangSYNC error: " + e);
        } catch (Exception e) {
            System.out.println("s0mething wrong errC: " + e);
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nameUserH = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableShowHutangSMember = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        findIDPinjam = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        nominalBayar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nominalValidation = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        advancedOptionQQ = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        bungaIncludeCB = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        applyCancel = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        resetButtonJ = new javax.swing.JButton();
        confirmButtonJ = new javax.swing.JButton();
        resetFAdvanced = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        nameUserH.setText("undefined");

        jLabel2.setText("icon User");

        jPanel2.setForeground(new java.awt.Color(204, 255, 255));

        tableShowHutangSMember.setBackground(new java.awt.Color(0, 153, 153));
        tableShowHutangSMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "nama", "hutang", "batas_awal", "batas_baru", "bunga", "keterlambatan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tableShowHutangSMember);
        if (tableShowHutangSMember.getColumnModel().getColumnCount() > 0) {
            tableShowHutangSMember.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableShowHutangSMember.getColumnModel().getColumn(2).setPreferredWidth(200);
            tableShowHutangSMember.getColumnModel().getColumn(3).setPreferredWidth(100);
            tableShowHutangSMember.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        findIDPinjam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        findIDPinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findIDPinjamActionPerformed(evt);
            }
        });

        jLabel1.setText("id hutang e");

        jLabel3.setText("nominal uang");

        nominalValidation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nominalValidationActionPerformed(evt);
            }
        });

        jLabel4.setText("check cn");

        advancedOptionQQ.setBackground(new java.awt.Color(0, 153, 153));

        jLabel5.setText("advanced Option");

        bungaIncludeCB.setText("bunga inc");

        jCheckBox2.setText("bunga++ V");

        jCheckBox3.setText("specific Msg");

        jCheckBox4.setText("corrector");

        jCheckBox5.setText("Dsp.kembalian");

        jCheckBox6.setText("coming soon");

        jCheckBox7.setText("coming soon");

        jCheckBox8.setText("coming soon");

        applyCancel.setText("apply");
        applyCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout advancedOptionQQLayout = new javax.swing.GroupLayout(advancedOptionQQ);
        advancedOptionQQ.setLayout(advancedOptionQQLayout);
        advancedOptionQQLayout.setHorizontalGroup(
            advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advancedOptionQQLayout.createSequentialGroup()
                .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(advancedOptionQQLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBox4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bungaIncludeCB, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox5)
                            .addComponent(jCheckBox6)
                            .addComponent(jCheckBox7)
                            .addComponent(jCheckBox8)))
                    .addGroup(advancedOptionQQLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel5))
                    .addGroup(advancedOptionQQLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(applyCancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        advancedOptionQQLayout.setVerticalGroup(
            advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advancedOptionQQLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bungaIncludeCB)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advancedOptionQQLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(applyCancel)
                .addGap(18, 18, 18))
        );

        jButton1.setText("srch");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("advanced");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        resetButtonJ.setText("reset");
        resetButtonJ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetButtonJMouseClicked(evt);
            }
        });
        resetButtonJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonJActionPerformed(evt);
            }
        });

        confirmButtonJ.setText("confirm");
        confirmButtonJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonJActionPerformed(evt);
            }
        });

        resetFAdvanced.setText("reset");
        resetFAdvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFAdvancedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(findIDPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(nominalValidation)
                        .addComponent(nominalBayar, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(resetButtonJ)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(confirmButtonJ))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resetFAdvanced, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addComponent(advancedOptionQQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(advancedOptionQQ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(findIDPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nominalValidation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(resetFAdvanced))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(nominalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetButtonJ)
                            .addComponent(confirmButtonJ))))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(nameUserH))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(nameUserH))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    int displayChc = 1;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (!advancedOptionQQ.isVisible()) {
            advancedOptionQQ.setVisible(true);
            displayChc = 0;
        } else {
            //if (advancedOptionQQ.is)
            advancedOptionQQ.setVisible(false);
            displayChc = 1;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
        // tolong lanjut ke batgian mirip flowing mouse
        try {
            displayFindSyncOrD(0, Integer.parseInt(String.valueOf(findIDPinjam.getSelectedItem())));
        } catch (NumberFormatException e) {
            findIDPinjam.removeAllItems();
            findIDPinjam.addItem("null");
            System.out.println("kemungkinan karena sudah tidak ada hutang error: " + e);
        } catch (Exception e) {
            System.err.println("something wrong in button search error: " + e);
        }
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        findButtSync = true;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        // TODO add your handling code here:
        if (!findButtSync) {
            displayFindSyncOrD(1, 0);
        }
    }//GEN-LAST:event_jButton1MouseExited

    private void resetButtonJMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonJMouseClicked
        // TODO add your handling code here:
        findButtSync = false;
        displayFindSyncOrD(1, 0);
    }//GEN-LAST:event_resetButtonJMouseClicked

    boolean localApplyCancelCondition = false;
    //conditionaAdvanced
    private boolean bungaInclude = false;
    private void applyCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyCancelActionPerformed
        // TODO add your handling code here:
//        if (localApplyCancelCondition) {
//            
//            localApplyCancelCondition = false;
//        } else {
//            
//            localApplyCancelCondition = true;
//        }
        resetFAdvanced.setVisible(true);
        //different methode
        bungaInclude = bungaIncludeCB.isSelected();
        // apply all selected setting
        advancedOptionQQ.setVisible(false);
    }//GEN-LAST:event_applyCancelActionPerformed

    private void confirmButtonJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonJActionPerformed
        // TODO add your handling code here:
        handleBayarHutangSYNC();
    }//GEN-LAST:event_confirmButtonJActionPerformed

    public boolean resetFAdvancedBooleanFC() {
        bungaInclude = false;
        bungaIncludeCB.setSelected(false);
        return true;
    }
    private void resetFAdvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFAdvancedActionPerformed
        // TODO add your handling code here:
        resetFAdvancedBooleanFC();
        resetFAdvanced.setVisible(false);
    }//GEN-LAST:event_resetFAdvancedActionPerformed

    private void resetButtonJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonJActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonJActionPerformed

    private void findIDPinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findIDPinjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_findIDPinjamActionPerformed

    private void nominalValidationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nominalValidationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nominalValidationActionPerformed

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
            java.util.logging.Logger.getLogger(HomeExtend_BayarPinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_BayarPinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_BayarPinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeExtend_BayarPinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeExtend_BayarPinjaman().runNConstruct(45); //change for another debug
//                new BayarPinjaman().setVisible(true);
            }
        });

        //BayarPinjaman byt = new BayarPinjaman();
        //new BayarPinjaman().runNConstruct();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel advancedOptionQQ;
    private javax.swing.JButton applyCancel;
    private javax.swing.JCheckBox bungaIncludeCB;
    private javax.swing.JButton confirmButtonJ;
    private javax.swing.JComboBox<String> findIDPinjam;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel nameUserH;
    private javax.swing.JTextField nominalBayar;
    private javax.swing.JTextField nominalValidation;
    private javax.swing.JButton resetButtonJ;
    private javax.swing.JButton resetFAdvanced;
    private javax.swing.JTable tableShowHutangSMember;
    // End of variables declaration//GEN-END:variables
}
