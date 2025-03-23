/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kopr.defaultAccess;


import DatabaseConnector.Mysql;
import java.io.File;
import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.swing.JOptionPane;
import static kopr.manager.KopRManager.configLoader;
import static kopr.systemCompatibility.GetRequirementS.decrypt;
import static kopr.systemCompatibility.GetRequirementS.encrypt;
/**
 *
 * @author Mobius
 */
public class Auth {
    
    Mysql maria = new Mysql();
    
    private String tb_login;
    
    private String defLogin = "el_karyawan";
    
    public Auth (String login) {
        this.tb_login = login;
    }
    public Auth () {
        this.tb_login = this.defLogin;
    }
    
    
    
    
    public boolean login (String user, String pass) {
        
        String psmt = "SELECT * FROM karyawan_auth WHERE user = ? AND pass = ?";
        
        try (Connection conector = (Connection) maria.connect()) {
            PreparedStatement pst = conector.prepareStatement(psmt);
            pst.setString(1, user);
            pst.setString(2, pass);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()) {
                //System.out.println("login succes");
                return true;
            } else {
                //System.out.println("user or pass wrongs");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("gagal login karena ada kesalahan di SQL: " + e.getSQLState());
        }
        return false;
    }
    
    public boolean localKeyLogCFG (String configPath, String user, String pass) throws Exception{
        LocalDateTime now = LocalDateTime.now();
        File configFile = new File(configPath);
        
        final long ABSOLUT_EXPIRED_D = 1; //expired after 1 day
        final int ABSOLUT_EXPIRED_H = 0;
        final long ABSOLUT_EXPIRED_M = 0;
        
        if (!configFile.exists()) {
            saveEncryptedLogin(configPath, "isNotLoginYet@12-29");
        }
        
        Properties config = configLoader(configPath);
        
        LocalDateTime absDateLDT = now.plusDays(ABSOLUT_EXPIRED_D).plusHours(ABSOLUT_EXPIRED_H).plusMinutes(ABSOLUT_EXPIRED_M);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
//        String passEnc = decrypt(config.getProperty("AuthKeysA"));
        String keyExpress = user + "+" + pass + "@" + absDateLDT.format(formatter);
        

        config.setProperty("AuthKeysA", encrypt(keyExpress));
        try (FileOutputStream output = new FileOutputStream(configFile)) {
                config.store(output, "Authenticate available only 10minute");
            }
        return true;
    }
    
    public static void saveEncryptedLogin(String configPath, String loginData) throws Exception {
        String encryptedLData = encrypt(loginData);

        // Buat file konfigurasi
        Properties config = new Properties();
        config.setProperty("AuthKeysA", encryptedLData);

        try (FileOutputStream output = new FileOutputStream(configPath)) {
            config.store(output, "Authenticate available only 10minute");
        }
    }
    
    
}
