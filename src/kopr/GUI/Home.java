package kopr.GUI;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import DatabaseConnector.Mysql;
import java.awt.Color;
import java.io.File;
import java.text.ParseException;
import java.util.Properties;
import kopr.defaultAccess.QueryGlobalAccess;
import static kopr.manager.KopRManager.configLoader;

/**
 *
 * @author Kunti Inayati
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form HomeTest
     */
    Mysql maria = new Mysql();
    QueryGlobalAccess trn = new QueryGlobalAccess();

    private boolean globalEditFindState;
    private boolean globalDeleteFindSatate;
    private boolean globalTransactionFindState;

    public Home() {
        initComponents();
//        displayTable();
//        ttlKoperaShow();
//        findDataMemberID();
    }
    public boolean runHome () {
        setVisible(true);
        setLocationRelativeTo(null);
        displayTable();
        ttlKoperaShow();
        findDataMemberID();
        return true;
    }
    
    public void foundAMemberTRN() {
        String theId = IDFinderT.getSelectedItem().toString();

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("SELECT a.nama_anggota, s.saldo_simpanan FROM anggota a left join simpanan s ON a.id_anggota = s.id_anggota where a.id_anggota = ?");
            pst.setString(1, theId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameFoundT.setText(rs.getString(1));
                pendapatanT.setText(rs.getString(2));
            }
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
        }
    }
    
    public void searchNameDashboard () {
        int que;
        
        String nameSpell = seachAsName.getText();
        
        String query = "SELECT * FROM anggota WHERE Nama_Anggota LIKE \""+ nameSpell +"%\"";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
            que = rose.getColumnCount();

            DefaultTableModel dft = (DefaultTableModel) informationMemberD.getModel();
            dft.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int c = 1; c <= que; c++) {
                    v2.add(rs.getString("ID_Anggota"));
                    v2.add(rs.getString("Nama_Anggota"));
                    v2.add(rs.getString("alamat"));
                    v2.add(rs.getString("nomor_telepon"));
                    v2.add(rs.getString("Pekerjaan"));
                    v2.add(rs.getString("Pendapatan"));
                    v2.add(rs.getString("No_Ktp"));
                }
                dft.addRow(v2);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass());
        }
    }

    
    
    
    
    
    
//    public void selectMethodeReg() {
//        IDFinderT.removeAllItems();
//        String[] methode = {"zero", "Add_M", "Edit_M", "Delete_M"};
//        for (String methode1 : methode) {
//            IDFinderT.addItem(methode1);
//        }
//    }
    public void findDataMemberID() {// digunakan untuk mencari data limiter atau total data
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("SELECT ID_anggota FROM simpanan");
            ResultSet rs = pst.executeQuery();
            IDFinderT.removeAllItems();
            while (rs.next()) {
                IDFinderT.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
        }
    }

    public void selectorLGetA(int ActionCh) {
        if (ActionCh == 0) {
            try {
                Connection conn = maria.connect();
                PreparedStatement pst = conn.prepareStatement("SELECT ID_anggota FROM anggota");
                ResultSet rs = pst.executeQuery();
                editAnggotaA.removeAllItems();
                while (rs.next()) {
                    editAnggotaA.addItem(rs.getString(1));
                }
            } catch (SQLException e) {
            }
        } else if (ActionCh == 1) {
            try {
                Connection conn = maria.connect();
                PreparedStatement pst = conn.prepareStatement("SELECT ID_anggota FROM anggota");
                ResultSet rs = pst.executeQuery();
                delAnggotaA.removeAllItems();
                while (rs.next()) {
                    delAnggotaA.addItem(rs.getString(1));
                }
            } catch (SQLException e) {
            }

        }
    }

    public void displayEditClear() {
        EditNama.setText("");
        EditAlamat.setText("");
        EditNoTelp.setText("");
        EditTlahir.setText("");
        EditStatusP.setText("");
        EditPekerjaan.setText("");
        EditPendapatan.setText("");
    }

    public void displayAEdit() {
        String IdChoosen = String.valueOf(editAnggotaA.getSelectedItem());

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM anggota where id_anggota = ?");
            pst.setString(1, IdChoosen);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                EditNama.setText(rs.getString(2));
                EditAlamat.setText(rs.getString(3));
                EditNoTelp.setText(rs.getString(4));
                EditTlahir.setText(rs.getString(6));
                EditStatusP.setText(rs.getString(7));
                EditPekerjaan.setText(rs.getString(8));
                EditPendapatan.setText(rs.getString(9));
            }
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
        }
    }

    public void ttlKoperaShow() {
        String getherTtlAng = null;
        String getherTtlLapor = null;

        PreparedStatement pst;
        ResultSet rs;
        CallableStatement cst;

        try {
            Connection conn = maria.connect();
            pst = conn.prepareStatement("select * from v_jml_total_anggota");
            rs = pst.executeQuery();
            while (rs.next()) {
                getherTtlAng = rs.getString(1);
            }
            totalAnggotaDash.setText(getherTtlAng);

//            cst = conn.prepareCall("{call total_laporan(?)}");
//            cst.registerOutParameter(1, Types.INTEGER);
//            cst.execute();

//            totalLaporanDash.setText(String.valueOf(cst.getInt(1)));
            
            pst = conn.prepareStatement("select * from v_total_laporan_koperasi");
            rs = pst.executeQuery();
            while (rs.next()) {
                totalLaporanDash.setText(rs.getString(1));
            }
            
//            cst = conn.prepareCall("{call total_pemasukan_log(?)}");
//            cst.registerOutParameter(1, Types.INTEGER);
//            cst.execute();

            pst = conn.prepareStatement("select * from v_total_pemasukan_l");
            rs = pst.executeQuery();
            while (rs.next()) {
                totalPemasukanDash.setText(rs.getString(1));
            }

            //totalPemasukanDash.setText(String.valueOf(cst.getInt(1)));

//            cst = conn.prepareCall("{call total_pengeluaran_log(?)}");
//            cst.registerOutParameter(1, Types.INTEGER);
//            cst.execute();

            pst = conn.prepareStatement("select * from v_total_pengeluaran_l");
            rs = pst.executeQuery();
            while (rs.next()) {
                totalPenarikanDash.setText(rs.getString(1));
            }

            conn.close();
            rs.close();
            //totalPenarikanDash.setText(String.valueOf(cst.getInt(1)));

        } catch (Exception e) {}
    }
    
    
    public boolean checkIsNgutang (int id_member) {
        int checker = -1;
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("select check_is_ngutang(?)");
            pst.setInt(1, id_member);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                checker = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("errorbya: " + e);
        }
        
        return (checker == 1);
        
    }
    
    public void getPenghutangTerlambat () {
        int que;

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("select * from v_penghutang_terlambat");

            DefaultTableModel dft = (DefaultTableModel) tableShowTerlambatBayar.getModel();
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
                    v2.add(rs.getString("ID_Anggota"));
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
            System.err.println(e);
        }
    }
    public void getPenghutang () {
        int que;

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("select * from v_penghutang");

            DefaultTableModel dft = (DefaultTableModel) tabelShowPenghutang.getModel();
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
                    v2.add(rs.getString("ID_Anggota"));
                    v2.add(rs.getString("nama"));
                    v2.add("Rp" + rs.getString("hutang"));
                    v2.add((Double.parseDouble(rs.getString("bunga")) * 100) + "%");
                    v2.add(rs.getString("tanggalBatas"));
                }
                dft.addRow(v2);
            }
                hasResult = pst.getMoreResults();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass());
        }
    }
    
    
    

    public void displayTable() {
        /*
        disarankan membuat fungsi ini dynamic pada
        class Crud agar bisa digunakan sebagai config atau func
         */

        int que;
        String query = "SELECT * FROM anggota";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
            que = rose.getColumnCount();

            DefaultTableModel dft = (DefaultTableModel) informationMemberD.getModel();
            dft.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int c = 1; c <= que; c++) {
                    v2.add(rs.getString("ID_Anggota"));
                    v2.add(rs.getString("Nama_Anggota"));
                    v2.add(rs.getString("alamat"));
                    v2.add(rs.getString("nomor_telepon"));
                    v2.add(rs.getString("Pekerjaan"));
                    v2.add(rs.getString("Pendapatan"));
                    v2.add(rs.getString("No_ktp"));
                }
                dft.addRow(v2);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass());
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

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner1 = new javax.swing.JSpinner();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jSlider1 = new javax.swing.JSlider();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel7 = new javax.swing.JPanel();
        jMenuItem1 = new javax.swing.JMenuItem();
        scrollPane1 = new java.awt.ScrollPane();
        jLabel35 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jMenuItem2 = new javax.swing.JMenuItem();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jDialog1 = new javax.swing.JDialog();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        NavBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        CDL = new javax.swing.JPanel();
        Dashboard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        totalLaporanDash = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        totalPenarikanDash = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        totalPemasukanDash = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        informationMemberD = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalAnggotaDash = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        seachAsName = new javax.swing.JTextField();
        Transaksi = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        selectMethodeT = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        saldo = new javax.swing.JLabel();
        TanggalBatasTPL = new javax.swing.JLabel();
        pendapatanT = new javax.swing.JTextField();
        inputTransaction = new javax.swing.JTextField();
        nameFoundT = new javax.swing.JTextField();
        TanggalBatasTP = new javax.swing.JTextField();
        nominalT = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        sendTransaction = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        IDFinderT = new javax.swing.JComboBox<>();
        Anggota = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jMultiC = new javax.swing.JTabbedPane();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        CRD = new javax.swing.JPanel();
        tambahanggotaa = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        AddNama = new javax.swing.JTextField();
        AddSelectorGenre = new javax.swing.JComboBox<>();
        AddAlamat = new javax.swing.JTextField();
        AddNumber = new javax.swing.JTextField();
        AddPekerjaan = new javax.swing.JTextField();
        AddPendapatan = new javax.swing.JTextField();
        AddStatus = new javax.swing.JTextField();
        sendDataAdd = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        AddSimpananType = new javax.swing.JTextField();
        warningSymbolDate = new javax.swing.JLabel();
        warningAddCpenda = new javax.swing.JLabel();
        AddDateTTL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        no_ktp = new javax.swing.JTextField();
        editanggota = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        EditNama = new javax.swing.JTextField();
        findMForEdit = new javax.swing.JButton();
        editGenreSelector = new javax.swing.JComboBox<>();
        EditAlamat = new javax.swing.JTextField();
        EditNoTelp = new javax.swing.JTextField();
        EditPekerjaan = new javax.swing.JTextField();
        EditPendapatan = new javax.swing.JTextField();
        EditStatusP = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        editAnggotaA = new javax.swing.JComboBox<>();
        EditTlahir = new javax.swing.JTextField();
        hapusanggota = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        findDelete = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        DelNama = new javax.swing.JTextField();
        DelTTL = new javax.swing.JTextField();
        DelAlamat = new javax.swing.JTextField();
        confirmationCheckP = new javax.swing.JCheckBox();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        delAnggotaA = new javax.swing.JComboBox<>();
        Laporan = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelShowPenghutang = new javax.swing.JTable();
        jLabel50 = new javax.swing.JLabel();
        advancedLaporan = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableShowTerlambatBayar = new javax.swing.JTable();
        jLabel51 = new javax.swing.JLabel();
        pengaturan = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jCheckBox1.setText("jCheckBox1");

        jFormattedTextField1.setText("jFormattedTextField1");

        jScrollPane1.setViewportView(jEditorPane1);

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel13.setText("Jenis Transaksi");

        jLabel16.setText("jLabel16");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12", "18" }));

        jLabel21.setBackground(new java.awt.Color(0, 102, 102));
        jLabel21.setText("Anggota");

        jLabel22.setText("jLabel22");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenuItem1.setText("jMenuItem1");

        jLabel35.setText("jLabel35");

        jLabel24.setText("jLabel24");

        jTextField15.setText("jTextField15");

        jLabel45.setText("jLabel45");

        jLabel49.setText("jLabel49");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(jTable4);

        jLabel53.setText("Pengaturan");

        jLabel55.setText("jLabel55");

        jMenuItem2.setText("jMenuItem2");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(jList1);

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(null);

        NavBar.setBackground(new java.awt.Color(0, 153, 153));
        NavBar.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Monotype Corsiva", 1, 14)); // NOI18N
        jLabel1.setText("Koperasi Simpan Pinjam");
        NavBar.add(jLabel1);
        jLabel1.setBounds(20, 100, 140, 17);

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-home-30.png"))); // NOI18N
        jButton1.setText("Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        NavBar.add(jButton1);
        jButton1.setBounds(20, 120, 130, 30);

        jButton3.setBackground(new java.awt.Color(0, 153, 153));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/kopKecilnoback.png"))); // NOI18N
        NavBar.add(jButton3);
        jButton3.setBounds(40, 0, 90, 90);

        jButton2.setBackground(new java.awt.Color(0, 153, 153));
        jButton2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-estimate-24.png"))); // NOI18N
        jButton2.setText("Transaksi");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        NavBar.add(jButton2);
        jButton2.setBounds(20, 160, 130, 30);

        jButton4.setBackground(new java.awt.Color(0, 153, 153));
        jButton4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-user-30.png"))); // NOI18N
        jButton4.setText("Anggota");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        NavBar.add(jButton4);
        jButton4.setBounds(20, 200, 130, 30);

        jButton5.setBackground(new java.awt.Color(0, 153, 153));
        jButton5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-paper-24.png"))); // NOI18N
        jButton5.setText("Laporan");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        NavBar.add(jButton5);
        jButton5.setBounds(20, 240, 130, 35);

        jButton6.setBackground(new java.awt.Color(0, 153, 153));
        jButton6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-logout-30.png"))); // NOI18N
        jButton6.setText("Logout");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        NavBar.add(jButton6);
        jButton6.setBounds(20, 300, 130, 30);

        jButton7.setBackground(new java.awt.Color(0, 153, 153));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-setting-24.png"))); // NOI18N
        jButton7.setText("Pengaturan");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        NavBar.add(jButton7);
        jButton7.setBounds(20, 500, 130, 35);

        getContentPane().add(NavBar);
        NavBar.setBounds(0, 0, 180, 590);

        CDL.setLayout(new java.awt.CardLayout());

        Dashboard.setBackground(new java.awt.Color(255, 255, 255));
        Dashboard.setLayout(null);

        jLabel2.setBackground(new java.awt.Color(0, 204, 204));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Dashboard");
        Dashboard.add(jLabel2);
        jLabel2.setBounds(30, 30, 222, 42);

        jPanel2.setBackground(new java.awt.Color(148, 202, 191));

        totalLaporanDash.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 24)); // NOI18N
        totalLaporanDash.setForeground(new java.awt.Color(51, 51, 51));
        totalLaporanDash.setText("-");

        jLabel10.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Total Laporan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalLaporanDash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalLaporanDash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        Dashboard.add(jPanel2);
        jPanel2.setBounds(430, 110, 120, 80);

        jPanel4.setBackground(new java.awt.Color(0, 158, 141));

        totalPenarikanDash.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 24)); // NOI18N
        totalPenarikanDash.setForeground(new java.awt.Color(255, 255, 255));
        totalPenarikanDash.setText("-");

        jLabel4.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Penarikan");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalPenarikanDash, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalPenarikanDash, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        Dashboard.add(jPanel4);
        jPanel4.setBounds(170, 110, 110, 80);

        jPanel5.setBackground(new java.awt.Color(143, 221, 204));

        jLabel8.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Total Pemasukan");

        totalPemasukanDash.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 24)); // NOI18N
        totalPemasukanDash.setForeground(new java.awt.Color(102, 102, 102));
        totalPemasukanDash.setText("-");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalPemasukanDash, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalPemasukanDash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        Dashboard.add(jPanel5);
        jPanel5.setBounds(300, 110, 110, 80);

        informationMemberD.setBackground(new java.awt.Color(119, 209, 209));
        informationMemberD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));
        informationMemberD.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        informationMemberD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "Name", "alamat", "no_telp", "pekerjaan", "pendapatan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        informationMemberD.setGridColor(new java.awt.Color(0, 204, 204));
        jScrollPane5.setViewportView(informationMemberD);

        Dashboard.add(jScrollPane5);
        jScrollPane5.setBounds(30, 280, 530, 230);

        jPanel3.setBackground(new java.awt.Color(0, 133, 119));

        jLabel6.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total Anggota");

        totalAnggotaDash.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 24)); // NOI18N
        totalAnggotaDash.setForeground(new java.awt.Color(255, 255, 255));
        totalAnggotaDash.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalAnggotaDash, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(totalAnggotaDash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Dashboard.add(jPanel3);
        jPanel3.setBounds(30, 110, 120, 80);

        jButton9.setText("search");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        Dashboard.add(jButton9);
        jButton9.setBounds(170, 250, 80, 27);

        seachAsName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seachAsNameActionPerformed(evt);
            }
        });
        Dashboard.add(seachAsName);
        seachAsName.setBounds(40, 250, 120, 26);

        CDL.add(Dashboard, "card2");

        Transaksi.setBackground(new java.awt.Color(255, 255, 255));
        Transaksi.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 102));
        jLabel11.setText("Transaksi");
        Transaksi.add(jLabel11);
        jLabel11.setBounds(32, 16, 199, 44);
        Transaksi.add(jScrollPane2);
        jScrollPane2.setBounds(110, 170, 6, 6);

        jPanel6.setBackground(new java.awt.Color(119, 209, 209));

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel15.setText("Nama Anggota");

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel12.setText("ID Anggota");

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel14.setText("Jenis Transaksi");

        selectMethodeT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simpan", "Pinjam", "bayar hoetang" }));
        selectMethodeT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectMethodeTMouseClicked(evt);
            }
        });
        selectMethodeT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMethodeTActionPerformed(evt);
            }
        });

        jTextField1.setText("Masukkan Data Transaksi");

        jLabel17.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        jLabel17.setText("Nama ");

        saldo.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        saldo.setText("saldo");

        TanggalBatasTPL.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        TanggalBatasTPL.setText("batas Hari");

        pendapatanT.setEditable(false);
        pendapatanT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendapatanTActionPerformed(evt);
            }
        });

        inputTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTransactionActionPerformed(evt);
            }
        });

        nameFoundT.setEditable(false);
        nameFoundT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFoundTActionPerformed(evt);
            }
        });

        TanggalBatasTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalBatasTPActionPerformed(evt);
            }
        });

        nominalT.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        nominalT.setText("Nominal");

        jButton8.setBackground(new java.awt.Color(204, 204, 204));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-search-24_1.png"))); // NOI18N
        jButton8.setText("Find");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        sendTransaction.setBackground(new java.awt.Color(204, 204, 204));
        sendTransaction.setText("Send");
        sendTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendTransactionActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(204, 204, 204));
        jButton10.setText("Reset");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        IDFinderT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        IDFinderT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IDFinderTMouseClicked(evt);
            }
        });
        IDFinderT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDFinderTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField1)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TanggalBatasTPL, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nominalT, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(pendapatanT)
                                .addComponent(selectMethodeT, javax.swing.GroupLayout.Alignment.TRAILING, 0, 142, Short.MAX_VALUE)
                                .addComponent(TanggalBatasTP)
                                .addComponent(inputTransaction))
                            .addComponent(nameFoundT, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(IDFinderT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)))
                .addGap(63, 100, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendTransaction)
                .addGap(49, 49, 49))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectMethodeT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(IDFinderT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(nameFoundT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saldo)
                            .addComponent(pendapatanT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TanggalBatasTPL)
                            .addComponent(TanggalBatasTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nominalT)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(287, 287, 287)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(444, 444, 444)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Transaksi.add(jPanel6);
        jPanel6.setBounds(40, 90, 470, 360);

        CDL.add(Transaksi, "card3");

        Anggota.setBackground(new java.awt.Color(255, 255, 255));
        Anggota.setLayout(null);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 102));
        jLabel23.setText("Anggota");
        Anggota.add(jLabel23);
        jLabel23.setBounds(50, 30, 197, 49);

        jMultiC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMultiCMouseClicked(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(204, 204, 204));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton13.setText("Masukkan Data Anggota Baru");
        jMultiC.addTab("Tambah Anggota", jButton13);

        jButton14.setBackground(new java.awt.Color(204, 204, 204));
        jButton14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton14.setText("Masukkan Data yang ingin diubah");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jMultiC.addTab("Edit Anggota", jButton14);

        jButton15.setBackground(new java.awt.Color(204, 204, 204));
        jButton15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton15.setText("Pilih data yang ingin dihapus");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jMultiC.addTab("Hapus Anggota", jButton15);

        Anggota.add(jMultiC);
        jMultiC.setBounds(60, 90, 440, 60);

        CRD.setLayout(new java.awt.CardLayout());

        tambahanggotaa.setBackground(new java.awt.Color(155, 221, 221));

        jLabel34.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel34.setText("Nama Lengkap       :");

        jLabel36.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel36.setText("Jenis Kelamin          :");

        jLabel37.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel37.setText("Tanggal lahir           :");

        jLabel38.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel38.setText("Alamat                      :");

        jLabel39.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel39.setText("No. Telepon             :");

        jLabel40.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel40.setText("Pekerjaan                 :");

        jLabel41.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel41.setText("Pendapatan             :");

        jLabel42.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel42.setText("Status                        :");

        AddNama.setForeground(new java.awt.Color(0, 0, 0));

        AddSelectorGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan" }));

        AddAlamat.setForeground(new java.awt.Color(0, 0, 0));
        AddAlamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddAlamatActionPerformed(evt);
            }
        });

        AddNumber.setForeground(new java.awt.Color(0, 0, 0));

        AddPekerjaan.setForeground(new java.awt.Color(0, 0, 0));

        AddPendapatan.setForeground(new java.awt.Color(0, 0, 0));
        AddPendapatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddPendapatanActionPerformed(evt);
            }
        });

        AddStatus.setForeground(new java.awt.Color(0, 0, 0));

        sendDataAdd.setBackground(new java.awt.Color(51, 204, 0));
        sendDataAdd.setText("Create");
        sendDataAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendDataAddActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(255, 0, 51));
        jButton12.setText("Reset");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel56.setText("jenis Simpanan                        :");

        AddSimpananType.setForeground(new java.awt.Color(0, 0, 0));
        AddSimpananType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSimpananTypeActionPerformed(evt);
            }
        });

        warningSymbolDate.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        warningSymbolDate.setForeground(new java.awt.Color(155, 221, 221));
        warningSymbolDate.setText("-");

        warningAddCpenda.setBackground(new java.awt.Color(155, 221, 221));
        warningAddCpenda.setForeground(new java.awt.Color(155, 221, 221));
        warningAddCpenda.setText("-");

        AddDateTTL.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("No. KTP : ");

        no_ktp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no_ktpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tambahanggotaaLayout = new javax.swing.GroupLayout(tambahanggotaa);
        tambahanggotaa.setLayout(tambahanggotaaLayout);
        tambahanggotaaLayout.setHorizontalGroup(
            tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahanggotaaLayout.createSequentialGroup()
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tambahanggotaaLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendDataAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tambahanggotaaLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddStatus))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddPendapatan))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddPekerjaan))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddNumber))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddAlamat))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddDateTTL))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddSelectorGenre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AddNama, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(AddSimpananType))
                                    .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(no_ktp)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(warningSymbolDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                                .addComponent(warningAddCpenda)
                                .addGap(0, 93, Short.MAX_VALUE)))))
                .addGap(30, 30, 30))
        );
        tambahanggotaaLayout.setVerticalGroup(
            tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahanggotaaLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addComponent(AddNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(AddSelectorGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(warningSymbolDate)
                    .addComponent(AddDateTTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(AddAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(AddNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(AddPekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AddPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(warningAddCpenda)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(AddStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddSimpananType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(tambahanggotaaLayout.createSequentialGroup()
                        .addComponent(no_ktp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tambahanggotaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12)
                            .addComponent(sendDataAdd))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CRD.add(tambahanggotaa, "card2");

        editanggota.setBackground(new java.awt.Color(155, 221, 221));

        jLabel25.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel25.setText("ID Anggota        : ");

        jLabel26.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel26.setText("Nama Lengkap :");

        jLabel27.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel27.setText("Jenis Kelamin    :");

        jLabel28.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel28.setText("Tanggal Lahir    :");

        jLabel29.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel29.setText("Alamat                :");

        jLabel30.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel30.setText("No. Telepon       :");

        jLabel31.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel31.setText("Pekerjaan           :");

        jLabel32.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel32.setText("Pendapatan       :");

        jLabel33.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel33.setText("Status                  :");

        findMForEdit.setBackground(new java.awt.Color(204, 204, 204));
        findMForEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        findMForEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-search-24_1.png"))); // NOI18N
        findMForEdit.setText("Find ");
        findMForEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                findMForEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                findMForEditMouseExited(evt);
            }
        });
        findMForEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findMForEditActionPerformed(evt);
            }
        });

        editGenreSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan", " " }));

        EditStatusP.setText("menikah / lajang");

        jButton17.setBackground(new java.awt.Color(51, 204, 0));
        jButton17.setText("Simpan");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(255, 0, 51));
        jButton18.setText("clear");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        editAnggotaA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout editanggotaLayout = new javax.swing.GroupLayout(editanggota);
        editanggota.setLayout(editanggotaLayout);
        editanggotaLayout.setHorizontalGroup(
            editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editanggotaLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditStatusP, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditPekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditTlahir))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(18, 18, 18)
                        .addComponent(editGenreSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(18, 18, 18)
                        .addComponent(EditNama, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editanggotaLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(12, 12, 12)
                        .addComponent(editAnggotaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(findMForEdit)))
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editanggotaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton17)
                .addGap(16, 16, 16))
        );
        editanggotaLayout.setVerticalGroup(
            editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editanggotaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(findMForEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editAnggotaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(EditNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(editGenreSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(EditTlahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(EditAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(EditNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(EditPekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(EditPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(EditStatusP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(editanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton18))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        CRD.add(editanggota, "card3");

        hapusanggota.setBackground(new java.awt.Color(155, 221, 221));

        jLabel43.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel43.setText("Id Anggota         : ");

        findDelete.setBackground(new java.awt.Color(204, 204, 204));
        findDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-search-24_1.png"))); // NOI18N
        findDelete.setText("Find");
        findDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                findDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                findDeleteMouseExited(evt);
            }
        });
        findDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findDeleteActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel44.setText("Nama Lengkap : ");

        jLabel46.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel46.setText("Tanggal Lahir    : ");

        jLabel47.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        jLabel47.setText("Alamat                : ");

        confirmationCheckP.setText("Hapus data ini secara permanen\n");
        confirmationCheckP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmationCheckPActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(51, 204, 0));
        jButton20.setText("Delete");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(255, 0, 51));
        jButton21.setText("Cancel");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        delAnggotaA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout hapusanggotaLayout = new javax.swing.GroupLayout(hapusanggota);
        hapusanggota.setLayout(hapusanggotaLayout);
        hapusanggotaLayout.setHorizontalGroup(
            hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hapusanggotaLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hapusanggotaLayout.createSequentialGroup()
                            .addComponent(jLabel47)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(DelAlamat))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hapusanggotaLayout.createSequentialGroup()
                            .addComponent(jLabel46)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(DelTTL))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hapusanggotaLayout.createSequentialGroup()
                            .addComponent(jLabel44)
                            .addGap(18, 18, 18)
                            .addComponent(DelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hapusanggotaLayout.createSequentialGroup()
                            .addComponent(jLabel43)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(delAnggotaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(findDelete)))
                    .addGroup(hapusanggotaLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(confirmationCheckP)))
                .addContainerGap(94, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hapusanggotaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20)
                .addGap(20, 20, 20))
        );
        hapusanggotaLayout.setVerticalGroup(
            hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hapusanggotaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(findDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delAnggotaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(DelNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addComponent(DelTTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(DelAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirmationCheckP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addGroup(hapusanggotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jButton21))
                .addGap(54, 54, 54))
        );

        CRD.add(hapusanggota, "card4");

        Anggota.add(CRD);
        CRD.setBounds(60, 160, 440, 380);

        CDL.add(Anggota, "card5");

        Laporan.setBackground(new java.awt.Color(255, 255, 255));
        Laporan.setLayout(null);

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 102, 102));
        jLabel48.setText("Laporan");
        Laporan.add(jLabel48);
        jLabel48.setBounds(20, 10, 239, 35);

        tabelShowPenghutang.setBackground(new java.awt.Color(0, 153, 153));
        tabelShowPenghutang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id", "nama", "hutang", "bunga", "jangka_waktu_bayar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tabelShowPenghutang);
        if (tabelShowPenghutang.getColumnModel().getColumnCount() > 0) {
            tabelShowPenghutang.getColumnModel().getColumn(0).setPreferredWidth(30);
            tabelShowPenghutang.getColumnModel().getColumn(1).setMinWidth(50);
            tabelShowPenghutang.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabelShowPenghutang.getColumnModel().getColumn(4).setMinWidth(120);
        }

        Laporan.add(jScrollPane4);
        jScrollPane4.setBounds(86, 320, 490, 120);

        jLabel50.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 147, 147));
        jLabel50.setText("penghoetang");
        Laporan.add(jLabel50);
        jLabel50.setBounds(100, 300, 160, 16);

        advancedLaporan.setBackground(new java.awt.Color(0, 153, 153));
        advancedLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        advancedLaporan.setForeground(new java.awt.Color(0, 0, 0));
        advancedLaporan.setText("Advanced Log");
        advancedLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedLaporanActionPerformed(evt);
            }
        });
        Laporan.add(advancedLaporan);
        advancedLaporan.setBounds(430, 510, 120, 27);

        tableShowTerlambatBayar.setBackground(new java.awt.Color(0, 153, 153));
        tableShowTerlambatBayar.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane8.setViewportView(tableShowTerlambatBayar);
        if (tableShowTerlambatBayar.getColumnModel().getColumnCount() > 0) {
            tableShowTerlambatBayar.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableShowTerlambatBayar.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableShowTerlambatBayar.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableShowTerlambatBayar.getColumnModel().getColumn(3).setMinWidth(80);
            tableShowTerlambatBayar.getColumnModel().getColumn(4).setMinWidth(100);
            tableShowTerlambatBayar.getColumnModel().getColumn(5).setMinWidth(50);
            tableShowTerlambatBayar.getColumnModel().getColumn(6).setMinWidth(30);
        }

        Laporan.add(jScrollPane8);
        jScrollPane8.setBounds(30, 110, 550, 120);

        jLabel51.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 147, 147));
        jLabel51.setText("Makhluk Terlambat Bayar");
        Laporan.add(jLabel51);
        jLabel51.setBounds(70, 90, 160, 16);

        CDL.add(Laporan, "card5");

        pengaturan.setLayout(null);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel54.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel54.setText("Pengaturan ");

        jButton22.setBackground(new java.awt.Color(153, 153, 153));
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-admin-settings-male-24.png"))); // NOI18N
        jButton22.setText("User Settings");

        jButton23.setBackground(new java.awt.Color(145, 145, 145));
        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-theme-24.png"))); // NOI18N
        jButton23.setText("  Theme");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(181, 181, 181));
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-shield-24.png"))); // NOI18N
        jButton24.setText("Security ");

        jButton25.setBackground(new java.awt.Color(153, 153, 153));
        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons8-ask-question-24.png"))); // NOI18N
        jButton25.setText("Help");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(255, 0, 51));
        jButton26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton26.setForeground(new java.awt.Color(255, 153, 153));
        jButton26.setText("Close");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton26)
                        .addGap(19, 19, 19))))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel54)
                .addGap(32, 32, 32)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jButton26)
                .addGap(25, 25, 25))
        );

        pengaturan.add(jPanel12);
        jPanel12.setBounds(128, 67, 275, 398);

        CDL.add(pengaturan, "card6");

        getContentPane().add(CDL);
        CDL.setBounds(180, 0, 600, 590);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CDL.removeAll();
        CDL.add(Transaksi);
        CDL.repaint();
        CDL.revalidate();
        
        TanggalBatasTP.setVisible(false);
        TanggalBatasTPL.setVisible(false);

        findDataMemberID();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        CDL.removeAll();
        CDL.add(Anggota);
        CDL.repaint();
        CDL.revalidate();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void selectMethodeTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectMethodeTMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_selectMethodeTMouseClicked

    private void inputTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTransactionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTransactionActionPerformed

    private void TanggalBatasTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TanggalBatasTPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalBatasTPActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        nameFoundT.setText("");
        pendapatanT.setText("");
        inputTransaction.setText("");
        globalTransactionFindState = false;
        simpleCloseAdvP();
    }//GEN-LAST:event_jButton10ActionPerformed

    public boolean controlNominalHide (int Methode) {
        switch (Methode) {
            case 0 -> {
                if (selectMethodeT.getSelectedItem().equals("bayar hoetang")) {
                    return true;
                } else {
                    return false;
                }
            }
            case 1 -> {
                inputTransaction.setVisible(false);
                nominalT.setVisible(false);
            }
            case 2 -> {
                inputTransaction.setVisible(true);
                nominalT.setVisible(true);
            }
            default -> throw new AssertionError();
        }
        return false;
    }
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        globalTransactionFindState = true;
        if (controlNominalHide(0)) {
            controlNominalHide(1);
        } else {
            controlNominalHide(2);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        globalEditFindState = false;
        displayEditClear();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void findDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findDeleteActionPerformed
        // TODO add your handling code here:
        globalDeleteFindSatate = true;
    }//GEN-LAST:event_findDeleteActionPerformed

    private void confirmationCheckPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmationCheckPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmationCheckPActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        // when delete nnti akan muncul pop up , apakah anda yakin ingin menghapus data ini? , Data berhasil dihapus
//        JOptionPane.showConfirmDialog(this, "testTest");

        if (confirmationCheckP.isSelected()) {
            int actionPerrm = JOptionPane.showConfirmDialog(this, "apa anda yakin ingin menghapus data ini", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (actionPerrm == 0) {
                String dataS = String.valueOf(delAnggotaA.getSelectedItem());
                trn.deleteMemberKops(Integer.parseInt(dataS));
                deleteShowingClearI();
                selectorLGetA(1);
                String message = "data dengan id " + dataS + " telah dihapus dan";
                trn.sendRecordLogApp(message, "delete_data");
                globalDeleteFindSatate = false;
            }
        } else {
            JOptionPane.showMessageDialog(this, " tolong centang konfirmasi box", "AnotherConfirmation", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here: 
        // tampilkan pop-up apakah anda ingin keluar dari aplikasi? ya atau tidak
        int actionPerrm = JOptionPane.showConfirmDialog(this, "apakah anda ingin keluar dari aplikasi?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        try {
            if (actionPerrm == 0) {
                File configFile = new File("config/key.properties");
                configFile.delete();
                this.dispose();
                System.exit(0);
            }
        } catch (Exception e) {}
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    public void simpleCloseAdvP () {
        TanggalBatasTP.setText("");
        TanggalBatasTP.setVisible(false);
        TanggalBatasTPL.setVisible(false);
    }
    private void sendTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendTransactionActionPerformed
        // TODO add your handling code here:
        // make smaller
        fullCodeJbutton();

//        boolean status = false;
//        int statusHandler = -1;
//        int id_member = Integer.parseInt(IDFinderT.getSelectedItem().toString());
//        int nominal = 0;
//        try {
//            nominal = Math.abs(Integer.parseInt(inputTransaction.getText()));
//        } catch (NumberFormatException numberFormatException) {
//            JOptionPane.showMessageDialog(null, "jangan biarkan input kosong");
//        }
//        String namaMember = String.valueOf(nameFoundT.getText());
//        
//        try {
//            switch (selectMethodeT.getSelectedItem().toString()) {
//                case String z when z.equals("Simpan") -> {
//                    boolean afterCheck = false;
//                    BayarPinjaman panExt = new BayarPinjaman();
//                    if (checkIsNgutang(id_member)) {
//                        int chc = JOptionPane.showConfirmDialog(null, "bro satu ini punya hutang, mau bayar gak?", "bro_ngutang", JOptionPane.YES_NO_OPTION);
//                        if (chc == 1) {
//                            JOptionPane.showMessageDialog(null, "melanjutkan penyimpanan uang");
//                            status = trn.transaction(1,
//                                    id_member,
//                                    namaMember,
//                                    nominal
//                            );
//                        } else {
////                            panExt.setVisible(true);
//                            status = panExt.showSimpananExtend();
//                        }
////                            System.out.println(chc);
////                        JOptionPane.showMessageDialog(null, "");
//                    } else {
//                        status = trn.transaction(1,
//                                id_member,
//                                namaMember,
//                                nominal
//                        );
//                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
//                    }
//                    
//                    
//                }
//                case String z when z.equals("Pinjam") -> {
//                    status = trn.transaction(-1,
//                            id_member,
//                            namaMember,
//                            nominal
//                    );
//                    if (!status) {
//                        status = trn.transactionPinjamUangH(id_member, nominal, Integer.parseInt(TanggalBatasTP.getText()), "CardSementara");
//                        statusHandler = 1;
//                    } else {
//                        statusHandler = 0;
//                    }
//                    if (statusHandler == 1) {
//                        Connection conn = maria.connect();
//                        String message = namaMember + " melakukan peminjaman pada sebanyak";
//                        PreparedStatement pstSendLog = conn.prepareStatement("call sendLog_pengeluaran(?, ?, ?)");
//                        pstSendLog.setInt(1, id_member);
//                        pstSendLog.setString(2, message);
//                        pstSendLog.setInt(3, nominal);
//                        pstSendLog.executeUpdate();
//                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
//                    } else if (statusHandler == 0) {
//                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
//                    } else {}
////                    if (!status) {
////                        TanggalBatasTP.setVisible(true);
////                        TanggalBatasTPL.setVisible(true);
////                        status = trn.transactionPinjamUangH(id_member, nominal, Integer.parseInt(TanggalBatasTP.getText()), "CardSementara");
////                    }
//                }
//                default ->
//                    throw new AssertionError();
//            }
//        } catch (SQLException eSql) {
//            System.err.println("error SQL h: " + eSql);
//        } catch (NumberFormatException nE) {
//            System.out.println("error formating (bisa diabaikan) h: " + nE);
//            JOptionPane.showMessageDialog(null, "tidak bisa melakukan penarikan biasa, ini akan didaftarkan ke peminjaman, silahkan ulangi prosedur");
//            TanggalBatasTP.setVisible(true);
//            TanggalBatasTPL.setVisible(true);
//        } catch (Exception e) {
//            System.err.println(e);
//            System.out.println("class Home error di baris 1752");
//        }
//        
//        if (status) {
//            simpleCloseAdvP();
//            globalTransactionFindState = false;
//            nameFoundT.setText("");
//            pendapatanT.setText("");
//            inputTransaction.setText("");
//        }
    }//GEN-LAST:event_sendTransactionActionPerformed
    public boolean fullCodeJbutton () {
        boolean status = false;
        int statusHandler = -1;
        int id_member = Integer.parseInt(IDFinderT.getSelectedItem().toString());
        
        String nominalNVL = inputTransaction.getText();
        int nominal = 0;
        
        try {
            if (!nominalNVL.isEmpty()) {
                nominal = Math.abs(Integer.parseInt(nominalNVL));
            } else {
                if (!selectMethodeT.getSelectedItem().equals("bayar hoetang")) {
                    JOptionPane.showMessageDialog(null, "jangan biarkan input kosong");
                    return false;
                }
            }
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "masukkan format yang benar");
            System.out.println(numberFormatException);
            return false;
        }
        String namaMember = String.valueOf(nameFoundT.getText());
        
        try {
            switch (selectMethodeT.getSelectedItem().toString()) {
                case String z when z.equals("Simpan") -> {
                    boolean afterCheck = false;
                    HomeExtend_BayarPinjaman panExt = new HomeExtend_BayarPinjaman();
                    if (checkIsNgutang(id_member)) {
                        int chc = JOptionPane.showConfirmDialog(null, "bro satu ini punya hutang, mau bayar gak?", "bro_ngutang", JOptionPane.YES_NO_OPTION);
                        if (chc == 1) {
                            JOptionPane.showMessageDialog(null, "melanjutkan penyimpanan uang");
                            status = trn.transaction(1,
                                    id_member,
                                    namaMember,
                                    nominal
                            );
                        } else if (chc == 0) {
//                            panExt.setVisible(true);
                            //status = panExt.showSimpananExtend();
                            panExt.runNConstruct(id_member);
                        } else {
                            System.out.println("cancel option");
                        }
                            System.out.println(chc);
//                            System.out.println(chc);
//                        JOptionPane.showMessageDialog(null, "");
                    } else {
                        status = trn.transaction(1,
                                id_member,
                                namaMember,
                                nominal
                        );
                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
                    }
                    
                    
                }
                case String z when z.equals("Pinjam") -> {
                    status = trn.transaction(-1,
                            id_member,
                            namaMember,
                            nominal
                    );
                    if (!status) {
                        String cardType = ""; // change this method and make this from user inputer
                        try { 
                            Connection conn = maria.connect();
                            PreparedStatement pst = conn.prepareStatement("select jenis_simpanan from simpanan where id_anggota = ?");
                            pst.setInt(1, id_member);
                            ResultSet rs = pst.executeQuery();
                            while (rs.next()) {
                                cardType = rs.getString(1);
                            }
                        } catch (SQLException e) {
                            System.err.println("error Sql add pinjaman in line 2115 class Home \nerror: " + e);
                        } // eida
                        // tolong perbaiki kode ini agar metode nya bisa lebih baik, seharusnya anda memakai kondisi isVisible di komponen batasHari
                        status = trn.transactionPinjamUangH(id_member, nominal, Integer.parseInt(TanggalBatasTP.getText()), cardType);
                        statusHandler = 1;
                    } else {
                        statusHandler = 0;
                    }
                    if (statusHandler == 1) {
                        Connection conn = maria.connect();
                        String message = namaMember + " melakukan peminjaman pada sebanyak";
                        PreparedStatement pstSendLog = conn.prepareStatement("call sendLog_pengeluaran(?, ?, ?)");
                        pstSendLog.setInt(1, id_member);
                        pstSendLog.setString(2, message);
                        pstSendLog.setInt(3, nominal);
                        pstSendLog.executeUpdate();
                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
                    } else if (statusHandler == 0) {
                        JOptionPane.showMessageDialog(this, status ? "succes" : "fail");
                    } else {}
//                    if (!status) {
//                        TanggalBatasTP.setVisible(true);
//                        TanggalBatasTPL.setVisible(true);
//                        status = trn.transactionPinjamUangH(id_member, nominal, Integer.parseInt(TanggalBatasTP.getText()), "CardSementara");
//                    }
                }
                case String q when q.equals("bayar hoetang") -> {
                    if (checkIsNgutang(id_member)) {
                        new HomeExtend_BayarPinjaman().runNConstruct(id_member);
                    } else {
                        JOptionPane.showMessageDialog(null, "bro ini gak punya hutang");
                    }
                }
                default ->
                    throw new AssertionError();
            }
        } catch (NumberFormatException nE) {
            System.out.println("error formating (bisa diabaikan) h: " + nE);
            JOptionPane.showMessageDialog(null, "tidak bisa melakukan penarikan biasa, ini akan didaftarkan ke peminjaman, silahkan ulangi prosedur");
            TanggalBatasTP.setVisible(true);
            TanggalBatasTPL.setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("class Home error di baris 1752");
        }
        
        if (status) {
            simpleCloseAdvP();
            globalTransactionFindState = false;
            nameFoundT.setText("");
            pendapatanT.setText("");
            inputTransaction.setText("");
            return true;
        }
        return false;
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CDL.removeAll();
        CDL.add(Dashboard);
        CDL.repaint();
        CDL.revalidate();

        ttlKoperaShow();
        displayTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CDL.removeAll();
        CDL.add(Laporan);
        CDL.repaint();
        CDL.revalidate();
        
        getPenghutang();
        getPenghutangTerlambat();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        CDL.removeAll();
        CDL.add(pengaturan);
        CDL.repaint();
        CDL.revalidate();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMultiCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMultiCMouseClicked
        // TODO add your handling code here:
        int buttonClick = jMultiC.getSelectedIndex();
        switch (buttonClick) {
            case 0 -> {
                CRD.removeAll();
                CRD.add(tambahanggotaa);
                CRD.repaint();
                CRD.revalidate();
            }
            case 1 -> {
                CRD.removeAll();
                CRD.add(editanggota);
                CRD.repaint();
                CRD.revalidate();
                selectorLGetA(0);
            }
            case 2 -> {
                CRD.removeAll();
                CRD.add(hapusanggota);
                CRD.repaint();
                CRD.revalidate();
                selectorLGetA(1);
            }
            default ->
                throw new AssertionError();
        }
    }//GEN-LAST:event_jMultiCMouseClicked

    public void flashTheInputer() {
        AddNama.setText("");
        AddAlamat.setText("");
        AddNumber.setText("");
        AddDateTTL.setText("");
        AddStatus.setText("");
        AddPekerjaan.setText("");
        AddSimpananType.setText("");
        AddPendapatan.setText("");
        warningAddCpenda.setText("");
        warningSymbolDate.setText("");
    }

    public boolean checkStringIsNull(String str) {
        return str == null || str.isEmpty();
    }

    public boolean checkLongIsNull(long lg) {
        return lg == 0;
    }

    public boolean sendDataRepError() {
        String nama = AddNama.getText();
        String alamat = AddAlamat.getText();
        String no_telp = AddNumber.getText();
        String genre = String.valueOf(AddSelectorGenre.getSelectedItem());
        String ttl = AddDateTTL.getText();
        String statusP = AddStatus.getText();
        String pekerjaan = AddPekerjaan.getText();
        String tabunganType = AddSimpananType.getText();
        //default type long
        String Spendapatan = AddPendapatan.getText();
        String noKtp = no_ktp.getText();
        long pendapatan = 0;
        try {
            pendapatan = Long.parseLong(Spendapatan);
        } catch (NumberFormatException e) {
            if (Spendapatan.equals("")) {
                System.out.println("data nggak boleh ada yang kosong");
                JOptionPane.showMessageDialog(null, "tolong pastikan untuk mengisi semua data", "UnexpectedOperan", JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                System.err.println("kesalahan penerimaan type / gagal conv, jenisError: " + e);
                //JOptionPane.showMessageDialog(null, "harus menggunakan angka di colom pendapatan", "UnexpectedOperan", JOptionPane.WARNING_MESSAGE);
                warningAddCpenda.setForeground(Color.black);
                warningAddCpenda.setText("gunakan angka");
                return false;

            }
        }

        if (ttl.contains("-") || ttl.contains("_") || ttl.contains("/")) {
        } else {
            warningSymbolDate.setText("gunakan pemisah \" /,-,_ \" ");
            warningSymbolDate.setForeground(Color.black);
            return false;
        }

        if (checkStringIsNull(nama)
                || checkStringIsNull(alamat)
                || checkStringIsNull(no_telp)
                || checkStringIsNull(genre)
                || checkStringIsNull(statusP)
                || checkStringIsNull(ttl)
                || checkStringIsNull(pekerjaan)
                || checkStringIsNull(noKtp)
                || checkStringIsNull(tabunganType)) {
            System.out.println("data nggak boleh ada yang kosong");
            JOptionPane.showMessageDialog(null, "data nggak boleh ada yang kosong", "UnexpectedOperan", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (trn.createMRDAddAnggota(nama, alamat, no_telp, genre, statusP, ttl, pekerjaan, pendapatan, tabunganType, noKtp)) {
            JOptionPane.showMessageDialog(null, "berhasil menambahkan Anggota", "200", JOptionPane.INFORMATION_MESSAGE);
            String message = "menambahkan " + nama + " sebagai anggota";
            trn.sendRecordLogApp(message, "add_anggota");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "gagal menambahkan anggota karena suatu kesalahan", "403", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
    private void sendDataAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendDataAddActionPerformed
        // TODO add your handling code here:
        if (sendDataRepError()) {
            flashTheInputer();
        }
    }//GEN-LAST:event_sendDataAddActionPerformed

    private void AddSimpananTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSimpananTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddSimpananTypeActionPerformed

    private void AddPendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddPendapatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddPendapatanActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        flashTheInputer();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void findMForEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findMForEditActionPerformed
        // TODO add your handling code here:
        globalEditFindState = true;
    }//GEN-LAST:event_findMForEditActionPerformed

    private void findDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findDeleteMouseEntered
        // TODO add your handling code here:
        int idSelected = Integer.parseInt(String.valueOf(delAnggotaA.getSelectedItem()));
        DelNama.setText(trn.searchSpecificDataA(idSelected, 1));
        DelAlamat.setText(trn.searchSpecificDataA(idSelected, 2));
        DelTTL.setText(trn.searchSpecificDataA(idSelected, 5));
    }//GEN-LAST:event_findDeleteMouseEntered

    public void deleteShowingClearI() {
        DelNama.setText("");
        DelTTL.setText("");
        DelAlamat.setText("");
    }
    private void findDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findDeleteMouseExited
        // TODO add your handling code here:
        if (!globalDeleteFindSatate) {
            deleteShowingClearI();
        }
    }//GEN-LAST:event_findDeleteMouseExited

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        deleteShowingClearI();
        globalDeleteFindSatate = false;
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        String nama = EditNama.getText();
        String alamat = EditAlamat.getText();
        String noTelp = EditNoTelp.getText();
        String Tlahir = EditTlahir.getText();
        String statusP = EditStatusP.getText();
        String pekerjaan = EditPekerjaan.getText();
        long pendapatan = Long.parseLong(EditPendapatan.getText());

        boolean statusSendEdit = false;
        int actionPerrm = JOptionPane.showConfirmDialog(this, "apa anda yakin ingin mengubah data ini", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (actionPerrm == 0) {
            statusSendEdit = trn.modifyDataAnggota(
                    Integer.parseInt(String.valueOf(editAnggotaA.getSelectedItem())),
                    nama,
                    alamat,
                    noTelp,
                    String.valueOf(editGenreSelector.getSelectedItem()),
                    Tlahir,
                    statusP,
                    pekerjaan,
                    pendapatan
            );
            globalEditFindState = (statusSendEdit) ? true : false;
            if (statusSendEdit) { displayAEdit();
                String message = "data milik " + nama + " berubah dan";
                trn.sendRecordLogApp(message, "edit_data");
            }
            JOptionPane.showMessageDialog(null, (statusSendEdit) ? "record data diubah" : "gagal merubah record data");
        }

    }//GEN-LAST:event_jButton17ActionPerformed

    private void advancedLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedLaporanActionPerformed
        // TODO add your handling code here:
        new HomeExtend_Laporan().setVisible(true);
    }//GEN-LAST:event_advancedLaporanActionPerformed

    private void findMForEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findMForEditMouseEntered
        // TODO add your handling code here:
        displayAEdit();
    }//GEN-LAST:event_findMForEditMouseEntered

    private void findMForEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findMForEditMouseExited
        // TODO add your handling code here:
        if (!globalEditFindState) {
            displayEditClear();
        }
    }//GEN-LAST:event_findMForEditMouseExited

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        // TODO add your handling code here:
        foundAMemberTRN();
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        // TODO add your handling code here:
        if (!globalTransactionFindState) {
            nameFoundT.setText("");
            pendapatanT.setText("");
            inputTransaction.setText("");
        }
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton25ActionPerformed

    private void IDFinderTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IDFinderTMouseClicked
        // TODO add your handling code here:/herhe
        simpleCloseAdvP();
    }//GEN-LAST:event_IDFinderTMouseClicked

    private void seachAsNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seachAsNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seachAsNameActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        searchNameDashboard();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void no_ktpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no_ktpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_no_ktpActionPerformed

    private void AddAlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddAlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddAlamatActionPerformed

    private void selectMethodeTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectMethodeTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectMethodeTActionPerformed

    private void pendapatanTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendapatanTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pendapatanTActionPerformed

    private void nameFoundTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFoundTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFoundTActionPerformed

    private void IDFinderTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDFinderTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDFinderTActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Home().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddAlamat;
    private javax.swing.JTextField AddDateTTL;
    private javax.swing.JTextField AddNama;
    private javax.swing.JTextField AddNumber;
    private javax.swing.JTextField AddPekerjaan;
    private javax.swing.JTextField AddPendapatan;
    private javax.swing.JComboBox<String> AddSelectorGenre;
    private javax.swing.JTextField AddSimpananType;
    private javax.swing.JTextField AddStatus;
    private javax.swing.JPanel Anggota;
    private javax.swing.JPanel CDL;
    private javax.swing.JPanel CRD;
    private javax.swing.JPanel Dashboard;
    private javax.swing.JTextField DelAlamat;
    private javax.swing.JTextField DelNama;
    private javax.swing.JTextField DelTTL;
    private javax.swing.JTextField EditAlamat;
    private javax.swing.JTextField EditNama;
    private javax.swing.JTextField EditNoTelp;
    private javax.swing.JTextField EditPekerjaan;
    private javax.swing.JTextField EditPendapatan;
    private javax.swing.JTextField EditStatusP;
    private javax.swing.JTextField EditTlahir;
    private javax.swing.JComboBox<String> IDFinderT;
    private javax.swing.JPanel Laporan;
    private javax.swing.JPanel NavBar;
    private javax.swing.JTextField TanggalBatasTP;
    private javax.swing.JLabel TanggalBatasTPL;
    private javax.swing.JPanel Transaksi;
    private javax.swing.JButton advancedLaporan;
    private javax.swing.JCheckBox confirmationCheckP;
    private javax.swing.JComboBox<String> delAnggotaA;
    private javax.swing.JComboBox<String> editAnggotaA;
    private javax.swing.JComboBox<String> editGenreSelector;
    private javax.swing.JPanel editanggota;
    private javax.swing.JButton findDelete;
    private javax.swing.JButton findMForEdit;
    private javax.swing.JPanel hapusanggota;
    private javax.swing.JTable informationMemberD;
    private javax.swing.JTextField inputTransaction;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTabbedPane jMultiC;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField nameFoundT;
    private javax.swing.JTextField no_ktp;
    private javax.swing.JLabel nominalT;
    private javax.swing.JTextField pendapatanT;
    private javax.swing.JPanel pengaturan;
    private javax.swing.JLabel saldo;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JTextField seachAsName;
    private javax.swing.JComboBox<String> selectMethodeT;
    private javax.swing.JButton sendDataAdd;
    private javax.swing.JButton sendTransaction;
    private javax.swing.JTable tabelShowPenghutang;
    private javax.swing.JTable tableShowTerlambatBayar;
    private javax.swing.JPanel tambahanggotaa;
    private javax.swing.JLabel totalAnggotaDash;
    private javax.swing.JLabel totalLaporanDash;
    private javax.swing.JLabel totalPemasukanDash;
    private javax.swing.JLabel totalPenarikanDash;
    private javax.swing.JLabel warningAddCpenda;
    private javax.swing.JLabel warningSymbolDate;
    // End of variables declaration//GEN-END:variables
}
