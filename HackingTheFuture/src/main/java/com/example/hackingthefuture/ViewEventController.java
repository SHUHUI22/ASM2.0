package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.PriorityQueue;

public class ViewEventController {
    @FXML
    public Button registerBTN1;
    public Button registerBTN2;
    public Button registerBTN3;
    public Label eventTF1;
    public Label eventTF2;
    public Label eventTF3;

    public Connection con;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);

            PassData data = PassData.getInstance();
            LocalDate currentDate = LocalDate.now();
            Events events;
            PriorityQueue<Events> eventList = new PriorityQueue<>();

            String query = "SELECT * FROM createEvent ";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                events = new Events();
                LocalDate eventDate = rs.getDate("eventDate").toLocalDate();
                if(eventDate.isEqual(currentDate)||eventDate.isAfter(currentDate)) {
                    events.setEventDate(eventDate);
                    events.setEventTitle(rs.getString("eventTitle"));
                    events.setEventDescription(rs.getString("eventDescription"));
                    events.setEventVenue(rs.getString("eventVenue"));
                    events.setStartTime(rs.getTime("startTime").toLocalTime());
                    events.setEndTime(rs.getTime("endTime").toLocalTime());
                    eventList.offer(events);
                }
            }
            eventTF1.setText(eventList.poll().toString());
            eventTF2.setText(eventList.poll().toString());
            eventTF3.setText(eventList.poll().toString());
            if(data.getRole().equals("Educator")){
                registerBTN1.setVisible(false);
                registerBTN2.setVisible(false);
                registerBTN3.setVisible(false);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void backBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Educator.fxml",sourceNode,"Educator");
    }

    public void viewProfileBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
    }
}

