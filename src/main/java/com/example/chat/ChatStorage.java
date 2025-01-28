package com.example.chat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

/**
 * ChatStorage that stores Chats Objects.
 */

public class ChatStorage extends HashMap<Integer, Chat> implements Serializable {

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

  /**
   * Adds the chat to the array, sets up the id and creation date of the new chat.
   */

  public void addChat(Chat chat) {
    put(id++, chat);
  }

  public Chat getChat(int id) {
    return get(id);
  }

  /**
   * Creates a List of Labels that visualizes all the chat objects stores in the array,
   * writes the content and sets up the id and returns it.
   */

  public ArrayList<Label> visualizeChats(ResourceBundle bundle) {
    ArrayList<Label> labels = new ArrayList<>();
    for (int i = 0; i < size(); i++) {
      Chat chat = get(i);
      Label label = new Label(bundle.getString("chat_from")
         + " "  + chat.getDate() + "\n" + i);
      label.setId(String.valueOf(i));
      labels.add(label);
    }
    return labels;
  }
}