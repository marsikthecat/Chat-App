package com.example.chat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * ChatStorage that stores Chats Objects.
 */

public class ChatStorage implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final List<Chat> chatList;
  private int id = 0;

  public ChatStorage() {
    this.chatList = new ArrayList<>();
  }

  /**
   * saves the Chat by serialization.
   */
  public void saveChatStorage() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Storage.ser"))) {
      out.writeObject(this);
    } catch (IOException i) {
      i.getCause();
    }
  }

  /**
   * Lodes ChatStorage from the File and deserialize it.
   */
  public static ChatStorage loadChatStorage() {
    ChatStorage chatStorage;
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("Storage.ser"))) {
      chatStorage = (ChatStorage) in.readObject();
    } catch (IOException | ClassNotFoundException i) {
      i.getCause();
      return new ChatStorage();
    }
    return chatStorage;
  }

  /**
   * Adds the chat to the array, sets up the id and creation date of the new chat.
   */

  public void addChat(Chat chat) {
    chat.setId(++id);
    chatList.add(chat);
  }

  public Chat getChat(int i) {
    return chatList.get(i);
  }

  public ArrayList<Chat> getChatStorage() {
    return new ArrayList<>(chatList);
  }

  /**
   * Creates a List of Labels that visualizes all the chat objects stores in the array,
   * writes the content and sets up the id and returns it.
   */

  public ArrayList<Label> visualizeChats(Locale locale) {
    ArrayList<Label> labels = new ArrayList<>();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
    for (Chat chat : chatList) {
      Label label = new Label(resourceBundle.getString("chat_from")
              + chat.getDate() + "\n" + chat.getId());
      label.setStyle(
              "-fx-font-size: 12pt; "
                      + "-fx-padding: 10px; "
                      + "-fx-background-color: #f0f0f0; "
                      + "-fx-border-color: #cccccc; "
                      + "-fx-border-radius: 5px; "
                      + "-fx-background-radius: 5px; "
                      + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 3, 0, 0, 1);"
      );
      label.setPrefWidth(350);
      VBox.setMargin(label, new Insets(10, 0, 0, 10));
      label.setId(String.valueOf(chat.getId()));
      label.setOnMouseEntered(e -> label.setStyle(
              "-fx-font-size: 12pt; "
                      + "-fx-padding: 10px; "
                      + "-fx-background-color: #e0e0e0; "
                      + "-fx-border-color: #bbbbbb; "
                      + "-fx-border-radius: 5px; "
                      + "-fx-background-radius: 5px; "
                      + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 4, 0, 0, 2);"
      ));
      label.setOnMouseExited(e -> label.setStyle(
              "-fx-font-size: 12pt; "
                      + "-fx-padding: 10px; "
                      + "-fx-background-color: #f0f0f0; "
                      + "-fx-border-color: #cccccc; "
                      + "-fx-border-radius: 5px; "
                      + "-fx-background-radius: 5px; "
                      + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 3, 0, 0, 1);"
      ));
      label.setPrefWidth(350);
      VBox.setMargin(label, new Insets(10, 0, 0, 10));
      label.setId(String.valueOf(chat.getId()));
      labels.add(label);
    }
    return labels;
  }
}