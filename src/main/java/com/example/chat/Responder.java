package com.example.chat;

import java.util.Random;

/**
 * Responder Class that generates a response.
 */

public class Responder {
  private final String[] start = {"Hello", "Hey", "Here you are again", "Hey bro",
    "Hello man", "Hello bro"};

  private final String[] noAnswer = {"I don't understand you", "Bye"};

  /**
  * Return Message Object.
  *
   * @param message message of the user.
  * @return Message Object with random answer.
  */

  public Message makeRespond(String message) {
    Random random = new Random();
    if (message.contains("hello")) {
      return new Message(start[random.nextInt(start.length)], User.FROM_RESPONDER);
    } else {
      return new Message(noAnswer[random.nextInt(noAnswer.length)], User.FROM_RESPONDER);
    }
  }
}
