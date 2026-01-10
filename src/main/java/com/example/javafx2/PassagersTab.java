package com.example.javafx2;

import com.example.javafx2.logic.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PassagersTab extends BorderPane {
    
    private gestion_aeroport aeroport;
    private ComboBox<Vol> volComboBox;
    private Vol selectedVol;
    
    // Passenger form fields
    private TextField passagerPassportField;
    private TextField passagerNomField;
    private TextField passagerPrenomField;
    private TextField passagerGenreField;
    private DatePicker passagerDateNaissance;
    private TextField passagerBagageField;
    
    // Employee form fields
    private TextField employeePassportField;
    private TextField employeeNomField;
    private TextField employeePrenomField;
    private TextField employeeGenreField;
    private DatePicker employeeDateNaissance;
    private TextField employeeRoleField;
    
    public PassagersTab(gestion_aeroport aeroport) {
        this.aeroport = aeroport;
        setupUI();
    }
    
    private void setupUI() {
        setPadding(new Insets(20));
        
        // Top: ComboBox for selecting flight
        Label volLabel = new Label("Sélectionner un Vol:");
        volComboBox = new ComboBox<>();
        updateVolComboBox();
        volComboBox.setOnAction(e -> {
            selectedVol = volComboBox.getValue();
            updateForms();
        });
        
        volComboBox.setCellFactory(param -> new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                } else {
                    setText("Vol ID: " + vol.getId_vol() + " - " + vol.getDepart() + " → " + vol.getDestination());
                }
            }
        });
        
        volComboBox.setButtonCell(new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                } else {
                    setText("Vol ID: " + vol.getId_vol() + " - " + vol.getDepart() + " → " + vol.getDestination());
                }
            }
        });
        
        HBox topBox = new HBox(10);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.getChildren().addAll(volLabel, volComboBox);
        topBox.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBox);
        
        // Center: Split into two sections
        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(10));
        
        // Left: Passenger Form
        VBox passagerBox = createPassengerForm();
        passagerBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-padding: 10px;");
        
        // Right: Employee Form
        VBox employeeBox = createEmployeeForm();
        employeeBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-padding: 10px;");
        
        centerBox.getChildren().addAll(passagerBox, employeeBox);
        setCenter(centerBox);
        
        // Initialize forms as disabled until a flight is selected
        updateForms();
    }
    
    private VBox createPassengerForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(400);
        
        Label title = new Label("Ajouter un Passager");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label passportLabel = new Label("Passeport:");
        passagerPassportField = new TextField();
        
        Label nomLabel = new Label("Nom:");
        passagerNomField = new TextField();
        
        Label prenomLabel = new Label("Prénom:");
        passagerPrenomField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        passagerGenreField = new TextField();
        
        Label dateLabel = new Label("Date de Naissance:");
        passagerDateNaissance = new DatePicker();
        passagerDateNaissance.setValue(LocalDate.now().minusYears(30));
        
        Label bagageLabel = new Label("Masse Bagage (kg):");
        passagerBagageField = new TextField();
        
        Button addPassagerButton = new Button("Ajouter Passager");
        addPassagerButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        addPassagerButton.setOnAction(e -> ajouterPassager());
        
        form.getChildren().addAll(title, passportLabel, passagerPassportField,
                nomLabel, passagerNomField, prenomLabel, passagerPrenomField,
                genreLabel, passagerGenreField, dateLabel, passagerDateNaissance,
                bagageLabel, passagerBagageField, addPassagerButton);
        
        return form;
    }
    
    private VBox createEmployeeForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(400);
        
        Label title = new Label("Ajouter un Employé");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label passportLabel = new Label("Passeport:");
        employeePassportField = new TextField();
        
        Label nomLabel = new Label("Nom:");
        employeeNomField = new TextField();
        
        Label prenomLabel = new Label("Prénom:");
        employeePrenomField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        employeeGenreField = new TextField();
        
        Label dateLabel = new Label("Date de Naissance:");
        employeeDateNaissance = new DatePicker();
        employeeDateNaissance.setValue(LocalDate.now().minusYears(30));
        
        Label roleLabel = new Label("Rôle (Captaine/Copilote/STEWARD):");
        employeeRoleField = new TextField();
        
        Button addEmployeeButton = new Button("Ajouter Employé");
        addEmployeeButton.setStyle("-fx-font-size: 14px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        addEmployeeButton.setOnAction(e -> ajouterEmployee());
        
        form.getChildren().addAll(title, passportLabel, employeePassportField,
                nomLabel, employeeNomField, prenomLabel, employeePrenomField,
                genreLabel, employeeGenreField, dateLabel, employeeDateNaissance,
                roleLabel, employeeRoleField, addEmployeeButton);
        
        return form;
    }
    
    public void updateVolComboBox() {
        Vol selected = volComboBox.getValue();
        volComboBox.getItems().clear();
        volComboBox.getItems().addAll(aeroport.getVols());
        // Restaurer la sélection si elle existe encore
        if (selected != null && volComboBox.getItems().contains(selected)) {
            volComboBox.setValue(selected);
        }
    }
    
    private void updateForms() {
        if (selectedVol != null) {
            // Enable forms when a flight is selected
            passagerPassportField.setDisable(false);
            passagerNomField.setDisable(false);
            passagerPrenomField.setDisable(false);
            passagerGenreField.setDisable(false);
            passagerDateNaissance.setDisable(false);
            passagerBagageField.setDisable(false);
            
            employeePassportField.setDisable(false);
            employeeNomField.setDisable(false);
            employeePrenomField.setDisable(false);
            employeeGenreField.setDisable(false);
            employeeDateNaissance.setDisable(false);
            employeeRoleField.setDisable(false);
        } else {
            // Disable forms when no flight is selected
            passagerPassportField.setDisable(true);
            passagerNomField.setDisable(true);
            passagerPrenomField.setDisable(true);
            passagerGenreField.setDisable(true);
            passagerDateNaissance.setDisable(true);
            passagerBagageField.setDisable(true);
            
            employeePassportField.setDisable(true);
            employeeNomField.setDisable(true);
            employeePrenomField.setDisable(true);
            employeeGenreField.setDisable(true);
            employeeDateNaissance.setDisable(true);
            employeeRoleField.setDisable(true);
        }
    }
    
    private void ajouterPassager() {
        if (selectedVol == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol.");
            alert.showAndWait();
            return;
        }
        
        try {
            if (passagerPassportField.getText().isEmpty() || passagerNomField.getText().isEmpty() ||
                passagerPrenomField.getText().isEmpty() || passagerGenreField.getText().isEmpty() ||
                passagerDateNaissance.getValue() == null || passagerBagageField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }
            
            String passport = passagerPassportField.getText();
            String nom = passagerNomField.getText();
            String prenom = passagerPrenomField.getText();
            String genre = passagerGenreField.getText();
            LocalDate localDate = passagerDateNaissance.getValue();
            Date dateNaissance = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            double masseBagage = Double.parseDouble(passagerBagageField.getText());
            
            // Create Passager - constructor will check seat availability and add to vol
            Passager passager = new Passager(passport, nom, prenom, genre, dateNaissance, selectedVol, masseBagage);
            
            // Verify baggage weight
            passager.verifier_bag();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Passager ajouté avec succès!");
            alert.showAndWait();
            
            // Clear fields
            passagerPassportField.clear();
            passagerNomField.clear();
            passagerPrenomField.clear();
            passagerGenreField.clear();
            passagerDateNaissance.setValue(LocalDate.now().minusYears(30));
            passagerBagageField.clear();
            
        } catch (VolCompletException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vol Complet");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (BagageTropLourdException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Bagage Trop Lourd");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une masse de bagage valide (nombre).");
            alert.showAndWait();
        }
    }
    
    private void ajouterEmployee() {
        if (selectedVol == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol.");
            alert.showAndWait();
            return;
        }
        
        try {
            if (employeePassportField.getText().isEmpty() || employeeNomField.getText().isEmpty() ||
                employeePrenomField.getText().isEmpty() || employeeGenreField.getText().isEmpty() ||
                employeeDateNaissance.getValue() == null || employeeRoleField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }
            
            String passport = employeePassportField.getText();
            String nom = employeeNomField.getText();
            String prenom = employeePrenomField.getText();
            String genre = employeeGenreField.getText();
            LocalDate localDate = employeeDateNaissance.getValue();
            Date dateNaissance = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String role = employeeRoleField.getText();
            
            // Create Employee and add to vol
            Employee employee = new Employee(passport, nom, prenom, genre, dateNaissance, selectedVol, role);
            selectedVol.ajouter_employee(employee);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Employé ajouté avec succès!");
            alert.showAndWait();
            
            // Clear fields
            employeePassportField.clear();
            employeeNomField.clear();
            employeePrenomField.clear();
            employeeGenreField.clear();
            employeeDateNaissance.setValue(LocalDate.now().minusYears(30));
            employeeRoleField.clear();
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de l'employé: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
