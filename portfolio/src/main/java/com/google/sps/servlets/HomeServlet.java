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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/log-in")
public class HomeServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      // If user has not set a nickname, redirect to nickname page
      String nickname = getUserNickname(userService.getCurrentUser().getUserId());
      if (nickname == null) {
        response.getWriter().println("<p>Choose a nickname.</p>");
        response.getWriter().println("<a href=\"/nickname\">Nickname</a>");
        return;
      }
      
      String userEmail = userService.getCurrentUser().getEmail();
      String logoutUrl = userService.createLogoutURL("/");
      response.getWriter().println("<p>Hello " + nickname + "!</p>");
      response.getWriter().println("<a href=\"" + logoutUrl + "\">Logout</a>");
    } else {
      String loginUrl = userService.createLoginURL("/");
      response.getWriter().println("<p>Hello stranger</p>");
      response.getWriter().println("<a href=\"" + loginUrl + "\">Login</a>");
    }
  }

  // Returns the nickname of the user with id, or null if the user has not set a nickname.
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
