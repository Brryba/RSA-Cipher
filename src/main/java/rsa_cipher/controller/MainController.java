package rsa_cipher.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import rsa_cipher.rsa_cipher.RSACipher;
import rsa_cipher.utils.InputValidator;

import java.util.List;

@Controller
public class MainController {
    @FXML
    private TextField pInput;
    @FXML
    private TextField qInput;
    @FXML
    private TextField closedKeyInput;

    @FXML
    private TextField rOutput;
    @FXML
    private TextField eulerOutput;
    @FXML
    private TextField openKeyOutput;

    @FXML
    private TextArea plainTextOutput;
    @FXML
    private TextArea cipherTextOutput;

    @FXML
    private Button calcultateButton;
    @FXML
    private Button cipherButton;
    @FXML
    private Button encipherButtom;

    private InputValidator inputValidator;
    private RSACipher rsaCipher;

    private int p;
    private int q;
    private int r;
    private int rEuler;
    private int openKey;
    private int closedKey;
    private boolean isDataCorrect;

    private List<Byte> plainBytes;
    private List<Byte> cipherBytes;

    @FXML
    public void initialize() {
        calcultateButton.setOnAction(_ -> handleCalculateButton());
        cipherButton.setOnAction(_ -> handleCipherButton());
        encipherButtom.setOnAction(_ -> handleEncipherButton());
        inputValidator = new InputValidator();
        rsaCipher = new RSACipher();
    }

    private void handleCalculateButton() {
        try {
            int p = Integer.parseInt(pInput.getText());
            int q = Integer.parseInt(qInput.getText());
            int closedKey = Integer.parseInt(closedKeyInput.getText());

            if (inputValidator.isPrime(p) && inputValidator.isPrime(q)) {
                this.p = p;
                this.q = q;
                this.r = rsaCipher.countR(p, q);
                this.rEuler = rsaCipher.countREuler(p, q);
                if (inputValidator.isClosedKeyCorrect(closedKey, rEuler)) {
                    rOutput.setText(String.valueOf(r));
                    eulerOutput.setText(String.valueOf(rEuler));
                    this.openKey = rsaCipher.countOpenKey(rEuler, closedKey);
                    openKeyOutput.setText(String.valueOf(openKey));
                    this.isDataCorrect = true;
                } else {
                    showError("Закрытый ключ должен быть меньше функции Эйлера от r и взаимнопростым с ней!");
                }
            } else {
                showError("p и q должны быть простыми числами!");
            }
        } catch (NumberFormatException e) {
            showError("p и q должны быть целыми числами");
        }
    }

    private void handleCipherButton() {
        String plainText = plainTextOutput.getText();
        if (plainText.isEmpty()) {
            plainTextOutput.setText("Введите текст для шифрования");
            return;
        }

        // Пример шифрования (заглушка)
        String cipherText = "Зашифрованный: " + plainText;
        cipherTextOutput.setText(cipherText);
    }

    private void handleEncipherButton() {
        String cipherText = cipherTextOutput.getText();
        if (cipherText.isEmpty()) {
            cipherTextOutput.setText("Нет зашифрованного текста");
            return;
        }

        // Пример расшифровки (заглушка)
        String decryptedText = "Расшифрованный: " + cipherText;
        plainTextOutput.setText(decryptedText);
    }

    private String generateOpenKey(int p, int q) {
        return "Открытый ключ для p=" + p + ", q=" + q;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
        this.isDataCorrect = false;
    }
}