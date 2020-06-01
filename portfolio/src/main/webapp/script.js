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
  
  /* Move text back and forth:
   * Add function that simulates f(x) = |x| on the interval -range < x < range
   * We use position to denote f(x) which also determines the width of the left margin.
   * The width of the left margin oscillates between 0 and range.
   */
  var x = 0;
  const range = 100;
  if (factsContainer.style.marginLeft == "")
    setInterval( function() {
      const position = Math.abs(x);
      factsContainer.style.marginLeft = position.toString() + "px";
      x = (x == range) ? -range : x + 1;
    }, 20);
}

/*
 * Add or remove .hidden to div
 */
function hideUnhide(content)  {
  if (content.classList.contains("hidden")) {
    content.classList.remove("hidden");
  } else {
    content.classList.add("hidden");
  }
}

/*
 * Defines behavior of .collapsible and .content
 */
function collapsible() {
  // Get list of buttons in personal project
  var collapsibles = document.getElementsByClassName("collapsible");
  for (const i in collapsibles) {
    collapsibles[i].addEventListener("click", function() {
      // When button is clicked, hide or undhide its respective content
      var content = this.nextElementSibling;
      hideUnhide(content);
    })};
}

/*
 * Swaps back and forth between .m1 and .m2
 * when button is clicked
 */
function swapMotorcyclePicture() {
    hideUnhide(document.getElementById('m1')); 
    hideUnhide(document.getElementById('m2')); 
}

/*
 * send GET request to server let
 */
function getRandomQuote() {
  fetch('/data').then(response => response.text()).then((quote) => {
    document.getElementById('quote-container').innerHTML = quote;
  });
}
