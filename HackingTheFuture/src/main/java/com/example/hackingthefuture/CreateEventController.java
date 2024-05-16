package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class CreateEventController {
    @FXML
    public TextField eventTitleTF;
    public TextField eventDescriptionTF;
    public TextField eventVenueTF;
    public DatePicker eventDateTF;
    public TextField startTimeTF;
    public TextField endTimeTF;
    public Connection con;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewProfileBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
    }

    @FXML
    public void backBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Educator.fxml",sourceNode,"Educator");
    }

    @FXML
    public void saveBTN(ActionEvent actionEvent) {
        PassData data = PassData.getInstance();
        String username=data.getUsername();
        String eventTitle = eventTitleTF.getText();
        String eventDescription = eventDescriptionTF.getText();
        String eventVenue = eventVenueTF.getText();
        LocalDate eventDate =eventDateTF.getValue();
        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.parse(startTimeTF.getText());
            endTime = LocalTime.parse(endTimeTF.getText());
        }
        catch (DateTimeParseException e) {
            Function.warning("Warning","","Please enter a valid Event Time!");
            return; // Exit the method since the time is not valid
        }
        try{
            if(eventTitle.isEmpty()){
                Function.warning("Warning","","Event Title is required!");
            }
            else if (eventDescription.isEmpty()) {
                Function.warning("Warning","","Event Description is required!");
            }
            else if (eventVenue.isEmpty()) {
                Function.warning("Warning","","Event Venue is required!");
            }
            else if(eventDate==null){
                Function.warning("Warning","","Please select Event Date!");
            }
            else{
                int numberOfEvent=1;
                String query = "SELECT * FROM createEvent WHERE username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    numberOfEvent = rs.getInt("numberOfEvent")+1;
                }

                query = "INSERT INTO createEvent (username,eventTitle,eventDescription,eventVenue,eventDate,startTime,endTime,numberOfEvent) VALUES (?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, eventTitle);
                ps.setString(3, eventDescription);
                ps.setString(4, eventVenue);
                ps.setDate(5, java.sql.Date.valueOf(eventDate));
                ps.setTime(6, java.sql.Time.valueOf(startTime));
                ps.setTime(7, java.sql.Time.valueOf(endTime));
                ps.setInt(8, numberOfEvent);
                ps.executeUpdate();
                eventTitleTF.setText("");
                eventDescriptionTF.setText("");
                eventVenueTF.setText("");
                eventDateTF.setValue(null);
                startTimeTF.setText(null);
                endTimeTF.setText(null);
                Function.success("Success","","Save Successfully!");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
}

