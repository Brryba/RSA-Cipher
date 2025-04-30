module rsa_cipher {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires spring.beans;


    opens rsa_cipher to javafx.fxml;
    exports rsa_cipher;
    exports rsa_cipher.controller;
    opens rsa_cipher.controller to javafx.fxml;
}