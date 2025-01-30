package com.example.chat;

import com.example.chat.datastructures.Chat;
import com.example.chat.datastructures.ChatStorage;
import com.example.chat.datastructures.Message;
import com.example.chat.uiutils.DynamicMenu;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main Program for chat.
 * Chat: 70 lines of code.
 * ChatStorage: 90 lines of code.
 * Message: 33 lines of code.
 * DynamicMenu: 76 lines of code.
 * Main: 164 lines of code.
 * Responder: 27 lines of code.
 * 460 lines of code.
 */

public class Main extends Application {

  private final Responder responder = Responder.getInstance();
  private final ChatStorage chatStorage = ChatStorage.loadChatStorage();
  private Chat chat;
  private final ResourceBundle bundle =
          ResourceBundle.getBundle("messages", Locale.getDefault());
  private Stage stage;
  private VBox chatBox;
  private DynamicMenu dynamicMenu;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    Object[] object = chatStorage.getNewChat();
    chat = (Chat) object[0];
    if ((boolean) object[1]) {
      chatStorage.addChat(chat);
      changeTitle("new_chat", chat.getId());
    } else {
      changeTitle("chat_from", chat.getDate(), " ID: ", chat.getId());
    }
    chatBox = new VBox();
    chatBox.setAlignment(Pos.BASELINE_CENTER);
    chatBox.setSpacing(10);
    chatBox.getStyleClass().add("container");
    ScrollPane scrollPane = new ScrollPane(chatBox);
    scrollPane.setPrefSize(400, 230);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);

    dynamicMenu = new DynamicMenu(chatStorage, bundle);
    ScrollPane menuPane = new ScrollPane(dynamicMenu);
    menuPane.setPrefWidth(200);
    dynamicMenu.getChat().addListener((observable, oldChat, newChat) -> {
      this.chat = newChat;
      changeTitle("chat_from", chat.getDate(), " ID: " + chat.getId());
      loadChat();
    });

    Button newChatBtn = new Button(bundle.getString("create_new_chat"));
    newChatBtn.getStyleClass().add("button");
    newChatBtn.setOnMouseClicked(e -> actOnNewChatRequest());
    dynamicMenu.addCreationBtn(newChatBtn);

    HBox ioBox = new HBox(10);
    ioBox.setStyle("-fx-padding: 10px");
    Button sendBtn = new Button(bundle.getString("sent_message"));
    sendBtn.getStyleClass().add("button");
    TextField input = new TextField();
    input.getStyleClass().add("text-field");
    ioBox.getChildren().addAll(input, sendBtn);

    sendBtn.setOnAction(e -> addMessages(input.getText()));
    sendBtn.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        addMessages(input.getText());
      }
    });
    Button menuButton = new Button("≡");
    menuButton.setOnAction(e -> {
      dynamicMenu.setChatProperty(this.chat);
      dynamicMenu.toggleMenu();
    });

    VBox content = new VBox();
    content.getChildren().addAll(scrollPane, ioBox);

    HBox mainContent = new HBox();
    mainContent.getChildren().addAll(menuPane, content);

    BorderPane root = new BorderPane();
    root.setCenter(mainContent);
    root.setLeft(menuButton);
    Scene scene = new Scene(root, 600, 400);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/styleFolder/messageBubble.css")).toExternalForm());
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/styleFolder/visualizedChatLabel.css")).toExternalForm());
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/styleFolder/otherUiComponents.css")).toExternalForm());
    stage.setOnCloseRequest(e -> chatStorage.updateChatStorage());
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Constructor.
   */
  public void addMessages(String messageText) {
    Message message = new Message(messageText, 0);
    chat.addMessage(message);
    chatBox.getChildren().add(message.getMessageBubble());
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> {
      Message respondMessage = responder.makeRespond();
      chat.addMessage(respondMessage);
      chatBox.getChildren().add(respondMessage.getMessageBubble());
    });
    pause.play();
  }

  private void actOnNewChatRequest() {
    if (chat.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setHeaderText("You already have a new empty chat");
      alert.showAndWait();
    } else {
      Object[] object = chatStorage.getNewChat();
      this.chat = (Chat) object[0];
      if ((boolean) object[1]) {
        chatStorage.addChat(chat);
        dynamicMenu.loadChatLabels(bundle, chatStorage);
        changeTitle("new_chat", chat.getId());
      } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("There are already empty chats in the storage");
        alert.setContentText("The last empty chat has been loaded:" + chat.getId());
        alert.showAndWait();
        changeTitle("chat_from", chat.getDate(), " ID: " + chat.getId());
      }
      loadChat();
    }
  }

  private void loadChat() {
    chatBox.getChildren().clear();
    chatBox.getChildren().addAll(chat.getListOfChatBubbles());
  }

  private void changeTitle(String key, Object... furtherText) {
    String additionOnTitle = String.join(" ",
            Stream.of(furtherText).map(String::valueOf).toArray(String[]::new)
    );
    stage.setTitle(bundle.getString(key) + " " + additionOnTitle);
  }

  public static void main(String[] args) {
    launch();
  }
}