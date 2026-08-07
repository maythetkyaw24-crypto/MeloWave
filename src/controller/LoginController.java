package controller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.UserModel;

import java.io.File;
import java.time.LocalDate;
import java.util.Random;

public class LoginController {

    // Login Components
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField; 
    @FXML private Button togglePasswordButton;
    @FXML private Button loginButton;
    @FXML private Text statusText;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Button googleButton, iCloudButton, facebookButton;

    // Sign Up Components 
    @FXML private VBox loginFormBox;
    @FXML private VBox signupFormBox;
    @FXML private TextField regUsernameField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;
    @FXML private DatePicker regDobPicker;
    @FXML private Text regStatusText;
    @FXML private Button regGoogleButton;
    @FXML private Button regICloudButton;
    @FXML private Button regFacebookButton;
    @FXML private PasswordField regConfirmPasswordField;
    @FXML private Canvas animationCanvas;
    @FXML private TextField regPasswordVisibleField;
    @FXML private Button regTogglePasswordButton;
    @FXML private TextField regConfirmVisibleField;
    @FXML private Button regToggleConfirmButton;
    
    private final UserModel userModel = new UserModel();
    private GraphicsContext gc;
    private Image skeletonImage;
    private final Random random = new Random();
    
    private double waveOffset = 0;
    private double bgMoveOffset = 0;
    private double alphaOffset = 0;
    private int particleDelayCounter = 0;
    
    private double[] dropY = new double[30];
    private double[] dropX = new double[30];
    private double[] dropSpeed = new double[30];

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("User", "Admin", "Artist");
 
        loginFormBox.setVisible(true);
        signupFormBox.setVisible(false);

        gc = animationCanvas.getGraphicsContext2D();
        StackPane parent = (StackPane) animationCanvas.getParent();
        animationCanvas.widthProperty().bind(parent.widthProperty());
        animationCanvas.heightProperty().bind(parent.heightProperty());

        for (int i = 0; i < 30; i++) {
            dropY[i] = Math.random() * -600; 
            dropX[i] = Math.random() * 800; 
            dropSpeed[i] = 1.0 + random.nextDouble() * 2.0;
        }

        try {
            String imagePath = "file:/C:/Users/Yamin Hlaing/OneDrive/Pictures/bg_skeleton_6.png";
            skeletonImage = new Image(imagePath);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        try {
            File googleFile = new File("C:/Users/Yamin Hlaing/OneDrive/Pictures/Music_Player/Google_icon_image.png");
            googleButton.setGraphic(createIcon(googleFile));
            regGoogleButton.setGraphic(createIcon(googleFile)); 

            File iCloudFile = new File("C:/Users/Yamin Hlaing/OneDrive/Pictures/Music_Player/iCloud_icon_image.png");
            iCloudButton.setGraphic(createIcon(iCloudFile));
            regICloudButton.setGraphic(createIcon(iCloudFile)); 

            File fbFile = new File("C:/Users/Yamin Hlaing/OneDrive/Pictures/Music_Player/Facebook_icon_image.png");
            facebookButton.setGraphic(createIcon(fbFile));
            regFacebookButton.setGraphic(createIcon(fbFile)); 
        } catch (Exception e) {
            System.out.println("Social icons loading error: " + e.getMessage());
        }

        passwordField.textProperty().bindBidirectional(passwordVisibleField.textProperty());
        passwordVisibleField.setVisible(false);
        togglePasswordButton.setOnAction(e -> togglePasswordVisibility());
        
        // Sign Up Password Bind 
        regPasswordField.textProperty().bindBidirectional(regPasswordVisibleField.textProperty());
        regPasswordVisibleField.setVisible(false);
        regTogglePasswordButton.setOnAction(e -> toggleRegPasswordVisibility());

        // Sign Up Confirm Password Bind 
        regConfirmPasswordField.textProperty().bindBidirectional(regConfirmVisibleField.textProperty());
        regConfirmVisibleField.setVisible(false);
        regToggleConfirmButton.setOnAction(e -> toggleRegConfirmVisibility());
    
        startAnimation();
    }

    // --- Form Switch Methods ---
    @FXML
    void showSignUpForm(ActionEvent event) {
        loginFormBox.setVisible(false);
        signupFormBox.setVisible(true);
    }

    @FXML
    void showLoginForm(ActionEvent event) {
        signupFormBox.setVisible(false);
        loginFormBox.setVisible(true);
    }

    @FXML
    void handleRegister(ActionEvent event) {
        String username = regUsernameField.getText();
        String email = regEmailField.getText();
        String password = regPasswordField.getText();
        String confirmPassword = regConfirmPasswordField.getText();
        LocalDate dob = regDobPicker.getValue();
        
        String role = "User";

        // 1. Checking for completeness of blank fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob == null) {
            regStatusText.setText("PLEASE FILL ALL FIELDS!");
            regStatusText.setStyle("-fx-fill: #F24455;");
            return;
        }

        // 2. Same Password 
        if (!password.equals(confirmPassword)) {
            regStatusText.setText("PASSWORDS DO NOT MATCH!");
            regStatusText.setStyle("-fx-fill: #F24455;");
            return;
        }

        // 3. to Database 
        boolean success = userModel.registerUser(username, email, password, dob.toString(), role);
        if (success) {
            regStatusText.setText("ACCOUNT CREATED SUCCESSFULLY!");
            regStatusText.setStyle("-fx-fill: #FF94B2;");
            showLoginForm(event); //to Login Form 
        } else {
            regStatusText.setText("REGISTRATION FAILED (Username/Email might exist)!");
            regStatusText.setStyle("-fx-fill: #F24455;");
        }
    }

    @FXML void handleGoogleLogin(ActionEvent event) { statusText.setText("Redirecting to Google..."); }
    @FXML void handleFacebookLogin(ActionEvent event) { statusText.setText("Redirecting to Facebook..."); }
    @FXML void handleiCloudLogin(ActionEvent event) { statusText.setText("Redirecting to iCloud..."); }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        boolean remember = rememberMeCheckBox.isSelected();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            statusText.setText("PLEASE SELECT ROLE & FILL FIELDS!");
            statusText.setStyle("-fx-fill: #F24455;");
            return;
        }

        if (userModel.authenticate(username, password, role)) {
            statusText.setText("WELCOME " + role.toUpperCase() + "!");
            statusText.setStyle("-fx-fill: #FF94B2;");
        } else {
            statusText.setText("INVALID CREDENTIALS!");
            statusText.setStyle("-fx-fill: #F24455;");
        }
    }

    private void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordVisibleField.setVisible(true);
            togglePasswordButton.setText("👁‍🗨");
        } else {
            passwordVisibleField.setVisible(false);
            passwordField.setVisible(true);
            togglePasswordButton.setText("👁");
        }
    }
    private void toggleRegPasswordVisibility() {
        if (regPasswordField.isVisible()) {
            regPasswordField.setVisible(false);
            regPasswordVisibleField.setVisible(true);
            regTogglePasswordButton.setText("👁‍🗨");
        } else {
            regPasswordVisibleField.setVisible(false);
            regPasswordField.setVisible(true);
            regTogglePasswordButton.setText("👁");
        }
    }

    private void toggleRegConfirmVisibility() {
        if (regConfirmPasswordField.isVisible()) {
            regConfirmPasswordField.setVisible(false);
            regConfirmVisibleField.setVisible(true);
            regToggleConfirmButton.setText("👁‍🗨");
        } else {
            regConfirmVisibleField.setVisible(false);
            regConfirmPasswordField.setVisible(true);
            regToggleConfirmButton.setText("👁");
        }
    }

    private ImageView createIcon(File file) {
        Image img = new Image(file.toURI().toString());
        ImageView view = new ImageView(img);
        view.setFitWidth(22);
        view.setFitHeight(22);
        view.setPreserveRatio(true);
        return view;
    }

    private void startAnimation() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawFrame();
            }
        }.start();
    }

    private void drawFrame() {
        double width = animationCanvas.getWidth();
        double height = animationCanvas.getHeight();
        gc.clearRect(0, 0, width, height);

        if (skeletonImage != null && !skeletonImage.isError()) {
            alphaOffset += 0.02;
            bgMoveOffset += 0.005;
            double dynamicAlpha = 0.35 + Math.sin(alphaOffset) * 0.12;
            gc.setGlobalAlpha(dynamicAlpha);
            double zoom = 1.08 + Math.sin(bgMoveOffset * 0.5) * 0.03;
            double movementX = Math.cos(bgMoveOffset * 0.5) * 25.0;
            double movementY = Math.sin(bgMoveOffset * 0.7) * 15.0;
            double w = width * zoom;
            double h = height * zoom;
            gc.drawImage(skeletonImage, ((width - w) / 2) + movementX, ((height - h) / 2) + movementY, w, h);
            gc.setGlobalAlpha(1.0);
        }
        
        gc.setFill(Color.web("#FF94B2", 0.7)); 
        gc.setFont(Font.font("Arial", 16)); 
        String[] notes = {"♪", "♫", "♩", "♬", "🎶", "🎼"};
        for (int i = 0; i < 30; i++) {
            gc.fillText(notes[i % notes.length], dropX[i], dropY[i]);
            dropY[i] += dropSpeed[i];
            if (dropY[i] > height) {
                dropY[i] = -20;
                dropX[i] = Math.random() * width;
            }
        }
        
        waveOffset += 0.02;
        gc.setLineWidth(2.0);
        gc.setStroke(Color.web("#F24455", 0.7));
        drawWave(width, height, waveOffset, 22.0, 0.015);
        gc.setStroke(Color.web("#FF94B2", 0.5));
        drawWave(width, height, waveOffset + 1.0, 16.0, 0.02);
        gc.setStroke(Color.web("#E5203A", 0.4));
        drawWave(width, height, waveOffset - 0.7, 28.0, 0.01);
        gc.setStroke(Color.web("#FFDBE8", 0.3));
        drawWave(width, height, waveOffset + 2.0, 12.0, 0.025);

        int totalBars = 200;
        int barWidth = 5;
        int spacing = 3;
        double startX = (width - (totalBars * (barWidth + spacing))) / 2;
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, null, 
                new Stop(0, Color.web("#FF3344", 0.8)), new Stop(1, Color.web("#E5203A", 0.2)));
        gc.setFill(gradient);

        for (int i = 0; i < totalBars; i++) {
            double h = 10 + Math.abs(Math.sin(waveOffset * 1.2 + i * 0.25)) * 35;
            gc.fillRoundRect(startX + (i * (barWidth + spacing)), height - 20 - h, barWidth, h, 3, 3);
        }

        particleDelayCounter++;
        if (particleDelayCounter % 10 == 0) {
            gc.setFill(Color.web("#F5F2ED", 0.5));
            gc.fillOval(random.nextDouble() * width, random.nextDouble() * height, 2, 2);
        }
    }

    private void drawWave(double w, double h, double offset, double amplitude, double frequency) {
        gc.beginPath();
        for (int x = 0; x < w; x += 5) {
            double y = (h / 2) + Math.sin(x * frequency + offset) * amplitude;
            if (x == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();
    }
}