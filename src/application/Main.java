package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

import java.util.function.UnaryOperator;



public class Main extends Application {
    private TabPane tabPane;
    private BorderPane root;
    private VBox body;
    private TextArea outputTextArea;
    private Button nextCycleButton;

    private boolean programmaticTabSwitch = false;

     static public String[] reservationFields = new String[4];
     static public String[] latenciesFields = new String[6];
    public static String  Instrcutions = "";

    public static CPU GGotham;
    @Override
    public void start(Stage primaryStage) {
        try {
 
            root = new BorderPane();

            // Create menu with tabs
            tabPane = new TabPane();
            Tab reservationTab = new Tab("Reservation Stations");
            Tab latenciesTab = new Tab("Latencies");
            Tab instructionsTab = new Tab("Instructions"); // New tab for instructions

            // Make the tabs fixed
            reservationTab.setClosable(false);
            latenciesTab.setClosable(false);
            instructionsTab.setClosable(false);

            // Customize the tabs with a wider minimum width
            reservationTab.setStyle("-fx-min-width: 120;");
            latenciesTab.setStyle("-fx-min-width: 120;");
            instructionsTab.setStyle("-fx-min-width: 120;");

            tabPane.getTabs().addAll(reservationTab, latenciesTab, instructionsTab);

            // Create body with an image
            body = new VBox();
            body.setStyle("-fx-background-size: cover;");

            // Set the layout for the tabs and body
            root.setTop(tabPane);
            root.setCenter(body);

            // Set the action when a tab is selected
            reservationTab.setOnSelectionChanged(event -> showReservationForm());
            latenciesTab.setOnSelectionChanged(event -> showLatenciesForm());
            instructionsTab.setOnSelectionChanged(event -> showInstructionsForm(primaryStage)); // Show instructions form when the Instructions tab is selected

            // Create the scene
            Scene scene = new Scene(root, 800, 600);

            // Set stage properties
            primaryStage.setTitle("Tomasulu");
            primaryStage.setScene(scene);

            // Call showReservationForm() to display the Reservation Stations form initially
            showReservationForm();
            primaryStage.show();
        
        		
        		
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    private void showReservationForm() {
        body.getChildren().clear(); // Remove the image

        // Create a form with input fields for reservation data
        GridPane reservationForm = new GridPane();
        reservationForm.setPadding(new javafx.geometry.Insets(20));
        reservationForm.setHgap(10);
        reservationForm.setVgap(15); // Increase vertical gap

        // Add form components (labels, text fields, buttons, etc.)
        Label titleLabel = new Label("Enter numbers of Reservation Stations");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        reservationForm.add(titleLabel, 0, 0);

        // Labeled input fields for Reservation Stations
        TextField rsAddSubField = addLabeledInputField(reservationForm, "RS Add/Sub:", "Enter RS");
        TextField rsMulDivField = addLabeledInputField(reservationForm, "RS MUL/Div:", "Enter RS");
        TextField loadBuffersField = addLabeledInputField(reservationForm, "Load Buffers:", "Enter L.BUffers");
        TextField storeBuffersField = addLabeledInputField(reservationForm, "Store Buffers:", "Enter S.Bufferes");

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            reservationFields[0] = rsAddSubField.getText();
            reservationFields[1] = rsMulDivField.getText();
            reservationFields[2] = loadBuffersField.getText();
            reservationFields[3] = storeBuffersField.getText();

            if (validateInputFields(reservationFields)) {
                for (String t : reservationFields) {
                    System.out.print(t + " ");
                }
                // Save the reservation data
                // Tomasulu.sa0veReservationData();
                programmaticTabSwitch = true; // Set the flag before switching tabs
                tabPane.getSelectionModel().select(1);
            } else {
                showAlert("All fields are required!");
            }
        });

        reservationForm.add(okButton, 0, reservationForm.getRowCount());

        // Add the form to the body
        body.getChildren().add(reservationForm);
    }

    private void showLatenciesForm() {
        body.getChildren().clear(); // Remove the image

        // Create a form with input fields for latencies data
        GridPane latenciesForm = new GridPane();
        latenciesForm.setPadding(new javafx.geometry.Insets(20));
        latenciesForm.setHgap(15);
        latenciesForm.setVgap(25); // Increase vertical gap

        // Add form components for each operation
        TextField fpAddField = addLatenciesInputField(latenciesForm, "FP ADD:", "Enter latency");
        TextField fpSubField = addLatenciesInputField(latenciesForm, "SUB:", "Enter latency");
        TextField fpMulField = addLatenciesInputField(latenciesForm, "FP MUL:", "Enter latency");
        TextField fpDivField = addLatenciesInputField(latenciesForm, "FP DIV:", "Enter latency");
        TextField lDField = addLatenciesInputField(latenciesForm, "L.D:   ", "Enter latency");
        TextField sDField = addLatenciesInputField(latenciesForm, "S.D:   ", "Enter latency");

        // Add OK button
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            latenciesFields[0] = fpAddField.getText();
            latenciesFields[1] = fpSubField.getText();
            latenciesFields[2] = fpMulField.getText();
            latenciesFields[3] = fpDivField.getText();
            latenciesFields[4] = lDField.getText();
            latenciesFields[5] = sDField.getText();

            if (validateInputFields(latenciesFields)) {
                for (String t : latenciesFields) {
                    System.out.print(t + " ");
                }
                programmaticTabSwitch = true; // Set the flag before switching tabs

                // Switch to the Instructions tab
                tabPane.getSelectionModel().select(2);
            } else {
                showAlert("All fields are required!");
            }
        });

        latenciesForm.add(okButton, 0, latenciesForm.getRowCount());

        // Add the form to the body
        body.getChildren().add(latenciesForm);
    }


    private boolean validateInputFields(String[] fieldValue) {
        for (String t : fieldValue) {
            if (t.isEmpty())
                return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TextField addLatenciesInputField(GridPane form, String operationLabel, String promptText) {
        Label label = new Label(operationLabel);
        TextField textField = new TextField();
        textField.setPromptText(promptText);

        // Restrict input to numeric only
        textField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d*")) ? change : null));

        // Add label and text field in an HBox
        HBox hbox = new HBox(label, textField);
        hbox.setSpacing(10); // Adjust spacing as needed

        // Add the HBox to the form
        form.add(hbox, 0, form.getRowCount());

        // Return the TextField object
        return textField;
    }

    private TextField addLabeledInputField(GridPane form, String labelValue, String promptText) {
        Label label = new Label(labelValue);
        TextField textField = new TextField();
        textField.setPromptText(promptText);

        // Restrict input to numeric only
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        textField.setTextFormatter(new TextFormatter<>(filter));

        // Add label and text field in an HBox
        HBox hbox = new HBox(label, textField);
        hbox.setSpacing(10); // Adjust spacing as needed

        // Add the HBox to the form
        form.add(hbox, 0, form.getRowCount());

        // Return the TextField object
        return textField;
    }
    private void showInstructionsForm(Stage primaryStage) {
        // Create a TextArea for instructions
        TextArea instructionsTextArea = new TextArea();
        instructionsTextArea.setPromptText("Enter instructions here line by line...");

        // Add the TextArea to the body
        body.getChildren().clear(); // Clear existing content
        body.getChildren().add(instructionsTextArea);

        // Set the action when instructions are submitted
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
             Instrcutions = instructionsTextArea.getText(); // Get the raw text

            if (!Instrcutions.isEmpty()) {
                // Do something with the instructions, e.g., print or process them
                System.out.println("Instructions:\n" + Instrcutions);
                // Clear the TextArea
                instructionsTextArea.clear();
                // Close the scene
                closeSceneAndExecuteMain();
            } else {
                showAlert("Instructions cannot be empty!");
            }
        });

        HBox buttonBox = new HBox(okButton);
        buttonBox.setSpacing(10);
        body.getChildren().add(buttonBox);
    }

    private void displayCycleScene(String cycleResult) {
        // Create a new VBox to hold the cycle result and the "Next" button
        VBox cycleBox = new VBox();
        cycleBox.setSpacing(10);

        // Create a TextArea to display the cycle result
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setText(cycleResult);

        // Set the preferred height of the TextArea
        resultTextArea.setPrefHeight(600); // Adjust the height as needed

        cycleBox.getChildren().add(resultTextArea);

        // Create a "Next" button
        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            // Call runCycle and update the resultTextArea
            String newCycleResult = GGotham.runCycle();
            resultTextArea.setText(newCycleResult);

            // Check if all instructions are executed
            if (GGotham.isAllInstructionsExecuted()) {
                // Optionally, show a message or perform any final actions
                showAlert("All instructions executed!");
            }
        });
        cycleBox.getChildren().add(nextButton);

        // Create a new scene with the VBox
        Scene cycleScene = new Scene(cycleBox, 800, 600);

        // Set the scene on the primary stage
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(cycleScene);
    }

    private void closeSceneAndExecuteMain() {
        Stage stage = (Stage) root.getScene().getWindow();
  
         GGotham = new CPU(Instrcutions,Integer.parseInt(latenciesFields[2]),
        		Integer.parseInt(latenciesFields[0]),Integer.parseInt(latenciesFields[1]),
        		Integer.parseInt(latenciesFields[3]),Integer.parseInt(latenciesFields[4]),
        		Integer.parseInt(latenciesFields[5]),Integer.parseInt(reservationFields[0]),
        		Integer.parseInt(reservationFields[1]),Integer.parseInt(reservationFields[2]),
        		Integer.parseInt(reservationFields[3]));
         displayCycleScene(GGotham.runCycle());
    }
   

    public static void main(String[] args) {
        launch(args);

    }
}
