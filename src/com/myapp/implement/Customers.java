package com.myapp.implement;

import com.myapp.classes.DBConnection;
import com.myapp.classes.Flight;
import com.myapp.classes.Users;
import com.myapp.services.Auth;
import com.myapp.classes.Booking;

import com.myapp.services.Update_del;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

//********************************************************************************************************************

public class Customers extends Users implements Auth, Update_del {
    public static int currentUserId;

//********************************************************************************************************************

    @Override
    public void signup() {

        System.out.println("Admin registration.");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your email: ");
        String email = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        System.out.print("Enter your phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        String sql = "INSERT INTO users (name, email, password, phone, address, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setInt(6, age);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Registration successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //********************************************************************************************************************

    @Override
    public boolean login() {
        System.out.println("User Login.");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your email: ");
        String email = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.currentUserId = rs.getInt("id");
                System.out.println(" Login successful! Welcome, " + rs.getString("name"));
                return true;
            } else {
                System.out.println(" Invalid email or password.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    //***************************************************************************************************************************************************************

    @Override
    public void update() {
        if (currentUserId == 0) {
            System.out.println("Get connected to modify your profil");
            return;
        }

        System.out.println("\n ==================== MODIFICATION OF PROFIL ======================");

        // Afficher les informations actuelles
        displayCurrentProfile();

        System.out.println("\n Which   date do you want to update? ?");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Password");
        System.out.println("4. phone");
        System.out.println("5. Adress");
        System.out.println("6. Age");
        System.out.println("7. Cancel");
        Scanner sc = new Scanner(System.in);

        System.out.print("Choise : ");
        int choice = sc.nextInt();
        sc.nextLine(); //

        String sql = "";
        PreparedStatement pstmt = null;

        try (Connection conn = DBConnection.getConnection()) {
            switch (choice) {
                case 1:
                    System.out.print("New  name : ");
                    String newName = sc.nextLine();
                    sql = "UPDATE users SET name = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newName);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 2:
                    System.out.print("New email : ");
                    String newEmail = sc.nextLine();
                    sql = "UPDATE users SET email = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newEmail);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 3:
                    System.out.print("New password : ");
                    String newPassword = sc.nextLine();
                    sql = "UPDATE users SET password = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newPassword);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 4:
                    System.out.print("New number : ");
                    String newPhone = sc.nextLine();
                    sql = "UPDATE users SET phone = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newPhone);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 5:
                    System.out.print("New adresse : ");
                    String newAddress = sc.nextLine();
                    sql = "UPDATE users SET address = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newAddress);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 6:
                    System.out.print("new Age : ");
                    int newApp = sc.nextInt();
                    sc.nextLine();
                    sql = "UPDATE users SET age = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, newApp);
                    pstmt.setInt(2, currentUserId);
                    break;

                case 7:
                    System.out.println("modification canceled");
                    return;

                default:
                    System.out.println("Choise out of range.");
                    return;
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" Profil updated successfully. !");
            } else {
                System.out.println("No modification ");
            }

        } catch (Exception e) {
            System.out.println(" Error while modifying  : " + e.getMessage());
        }


    }
    //********************************************************************************************************************

    private void displayCurrentProfile() {
        String sql = "SELECT name, email, phone, address, app FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println(" Profil actuel :");
                System.out.println("   Name : " + rs.getString("name"));
                System.out.println("   Email : " + rs.getString("email"));
                System.out.println("   Phone : " + rs.getString("phone"));
                System.out.println("   Adress : " + rs.getString("address"));
                System.out.println("   age : " + rs.getInt("age"));
            }

        } catch (Exception e) {
            System.out.println("Error to take back the profil : " + e.getMessage());
        }
    }

    //********************************************************************************************************************

    @Override
    public void delete() {
        if (currentUserId == 0) {
            System.out.println(" You should be connected to delete ur account");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("\n======= Deleting of Account ======");
        System.out.println("⚠  Warn : This action in irreversible !");
        System.out.print("Confirm (press 'DEL' ) : ");
        String confirmation = sc.nextLine();

        if ("DEL".equals(confirmation)) {
            String sql = "DELETE FROM users WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, currentUserId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println(" Account deleted  !");
                    currentUserId = 0; // Déconnecter l'utilisateur
                } else {
                    System.out.println("error while deleting the account");
                }

            } catch (Exception e) {
                System.out.println("Error while deleting : " + e.getMessage());
            }
        } else {
            System.out.println(" Deleting canceled !");
        }
    }
    //********************************************************************************************************************

    public void reserveFlight() {
        if (currentUserId == 0) {
            System.out.println(" You must be logged in!");
            return;
        }

        Booking book = new Booking();
        book.setCurrentUserId(currentUserId);
        book.bookFlight();
    }

    //********************************************************************************************************************

    public void DisplayMyFlights(){
        if (currentUserId == 0) {
            System.out.println(" You must be logged in!");
            return;
        }
        Flight flight = new Flight();
        flight.displayAllFlights();
    }

    //********************************************************************************************************************

    public void CancelFlight() {
        Users user = new Users();
        user.CancelFlight();

    }


    //********************************************************************************************************************

    public void viewMyReservations() {
        Users user = new Users();
        user.viewMyReservations();

    }


}


