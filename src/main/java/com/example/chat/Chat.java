package com.example.chat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

  public void addMessage(Message message) {
    messageList.add(message);
  }

  public int getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isEmpty() {
    return messageList.isEmpty();
  }

  public List<Message> getMessageList() {
    return new ArrayList<>(messageList);
  }
}