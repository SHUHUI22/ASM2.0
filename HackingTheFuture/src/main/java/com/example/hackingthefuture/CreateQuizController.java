package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateQuizController {
    @FXML
    public TextField quizTitleTF;
    public TextField quizDescriptionTF;
    public MenuButton quizThemeMENU;
    public MenuItem Science;
    public MenuItem Technology;
    public MenuItem Engineering;
    public MenuItem Mathematics;
    public TextField quizLinkTF;
    public Connection con;

    String selectedTheme;

    @FXML
    public void initialize() {
        try {
            String SUrl = "jdbc:mysql://localhost:3306/hackingthefuture";
            String SUser = "root";
            String SPass = "";
            con = DriverManager.getConnection(SUrl, SUser, SPass);
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
        Function.nextPage("ViewProfile.fxml",sourceNode,"Profile");
    }

    @FXML
    public void saveBTN(ActionEvent actionEvent) {
        try {
            PassData data = PassData.getInstance();
            String username=data.getUsername();
            String quizTitle=quizTitleTF.getText();
            String quizDescription=quizDescriptionTF.getText();
            if (quizTitle.isEmpty()) {
                Function.warning("Warning","","Quiz Title is required!");
            } else if (quizDescription.isEmpty()) {
                Function.warning("Warning","","Quiz Description is required!");
            } else if (quizThemeMENU == null) {
                Function.warning("Warning","","Please select a theme!");
            } else if (quizLinkTF.getText().isEmpty()) {
                Function.warning("Warning","","Please upload quiz link!");
            } else {
                int numberOfQuiz=1;
                String query = "SELECT * FROM createQuiz WHERE username=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    numberOfQuiz= rs.getInt("numberOfQuiz")+1;
                }
                query = "INSERT INTO createQuiz (username,quizTitle,quizDescription,selectedTheme,quizLink,numberOfQuiz) VALUES (?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, quizTitle);
                ps.setString(3, quizDescription);
                ps.setString(4, selectedTheme);
                ps.setString(5, quizLinkTF.getText());
                ps.setInt(6, numberOfQuiz);
                ps.executeUpdate();
                quizTitleTF.setText("");
                quizDescriptionTF.setText("");
                quizThemeMENU.setText("Quiz Theme");
                quizLinkTF.setText("");
                Function.success("Success","","Save Successfully!");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void scienceSelected(ActionEvent actionEvent) {
        selectedTheme = "Science";
        quizThemeMENU.setText(selectedTheme);
    }

    public void technologySelected(ActionEvent actionEvent) {
        selectedTheme = "Technology";
        quizThemeMENU.setText(selectedTheme);
    }

    public void engineeringSelected(ActionEvent actionEvent) {
        selectedTheme = "Engineering";
        quizThemeMENU.setText(selectedTheme);
    }

    public void mathematicsSelected(ActionEvent actionEvent) {
        selectedTheme = "Mathematics";
        quizThemeMENU.setText(selectedTheme);
    }

    public void goToLink(ActionEvent actionEvent) throws IOException {
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        desktop.browse(java.net.URI.create(quizLinkTF.getText()));
    }
}

