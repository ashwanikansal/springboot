package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "src/main/java/org/example/localDb/users.json";

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUsers();
    }

    private void loadUsers() throws IOException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public User loginUser(String name, String password) {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(name) && UserServiceUtil.checkPassword(password, user1.getHashedPassword());
        }).findFirst();
        foundUser.ifPresent(value -> user = value);
        return foundUser.orElse(null);
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    public Boolean cancelBooking(Ticket ticket) throws IOException{
        boolean cancelled = user.getTicketsBooked().removeIf(t -> t.getTicketId().equals(ticket.getTicketId()));
        TrainService trainService = new TrainService();
        if (cancelled) {
            Train train = ticket.getTrain();
            System.out.println("Ticket with ID " + ticket.getTicketId() + " has been canceled.");
            saveUserListToFile();
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticket.getTicketId());
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int col, Ticket newTicket) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row>=0 && row<=seats.size() && col>=0 && col<seats.get(row).size()) {
                if(seats.get(row).get(col) == 0) {
                    seats.get(row).set(col, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    List<Ticket> tickets = user.getTicketsBooked();
                    if(tickets.isEmpty()) tickets = new ArrayList<>();
                    tickets.add(newTicket);
                    user.setTicketsBooked(tickets);

                    for (int i = 0; i < userList.size(); i++) {
                        User u = userList.get(i);
                        if (u.getUserId().equalsIgnoreCase(user.getUserId())) { // or use equals if String
                            userList.set(i, user);
                            break; // stop after updating the matched user
                        }
                    }
                    saveUserListToFile();

                    return true;
                } else {
                    return false; // seat already booked
                }
            } else {
                return false; // Invalid row or col index
            }
        } catch (IOException e) {
            System.out.println((e.getMessage()));
            return Boolean.FALSE;
        }
    }
}
