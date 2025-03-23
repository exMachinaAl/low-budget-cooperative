/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kopr.systemCompatibility;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author GM.Genius
 */
public class GetRequirementH {
    
    public static String getPcInc() throws UnknownHostException {
        //try {
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName();
        //System.out.println("the : " + hostname);
//        } catch (UnknownHostException e) {
//            System.out.println("Hostname can not be resolved");
//        }
        return hostname;
    }

    public static String getIpLocal() throws SocketException, UnknownHostException {
        String ip;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            //System.out.println(ip);
        }
        return ip;
    }

    public static String getIpPublic() throws java.io.IOException {
        String getIp;
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            //System.out.println("My current IP address is " + s.next());
            getIp = s.next();
            return getIp;
        } catch (java.io.IOException e) {
        }
        return "";
    }
    
}
