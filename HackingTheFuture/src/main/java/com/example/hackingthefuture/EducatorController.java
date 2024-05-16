package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
public class EducatorController extends ViewProfileController{
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameSearchTF;

    public void initialize() {
        PassData data = PassData.getInstance();
        nameLabel.setText(data.getUsername());
    }

    @FXML
    public void viewProfileBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ViewProfile.fxml",sourceNode,"Profile");
    }

    @FXML
    public void viewOtherProfile(ActionEvent actionEvent) {
        String nameSearch=nameSearchTF.getText();
        boolean found=false;
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
            String email="";
            double coordinateX;
            double coordinateY;
            String role="";
            String query = "SELECT * FROM user WHERE username=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,nameSearch);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                email=rs.getString("email");
                coordinateX=rs.getDouble("coordinateX");
                coordinateY=rs.getDouble("coordinateY");
                role=rs.getString("role");
                found=true;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ViewProfileController viewProfileController = fxmlLoader.getController();
                viewProfileController.profile(nameSearch,email,coordinateX,coordinateY,role); // Pass searched username to ViewProfileController
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) nameSearchTF.getScene().getWindow();
                currentStage.close();
            }
            if (!found){
                Function.warning("Warning","","User not found");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEventBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("CreateEvent.fxml",sourceNode,"Create Event");
    }

    public void createQuizBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("CreateQuiz.fxml",sourceNode,"Create Quiz");
    }

    public void viewEventBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ViewEvent.fxml",sourceNode,"Login");
    }

    public void logoutBTN(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Login.fxml",sourceNode,"Login");
    }
}

