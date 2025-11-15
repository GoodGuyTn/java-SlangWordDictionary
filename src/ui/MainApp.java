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
import Model.SlangWord;

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

        showSearchPane();

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
        Button btnManage = createMenuButton("✏️ Manage");
        Button btnRandom = createMenuButton("🎲 Random Slang");
        Button btnQuiz = createMenuButton("❓ Quiz");
        Button btnReset = createMenuButton("🔄 Reset Data");
        Button btnExit = createMenuButton("❌ Exit");

        btnSearch.setOnAction(e -> {showSearchPane();});
        btnHistory.setOnAction(e -> {showHistoryPane();});
        btnExit.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(lblMenu, new Separator(), btnSearch, btnHistory, btnManage, btnRandom, btnQuiz, new Separator(), btnReset, btnExit);
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

    /**
     * 1 + 2: Search pane
     * Users can enter keywords (slang/definition keyword) to lookup
     * Choose between find by Slang or by Definition
     */
    private void showSearchPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Lookup Slang Word");

        TextField tfKeyword = new TextField();
        tfKeyword.setPromptText("Enter keyword...");

        RadioButton rbSlang = new RadioButton("By Slang");
        RadioButton rbDef = new RadioButton("By Definition");
        ToggleGroup group = new ToggleGroup();
        rbSlang.setToggleGroup(group);
        rbDef.setToggleGroup(group);
        rbSlang.setSelected(true);

        HBox options = new HBox(20, rbSlang, rbDef);

        Button btnFind = new Button("Find");

        Label lblResult = new Label("Result:");

        ListView<String> listView = new ListView<>();

        btnFind.setOnAction(e -> {
            String keyword = tfKeyword.getText();
            if (keyword.isEmpty()) {
                showAlert("Error", "Please enter a keyword!");
                return;
            }

            listView.getItems().clear();

            if(rbSlang.isSelected()) {
                SlangWord result = slangService.searchBySlang(keyword);
                if (result != null) {
                    listView.getItems().add(result.toString());
                } else {
                    showAlert("Notification", "Slang word not found!");
                }
            } else {
                Set<SlangWord> result = slangService.searchByDefinition(keyword);
                if (result != null && !result.isEmpty()) {
                    for (SlangWord slang : result) {
                        listView.getItems().add(slang.toString());
                    }
                } else {
                    showAlert("Notification", "Slang word not found!");
                }
            }
        });

        contentArea.getChildren().addAll(lblTitle, tfKeyword, options, btnFind, lblResult, listView);
    }

    /**
     * 3: History pane
     * Users can view their search history (only existing slang words can be listed)
     */
    private void showHistoryPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Search History");

        ListView<String> listView = new ListView<>();
        List<String> history = slangService.getSlangHistory();
        if (history.isEmpty()) {
            listView.getItems().add("No history found!");
        } else {
            listView.getItems().addAll(history);
        }

        contentArea.getChildren().addAll(lblTitle, listView);
    }

}













