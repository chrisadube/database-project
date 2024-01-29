/**
 * COSC 578: Database Management Systems
 * Towson University
 * Spring 2023
 * Dr. Hong
 * Project 2
 * Keith's Transportation
 * Members: Everett, Qin, Chris, Chailuka
 * 
 * To Execute with mysql connector in same directory:
 * java -cp ".:mysql-connector-j-8.0.32.jar" MainDatabaseApp
 * 
 * To execute with mysql connector in different directory (change directory):
 * java -cp ".:/usr/share/java/mysql-connector-j-8.0.32.jar" MainDatabaseApp
 * 
 * NOTES:
 * Using ip address to non-static host.
 */

/**
 * COMPILATION & EXECUTION
 * javac MainDatabaseApp.java
 * java -cp ".:/usr/share/java/mysql-connector-j-8.0.32.jar:Cosc578Proj.jar" MainDatabaseApp
 * 
 * javac -d ./build MainDatabaseApp.java
 * java -jar Cosc578Proj.jar
 * 
 */

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.awt.Insets.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainDatabaseApp {
    // Filename that contains database profile information
    final static String filename = ".database_profile.txt";
    Database db;

    public static void main(String[] args) {
        new MainDatabaseApp().run();
    }

    /**
     * run
     * Driver method for the application.
     * Also using this method for testing updates
     * and queries to the database.
     */
    void run() {
        // Load database profile from file and set singleton Database object to the profile
        DatabaseProfile profile = loadDBProfile();
        db = Database.getDatabase();
        db.setProfile(profile);

        // Screen Window must be set prior to calling and subclass of Screen
        Screen.setWindow(Window.getWindow());
        Screen.setDatabase(Database.getDatabase());

        // Initialize and display the main window set to LoginScreen
        Window.getWindow().setTitle("Keith's Transportation");
        Window.getWindow().setScreen(new LoginScreen());
        System.out.println("Main Window Constructed.");
    }
    
    /**
     * loadDBProfile
     * @return DatabaseProfile object containing database information
     */
    DatabaseProfile loadDBProfile() {
        DatabaseProfile profile = new DatabaseProfile();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into variable and value using '=' as the delimiter
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String variable = parts[0].trim();
                    String value = parts[1].trim();

                    if (variable.equals("host")) {
                        profile.setHost(value);
                    } else if (variable.equals("port")) {
                        profile.setPort(value);
                    } else if (variable.equals("name")) {
                        profile.setName(value);
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gaurd clause to ensure that all database profile information is present
        if (profile.getHost() == "" || profile.getPort() == "" || profile.getName() == "") {
            System.out.println("Error: Missing database profile information.");
            System.exit(1);
        }

        return profile;
    }



    

    // ----------------------------------------------------------------
    // ------------------------- SCREEN DATA --------------------------
    // ----------------------------------------------------------------
    
    /**
     * The SCREEN DATA section contains classes that hold data related
     * to specific Screen objects. They implement the ScreenData
     * interface, which allows screen data to be seamlessly passed
     * through methods and Screen objects.
     */
    
}
