package com.example.chat.uiutils;

import com.example.chat.datastructures.Chat;
import com.example.chat.datastructures.ChatStorage;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Toggle Menu that contains previous chats that can be selected.
 */

public class DynamicMenu extends VBox {

  private final TranslateTransition contentAnimation;
  private boolean isMenuVisible = false;
  private final ObjectProperty<Chat> chat = new SimpleObjectProperty<>();

  /**
     * Sets up the animation and then uses the method to load the chat labels.
     */
  public DynamicMenu(ChatStorage chatStorage, ResourceBundle bundle) {
    this.setStyle("-fx-background-color: #DDDDDD; -fx-padding: 10px;");
    this.setTranslateX(-200);
    this.setAlignment(Pos.TOP_CENTER);
    contentAnimation = new TranslateTransition(Duration.millis(400), this);
    loadChatLabels(bundle, chatStorage);
  }

  /**
     * loads the chat labels.
     * this method will be also used for reloading if a chat gets added.
     */

  public void loadChatLabels(ResourceBundle bundle, ChatStorage chatStorage) {
    getChildren().clear();
    Label previousChatLabel = new Label(bundle.getString("previous_chats"));
    getChildren().add(previousChatLabel);
    ArrayList<Label> labelsFromChatStorage = chatStorage.visualizeChats(bundle);
    for (Label label : labelsFromChatStorage) {
      int labelId = Integer.parseInt(label.getId());
      label.getStyleClass().add("chat-label");
      label.setOnMouseClicked(e -> chat.set(chatStorage.getChat(labelId)));
      getChildren().add(label);
    }
  }

  /**
     * self explaining.
     */

  public void toggleMenu() {
    contentAnimation.setToX(isMenuVisible ? -200 : 0);
    contentAnimation.play();
    isMenuVisible = !isMenuVisible;
  }

  public void addCreationBtn(Button button) {
    getChildren().add(button);
  }

  public void setChatProperty(Chat currentChat) {
    chat.set(currentChat);
  }

  public ObjectProperty<Chat> getChat() {
    return chat;
  }
}