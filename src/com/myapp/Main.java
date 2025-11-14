package com.myapp;

import com.myapp.implement.Admin;
import com.myapp.implement.Customers;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Customers customer = new Customers();
        Admin admin = new Admin();

        while (true) {
            System.out.println("\n==========================================================================================");
            System.out.println("\n                                                                                          ");

            System.out.println("  --------------------------------- WELCOME TO SDK AIRLINE---------------------------------  ");
            System.out.println("\n                                                                                          ");

            System.out.println("=============================================================================================");
            System.out.println("1. --------------------------------- Customer Signup ---------------------------------");
            System.out.println("2. --------------------------------- Customer Login  ---------------------------------");
            System.out.println("3. --------------------------------- Admin Login     ---------------------------------");
            System.out.println("4. --------------------------------- Exit            ---------------------------------");
            System.out.print("➡️  ---------------------------------  Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    customer.signup();
                    break;

                case 2:
                    if (customer.login()) { // ✅ currentUserId sera défini dans login()
                        customerMenu(customer);
                    } else {
                        System.out.println(" Login failed. Please try again.");
                    }
                    break;

                case 3:
                    if (admin.login()) {
                        adminMenu(admin);
                    } else {
                        System.out.println(" Admin login failed.");
                    }
                    break;

                case 4:
                    System.out.println(" Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println(" Invalid option. Try again.");
            }
        }
    }

    // =============================================================
    // CUSTOMER MENU
    // =============================================================
    private static void customerMenu(Customers customer) {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n==========================================================================");
            System.out.println("\n                                                                                          ");

            System.out.println(" --------------------------------- CUSTOMER DASHBOARD ---------------------------------");
            System.out.println("\n                                                                                          ");

            System.out.println("=============================================================================================");
            System.out.println("1. --------------------------------- View available flights ---------------------------------");
            System.out.println("2. --------------------------------- Reserve a flight       ----------------------------------");
            System.out.println("3. --------------------------------- View my reservations   ----------------------------------");
            System.out.println("4. --------------------------------- Cancel a reservation   ---------------------------------");
            System.out.println("5. --------------------------------- Update my profile      ---------------------------------");
            System.out.println("6. --------------------------------- Delete my account      ----------------------------------");
            System.out.println("7. --------------------------------- Logout                 ----------------------------------");
            System.out.print("➡  ----------------------------------- Choose:               ");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    customer.DisplayMyFlights();
                    break;

                case 2:
                    customer.reserveFlight();
                    break;

                case 3:
                    // ✅ NE PAS relancer login() ici, on garde l’utilisateur connecté
                    customer.viewMyReservations();
                    break;

                case 4:
                    customer.CancelFlight();
                    break;

                case 5:
                    customer.update();
                    break;

                case 6:
                    customer.delete();
                    break;

                case 7:
                    System.out.println(" Logging out...");
                    break;

                default:
                    System.out.println(" Invalid choice.");
            }

        } while (option != 7);
    }

    // =============================================================
// ADMIN MENU
// =============================================================
    private static void adminMenu(Admin admin) {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=================================================================================");
            System.out.println("\n                                                                                          ");

            System.out.println(" --------------------------------- ADMIN DASHBOARD --------------------------------- ");
            System.out.println("\n                                                                                          ");

            System.out.println("===================================================================================");
            System.out.println("1. --------------------------------- Add a flight                       ---------------------------------");
            System.out.println("2. --------------------------------- View all available flights         ---------------------------------");
            System.out.println("3. --------------------------------- View booked flights (reservations) ---------------------------------");
            System.out.println("4. --------------------------------- Delete a flight                    ----------------------------------");
            System.out.println("5. --------------------------------- Update admin account               ---------------------------------");
            System.out.println("6. --------------------------------- Delete admin account               ---------------------------------");
            System.out.println("7. --------------------------------- Logout                             ---------------------------------");
            System.out.print("➡ Choose: ");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    admin.AddingFlight();
                    break;
                case 2:
                    admin.DisplayAllFlights();
                    break;
                case 3:
                    admin.ViewScheduledFlights();
                    break;
                case 4:
                    admin.deleteFlight();
                    break;
                case 5:
                    admin.update();
                    break;
                case 6:
                    admin.delete();
                    break;
                case 7:
                    System.out.println(" Logging out...");
                    break;
                default:
                    System.out.println("️ Invalid choice. Try again.");
            }

        } while (option != 7);
    }

}
