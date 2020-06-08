// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;
 
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get userId to check for user's message
    UserService userService = UserServiceFactory.getUserService();
    Optional<String> currentUser = Optional.empty();
    if (userService.isUserLoggedIn()) {
      currentUser = Optional.of(userService.getCurrentUser().getUserId());
    }

    // Load in saved messages
    List<Message> messages = ofy().load().type(Message.class).list();
    Collections.sort(messages);

    // Initiate list of displayable messages
    ArrayList<DisplayableMessage> messagesToDisplay = new ArrayList<DisplayableMessage>();

    for (Message message : messages) {
      boolean messageCreatedByUser = currentUser.filter(currentUserId -> currentUserId.equals(message.userId)).isPresent();
      DisplayableMessage dispMessage = new DisplayableMessage(message, messageCreatedByUser);
      messagesToDisplay.add(dispMessage);
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(messagesToDisplay));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Only let logged in users to post
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {

      // Store id to retrieve nickname when displaying messages
      String userId = userService.getCurrentUser().getUserId();

      // Check for text
      Optional<String> userText = Optional.ofNullable(request.getParameter("text-input"));
      if (userText.filter(text -> !text.equals("")).isPresent()) {
        // Keep timestamp to display newest message first
        long timeCreated = System.currentTimeMillis();
        Message userMessage = new Message(userId, userText.get(), timeCreated);
        ofy().save().entity(userMessage).now();

      } else {
        throw new IllegalArgumentException("No message found");
      }
    } else {// Maybe throw error
      throw new IllegalStateException("User not logged in");
    }
  }

  // Extracts data from Message object to display
  class DisplayableMessage {
    String userName;
    String userText;
    Long messageId;
    boolean messageCreatedByUser;

    public DisplayableMessage(Message message, boolean messageCreatedByUser) {
      userName = getUserNickname(message.userId);
      userText = message.userText;
      messageId = message.messageId;
      this.messageCreatedByUser = messageCreatedByUser;
    }
  }

  /**
   * Returns the nickname of the user with id, or empty String if the user has not set a nickname.
   */
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    com.google.appengine.api.datastore.Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }

}