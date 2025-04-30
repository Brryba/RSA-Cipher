package rsa_cipher.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;
import rsa_cipher.rsa_cipher.RSACipher;
import rsa_cipher.utils.FileUtil;
import rsa_cipher.utils.InputValidator;

import java.io.IOException;
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
    private TextArea inputText;
    @FXML
    private TextArea outputText;

    @FXML
    private Button calcultateButton;

    @FXML
    private ToggleGroup modeSelector;

    private InputValidator inputValidator;
    private RSACipher rsaCipher;
    private FileUtil fileUtil;

    private int p;
    private int q;
    private int r;
    private int rEuler;
    private int openKey;
    private int closedKey;
    private boolean isDataCorrect;
    private boolean isEncoding = true;

    private List<Short> inputTextSymbolsArray;
    private List<Short> resultTextSymbolsArray;

    @FXML
    public void initialize() {
        calcultateButton.setOnAction(_ -> handleCalculateButton());
        this.inputValidator = new InputValidator();
        this.rsaCipher = new RSACipher();
        this.fileUtil = new FileUtil();
    }

    private void handleCalculateButton() {
        try {
            int p = Integer.parseInt(pInput.getText());
            int q = Integer.parseInt(qInput.getText());
            int closedKey = Integer.parseInt(closedKeyInput.getText());

            if (inputValidator.isPrime(p) && inputValidator.isPrime(q)) {
                this.p = p;
                this.q = q;
                this.closedKey = closedKey;
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

    private void encode() {
        if (this.isDataCorrect) {
            if (this.inputTextSymbolsArray.isEmpty()) {
                showError("Сначала введите данные для шифрования");
                return;
            }
            List<Short> encodedSymbols = rsaCipher.encodeSymbols(this.inputTextSymbolsArray, this.openKey, this.r);
            this.outputText.setText(encodedSymbols.toString());
            this.resultTextSymbolsArray = encodedSymbols;
        } else {
            showError("Сначала введите корректные p, q, d и сформируйте остальные нужные числа");
        }
    }

    private void decode() {
        if (this.isDataCorrect) {
            if (this.inputTextSymbolsArray.isEmpty()) {
                showError("Сначала введите данные для дешифрирования");
                return;
            }
            List<Short> decodeSymbols = rsaCipher.decodeSymbols(this.inputTextSymbolsArray, this.closedKey, this.r);
            this.outputText.setText(decodeSymbols.toString());
            this.resultTextSymbolsArray = decodeSymbols;
        } else {
            showError("Сначала введите корректные p, q, d и сформируйте остальные нужные числа");
        }
    }

    @FXML
    private void processData() {
        if (this.isEncoding) {
            encode();
        } else {
            decode();
        }
    }

    @FXML
    private void setMode() {
        switch (modeSelector.getSelectedToggle().getUserData().toString()) {
            case "encipher": {
                this.isEncoding = true;
                break;
            }
            case "decipher": {
                this.isEncoding = false;
                break;
            }
        };
        this.inputTextSymbolsArray = null;
        outputText.setText("");
        inputText.setText("");
    }

    @FXML
    private void openFile() {
        try {
            this.inputTextSymbolsArray = fileUtil.readFile(this.isEncoding);
            if (this.inputTextSymbolsArray.isEmpty()) {
                return;
            }
            inputText.setText(inputTextSymbolsArray.toString());
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void saveFile() {
        try {
            fileUtil.writeFile(this.resultTextSymbolsArray, this.isEncoding);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
        this.isDataCorrect = false;
    }
}