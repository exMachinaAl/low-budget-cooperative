/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package kopr.manager;

import DatabaseConnector.Mysql;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kopr.GUI.Home;
import kopr.GUI.LoadingAutoLogin;
import kopr.GUI.Login;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author GM.Genius
 */
import kopr.GUI.Login;
import kopr.defaultAccess.Auth;
import static kopr.systemCompatibility.GetRequirementS.decrypt;

public class KopRManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       // check database is ready
        new Mysql().connect();
        
        Login theAuth = new Login();
        Home sweetHome = new Home();
        try {
            if (!keyLoginStillA("config/key.properties")) {
                boolean isLogged = theAuth.showLogin();
                if (isLogged) {
                    sweetHome.runHome();
                }
            } else {
                sweetHome.runHome();
            }
        } catch (Exception ex) {
            Logger.getLogger(KopRManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static boolean keyLoginStillA (String configPath) throws Exception {
        Auth auth = new Auth();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Properties config = null;
        
        try {
            config = configLoader(configPath);
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "tidak punya log login");
            System.out.println("tidak ada log Login");
            return false;
        }
        //sus+rondo@2024-12-28 15\:19\:53
        String passEnc = decrypt(config.getProperty("AuthKeysA"));
        String[] spliter = passEnc.split("[@+]");
        //isLogged user and pass
        String userChecker = spliter[0];
        String passChecker = spliter[1];
        
        //date Verification [ternyata bisa langsung dieksekusi]
        
//        for (int c = 0; c<spliter.length; c++) {
//            System.out.print(spliter[c]);
//        }
        LocalDateTime specificDateTimeChecker = LocalDateTime.parse(spliter[2], formatter);
        //LocalDateTime currentTime = LocalDateTime.now();
        
        //System.out.println(spliter.toString());
        //System.out.println(absDateLDT.format(formatter));
        LoadingAutoLogin loadingAutoLogin = new LoadingAutoLogin();
        loadingAutoLogin.loginFor();
        if (now.isBefore(specificDateTimeChecker)) {
            if (auth.login(userChecker, passChecker)) {
                JOptionPane.showMessageDialog(null, "Login otomatis berhasil");
                return true;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Login kadaluarsa");
        }
        
        return false;
    }
    
    public static Properties configLoader (String configPath) throws Exception {
        Properties config = new Properties();
        
        File configFile = new File(configPath);

//        if (!configFile.exists()) {
//            saveEncryptedLogin(configPath, "isNotLigunYet@12-29");
//        }

        // Baca file konfigurasi
        try (FileInputStream input = new FileInputStream(configFile)) {
            config.load(input);
        }
        
        return config;
    }
    
    
//    public static void saveEncryptedLogin(String configPath, String loginData) throws Exception {
//        String encryptedLData = encrypt(loginData);
//
//        // Buat file konfigurasi
//        Properties config = new Properties();
//        config.setProperty("AuthKeysA", encryptedLData);
//
//        try (FileOutputStream output = new FileOutputStream(configPath)) {
//            config.store(output, "Authenticate available only 10minute");
//        }
//    }
    
}
