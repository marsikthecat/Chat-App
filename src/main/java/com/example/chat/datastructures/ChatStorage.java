package com.example.chat.datastructures;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

/**
 * ChatStorage that stores Chats Objects.
 */

public class ChatStorage extends ArrayList<Chat> implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private int id = 0;

  private ChatStorage() {}

  /**
   * saves the Chat by serialization.
   */
  public void updateChatStorage() {
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
      return chatStorage;
    } catch (IOException | ClassNotFoundException i) {
      i.getCause();
      return new ChatStorage();
    }
  }

  public void addChat(Chat chat) {
    chat.setId(id++);
    add(chat);
  }

  public Chat getChat(int id) {
    return get(id);
  }

  /**
   * Searches for an already existing chat that is empty.
   * If there is an empty chat it returns it with "false" (=No new Chat created).
   * If no empty chat is found, it returns a new chat with "true" (=New Chat was created).
   */
  public Object[] getNewChat() {
    for (int i = size() - 1; i >= 0; i--) {
      Chat chat = get(i);
      if (chat.isEmpty()) {
        return new Object[]{chat, false};
      }
    }
    return new Object[]{new Chat(), true};
  }

  /**
   * Creates a List of Labels that visualizes all the chat objects stores in the array,
   * writes the content and sets up the id and returns it.
   */
  public ArrayList<Label> visualizeChats(ResourceBundle bundle) {
    ArrayList<Label> labels = new ArrayList<>();
    for (int i = 0; i < size(); i++) {
      Chat chat = get(i);
      Label label = new Label(bundle.getString("chat_from") + " "
              + chat.getDate() + "\n" + "ID: " + i);
      label.setId(String.valueOf(i));
      labels.add(label);
    }
    return labels;
  }
}