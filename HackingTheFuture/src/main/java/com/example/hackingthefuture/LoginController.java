package com.example.hackingthefuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Button button_forgotpw;

    @FXML
    private Button button_login;

    @FXML
    private Button button_signup;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_username;

    private Stage stage;

    private Scene scene;

    @FXML
    void login(ActionEvent event) throws IOException, MessagingException {
        String usernameOrEmail = tf_username.getText();
        String password = pf_password.getText();
        if(usernameOrEmail.isEmpty() || password.isEmpty()){
            Function.warning("Login Error",null,"Please fill in the username/email and password.");
        }else{
            String sqlQuery = "SELECT * FROM user WHERE username='"+usernameOrEmail+"' OR email='" + usernameOrEmail +"' AND password='"+Function.hashPassword(password)+"'";
            ResultSet resultSet = Function.getData(sqlQuery);
            try {
                if(resultSet.next()){
//                    String code = Function.generateRandomNumber();
//                    PassData data = PassData.getInstance();
//                    data.setCode(code);
//                    data.setResend(3);
                    LoginDetail login = LoginDetail.getInstance();
                    login.setLogin(false);
                    login.setEmail(resultSet.getString("email"));
                    login.setPassword(Function.hashPassword(password));
                    login.setUsername(resultSet.getString("username"));
//                    JavaMail.sendmail(resultSet.getString("email"),"The login verification code is : " + code,"Login Verification");
                    Function.inform("Login Successful",null,"Succesfully logged in!");
                    Node sourceNode = (Node) event.getSource();
                    PassData data = PassData.getInstance();
                    data.setUsername(resultSet.getString("username"));
                    data.setEmail(resultSet.getString("email"));
                    data.setRole(resultSet.getString("role"));
                    data.setxCoordinate(resultSet.getDouble("coordinateX"));
                    data.setyCoordinate(resultSet.getDouble("coordinateY"));
                    data.setPoints(resultSet.getInt("points"));
                    String role=data.getRole();
                    if(role.equals("Educator")){
                        Function.nextPage("Educator.fxml",sourceNode,"Educator");
                    }
                    else if(role.equals("Young Student")){
                        //...
                    } else {
                        //...
                    }

                }else{
                    Function.inform("Login Error",null,"Incorrect username/email and password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void sign(ActionEvent event) throws IOException {

        Node sourceNode = (Node) event.getSource();
        Function.nextPage("Signup.fxml",sourceNode,"Signup");
    }

    @FXML
    void forgot(ActionEvent event) throws IOException {
        Node sourceNode = (Node) event.getSource();
        Function.nextPage("ForgotPassword.fxml",sourceNode,"Forgot Password");
    }
}
