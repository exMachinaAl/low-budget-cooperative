/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kopr.defaultAccess;

/**
 *
 * @author Mobius
 */
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import DatabaseConnector.Mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class QueryGlobalAccess {

    Mysql maria = new Mysql();

    public boolean createMRDAddAnggota(
            String nama,
            String alamat,
            String no_telp,
            String genre,
            String status,
            String ttl,
            String pekerjaan,
            long pendapatan,
            String jenisTabungan,
            String noKtp
    ) {

        String query = "call anggota_add_p(?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, nama);
            pst.setString(2, alamat);
            pst.setString(3, no_telp);
            pst.setString(4, genre);
            pst.setString(5, ttl);
            pst.setString(6, status);
            pst.setString(7, pekerjaan);
            pst.setLong(8, pendapatan);
            pst.setString(9, jenisTabungan);
            pst.setString(10, noKtp);

            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    public boolean modifyDataAnggota(
            int theId,
            String nama,
            String alamat,
            String no_telp,
            String genre,
            String ttl,
            String status,
            String pekerjaan,
            long pendapatan
    ) {

        String query = "UPDATE anggota SET "
                + "nama_anggota = ?, "
                + "alamat = ?, "
                + "nomor_telepon = ?, "
                + "jenis_kelamin = ?, "
                + "tanggal_lahir = ?, "
                + "status_perkawinan = ?, "
                + "pekerjaan = ?, "
                + "pendapatan = ? "
                + "WHERE id_anggota = ?";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, nama);
            pst.setString(2, alamat);
            pst.setString(3, no_telp);
            pst.setString(4, genre);
            pst.setString(5, ttl);
            pst.setString(6, status);
            pst.setString(7, pekerjaan);
            pst.setLong(8, pendapatan);
            pst.setInt(9, theId);

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    public String searchSpecificDataA(int idCHS, int columnTabel) {//this not include ID
        // actually never used only instae 
        String query;
        String stichQuery = "";
        String dataGeting = "gagal mendapatkan data";

        switch (columnTabel) {
            case 1 -> {
                stichQuery = "nama_anggota";
            }
            case 2 -> {
                stichQuery = "alamat";
            }
            case 3 -> {
                stichQuery = "nomor_telepon";
            }
            case 4 -> {
                stichQuery = "";
            }
            case 5 -> {
                stichQuery = "tanggal_lahir";
            }
            default ->
                throw new AssertionError();
        }

        query = "SELECT " + stichQuery + " FROM anggota WHERE ID_Anggota = ?";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, String.valueOf(idCHS));

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataGeting = rs.getString(1);
            }

            //DefaultTableModel dft = (DefaultTableModel)JTable1.getModel();
        } catch (SQLException e) {
            System.err.println(e.getClass());
        }

        return dataGeting;
    }

    public void readData() {

        int que;
        String query = "SELECT * FROM anggota";

        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement(query);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rose = (ResultSetMetaData) rs.getMetaData();
            que = rose.getColumnCount();

            //DefaultTableModel dft = (DefaultTableModel)JTable1.getModel();
        } catch (SQLException e) {
            System.err.println(e.getClass());
        }
    }

    public boolean deleteMemberKops(int theId) {
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("call anggota_del_P(?)");
            pst.setInt(1, theId);
            pst.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
            return false;
        }
    }

    public boolean transaction(int typeTransaction, int idMember, String namaMember, int NominalT) {
        Connection conn;
        PreparedStatement pst;
        if (typeTransaction == 1) {
            conn = maria.connect();
            try {
                conn.setAutoCommit(false);

                pst = conn.prepareStatement("call pemasukan_full_attach(?, ?)");
                pst.setInt(1, idMember);
                pst.setInt(2, NominalT);

                String message = namaMember + " menyetorkan uangnya pada tanggal";
                PreparedStatement pstSendLog = conn.prepareStatement("call sendLog_pemasukan(?, ?, ?)");
                pstSendLog.setInt(1, idMember);
                pstSendLog.setString(2, message);
                pstSendLog.setInt(3, NominalT);

                int rowsUpdated = pstSendLog.executeUpdate();
                int anotherRowUpdated = pst.executeUpdate();

                conn.commit();

            } catch (SQLException eSql) {
                System.out.println("this can rollback if something err on transaction or you can say \nsafety");
                System.out.println("error: " + eSql);
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error saat rollback: " + rollbackEx.getMessage());
                }
                return false;
            } finally {
                try {
                    if (conn != null) conn.setAutoCommit(true);
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    System.err.println("something err on line 214 class QueryGlobalAcces");
                }
            }
            return true;
        } else if (typeTransaction == -1) {
            conn = maria.connect();
            try {
                conn.setAutoCommit(false);

                // Memanggil prosedur penarikan
                CallableStatement cst = conn.prepareCall("{call penarikan_full_attach(?, ?, ?)}");
                cst.setInt(1, idMember);
                cst.setInt(2, NominalT);
                cst.registerOutParameter(3, Types.TINYINT);
                cst.execute();

                // Memeriksa output dari prosedur
                if (cst.getInt(3) == 0) {
                    // Kondisi gagal: uang kurang
                    System.out.println("Penarikan gagal, saldo tidak cukup.");
                    conn.commit(); // Tetap commit meskipun tidak ada perubahan signifikan
                    return false;
                }

                // Jika berhasil, catat log pengeluaran
                String message = namaMember + " menarik uangnya pada tanggal";
                PreparedStatement pstSendLog = conn.prepareStatement("call sendLog_pengeluaran(?, ?, ?)");
                pstSendLog.setInt(1, idMember);
                pstSendLog.setString(2, message);
                pstSendLog.setInt(3, NominalT);
                pstSendLog.executeUpdate();

                // Commit transaksi
                conn.commit();
                return true;

            } catch (SQLException eSql) {
                // Menangani error dan rollback
                System.err.println("Transaksi gagal, rollback dilakukan.");
                System.err.println("Error: " + eSql.getMessage());
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    System.err.println("Kesalahan saat rollback: " + rollbackEx.getMessage());
                }
                return false;

            } finally {
                // Mengembalikan koneksi ke keadaan default dan menutup koneksi
                try {
                    if (conn != null) {
                        conn.setAutoCommit(true); // Pastikan auto-commit dikembalikan
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Kesalahan saat menutup koneksi: " + e.getMessage());
                }
            }
        } else {
            System.out.println("youre not supposed to do");
        }

        return false;
    }

    public boolean transactionPinjamUangH(int member_id, int jumlahPinjem, int batasPinjemHari, String tipePinjaman) throws SQLException {
        Connection conn = maria.connect();
        PreparedStatement pst;

        CallableStatement cst = conn.prepareCall("{call pinjaman_addMember(?, ?, ?, ?)}");
        cst.setInt(1, member_id);
        cst.setInt(2, jumlahPinjem);
        cst.setInt(3, batasPinjemHari);
        cst.setString(4, tipePinjaman);

        cst.execute();

        return true;
    }

    public void sendRecordLogApp(String catatan, String type) {
        try {
            Connection conn = maria.connect();
            PreparedStatement pst = conn.prepareStatement("call sendLog_app(?, ?)");
            pst.setString(1, catatan);
            pst.setString(2, type);
            pst.executeUpdate();
        } catch (SQLException e) {
        }
    }

}
