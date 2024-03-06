package org.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.stream.LongStream;

public class RSApplication extends Application {
  private final RSA rsa = new RSA();
  private boolean encryptionMode = true;

  @Override
  public void start(Stage primaryStage) {
    Label label_encryption_title = new Label("RSA Encryption");
    label_encryption_title.setFont(Font.font(32));
    Label messageLabel = new Label("Bericht:");
    TextField messageField = new TextField();
    Label encryptedMessageNumbersLabel = new Label();
    Label nLabel = new Label("N waarde:");
    TextField nField = new TextField();
    Label eLabel = new Label("E waarde:");
    TextField eField = new TextField();
    Label pLabel = new Label("P waarde:");
    TextField pField = new TextField();
    Label qLabel = new Label("Q waarde:");
    TextField qField = new TextField();
    Label rekenLabel = new Label("Berkentijd:");
    TextField rekenField = new TextField();
    Label encryptLabel = new Label("Encrypte bericht:");
    TextArea encryptOutputArea = new TextArea();

    Button generateEButton = new Button("Genereer E");
    generateEButton.setOnMouseClicked(click -> rsa.calculateECode(nField, pField, qField, eField));

    Button calculatePQButton = new Button("Genereer P en Q");
    calculatePQButton.setOnMouseClicked(click -> rsa.calculatePandQCode(nField, pField, qField, rekenField));

    Button encryptButton = new Button("Versleutel Bericht");
    encryptButton.setOnMouseClicked(click -> {
      String nFieldValue = nField.getText();
      String eFieldValue = eField.getText();

      if(nFieldValue != null && eFieldValue != null) {
        long nValue = Long.parseLong(nField.getText());
        long eValue = Long.parseLong(eField.getText());
        String message = messageField.getText();
        if(!message.isEmpty()) {
          StringBuilder encryptedMessageNumbers = new StringBuilder();
          for (char character : message.toCharArray()) {
            long encryptedValue = rsa.encrypt((long) character, eValue, nValue);
            encryptedMessageNumbers.append(encryptedValue).append(", ");
          }
          if (encryptedMessageNumbers.length() > 2) {
            encryptedMessageNumbers.delete(encryptedMessageNumbers.length() - 2, encryptedMessageNumbers.length());
          }
          encryptOutputArea.setText("Versleuteld bericht: " + encryptedMessageNumbers);
          encryptedMessageNumbersLabel.setText("Het bericht na versleuteling: " + encryptedMessageNumbers);
        } else {
          encryptOutputArea.setText("Kan bericht niet encrypten. Het bericht mag niet leeg zijn.");
        }
      } else {
        encryptOutputArea.setText("Kan bericht niet encrypten. N en e mogen niet leeg zijn.");
      }
    });

    VBox vboxEncrypt = new VBox(label_encryption_title, messageLabel, messageField, nLabel, nField, calculatePQButton, pLabel, pField, qLabel, qField, rekenLabel, rekenField, generateEButton, eLabel, eField, encryptButton, encryptLabel, encryptOutputArea);

    Label label_decryption_title = new Label("RSA Decryption");
    label_decryption_title.setFont(Font.font(32));
    Label encryptedMessageLabel = new Label("Versleuteld Bericht:");
    TextField encryptedMessageField = new TextField();
    Label dLabel = new Label("D waarde:");
    TextField dField = new TextField();
    Label decryptLabel = new Label("Decrypte bericht:");
    TextArea decryptOutputArea = new TextArea();
    Label label_instruction_title = new Label(encryptionMode ? "Instructies Encryptie" : "Instructies Decryptie");
    label_instruction_title.setFont(Font.font(32));

    VBox instructionsVBox = new VBox(label_instruction_title);
    updateInstructionsVBox(instructionsVBox);
    instructionsVBox.setSpacing(7);

    Button switchInstructionsButton = new Button("Switch Instructies");
    switchInstructionsButton.setOnMouseClicked(event -> {
      encryptionMode = !encryptionMode;
      updateInstructionsVBox(instructionsVBox);
    });

    Button calculateDButton = new Button("Genereer D");
    calculateDButton.setOnMouseClicked(event -> {
      String nFieldValue = nField.getText();
      String eFieldValue = eField.getText();
      String pFieldValue = pField.getText();
      String qFieldValue = qField.getText();

      if (nFieldValue != null && eFieldValue != null && pFieldValue != null && qFieldValue != null) {
        try {
          long e = Long.parseLong(eFieldValue);
          long p = Long.parseLong(pFieldValue);
          long q = Long.parseLong(qFieldValue);
          long phi = ((p -1)*(q-1));
          long d = rsa.modInverse(e,phi);

          dField.setText(String.valueOf(d));
        } catch (NumberFormatException | ArithmeticException ex) {
          ex.printStackTrace();
        }
      }
    });

    Button decryptButton = new Button("Ontcijfer Bericht");
    decryptButton.setOnMouseClicked(click -> {
      String nFieldValue = nField.getText();
      String dFieldValue = dField.getText();

      if (nFieldValue != null && dFieldValue != null) {
        long nValue = Long.parseLong(nFieldValue);
        long dValue = Long.parseLong(dFieldValue);

        String encryptedInput = encryptedMessageField.getText();
        String[] encryptedValues = encryptedInput.split(",");
        StringBuilder decryptedMessage = new StringBuilder();

        for (String encryptedValue : encryptedValues) {
          try {
            long encryptedNumber = Long.parseLong(encryptedValue.trim());
            long decryptedNumber = rsa.decrypt(encryptedNumber, dValue, nValue);
            decryptedMessage.append((char) decryptedNumber);
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }

        decryptOutputArea.setText("Decrypted message: " + decryptedMessage);
      } else {
        decryptOutputArea.setText("N or D is null. Cannot decrypt the message.");
      }
    });

    VBox vboxDecrypt = new VBox(label_decryption_title, encryptedMessageLabel, encryptedMessageField,calculateDButton, dLabel, dField, decryptButton, decryptLabel, decryptOutputArea,
      instructionsVBox);

    HBox hbox = new HBox(vboxEncrypt, vboxDecrypt);
    Scene scene = new Scene(new VBox(hbox, switchInstructionsButton), 775, 630);

    primaryStage.setScene(scene);
    primaryStage.show();
    updateInstructionsVBox(instructionsVBox);
  }

  private void updateInstructionsVBox(VBox instructionsVBox) {
    instructionsVBox.getChildren().clear();

    Text[] bulletPoints;
    if (encryptionMode) {
      bulletPoints = new Text[]{
        new Text("Instructies Encryption 1/2"),
        new Text("• Voer een bericht in die je wilt versleutelen."),
        new Text("• Voor Encryptie mag je zelf een N bedenken."),
        new Text("• Genereer een P en een q door op de knop te drukken.\n   Deze zijn afgeleid van N."),
        new Text("• Genereer een random E door op de knop te drukken."),
        new Text("• Druk op versleutel bericht om je ingevoerde bericht te versleutelen.")
      };
    } else {
      bulletPoints = new Text[]{
        new Text("Instructies Decryption 2/2"),
        new Text("• Voer het versleutelde bericht in dat je wilt ontcijferen."),
        new Text("• Zorg dat er een N is ingevuld."),
        new Text("• Genereer een P en een q door op de knop te drukken.\n   Deze zijn afgeleid van N."),
        new Text("• Voer de D-waarde in (deze kun je genereren met de knop)."),
        new Text("• Druk op ontcijfer bericht om het versleutelde bericht te ontcijferen.")
      };
    }

    for (Text bulletPoint : bulletPoints) {
      bulletPoint.setFont(Font.font(16));
      bulletPoints[0].setFont(Font.font(32));
      instructionsVBox.getChildren().add(bulletPoint);
    }
    instructionsVBox.setSpacing(7);
  }

  public static void main(String[] args) {
    launch(args);
  }
}

class RSA {
  public void calculatePandQCode(TextField nField, TextField pField, TextField qField, TextField rekenField) {
    if (nField.getText() != null) {
      long n = Long.parseLong(nField.getText());
      long startTime = System.currentTimeMillis();
      long primeNumber = LongStream.iterate(2, i -> i + 1)
        .filter(this::isPrimeNumber)
        .filter(i -> n % i == 0)
        .findFirst()
        .orElseThrow();
      pField.setText(String.valueOf(primeNumber));
      qField.setText(String.valueOf(n / primeNumber));
      rekenField.setText("Het duurde "+(System.currentTimeMillis() - startTime) + "ms om p en q te berekenen.");
    }
  }
  public void calculateECode(TextField nField, TextField pField, TextField qField, TextField eField) {
    if (nField.getText() != null && pField.getText() != null && qField.getText() != null) {
      long p = Long.parseLong(pField.getText());
      long q = Long.parseLong(qField.getText());
      long phi = (p - 1) * (q - 1);
      long primeNumber = generateRandomCoprime(phi);
      eField.setText(String.valueOf(primeNumber));
    }
  }

  private long generateRandomCoprime(long phi) {
    long candidate;
    do {
      candidate = (long) (Math.random() * (phi - 2)) + 2;
    } while (!isPrimeAndCoprime(candidate, phi));
    return candidate;
  }

  private boolean isPrimeAndCoprime(long number, long phi) {
    return isPrimeNumber(number) && phi % number != 0;
  }

  private boolean isPrimeNumber(long inputNumber) {
    return inputNumber > 1 && LongStream.rangeClosed(2, (long) Math.sqrt(inputNumber)).noneMatch(i -> inputNumber % i == 0);
  }

  public Long encrypt(Long message, Long e, Long N) {
    return modPow(message, e, N);
  }
  public Long decrypt(Long encrypted, Long d, Long N) {
    return modPow(encrypted, d, N);
  }

  public Long modPow(Long base, Long exponent, Long modulus) {
    long result = 1L;
    base = base % modulus;
    while (exponent > 0) {
      if ((exponent & 1) == 1)
        result = (result * base) % modulus;
      exponent = exponent >> 1;
      base = (base * base) % modulus;
    }
    return result;
  }

  public long modInverse(long a, long m) {
    long m0 = m;
    long y = 0, x = 1;
    if (m == 1)
      return 0;
    while (a > 1) {
      long q = a / m;
      long t = m;
      m = a % m;
      a = t;
      t = y;
      y = x - q * y;
      x = t;
    }
    if (x < 0)
      x += m0;
    return x;
  }
}

