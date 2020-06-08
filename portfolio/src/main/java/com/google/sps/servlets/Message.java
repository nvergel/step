package com.google.sps.servlets;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Message implements Comparable< Message >{
  @Id Long messageId;
  long timeCreated;
  String userId;
  String userText;

  private Message() {}

  public Message(String userId, String userText, long timeCreated) {
    this.userId = userId;
    this.userText = userText;
    this.timeCreated = timeCreated;
  }

  @Override
  public int compareTo(Message o) {
    if (timeCreated == o.timeCreated) {
      return 0;
    } else if (timeCreated < o.timeCreated) {
      return 1;
    } else {
      return -1;
    }
  }
}
