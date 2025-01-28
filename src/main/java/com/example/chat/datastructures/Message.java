package com.example.chat.datastructures;

import java.io.Serial;
import java.io.Serializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Message Object with the content of the message and the user frm which the message comes from.
 */

public record Message(String content, int user) implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Gets a Chat bubble box with the Message Label inside.
   */
  public HBox getMessageBubble() {
    Label messageLabel = new Label(content);
    messageLabel.setWrapText(true);
    HBox messageBubble = new HBox(messageLabel);
    String alignmentClass = (user == 0) ? "message-from-me" : "message-from-others";
    messageBubble.getStyleClass().addAll("message-bubble", alignmentClass);
    Pos alignment = (user == 0) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT;
    messageBubble.setAlignment(alignment);
    messageBubble.setPrefWidth(Region.USE_COMPUTED_SIZE);
    return messageBubble;
  }
}