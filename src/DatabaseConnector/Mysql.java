package DatabaseConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DebugGraphics;
import javax.swing.JOptionPane;
import kopr.GUI.Home;
import kopr.GUI.Login;
import static kopr.systemCompatibility.GetRequirementS.encrypt;
import static kopr.systemCompatibility.GetRequirementS.decrypt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Mobius
 */
public class Mysql {

    private String username;
    private String password;
    private String jdbc = "jdbc:mysql://";// cari informasi tentang mengenai metode dynamicnya karena saya mendapati error dengan ini

    public static boolean defaultUse = true;
    public static String customUrl;
    public static String customUser;
    public static String customPass;
    public static boolean openDatabase = false;

    private static String defDB = "localhost:3306/dbkoperasi";
    private static String defUser = "root";
    private static String defPass = "";

//    public Mysql (String user, String pass, String DB) {
//        this.username = user;
//        this.password = pass;
//        this.jdbc += DB;
//    }
//    
//    public Mysql (String DB) {
//        this.jdbc += DB;
//    }
    public static boolean setCustomDatabase(String urlC, String userC, String passC) {
        try {
            //        customUrl = urlC;
//        customUser = userC;
//        customPass = passC;
//        defaultUse = false;
            configLoadCustomDBMethode(1, "config/database.properties", urlC, userC, passC);
        } catch (Exception ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static Properties configLoadCustomDBMethode(
            int type,
            String configPath,
            String urlC,
            String userC,
            String passC
    ) throws Exception {
        Properties config = new Properties();
        if (type == 0) { //load data

            File configFile = new File(configPath);

            try (FileInputStream input = new FileInputStream(configFile)) {
                config.load(input);

            }

        } else if (type == 1) { //save data
            config.setProperty("defaultUrl", encrypt(urlC));
            config.setProperty("dbUser", encrypt(userC));
            config.setProperty("dbPass", encrypt(passC));

            try (FileOutputStream output = new FileOutputStream(configPath)) {
                config.store(output, "database custom created");
            }
            return null;
        }
        return config;
    }

    public static void defaultDatabaseFile(String configPath) throws Exception {

        Properties config = new Properties();
        // Buat file konfigurasi
        config.setProperty("defaultUrl", encrypt(defDB));
        config.setProperty("dbUser", encrypt(defUser));
        config.setProperty("dbPass", encrypt(defPass));

        try (FileOutputStream output = new FileOutputStream(configPath)) {
            config.store(output, "database created");
        }
    }

    public Mysql() {
//        this.username = this.defUser;
//        this.password = this.defPass;
//        if (!defaultUse) {
        try {
            Properties config = configLoadCustomDBMethode(0, "config/database.properties", null, null, null);
            this.jdbc += decrypt(config.getProperty("defaultUrl"));
            System.out.println((!jdbc.equals("jdbc:mysql://" + defDB)) ? "its using custom db" : "its using origin db");
            this.username = decrypt(config.getProperty("dbUser"));
            this.password = decrypt(config.getProperty("dbPass"));
//            this.jdbc += customUrl;
//            this.username = customUser;
//            this.password = customPass;
        } catch (FileNotFoundException exception) {
            try {
                System.out.println("error database.properties Not found (bisa diabaikan)");
                //System.out.println("error database.properties Not found (bisa diabaikan)");
                defaultDatabaseFile("config/database.properties");
                Properties config = configLoadCustomDBMethode(0, "config/database.properties", null, null, null);
                System.out.println("error database.properties Not found (bisa diabaikan)");
                this.jdbc += decrypt(config.getProperty("defaultUrl"));
                this.username = decrypt(config.getProperty("dbUser"));
                this.password = decrypt(config.getProperty("dbPass"));
            } catch (Exception ex) {
                System.out.println("bukan error, hanya handler jika file defDB nggak ada");
                System.out.println("but read it bro: " + ex);
            }
        } catch (Exception e) {
            System.out.println("error nya ini: " + e);
//                this.jdbc += this.defDB;
//                this.username = this.defUser;
//                this.password = this.defPass;
        }
//        } else {
//            this.jdbc += this.defDB;
//        }
        System.out.println("url: " + jdbc);
        System.out.println("user: " + username);
        System.out.println("pass: " + password);
    }

    public Connection connect() {
        try {
            Connection conFetch = DriverManager.getConnection(jdbc, username, password);
            openDatabase = true;
            return conFetch;
        } catch (SQLException ex) {
            if ("08S01".equals(ex.getSQLState())) {
                File configFile = new File("config/database.properties");
                JOptionPane.showMessageDialog(null, "tidak bisa terhubung ke database\npastikan alamat atau koneksi anda berjalan normal\n setelah itu mulai ulang", "CriticalAction", JOptionPane.ERROR_MESSAGE);
                if (!openDatabase) { // for developer or beta tester
                    try {
                        if (JOptionPane.showConfirmDialog(null, "ada kemungkinan database anda berbeda \napakah anda ingin memastikannya", "custom database", JOptionPane.YES_NO_OPTION) == 0) {
                            Properties config = configLoadCustomDBMethode(0, "config/database.properties", null, null, null);
                            JOptionPane.showMessageDialog(null, "url: " + decrypt(config.getProperty("defaultUrl")));
                            if(JOptionPane.showConfirmDialog(null, "beda?", "custom database", JOptionPane.YES_NO_OPTION) == 0) {
                            JOptionPane.showMessageDialog(null, "ok melakukan penyesuaian");    
                            } else {
                            JOptionPane.showMessageDialog(null, "berarti sinyal anda");
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("something wrong " + e);
                    }
                }// for developer or beta tester
                configFile.delete();
                new Login().dispose();
                new Home().dispose();
                System.exit(0);
            } else if ("28000".equals(ex.getSQLState())) {
                File configFile = new File("config/database.properties");
                JOptionPane.showMessageDialog(null, "user atau pass SQL salah, menghapus database.properties\n error in setting database custom", "CriticalAction", JOptionPane.ERROR_MESSAGE);
                configFile.delete();
//                new Login().dispose();
//                new Home().dispose();
                System.exit(0);
            }
            System.err.println("error sql code: " + ex.getSQLState());
            return null;
        }
    }

}
