package org.example;

import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        User user = null;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            System.out.println("There is something wrong");
            System.out.println(e.getMessage());
            return;
        }
        while(option!=7) {
            System.out.println("Choose option : ");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the app");

            option = scanner.nextInt();
            Train trainSelectedForBooking = null;
            String source = "";
            String dest = "";

            switch (option) {
                case 1: //signup
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignup = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                    if(userBookingService.signUp(userToSignup)){
                        System.out.println("Signed Up successfully.");
                    } else {
                        System.out.println("Error in sign up. Try again.");
                    }
                    break;
                case 2: // login
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
//                    User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
                    try{
                        userBookingService = new UserBookingService();
                        user = userBookingService.loginUser(nameToLogin, passwordToLogin);
                        if(user != null) {
                            System.out.println("Welcome "+nameToLogin);
                        } else {
                            System.out.println("Invalid credentials");
                        }
                    }catch (IOException ex){
                        return;
                    }
                    break;
                case 3:
                    if(user != null) {
                        System.out.println("Fetching your bookings");
                        userBookingService.fetchBooking();
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 4:
                    System.out.println("Type your source station");
                    source = scanner.next();
                    System.out.println("Type your destination station");
                    dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    if(trains.isEmpty()) {
                        System.out.println("Sorry! No trains on this route.");
                        break;
                    } else {
                        int index = 1;
                        for (Train t : trains) {
                            System.out.println(index + " Train id : " + t.getTrainId());
                            for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                                System.out.println("\t station " + entry.getKey() + " time: " + entry.getValue());
                            }
                            ++index;
                            System.out.println();
                        }

                        System.out.println("Select a train by typing a number from 1 to "+trains.size());

                        int t = scanner.nextInt() - 1;
                        if (t >= 0 && t < trains.size()) {
                            trainSelectedForBooking = trains.get(t);
                            System.out.println("Book Ticket : (y/n)");
                            String bookTicket = scanner.next();
                            if(bookTicket.equalsIgnoreCase("y")) {
                                if(user == null) {
                                    System.out.println("Login to book a ticket.");
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        else {
                            System.out.println("Wrong train index entered!");
                            break;
                        }
                    }
                case 5:
                    if(trainSelectedForBooking == null) {
                        System.out.println("Select a train first to book ticket");
                        break;
                    }
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row: seats){
                        for (Integer val: row){
                            System.out.print(val+" ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat....");

                    // Generating Ticket
                    Ticket newTicket = new Ticket(UUID.randomUUID().toString(), user.getUserId(), source, dest, new Date(), trainSelectedForBooking);
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col, newTicket);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;
                case 6:
                    if(user==null) {
                        System.out.println("Please login first");
                        break;
                    }
                    List<Ticket> bookedTickets = user.getTicketsBooked();
                    if(bookedTickets.isEmpty()) {
                        System.out.println("No tickets booked yet");
                    } else {
                        for(int i=0; i<bookedTickets.size(); ++i) {
                            System.out.println((i+1)+" \n"+bookedTickets.get(i).getTicketInfo());
                        }
                    }
                    System.out.println("Enter index for which you want to cancel the booking (1,2,...) :");
                    int index = Integer.parseInt(scanner.next())-1;
                    try {
                        userBookingService.cancelBooking(bookedTickets.get(index));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                default:
                    break;

            }
        }
    }
}