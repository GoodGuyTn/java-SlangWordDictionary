package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Service.SlangService;

import java.util.*;

public class MainApp extends Application {

    private SlangService slangService;
    private BorderPane rootLayout;
    private VBox contentArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Init Service (Backend)
        slangService = new SlangService();

        // 2. Setup Main Layout
        rootLayout = new BorderPane();

        // Create Sidebar Menu
        VBox sideMenu = createSideMenu();
        rootLayout.setLeft(sideMenu);

        // Create Content Area
        contentArea = new VBox();
        contentArea.setPadding(new Insets(20));
        contentArea.setSpacing(10);
        rootLayout.setCenter(contentArea);

        // 3. Configure Scene and Stage
        Scene scene = new Scene(rootLayout, 900, 600);
        primaryStage.setTitle("Project #1 - Slang Dictionary");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Create Sidebar Menu (Left)
     */
    private VBox createSideMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");
        menu.setPrefWidth(220);

        // Menu Title
        Label lblMenu = new Label("MENU");
        lblMenu.setTextFill(javafx.scene.paint.Color.WHITE);
        lblMenu.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Menu Buttons
        Button btnSearch = createMenuButton("🔍 Search");
        Button btnHistory = createMenuButton("📜 History");
        Button btnAdd = createMenuButton("➕ Add New Slang");
        Button btnManage = createMenuButton("✏️ Manage (Edit/Delete)");
        Button btnRandom = createMenuButton("🎲 Random Slang");
        Button btnQuiz = createMenuButton("❓ Quiz");
        Button btnReset = createMenuButton("🔄 Reset Data");
        Button btnExit = createMenuButton("❌ Exit");

        btnExit.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(lblMenu, new Separator(), btnSearch, btnHistory, btnAdd, btnManage, btnRandom, btnQuiz, new Separator(), btnReset, btnExit);
        return menu;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"));
        return btn;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}