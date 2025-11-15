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
        Button btnAdd = createMenuButton("➕ Add");
        Button btnEdit = createMenuButton("🛠️ Edit");
        Button btnDelete = createMenuButton("🗑️ Delete");
        Button btnRandom = createMenuButton("🎲 Random Slang");
        Button btnQuiz = createMenuButton("❓ Quiz");
        Button btnReset = createMenuButton("🔄 Reset Data");
        Button btnExit = createMenuButton("❌ Exit");

        btnSearch.setOnAction(e -> {showSearchPane();});
        btnHistory.setOnAction(e -> {showHistoryPane();});
        btnAdd.setOnAction(e -> {showAddPane();});
        btnEdit.setOnAction(e -> {showEditPane();});
        btnExit.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(lblMenu, new Separator(), btnSearch, btnHistory, btnAdd, btnEdit, btnDelete, btnRandom, btnQuiz, new Separator(), btnReset, btnExit);
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
            String keyword = tfKeyword.getText().trim();
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

    private void showAddPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Add Slang Word");

        Label lblSlang = new Label("Slang Word:");
        TextField tfSlang = new TextField();
        tfSlang.setPromptText("Enter Slang Word");

        Label lblDefinition = new Label("Definition:");
        TextField tfDefinition = new TextField();
        tfDefinition.setPromptText("Enter Definition");

        Button btnAdd = new Button("Add Slang Word");

        btnAdd.setOnAction(e -> {
            String slang = tfSlang.getText().trim().toUpperCase();
            String definition = tfDefinition.getText().trim();

            if (slang.isEmpty() || definition.isEmpty()) {
                showAlert("Warning", "Please enter a Slang Word with its Definition!");
                return;
            }

            // Check slang existence
            SlangWord existing = slangService.searchBySlang(slang);
            if (existing != null) {
                // Case slang word exist
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Duplicate Slang Detected");
                confirm.setHeaderText("The slang '" + slang + "' already exists!");
                confirm.setContentText("Choose 'Overwrite' to replace old meaning.\nChoose 'Duplicate' to add a new meaning.");

                ButtonType btnOverwrite = new ButtonType("Overwrite");
                ButtonType btnDuplicate = new ButtonType("Duplicate");
                ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirm.getButtonTypes().setAll(btnOverwrite, btnDuplicate, btnCancel);

                confirm.showAndWait().ifPresent(type -> {
                    if (type == btnOverwrite) {
                        slangService.addSlangWord(slang, definition, true);
                        showAlert("Success", "Slang word overwritten successfully!");
                    } else if (type == btnDuplicate) {
                        slangService.addSlangWord(slang, definition, false);
                        showAlert("Success", "New definition added to existing slang word!");
                    }
                });
            } else {
                // Case new slang word
                slangService.addSlangWord(slang, definition);
                showAlert("Success", "New slang word added successfully!");
            }
            tfSlang.clear();
            tfDefinition.clear();
        });

        contentArea.getChildren().addAll(lblTitle, lblSlang, tfSlang, lblDefinition, tfDefinition, btnAdd);
    }

    private void showEditPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Edit Slang Word");

        Label lblSlang = new Label("Step 1: Find Slang Word");
        TextField tfSlang = new TextField();
        tfSlang.setPromptText("Enter Slang Word to Edit");

        Button btnFind = new Button("Find Slang");

        Label lblUpdate = new Label("Step 2: Add Edit Info");

        Label lblSlangWord = new Label("Slang Word:");
        TextField tfSlangWord = new TextField();
        tfSlangWord.setDisable(true);
        Label lblDefinitions = new Label("Definitions:");
        TextField tfDefinitions = new TextField();
        tfDefinitions.setDisable(true);

        Label lblOldDef = new Label("Old Definition (to replace):");
        TextField tfOldDef = new TextField();
        tfOldDef.setPromptText("Enter Old Definition (Copy Exactly)");

        Label lblNewDef = new Label("New Definition:");
        TextField tfNewDef = new TextField();
        tfNewDef.setPromptText("Enter New Definition");

        Button btnSave = new Button("Save Changes");
        btnSave.setDisable(true);


        btnFind.setOnAction(e -> {
            String slang = tfSlang.getText().trim().toUpperCase();
            SlangWord existing = slangService.searchBySlang(slang);
            if (existing == null) {
                showAlert("Error", "Slang word not found!");
                return;
            }

            tfSlangWord.setText(existing.getSlang());
            String allDefinitions = String.join(" | ", existing.getDefinitions());
            tfDefinitions.setText(allDefinitions);
            tfOldDef.setText(existing.getDefinitions().getFirst());
            btnSave.setDisable(false);
        });

        btnSave.setOnAction(e -> {
            slangService.editSlangWord(tfSlangWord.getText(), tfOldDef.getText(), tfNewDef.getText().trim());
            showAlert("Success", "Slang word edited successfully!");
            tfNewDef.clear();
        });

        contentArea.getChildren().addAll(lblTitle, lblSlang, tfSlang, btnFind, new Separator(),
                                            lblSlangWord, tfSlangWord, lblDefinitions, tfDefinitions,
                                            lblOldDef, tfOldDef, lblNewDef, tfNewDef, btnSave);
    }
}













