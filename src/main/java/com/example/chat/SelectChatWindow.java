package com.example.chat;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Confirmation Window for a customized Dialog.
 * <p> </p>
 */

public class SelectChatWindow extends Stage {

  int selectedLabel;

  /**
     * Constructor.
  */
  public SelectChatWindow(ChatStorage chatStorage, Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
    this.setTitle(resourceBundle.getString("select_chat"));
    this.initStyle(StageStyle.DECORATED);
    this.initModality(Modality.APPLICATION_MODAL);
    this.setResizable(false);
    this.setWidth(400);
    this.setHeight(300);
    VBox content = new VBox();
    content.setSpacing(10);
    content.setAlignment(Pos.CENTER);
    ScrollPane scrollPane = new ScrollPane(content);
    ArrayList<Label> labelsFromChatStorage = chatStorage.visualizeChats(locale);
    labelsFromChatStorage.forEach(label -> {
      label.setOnMouseClicked(this::getSelectedChat);
      content.getChildren().add(label);
    });
    Scene scene = new Scene(scrollPane, 400, 300);
    this.setScene(scene);
  }

  /**
     * Gets the id of the Label that has been clicked and puts it in the attribute.
   */

  public void getSelectedChat(Event e) {
    Label clickedLabel = (Label) e.getSource();
    this.selectedLabel = Integer.parseInt(clickedLabel.getId());
    this.close();
  }
}
