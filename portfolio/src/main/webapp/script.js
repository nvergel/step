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
  collapsible();
  expandSections();
  window.addEventListener('scroll', function(e) {
      if (window.scrollY < 184) {
          document.getElementById("contact-info-top").classList.remove("hidden");
      } else {
          document.getElementById("contact-info-top").classList.add("hidden");
      }
  });
}

function expandSections() {
  const blocks = document.getElementsByClassName("block");
  const numberOfElements = blocks.length;
  for (var i = 0; i < numberOfElements; ++i) {
    block = blocks[i];
    block.scrollTop = block.scrollHeight; 
  }
  document.getElementsByClassName("wrapper")[0].classList.remove("pre-load");
}

var numberOfCollapsibles = 0;
const sizes = ["one", "two", "three", "four"];
/*
 * Add or remove .hidden to element
 */
function hideUnhide(content) {
  var section = document.getElementById("third");
  if (section.classList.contains("transition1")) {
    section.classList.remove("transition1");
    section.classList.add("transition2");
  }
  section.classList.remove(sizes[numberOfCollapsibles]);

  if (content.classList.contains("hidden")) {
    content.classList.remove("hidden");
    numberOfCollapsibles++;
  } else {
    content.classList.add("hidden");
    numberOfCollapsibles--;
  }

  section.classList.add(sizes[numberOfCollapsibles]);
}

/*
 * This funcion adds a listener to .collapsble which hides/unhides .content
 */
function collapsible() {
  var section = document.getElementById("third");
  section.classList.add(sizes[numberOfCollapsibles]);
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
