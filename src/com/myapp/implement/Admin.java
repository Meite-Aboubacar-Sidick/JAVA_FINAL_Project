package com.myapp.implement;

import com.myapp.classes.DBConnection;
import com.myapp.classes.Flight;
import com.myapp.services.Auth;
import com.myapp.services.Update_del;
import com.myapp.classes.Users;

import java.sql.*;
import java.util.Scanner;

//********************************************************************************************************************

public class Admin extends Users implements Auth, Update_del {
    private int currentAdminId;
    @Override
    public void signup() {

        System.out.println("Admin already registered go to login .");
    }

    //********************************************************************************************************************

    @Override
    public boolean login() {
        System.out.println("Admin logged in.");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM admin WHERE name = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println(" Login successful! Welcome, " + rs.getString("name"));
                return true;
            } else {
                System.out.println(" Invalid name or password.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
 }

        return true;
    }

    //********************************************************************************************************************

    @Override
    public void update() {

        System.out.println("Admin account updated.");
    }
    //********************************************************************************************************************
    @Override
    public void delete() {

        if (currentAdminId == 0) {
            System.out.println(" You should be connected to delete ur account");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Deleting of Account ===");
        System.out.println("⚠  Warn : This action in irreversible !");
        System.out.print("Confirm (press 'DEL' ) : ");
        String confirmation = sc.nextLine();

        if ("DEL".equals(confirmation)) {
            String sql = "DELETE FROM users WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, currentAdminId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println(" Account deleted  !");
                    currentAdminId = 0; // Déconnecter l'utilisateur
                } else {
                    System.out.println(" Error");
                }

            } catch (Exception e) {
                System.out.println(" Deleting Error : " + e.getMessage());
            }
        } else {
            System.out.println(" Cancelled");
        }
    }

    //********************************************************************************************************************

    public void AddingFlight() {
        /*
        if (currentAdminId == 0) {
            System.out.println("❌ You must be logged in!");
            return;
        }
    */
        Flight flight = new Flight();
        flight.addFlight();
    }
    //********************************************************************************************************************


    public void DisplayAllFlights() {
        String sql = "SELECT * FROM book_flight";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n============================================================================");
            System.out.println("✈️  LIST OF ALL AVAILABLE FLIGHTS");
            System.out.println("===============================================================================");

            boolean hasFlights = false;
            while (rs.next()) {
                hasFlights = true;
                System.out.println("----------------------------------------------");
                System.out.println("--------------------------------- Flight ID: " + rs.getInt("id"));
                System.out.println("---------------------------------  Schedule: " + rs.getString("flight_schedule"));
                System.out.println("---------------------------------  Flight No: " + rs.getString("flight_no"));
                System.out.println("---------------------------------  Seats Available: " + rs.getInt("available_seats"));
                System.out.println("---------------------------------  Departure: " + rs.getString("dep"));
                System.out.println("---------------------------------  Destination: " + rs.getString("dest"));
                System.out.println("---------------------------------  Arrival Time: " + rs.getString("arrival_time"));
                System.out.println("⏱---------------------------------  Duration: " + rs.getString("flight_time"));
                System.out.println("---------------------------------  Gate : " + rs.getString("gate"));
            }

            if (!hasFlights) {
                System.out.println("  No flights found in the database.");
            }

            System.out.println("==============================================");

        } catch (SQLException e) {
            System.out.println(" Database error while retrieving flights: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
        }
    }

    //********************************************************************************************************************


    public void ViewScheduledFlights() {
        String sql = "SELECT r.id AS reservation_id, c.name, c.email, " +
                "f.flight_no, f.dep, f.dest, f.flight_schedule, f.arrival_time, f.flight_time " +
                "FROM reservations r " +
                "JOIN users c ON r.user_id = c.id " +
                "JOIN book_flight f ON r.flight_id = f.id " +
                "ORDER BY f.flight_schedule ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n====================================================================================");
            System.out.println("\n                                                                                    ");

            System.out.println("🛫 ---------------------- LIST OF BOOKED FLIGHTS (RESERVATIONS) --------------------  ");
            System.out.println("\n                                                                                    ");

            System.out.println("======================================================================================");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                System.out.println("---------------------------------  Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("---------------------------------  Customer: " + rs.getString("name") + " " + rs.getString("email"));
                System.out.println("---------------------------------  Flight No: " + rs.getString("flight_no"));
                System.out.println("---------------------------------  From: " + rs.getString("dep") + " ➡️ To: " + rs.getString("dest"));
                System.out.println("---------------------------------  Schedule: " + rs.getString("flight_schedule"));
                System.out.println("---------------------------------  Arrival Time: " + rs.getString("arrival_time"));
                System.out.println("---------------------------------  ️ Duration: " + rs.getString("flight_time"));
            }

            if (!hasData) {
                System.out.println("⚠️  No booked flights found in the database.");
            }

            System.out.println("==============================================");

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while retrieving scheduled flights: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
    }


    //********************************************************************************************************************

// DELETE A FLIGHT
// ===============================
    public void deleteFlight() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n🗑️  DELETE A FLIGHT");

        System.out.print("Enter the Flight Number to delete (e.g., KE-94): ");
        String flightNo = sc.nextLine();

        String sqlCheck = "SELECT * FROM book_flight WHERE flight_no = ?";
        String sqlDelete = "DELETE FROM book_flight WHERE flight_no = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            checkStmt.setString(1, flightNo);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("⚠️  No flight found with number: " + flightNo);
                return;
            }

            System.out.println("\nFlight found:");
            System.out.println("From: " + rs.getString("dep") + " → To: " + rs.getString("dest"));
            System.out.println("Schedule: " + rs.getString("flight_schedule"));
            System.out.print("Are you sure you want to delete this flight? (yes/no): ");
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("❎ Deletion cancelled.");
                return;
            }

            try (PreparedStatement delStmt = conn.prepareStatement(sqlDelete)) {
                delStmt.setString(1, flightNo);
                int rows = delStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("✅ Flight successfully deleted!");
                } else {
                    System.out.println("⚠️  Failed to delete flight.");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while deleting flight: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
    }



}