package com.myapp.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import static com.myapp.implement.Customers.currentUserId;

public class Users {
    //private int currentUserId;
    public void viewMyReservations() {
        //System.out.println("DEBUG currentUserId = " + currentUserId);

        String sql = "SELECT r.id AS res_id, r.flight_no, r.seats_booked, r.status, r.reservation_date, " +
                "f.dep, f.dest, f.flight_schedule " +
                "FROM reservations r JOIN book_flight f ON r.flight_id = f.id " +
                "WHERE r.user_id = ? ORDER BY f.flight_schedule";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            boolean hasReservations = false;

            System.out.println("\n ************* MY RESERVATIONS ************* ");
            System.out.println("----------------------------------");

            while (rs.next()) {
                hasReservations = true;

                System.out.println("--------------------------------- Reservation ID : " + rs.getInt("res_id"));
                System.out.println("--------------------------------- Flight : " + rs.getString("flight_no"));
                System.out.println("--------------------------------- From : " + rs.getString("dep") + " → To : " + rs.getString("dest"));
                System.out.println("--------------------------------- Flight Date : " + rs.getString("flight_schedule"));
                System.out.println("--------------------------------- Seats Booked : " + rs.getInt("seats_booked"));
                System.out.println("--------------------------------- Status : " + rs.getString("status"));
                System.out.println("--------------------------------- Bookig date : " + rs.getString("reservation_date"));
                System.out.println("------------------------------------------------------------------- --------------------------------- --------------------------------- ");
            }

            if (!hasReservations) {
                System.out.println("You ain't got a reservation.");
            }

        } catch (Exception e) {
            System.out.println(" Error while printing : " + e.getMessage());
        }
    }

    //*************************************************************************************************************************************

    public void CancelFlight() {
        if (currentUserId == 0) {
            System.out.println("You should be connected to cancel a flight");
            return;
        }

        Scanner sc = new Scanner(System.in);

        String userReservationsSql = "SELECT r.id AS res_id, r.flight_id, r.flight_no, r.seats_booked, r.status, " +
                "f.departure_city, f.arrival_city, f.flight_schedule " +
                "FROM reservations r JOIN book_flight f ON r.flight_id = f.id " +
                "WHERE r.user_id = ?";

        String deleteReservationSql = "DELETE FROM reservations WHERE id = ?";
        String restoreSeatsSql = "UPDATE book_flight SET available_seats = available_seats + ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            // 🔥 1. Affiche les réservations du user

            try (PreparedStatement stmt = conn.prepareStatement(userReservationsSql)) {
                stmt.setInt(1, currentUserId);
                ResultSet rs = stmt.executeQuery();

                boolean hasReservations = false;
                System.out.println("\n✈ YOUR RESERVATIONS :");
                System.out.println("----------------------------------");

                while (rs.next()) {
                    hasReservations = true;
                    System.out.println("---------------------------------  Réservation ID: " + rs.getInt("res_id"));
                    System.out.println("---------------------------------  Flight: " + rs.getString("flight_no"));
                    System.out.println("---------------------------------  From: " + rs.getString("dep") + " → " + rs.getString("dest"));
                    System.out.println("---------------------------------  Date: " + rs.getString("flight_schedule"));
                    System.out.println("---------------------------------  Seats Booked: " + rs.getInt("seats_booked"));
                    System.out.println("---------------------------------  Status: " + rs.getString("status"));
                    System.out.println("---------------------------------------------------------- --------------------------------- --------------------------------- ---------");
                }

                if (!hasReservations) {
                    System.out.println("No reservation found !! .");
                    return;
                }
            }

            //
            System.out.print("Enter the ID of the reservation to  cancel : ");
            int resId = sc.nextInt();
            sc.nextLine();

            //
            String getSeatInfo = "SELECT flight_id, seats_booked FROM reservations WHERE id = ? AND user_id = ?";
            int flightId = 0;
            int seatsBooked = 0;

            try (PreparedStatement stmt = conn.prepareStatement(getSeatInfo)) {
                stmt.setInt(1, resId);
                stmt.setInt(2, currentUserId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    flightId = rs.getInt("flight_id");
                    seatsBooked = rs.getInt("seats_booked");
                } else {
                    System.out.println("Reservation not found .");
                    return;
                }
            }

            // Confirmer
            System.out.print("Confirm canceling ? (yes/no): ");
            String confirm = sc.nextLine();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Cancelling.");
                return;
            }

            // 🔥 3. Supprimer & restaurer places
            conn.setAutoCommit(false);

            try (PreparedStatement del = conn.prepareStatement(deleteReservationSql)) {
                del.setInt(1, resId);
                del.executeUpdate();
            }

            try (PreparedStatement restore = conn.prepareStatement(restoreSeatsSql)) {
                restore.setInt(1, seatsBooked);
                restore.setInt(2, flightId);
                restore.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Reservation canceled !");

        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }

}
