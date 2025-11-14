package com.myapp.classes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Booking {
    private int currentUserId;


    public void setCurrentUserId(int id) {
        this.currentUserId = id;
    }

    //****************************************************************************************************************************
    public void bookFlight() {
        if (currentUserId == 0) {
            System.out.println(" You should login first!");
            return;
        }
        System.out.println("\n --------------------------------- BOOK A FLIGHT --------------------------------- ");
        // Afficher les vols disponibles
        //displayAllFlights();
        System.out.print("\nEnter the number of the flight (eg: KE-94): ");
        Scanner sc = new Scanner(System.in);
        String flightNumber = sc.nextLine();

        System.out.print("Number of seats: ");
        int seats = sc.nextInt();
        sc.nextLine(); // Nettoyer le buffer

        String checkFlightSql = "SELECT id, available_seats, dep, dest, flight_schedule FROM book_flight WHERE flight_no = ?";
        String insertReservationSql = "INSERT INTO reservations (user_id, flight_id, flight_no, seats_booked) VALUES (?, ?, ?, ?)";
        String updateFlightSql = "UPDATE book_flight SET available_seats = available_seats - ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Démarrer la transaction

            try {
                // Vérifier le vol et les places disponibles
                int flightId;
                int availableSeats;
                String departure, arrival, schedule;

                try (PreparedStatement checkStmt = conn.prepareStatement(checkFlightSql)) {
                    checkStmt.setString(1, flightNumber);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        flightId = rs.getInt("id");
                        availableSeats = rs.getInt("available_seats");
                        departure = rs.getString("dep");
                        arrival = rs.getString("dest");
                        schedule = rs.getString("flight_schedule");

                        if (seats > availableSeats) {
                            System.out.println("insufficiant seats! Available: " + availableSeats);
                            return;
                        }
                        // Afficher récapitulatif
                        System.out.println("\n --------------------------------- WRAP UP ! --------------------------------- ");
                        System.out.println("========================");
                        System.out.println("--------------------------------- Flight: " + flightNumber);
                        System.out.println("--------------------------------- From: " + departure + " → To: " + arrival);
                        System.out.println("--------------------------------- Date: " + schedule);
                        System.out.println("--------------------------------- Seats: " + seats);

                        System.out.println("========================");

                        System.out.print("Confirm? (yes/no): ");
                        String confirm = sc.nextLine();

                        if (!confirm.equalsIgnoreCase("yes")) {
                            System.out.println(" Booking cancelled!");
                            return;
                        }
                        // Insérer la réservation
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertReservationSql)) {
                            insertStmt.setInt(1, currentUserId);
                            insertStmt.setInt(2, flightId);
                            insertStmt.setString(3, flightNumber);
                            insertStmt.setInt(4, seats);
                            insertStmt.executeUpdate();
                        }

                        // Mettre à jour les places
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateFlightSql)) {
                            updateStmt.setInt(1, seats);
                            updateStmt.setInt(2, flightId);
                            updateStmt.executeUpdate();
                        }
                        conn.commit();

                    } else {
                        System.out.println("Flight unfound!");
                    }
                }

            } catch (SQLException e) {
                conn.rollback();
                System.out.println(" Error: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("connexion Error: " + e.getMessage());
        }

    }

    //****************************************************************************************************************************


    public void ViewScheduledFlights() {
        System.out.println("\n --------------------------------- LIST OF AVAILABLE FLIGHTS --------------------------------- ");
        System.out.println("----------------------------------");

        String sql = "SELECT flight_no, dep, dest, flight_schedule, available_seats " +
                "FROM book_flight ORDER BY flight_schedule ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean hasFlights = false;
            while (rs.next()) {
                hasFlights = true;
                System.out.println("--------------------------------- Flight: " + rs.getString("flight_no"));
                System.out.println("--------------------------------- Frm: " + rs.getString("dep") + " → TO: " + rs.getString("dest"));
                System.out.println("--------------------------------- Date: " + rs.getString("flight_schedule"));
                System.out.println("--------------------------------- Available seats: " + rs.getInt("available_seats"));
                System.out.println("-------------------------------------------- --------------------------------- --------------------------------- -----------------------");
            }

            if (!hasFlights) {
                System.out.println(" No Flight planned!!");
            }

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }



}
