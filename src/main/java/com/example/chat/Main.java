package com.example.chat;

import com.example.chat.datastructures.Chat;
import com.example.chat.datastructures.ChatStorage;
import com.example.chat.datastructures.Message;
import com.example.chat.utils.SelectChatWindow;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
 * SelectChatWindow: 60 lines of code.
 * Main: 163 lines of code.
 * Responder: 30 lines of code.
 * 446 lines of code.
 */

public class Main extends Application {

  private final Responder responder = new Responder();
  private final ChatStorage chatStorage = ChatStorage.loadChatStorage();
  private Chat chat;
  private final ResourceBundle resourceBundle =
          ResourceBundle.getBundle("messages", Locale.getDefault());
  private Stage stage;
  private VBox chatSectionBox;

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
    chatSectionBox = new VBox();
    chatSectionBox.setAlignment(Pos.BASELINE_CENTER);
    chatSectionBox.setSpacing(10);
    chatSectionBox.setStyle("-fx-padding: 7px");
    ScrollPane scrollPane = new ScrollPane(chatSectionBox);
    scrollPane.setPrefSize(400, 230);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);

    HBox ioBox = new HBox(10);
    ioBox.setStyle("-fx-padding: 10px");
    Button sendBtn = new Button(resourceBundle.getString("sent_message"));
    TextField input = new TextField();
    ioBox.getChildren().addAll(input, sendBtn);

    sendBtn.setOnAction(e -> addMessages(input.getText()));
    sendBtn.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        addMessages(input.getText());
      }
    });
    VBox content = new VBox();
    MenuBar menuBar = createMenuBar();
    content.getChildren().addAll(menuBar, scrollPane, ioBox);
    stage.setResizable(false);
    stage.setOnCloseRequest(e -> chatStorage.updateChatStorage());
    Scene scene = new Scene(content, 420, 300);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
            .getResource("/styleFolder/messageBubble.css")).toExternalForm()
    );
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Constructor.
   */
  public void addMessages(String messageText) {
    Message message = new Message(messageText, 0);
    chat.addMessage(message);
    chatSectionBox.getChildren().add(message.getMessageBubble());
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> {
      Message respondMessage = responder.makeRespond(messageText);
      chat.addMessage(respondMessage);
      chatSectionBox.getChildren().add(respondMessage.getMessageBubble());
    });
    pause.play();
  }

  private MenuBar createMenuBar() {
    Menu fileMenu = new Menu("Chat");
    MenuItem newChatItem = new MenuItem(resourceBundle.getString("create_new_chat"));
    MenuItem loadChatItem = new MenuItem(resourceBundle.getString("show_previous_chats"));
    fileMenu.getItems().addAll(newChatItem, loadChatItem);
    newChatItem.setOnAction(e -> actOnNewChatRequest());
    loadChatItem.setOnAction(e ->  selectNewChat());
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu);
    return menuBar;
  }

  private void selectNewChat() {
    SelectChatWindow selectChatWindow = new SelectChatWindow(chatStorage, resourceBundle);
    selectChatWindow.showAndWait();
    Chat selectedChat = selectChatWindow.getSelectedChat();
    if (selectedChat != null) {
      chat = selectChatWindow.getSelectedChat();
      changeTitle("chat_from", chat.getDate(), " ID: ", chat.getId());
      loadChat();
    }
  }

  private void actOnNewChatRequest() {
    if (chat.isEmpty()) {
      changeTitle("already_new_chat");
    } else {
      Object[] object = chatStorage.getNewChat();
      this.chat = (Chat) object[0];
      if ((boolean) object[1]) {
        chatStorage.addChat(chat);
        changeTitle("new_chat", chat.getId());
      } else {
        changeTitle("chat_from", chat.getDate(), " ID: " + chat.getId());
      }
      loadChat();
    }
  }

  private void loadChat() {
    chatSectionBox.getChildren().clear();
    chatSectionBox.getChildren().addAll(chat.getListOfChatBubbles());
  }

  private void changeTitle(String key, Object... furtherText) {
    String furtherTextString = Arrays.toString(furtherText);
    stage.setTitle(resourceBundle.getString(key) + " " + furtherTextString);
  }

  public static void main(String[] args) {
    launch();
  }
}