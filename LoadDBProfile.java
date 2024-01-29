/**
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadDBProfile {
    final static String filename = ".database_profile.txt";

    public static void main(String[] args) {
        String host = "", port = "", name = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into variable and value using '=' as the delimiter
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String variable = parts[0].trim();
                    String value = parts[1].trim();

                    if (variable.equals("host")) {
                        host = value;
                    } else if (variable.equals("port")) {
                        port = value;
                    } else if (variable.equals("name")) {
                        name = value;
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (host == "" || port == "" || name == "") {
            System.out.println("Error: Missing database profile information.");
            System.exit(1);
        }

        System.out.println("DB: " + host + ":" + port + "/" + name);
    }
}
