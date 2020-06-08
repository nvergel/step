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
import com.google.gson.Gson;
import java.util.Optional;

@WebServlet("/log-in")
public class HomeServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    doPost(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    // Initialize loginContainer to send login info
    LoginContainer loginContainer;

    if (userService.isUserLoggedIn()) {
    
      // Retrieve nickname, may or may have not been set
      Optional<String> nickname = Optional.ofNullable(getUserNickname(userService.getCurrentUser().getUserId()));

      // If nickname is set, greet user with nickname and give option to logout
      if (nickname.isPresent()) {
        String userEmail = userService.getCurrentUser().getEmail();
        String logoutUrl = userService.createLogoutURL("/");
        loginContainer = new LoginContainer("Hello " + nickname.get() + "!", logoutUrl, "Logout");
      } else { // If nickname not set, let user choose one
        loginContainer = new LoginContainer("Choose a nickname.", "/nickname.jsp", "Nickname");
      }
    } else { // Give option to login
      String loginUrl = userService.createLoginURL("/");
      loginContainer = new LoginContainer("Hello stranger", loginUrl, "Login");
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(loginContainer));
  }

  // Returns the nickname of the user or null if the user has not set a nickname.
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

  class LoginContainer {
    String greetingForVisitorOrUser;
    String urlToLogoutOrLogin;
    String typeOfMessage;

    public LoginContainer(String greetingForVisitorOrUser, String urlToLogoutOrLogin, String typeOfMessage) {
        this.greetingForVisitorOrUser = greetingForVisitorOrUser;
        this.urlToLogoutOrLogin = urlToLogoutOrLogin;
        this.typeOfMessage = typeOfMessage;
    }
  }
}
