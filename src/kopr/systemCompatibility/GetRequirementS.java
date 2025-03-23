/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kopr.systemCompatibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
//import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.ComputerSystem;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
//import org.threeten.bp.DateTimeUtils;
import oshi.software.os.OperatingSystem;


public class GetRequirementS {
    
    private static final String KEY = "1234567890123456"; // Panjang kunci 16 karakter


    public static String encrypt(String data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);
    }
    
    
    public static Properties loadConfig(String configPath) throws Exception {
        Properties config = new Properties();
        
//        String configPath = new File(SecureEmailSender.class.getProtectionDomain().getCodeSource().getLocation().toURI())
//        .getParentFile()
//        .getPath() + File.separator + "config" + File.separator + "config.properties";
        
        File configFile = new File(configPath);

        if (!configFile.exists()) {
            // Jika file tidak ada, buat file baru dengan konfigurasi default
            saveEncryptedData(configPath, "default-email-password", "myEmail", "senderEMil"); // Default password
            System.out.println("File konfigurasi baru dibuat.");
        }

        // Baca file konfigurasi
        try (FileInputStream input = new FileInputStream(configFile)) {
            config.load(input);
        }

        // Periksa apakah password sudah terenkripsi
        String email = config.getProperty("appEmi");
        String password = config.getProperty("appPass");
        String emailSnd = config.getProperty("appSnd");
        if (!isEncrypted(password)) {
            // Jika belum terenkripsi, enkripsi password dan simpan kembali
            String encryptedEmail = encrypt(email);
            String encryptedPassword = encrypt(password);
            String encryptedEmailSnd = encrypt(emailSnd);
            config.setProperty("appEmi", encryptedEmail);
            config.setProperty("appPass", encryptedPassword);
            config.setProperty("appSnd", encryptedEmailSnd);
            try (FileOutputStream output = new FileOutputStream(configFile)) {
                config.store(output, "Konfigurasi Email (Password Terenkripsi)");
            }
            System.out.println("Password pada file konfigurasi dienkripsi secara otomatis.");
        }

        return config;
    }
    
    
    public static void saveEncryptedData(String configPath, String plaintextPassword, String plaintextemail, String emailSender) throws Exception {
        String encryptedPassword = encrypt(plaintextPassword);
        String encryptedEmail = encrypt(plaintextemail);
        String encryptedEmailSender = encrypt(emailSender);

        // Buat file konfigurasi
        Properties config = new Properties();
        config.setProperty("appSnd", encryptedEmailSender);
        config.setProperty("appPass", encryptedPassword);
        config.setProperty("appEmi", plaintextemail); // Ganti dengan email Anda

        try (FileOutputStream output = new FileOutputStream(configPath)) {
            config.store(output, "Konfigurasi Email");
            System.out.println("Konfigurasi disimpan dengan password terenkripsi.");
        }
    }

    // Metode untuk memeriksa apakah password sudah terenkripsi
    public static boolean isEncrypted(String password) {
        try {
            //Base64.getDecoder().decode(password); // Jika bisa di-decode, asumsikan terenkripsi
            String se = decrypt(password);
            return true;
        } catch (Exception e) {
            return false; // Tidak terenkripsi jika decoding gagal
        } //catch (IllegalArgumentException e) {
//            return false; // Tidak terenkripsi jika decoding gagal            
//        }
    }
    
        public static String getCompatibilityApp() {

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        OperatingSystem os = systemInfo.getOperatingSystem();
        
        String fullInfoSystem = """
                                ======================================
                                ============== System Info ==============
                                ======================================
                                """;
        String InfoOs = "=  Os:         " + os.getFamily() + "\n";
        String InfoCorp = "=   corpPc:      " + computerSystem.getManufacturer() + "\n";
        String InfoModel = "=   PcModel:    " + computerSystem.getModel() + "\n";
        String InfoPcInc = "";
        String InfoPrivateIP = "";
        String InfoPublicIP = "";
        try {
            InfoPcInc = "=   Pc:        " + GetRequirementH.getPcInc() + "\n";
            InfoPrivateIP = "=   localIP:     " + GetRequirementH.getIpLocal() + "\n";
            InfoPublicIP = "=   publicIP:     " + GetRequirementH.getIpPublic() + "\n";
        } catch (Exception e) {
        }

        fullInfoSystem += InfoOs + InfoCorp + InfoModel + InfoPcInc + InfoPrivateIP + InfoPublicIP;

        return fullInfoSystem;
    }
    

    
    public static void main(String[] args) {
        // Mendapatkan HWID
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        ComputerSystem computerSystem = hardware.getComputerSystem();
        
        OperatingSystem os = systemInfo.getOperatingSystem();

        //String serialNumber = computerSystem.getSerialNumber(); // HWID
        //System.out.println("Serial Number: " + serialNumber);
        //String serialNumber = computerSystem.getManufacturer() + " dan " + computerSystem.getModel() + " dan " + os.getManufacturer(); // HWID
        //System.out.println("Serial Number: " + serialNumber);

        // Daftar HWID yang diizinkan
        String[] allowedHWIDs = {"SERIAL123", "SERIAL456"};
        boolean isAllowed = false;
        
        
        try {
            Properties config = loadConfig("config/config.properties");
            
//            String passDec = config.getProperty("password");
//            //Base64.getDecoder().decode(config.getProperty("password"));
//            System.out.println(passDec);
        } catch (Exception e) {
            System.err.println("error " + e);
        }
        
        
//        String myStrongPass = "HaDomainor";
//        try{
//        String deco = encrypt(myStrongPass);
//        
//            System.out.println(deco);
//        } catch (Exception e) {
//            
//        }
//        for (String hwid : allowedHWIDs) {
//            if (serialNumber.equals(hwid)) {
//                isAllowed = true;
//                break;
//            }
//        }
         //Jika perangkat tidak diizinkan, kirim email
//        if (!isAllowed) {
//            sendEmail("admin@example.com", "Perangkat Tidak Terdaftar", 
//                      "HWID: " + serialNumber + " tidak terdaftar!");
//        }
    
    }
    


    public void sendEmail(String subject, String content) throws MessagingException, Exception {
        // Konfigurasi email
        Properties config = loadConfig("config/config.properties");
        
        final String senderEmail = decrypt(config.getProperty("appEmi"));
        final String password = decrypt(config.getProperty("appPass"));
        final String recipient = decrypt(config.getProperty("appSnd"));

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
    }

//    public void sendMail(String coy@gm.co, String informationApp, String sysInfo) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
    
}
