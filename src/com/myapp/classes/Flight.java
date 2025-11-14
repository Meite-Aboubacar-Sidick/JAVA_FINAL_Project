package com.myapp.classes;

import java.sql.*;
import java.util.Scanner;

public class Flight {

    // Ajouter un nouveau vol
    public void addFlight() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n✈  Adding a flight");

        System.out.print("Date and hour (ex: Wednesday, 11 May 2022, 17:45 PM): ");
        String flightSchedule = scanner.nextLine();

        System.out.print("Number of flight (ex: KE-94): ");
        String flightNumber = scanner.nextLine();

        System.out.print("Number of seats available: ");
        int availableSeats = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer

        System.out.print("Depature city: ");
        String departure = scanner.nextLine();

        System.out.print("Arrival city: ");
        String arrival = scanner.nextLine();

        System.out.print("Arrival time (ex: Thu, 12-06-2022 07:45 AM): ");
        String arrivalTime = scanner.nextLine();

        System.out.print("Flight duration (ex: 14:00 hrs): ");
        String flightTime = scanner.nextLine();

        System.out.print("Flight date (ex: N-14): ");
        String date = scanner.nextLine();

        System.out.print("gate (ex: 6:00.62 / 11899.23): ");
        String distance = scanner.nextLine();

        String sql = "INSERT INTO book_flight (flight_schedule, flight_no, available_seats, " +
                "dep, dest, arrival_time, flight_time, gate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flightSchedule);
            pstmt.setString(2, flightNumber);
            pstmt.setInt(3, availableSeats);
            pstmt.setString(4, departure);
            pstmt.setString(5, arrival);
            pstmt.setString(6, arrivalTime);
            pstmt.setString(7, flightTime);
            pstmt.setString(8, date);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Flight added successfully !");
            } else {
                System.out.println("❌ Failed to add Flight !");
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Code d'erreur pour duplicate entry
                System.out.println("❌ This Flight already exists !");
            } else {
                System.out.println("❌ Erreur SQL: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    //********************************************************************************************************************

    // Afficher tous les vols
    public void displayAllFlights() {
        String sql = "SELECT * FROM book_flight ORDER BY flight_schedule";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📋 LISTE DES VOLS");
            System.out.println("==================================================================================================================================");
            //
            System.out.printf("| %-2s | %-35s | %-10s | %-4s | %-12s | %-12s | %-25s | %-10s |\n",
                    "ID", "Hours", "Flihts", "Seats", "Depature_city", "destination", "Dest Hour", "Duration");
            System.out.println("==================================================================================================================================");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                //
                System.out.printf("| %-2d | %-35s | %-10s | %-4d | %-12s | %-12s | %-10s | %-8s |\n",
                        rs.getInt("id"),
                        rs.getString("flight_schedule"),
                        rs.getString("flight_no"),
                        rs.getInt("available_seats"),
                        rs.getString("dep"),
                        rs.getString("dest"),
                        rs.getString("arrival_time"),
                        rs.getString("flight_time"));
            }

            if (!hasData) {
                System.out.println("---------------------------------  NO FLIGHT AVAILABLE !  --------------------------------- ");
            }
            System.out.println("==================================================================================================================================");

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'affichage des vols: " + e.getMessage());
        }
    }


}
