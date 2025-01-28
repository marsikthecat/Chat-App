package com.example.chat.utils;

import com.example.chat.datastructures.Chat;
import com.example.chat.datastructures.ChatStorage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Confirmation Window for a customized Dialog.
 */

public class SelectChatWindow extends Stage {

  private Chat selectedChat;

  /**
   * Constructor.
  */
  public SelectChatWindow(ChatStorage chatStorage, ResourceBundle bundle) {
    this.setTitle(bundle.getString("select_chat"));
    this.setResizable(false);
    this.initStyle(StageStyle.DECORATED);
    this.initModality(Modality.APPLICATION_MODAL);
    VBox content = new VBox(10);
    ArrayList<Label> labelsFromChatStorage = chatStorage.visualizeChats(bundle);
    for (Label label : labelsFromChatStorage) {
      int labelId = Integer.parseInt(label.getId());
      label.getStyleClass().add("chat-label");
      label.setOnMouseClicked(e -> {
        this.selectedChat = chatStorage.getChat(labelId);
        this.close();
      });
      content.getChildren().add(label);
    }
    content.setAlignment(Pos.CENTER);
    ScrollPane scrollPane = new ScrollPane();
    StackPane centeredContent = new StackPane(content);
    centeredContent.setAlignment(Pos.CENTER);
    scrollPane.setContent(centeredContent);
    scrollPane.setFitToWidth(true);
    Scene scene = new Scene(scrollPane, 400, 300);
    this.setScene(scene);
    scene.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource("/styleFolder/visualizedChatLabel.css")).toExternalForm());
  }

  public Chat getSelectedChat() {
    return selectedChat;
  }
}