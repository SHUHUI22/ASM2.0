package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

public class ViewProfileController {
    @FXML
    public Label nameLabel;
    public Label roleLabel;
    public Label emailLabel;
    public Label coordinateLabel;
    public Label label4;
    public Label label2R;
    public Label label3;

    public Connection con;

    @FXML
    public void initialize() {
        PassData data = PassData.getInstance();
        String username=data.getUsername();
        String email=data.getEmail();
        double coordinateX=data.getxCoordinate();
        double coordinateY=data.getyCoordinate();
        String role=data.getRole();
        profile(username,email,coordinateX,coordinateY,role);
    }

    public void profile(String username,String email,double coordinateX,double coordinateY,String role){
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
            nameLabel.setText("Username: "+username);
            emailLabel.setText("Email: "+email);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            coordinateLabel.setText("Coordinate: "+decimalFormat.format(coordinateX)+","+decimalFormat.format(coordinateY));
            roleLabel.setText("Role: "+role);
            switch (role){
                case "Educator":
                    int numberOfEvent=0;
                    int numberOfQuiz=0;
                    String query = "SELECT * FROM createEvent WHERE username=?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1,username);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        numberOfEvent = rs.getInt("numberOfEvent");
                    }
                    label3.setText("Number of events created: "+numberOfEvent);
                    query = "SELECT * FROM createQuiz WHERE username=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1,username);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        numberOfQuiz = rs.getInt("numberOfQuiz");
                    }
                    label4.setText("Number of quiz created: "+numberOfQuiz);
                    label2R.setVisible(false);
                    break;
                case "Parent":
                    label2R.setVisible(false);
                    label3.setText("Booking history: ");
                    label4.setText("Children: ");
                    // ...
                    break;
                case "Young Student":
                    int points=0;
                    query = "SELECT * FROM user WHERE username=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1,username);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        points = rs.getInt("points");
                    }
                    label2R.setVisible(true);
                    label2R.setText("Points: "+points);
                    label3.setText("Parents: ");
                    label4.setText("Friends: ");
                    // ...
                    break;
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
}
