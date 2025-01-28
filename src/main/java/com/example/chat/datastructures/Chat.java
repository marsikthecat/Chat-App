package com.example.chat.datastructures;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;

/**
 * Chat Objekt which contains Messages.
 */

public class Chat implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private int id;
  private final String date;
  private final List<Message> messageList;

  /**
   * Constructor.
   */

  public Chat() {
    this.messageList = new ArrayList<>();
    LocalDateTime creationDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    this.date = creationDate.format(formatter);
  }

  /**
   * Adds the message to the list if it's not null.
   */
  public void addMessage(Message message) {
    if (message == null) {
      throw new IllegalArgumentException("Message is not valid");
    }
    messageList.add(message);
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  /**
   * Creates a List of MessageBoxes from messages that are in the List.
   */
  public List<HBox> getListOfChatBubbles() {
    List<HBox> messageBoxes = new ArrayList<>();
    for (Message message : messageList) {
      messageBoxes.add(message.getMessageBubble());
    }
    return messageBoxes;
  }

  public boolean isEmpty() {
    return messageList.isEmpty();
  }
}