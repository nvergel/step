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

//Runs two functions on load
function executeFunctions() {
  signIn();
  moveText();
  displayMessages();
  collapsible();
}

// Run serverlet to get url for log in
function signIn() {
  fetch('/log-in', {method: 'POST'}).then(loginContainer => loginContainer.json()).then(loginContainer => {
    // Get [p, a] elements from log in div
    logInElements = document.getElementById("log-in").children;

    // Set inner text for paragraph
    logInElements[0].innerText = loginContainer.greetingForVisitorOrUser;

    // Set link and text for anchor
    logInAnchor = logInElements[1];
    logInAnchor.href = loginContainer.urlToLogoutOrLogin;
    logInAnchor.innerText = loginContainer.typeOfMessage;

    // User has to be logged in and with a nickname to post messages
    if (loginContainer.typeOfMessage != "Logout") {
      document.getElementById("input-container").classList.add("hidden");

      const textIfNotLoggedIn = document.createElement('p');

      if (loginContainer.typeOfMessage == "Login") {
        textIfNotLoggedIn.innerText = "Login with google account to post messages";
      }

      if (loginContainer.typeOfMessage == "Nickname") {
        textIfNotLoggedIn.innerText = "Choose a nickname to post messages";
      }

      document.getElementById("message-title").after(textIfNotLoggedIn);
    }
  });
}

// Load google maps inside map div
function getGoogleMap(placeToPutMarker, nameOfPlace) {
  var mapDiv = document.getElementsByClassName("map")[0]

  // This ensures expand is toggled on every time
  if (!mapDiv.classList.toggle("expand")) {
    mapDiv.classList.toggle("expand")}

  // Make the map
  var map = new google.maps.Map(mapDiv, {
    center: placeToPutMarker,
    zoom: 16,
    mapTypeId: 'satellite'
  });

  // Make the marker
  var marker = new google.maps.Marker({
    position: placeToPutMarker,
    animation: google.maps.Animation.DROP,
    map: map,
    title:nameOfPlace
  });
  
  // Note: We can use similar code to plot how "risky" an area is
  var heatmapData = [];
  for (var i = 0; i < 10; i++) {
    var latLng = new google.maps.LatLng(placeToPutMarker.lat + Math.random()/10, placeToPutMarker.lng + Math.random()/10);
    heatmapData.push(latLng);
  }
  var heatmap = new google.maps.visualization.HeatmapLayer({
    data: heatmapData,
    dissipating: false,
    map: map
  });
}

/**
 * Displays random fact about me
 */
function addRandomFact() {
  const facts =
    ['I am 20 years old', 'I live in Colorado', 'I am a human', 'I am not a robot'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factsContainer = document.getElementById('fact');
  factsContainer.innerText = fact;
}

/* Move text back and forth:
* Add function that simulates f(x) = |x| on the interval -range < x < range
* We use position to denote f(x) which also determines the width of the left margin.
* The width of the left margin oscillates between 0 and range.
*/
function moveText() {
  var x = 0;
  const range = 100;
  const factsContainer = document.getElementById('fact');
  setInterval( function() {
    const position = Math.abs(x);
    factsContainer.style.marginLeft = position.toString() + "px";
    x = (x == range) ? -range : x + 1;
  }, 20);
}

/*
 * Add or remove .hidden to element
 */
function hideUnhide(content)  {
  if (content.classList.contains("hidden")) {
    content.classList.remove("hidden");
  } else {
    content.classList.add("hidden");
  }
}

/*
 * This funcion adds a listener to .collapsble which hides/unhides .content
 */
function collapsible() {
  // Get list of buttons in personal project
  var collapsibles = document.getElementsByClassName("collapsible");
  const numberOfElements = collapsibles.length;
  for (var i = 0; i < numberOfElements; ++i) {
    collapsibles[i].addEventListener("click", function() {
      // When button is clicked, hide or undhide its respective content
      var content = this.nextElementSibling;
      hideUnhide(content);
    })
  };
}

/*
 * Swaps back and forth between .m1 and .m2
 * when button is clicked
 */
function swapMotorcyclePicture() {
  hideUnhide(document.getElementById('m1')); 
  hideUnhide(document.getElementById('m2')); 
}

// Delete Messages
function deleteMessage(id) {
  const params = new URLSearchParams();
  params.append('id', id);
  fetch('/delete-data', {method: 'POST', body: params}).then(() => displayMessages());
}

// Upload message to website
function postMessage() {
  const textInput = document.getElementById('text-input');
  const text = textInput.value;
  if (text != ""){
    const params = new URLSearchParams()
    params.append('text-input', text);
    const request = new Request('/data', {method: 'POST', body: params});
    fetch(request).then(() => displayMessages());
  }
  textInput.value = "";
}

// send GET request to server let
function displayMessages() {
  fetch('/data').then(response => response.json()).then( messages => {
    const height = Math.min(messages.length, 5) * 100
    const displayMessages = document.getElementById('messages-container');
    displayMessages.innerHTML = "";
    messages.forEach(message => displayMessages.appendChild(createMessage(message)));
    document.getElementById('messages-container').style.height = height.toString() + "px";
  });
}

// Creates element for individual message.
function createMessage(message) {

  const container = document.createElement('div');
  /* If current user created this message 
     add button to delete message */
  if (message.messageCreatedByUser) {
    addRemoveButton(container, message.messageId)
  }

  // display message with name
  addText(container, "name", message.userName)
  addText(container, "message", message.userText)

  return container;
}

// Helper functions for messages  -------

function addRemoveButton(container, id) {
  const remove = document.createElement('button');
  remove.innerText = "X";
  remove.onclick = () => deleteMessage(id);
  remove.classList.add("remove");
  container.appendChild(remove);
}

function addText(container, type, text) {
  const pElement = document.createElement('p');
  pElement.classList.add(type);
  pElement.innerText = text;
  container.appendChild(pElement);
}
