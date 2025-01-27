package com.example.chat;

import java.io.Serial;
import java.io.Serializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Message Object with the content of the message and the user frm which the message comes from.
 */

public record Message(String content, User user) implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Sends a Chat bubble box with the Label inside that contains the message.
   */
  public HBox getMessageBubble() {
    Label messageLabel = new Label(content);
    messageLabel.setWrapText(true); // Ensure the text wraps within the bubble

    HBox messageBubble = new HBox(messageLabel);
    Pos alignment = (user == User.FROM_ME) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT;
    BackgroundFill backgroundFill = new BackgroundFill(
            (user == User.FROM_ME) ? Color.LIGHTBLUE : Color.LIGHTGREEN,
            (user == User.FROM_ME) ? new CornerRadii(15, 15, 0, 15, false) :
                    new CornerRadii(15, 15, 15, 0, false), Insets.EMPTY
    );
    messageBubble.setAlignment(alignment);
    messageBubble.setBackground(new Background(backgroundFill));
    messageBubble.setMaxWidth(340);
    messageBubble.setPadding(new Insets(10));
    messageBubble.setPrefWidth(Region.USE_COMPUTED_SIZE);
    messageBubble.setStyle("-fx-font-size: 13pt; "
            + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 3, 0, 0, 1);");

    return messageBubble;
  }
}