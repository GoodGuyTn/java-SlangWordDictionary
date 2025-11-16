package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        btnDelete.setOnAction(e -> {showDeletePane();});
        btnReset.setOnAction(e -> {handleReset();});
        btnRandom.setOnAction(e -> {showRandomPane();});
        btnQuiz.setOnAction(e -> {showQuizPane();});
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

    /**
     * 4: Add slang word pane
     * Users can add a slang word with its definition,
     * If that slang already exist, users can choose between overwite or duplicate it.
     */
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

    /**
     * 5: Edit pane
     * Users have 3 actions to perform for a Slang word: add new definition, edit a definition, delete a definition
     */
    private void showEditPane() {
        contentArea.getChildren().clear();
        Label lblTitle = new Label("Edit Slang Word");

        Label lblSlang = new Label("Step 1: Find Slang Word");
        TextField tfSlang = new TextField();
        tfSlang.setPromptText("Enter Slang Word to Edit");

        Button btnFind = new Button("Find Slang");

        Label lblSlangWord = new Label("Slang Word:");
        TextField tfSlangWord = new TextField();
        tfSlangWord.setDisable(true);
        Label lblDefinitions = new Label("Definitions:");
        TextField tfDefinitions = new TextField();
        tfDefinitions.setDisable(true);

        Label lblUpdate = new Label("Step 2: Add Edit Info");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Tab 1: Adding a new definition
        Tab addTab = new Tab("Add Slang Definition");
        VBox addBox = new VBox(15);
        addBox.setPadding(new Insets(20));

        Label lblAddDef = new Label("New Definition to Add:");
        TextField tfAddDef = new TextField();
        tfAddDef.setPromptText("Enter New Definition");

        Button btnAdd = new Button("Add Definition");
        btnAdd.setDisable(true);

        btnAdd.setOnAction(e -> {
            String definition = tfAddDef.getText().trim();
            if (definition.isEmpty()) {
                return;
            }

            slangService.addSlangDefinition(tfSlangWord.getText(), definition);
            showAlert("Success", "New slang definition added successfully!");
            tfAddDef.clear();
        });

        addBox.getChildren().addAll(lblAddDef, tfAddDef, btnAdd);
        addTab.setContent(addBox);

        // Tab 2: replacing definition
        Tab replaceTab = new Tab("Replace Slang Definition");
        VBox replaceBox = new VBox(15);
        replaceBox.setPadding(new Insets(20));

        Label lblOldDef = new Label("Old Definition (to replace):");
        TextField tfOldDef = new TextField();
        tfOldDef.setPromptText("Enter Old Definition (Copy Exactly)");

        Label lblNewDef = new Label("New Definition:");
        TextField tfNewDef = new TextField();
        tfNewDef.setPromptText("Enter New Definition");

        Button btnSave = new Button("Save Changes");
        btnSave.setDisable(true);

        btnSave.setOnAction(e -> {
            String newDefinition = tfNewDef.getText().trim();
            if (newDefinition.isEmpty()) {
                return;
            }

            boolean result = slangService.editSlangWord(tfSlangWord.getText(), tfOldDef.getText().trim(), newDefinition);
            if (result) {
                showAlert("Success", "Slang word edited successfully!");showAlert("Success", "Slang word edited successfully!");
            } else {
                showAlert("Error", "Slang word could not be edited!");
            }
            tfOldDef.clear();
            tfNewDef.clear();
        });

        replaceBox.getChildren().addAll(lblOldDef, tfOldDef, lblNewDef, tfNewDef, btnSave);
        replaceTab.setContent(replaceBox);


        // Tab 3: remove a definition
        Tab removeTab = new Tab("Remove Slang Definition");
        VBox removeBox = new VBox(15);
        removeBox.setPadding(new Insets(20));

        Label lblDefinition = new Label("Definition to Remove:");
        TextField tfDefinition = new TextField();
        tfDefinition.setPromptText("Enter Definition to remove (Copy Exactly)");

        Button btnRemove = new Button("Remove Definition");
        btnRemove.setDisable(true);

        btnRemove.setOnAction(e -> {
            String definition = tfDefinition.getText().trim();
            if (definition.isEmpty()) {
                return;
            }

            boolean result = slangService.removeSlangDefinition(tfSlangWord.getText(), definition);
            if (result) {
                showAlert("Success", "Slang definition removed successfully!");
            } else {
                showAlert("Error", "Slang word could not be edited!");
            }
            tfDefinition.clear();
        });

        removeBox.getChildren().addAll(lblDefinition, tfDefinition, btnRemove);
        removeTab.setContent(removeBox);

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
            tfDefinition.setText(existing.getDefinitions().getFirst());
            btnAdd.setDisable(false);
            btnSave.setDisable(false);
            btnRemove.setDisable(false);
        });

        tabPane.getTabs().addAll(addTab, replaceTab, removeTab);

        contentArea.getChildren().addAll(lblTitle, lblSlang, tfSlang, btnFind, lblSlangWord, tfSlangWord, lblDefinitions, tfDefinitions, new Separator(), lblUpdate, tabPane);
    }

    /**
     * 6: Delete pane
     * Users can enter a slang to delete
     */
    private void showDeletePane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Delete Slang Word");

        Label lblSlang = new Label("Slang Word:");
        TextField tfSlang = new TextField();
        tfSlang.setPromptText("Enter Slang Word");

        Button btnDelete = new Button("Delete");

        btnDelete.setOnAction(e -> {
            String slang = tfSlang.getText().trim().toUpperCase();
            if (slang.isEmpty()) {
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete slang: " + slang + " ?");
            confirmation.setContentText("Are you sure? This cannot be undone.");

            confirmation.showAndWait().ifPresent(type -> {
                if (type == ButtonType.OK) {
                    boolean result = slangService.deleteSlangWord(slang);
                    if (result) {
                        showAlert("Success", "Slang word deleted successfully!");
                    } else {
                        showAlert("Error", "Slang word not found!");
                    }

                    tfSlang.clear();
                }
            });

        });

        contentArea.getChildren().addAll(lblTitle, lblSlang, tfSlang, btnDelete);
    }

    /**
     * 7: Reset data pane
     * Press button to reset, let users confirm before reset
     */
    private void handleReset() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Reset");
        confirmation.setHeaderText("All added/edited/deleted data will be lost permanently.");
        confirmation.setContentText("Are you sure you want to reset to the original dictionary?");

        confirmation.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                boolean result = slangService.resetOriginalSlangData();
                if (result) {
                    showAlert("Success", "Data reset to the original dictionary successfully!");
                } else {
                    showAlert("Error", "Error when resetting slang data!");
                }
            }
        });
    }

    /**
     * 8: Random slang pane
     * Press button to get a random slang
     */
    private void showRandomPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("On This Day Slang Word (Random Slang)");

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Label lblSlang = new Label("???");
        Label lblDefinition = new Label("Click the button below to randomize");

        Button btnRandom = new Button("Random");
        btnRandom.setOnAction(e -> {
            SlangWord random = slangService.getRandomSlangWord();
            if (random == null) {
                showAlert("Error", "Slang word not found!");
                return;
            } else {
                lblSlang.setText(random.getSlang());
                lblDefinition.setText(String.join(" | ", random.getDefinitions()));
            }
        });

        container.getChildren().addAll(lblSlang, lblDefinition, btnRandom);
        contentArea.getChildren().addAll(lblTitle, container);
    }

    /**
     * 9. Quiz pane
     * Users choose between guessing a slang from definition or guessing definition of a slang
     */

    private void showQuizPane() {
        contentArea.getChildren().clear();

        Label lblTitle = new Label("Slang Quiz");

        // 1. Mode section
        HBox modeBox = new HBox(20);
        modeBox.setAlignment(Pos.CENTER);
        RadioButton rbModeSlang = new RadioButton("Guess Definition (Given Slang)");
        RadioButton rbModeDef = new RadioButton("Guess Slang (Given Definition)");
        ToggleGroup group = new ToggleGroup();
        rbModeSlang.setToggleGroup(group);
        rbModeDef.setToggleGroup(group);
        rbModeSlang.setSelected(true);
        modeBox.getChildren().addAll(rbModeSlang, rbModeDef);

        // 2. Question area
        VBox questionBox = new VBox(20);
        questionBox.setAlignment(Pos.CENTER);
        Label lblQuestion = new Label("Question:");
        Label lblQuestionContent = new Label("Press Start to Begin!");
        questionBox.getChildren().addAll(lblQuestion, lblQuestionContent);

        // 3. Optional Answer area
        GridPane answerGrid = new GridPane();
        answerGrid.setHgap(10);
        answerGrid.setVgap(10);
        answerGrid.setAlignment(Pos.CENTER);

        Button[] btnAnswers = new Button[4];
        for (int i = 0; i < 4; i++) {
            btnAnswers[i] = new Button("Option" + (i + 1));
            btnAnswers[i].setDisable(true);
            answerGrid.add(btnAnswers[i], i % 2, i / 2);
        }

        // 4. Control button
        Button btnStart = new Button("Start / Next Question");

        Label lblResult = new Label("");

        btnStart.setOnAction(e -> {
            lblResult.setText("");

            // Get 4 random slang (index 0 is the correct slang)
            List<SlangWord> quizData = slangService.getQuizOptions();
            if (quizData == null || quizData.size() < 4) {
                showAlert("Error", "Not enough Slang to create quiz!");
                return;
            }

            SlangWord correctAnswer = quizData.getFirst();

            // Generate display options and use shuffle to change options position
            List<SlangWord> displayOptions = new ArrayList<>(quizData);
            Collections.shuffle(displayOptions);

            // Show question
            if (rbModeSlang.isSelected()) {
                lblQuestionContent.setText("What is the meaning of: \"" + correctAnswer.getSlang() + "\"?");
            } else {
                String definitions = String.join(" | ", correctAnswer.getDefinitions());
                lblQuestionContent.setText("Which slang means: \"" + definitions + "\"");
            }

            // Assign text to 4 option-buttons
            for (int i = 0; i < 4; i++) {
                Button btn = btnAnswers[i];
                btn.setDisable(false);

                SlangWord option = displayOptions.get(i);

                if (rbModeSlang.isSelected()) {
                    btn.setText(String.join(" | ", option.getDefinitions()));
                } else {
                    btn.setText(option.getSlang());
                }

                btn.setOnAction(event -> {
                   if (option.getSlang().equals(correctAnswer.getSlang())) {
                       lblResult.setText("Correct! Good Job 🙌");
                   } else {
                        if (rbModeSlang.isSelected()) {
                            lblResult.setText("Wrong! The correct answer was: " + String.join(" | ", correctAnswer.getDefinitions()));
                        } else {
                            lblResult.setText("Wrong! The correct answer was: " + correctAnswer.getSlang());
                        }
                   }

                   for (Button bt : btnAnswers) {
                       bt.setDisable(true);
                   }
                });
            }
        });



        contentArea.getChildren().addAll(lblTitle, modeBox, questionBox, answerGrid, lblResult, btnStart);
    }

}













