package com.example.chat;

import com.example.chat.datastructures.Message;
import java.util.Random;

/**
 * Singleton Responder Class that generates a response.
 */

public class Responder {
  private static final Responder responder = new Responder();
  private final String[] answers = {"Hey bro", "I don't know", "Bye"};

  private Responder() {}

  public static Responder getInstance() {
    return responder;
  }

  /**
  * Return Message Object with random response.
  */
  public Message makeRespond() {
    Random random = new Random();
    return new Message(answers[random.nextInt(answers.length)], 1);
  }
}