package com.example.chat;

import java.util.Locale;
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
 * Main: 141 lines of code.
 * Responder: 30 lines of code.
 * Message: 46 lines of code.
 * Chat: 62 lines of code.
 * ChatStorage: 97 lines of code.
 * SelectChatWindow: 59 lines of code.
 * 455 lines of code.
 */

public class Main extends Application {

  private final Responder responder = new Responder();
  private final ChatStorage chatStorage = ChatStorage.loadChatStorage();
  private Chat chat;
  private final ResourceBundle resourceBundle =
          ResourceBundle.getBundle("messages", Locale.getDefault());

  @Override
  public void start(Stage stage) {
    int possibleEmptyChatIndex = chatStorage.checkForEmptyChats();
    if (possibleEmptyChatIndex != -1) {
      this.chat = chatStorage.get(possibleEmptyChatIndex);
    } else {
      this.chat = new Chat();
      chatStorage.addChat(chat);
    }
    VBox chatSectionBox = new VBox();
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

    sendBtn.setOnAction(e -> addMessages(input.getText(), chatSectionBox));

    sendBtn.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        addMessages(input.getText(), chatSectionBox);
      }
    });

    VBox content = new VBox();
    content.getChildren().addAll(createMenuBar(chatSectionBox, stage), scrollPane, ioBox);
    stage.setTitle(resourceBundle.getString("new_chat"));
    stage.setResizable(false);
    stage.setOnCloseRequest(e -> chatStorage.updateChatStorage());
    Scene scene = new Scene(content, 420, 300);
    stage.setScene(scene);
    stage.show();
  }
  /**
   * Constructor.
   */

  public void addMessages(String messageText, VBox chatSection) {
    Message message = new Message(messageText, User.FROM_ME);
    chat.addMessage(message);
    chatSection.getChildren().add(message.getMessageBubble());
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> {
      Message respondMessage = responder.makeRespond(messageText);
      chat.addMessage(respondMessage);
      chatSection.getChildren().add(respondMessage.getMessageBubble());
    });
    pause.play();
  }

  private MenuBar createMenuBar(VBox chatSectionBox, Stage stage) {
    Menu fileMenu = new Menu("Chat");
    MenuItem newChatItem = new MenuItem(resourceBundle.getString("create_new_chat"));
    MenuItem loadChatItem = new MenuItem(resourceBundle.getString("show_previous_chats"));
    fileMenu.getItems().addAll(newChatItem, loadChatItem);
    newChatItem.setOnAction(e -> {
      if (!chat.isEmpty()) {
        int indexOfNextEmptyChat = chatStorage.checkForEmptyChats();
        if (indexOfNextEmptyChat == -1) {
          this.chat = new Chat();
          chatStorage.addChat(chat);
          stage.setTitle(resourceBundle.getString("new_chat"));
          loadChat(chatSectionBox);
        } else {
          this.chat = chatStorage.get(indexOfNextEmptyChat);
          stage.setTitle("Id: " + indexOfNextEmptyChat);
          loadChat(chatSectionBox);
        }
      } else {
        stage.setTitle(resourceBundle.getString("already_new_chat"));
      }
    });
    loadChatItem.setOnAction(e -> {
      SelectChatWindow selectChatWindow = new SelectChatWindow(chatStorage, resourceBundle);
      selectChatWindow.showAndWait();

      Chat selectedChat = selectChatWindow.getSelectedChat();
      if (selectedChat != null) {
        this.chat = selectChatWindow.getSelectedChat();
        stage.setTitle(resourceBundle.getString("chat_from") + " " + chat.getDate());
        loadChat(chatSectionBox);
      }
    });
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu);
    return menuBar;
  }

  private void loadChat(VBox chatSectionBox) {
    chatSectionBox.getChildren().clear();
    chatSectionBox.getChildren().addAll(chat.getMessageBoxes());
  }

  public static void main(String[] args) {
    launch();
  }
}