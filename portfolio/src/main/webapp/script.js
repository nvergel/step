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
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function addRandomFact() {
  var num = 100;
  const facts =
      ['I am 20 years old', 'I live in Colorado', 'I am a human', 'I am not a robot'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factsContainer = document.getElementById('fact');
  factsContainer.innerText = fact;

  // Add function to move text back and forth
  if (factsContainer.style.marginLeft == "")
    setInterval( function() {
            factsContainer.style.marginLeft = (Math.abs(100 - num)%100).toString() + "px";
            num = num % 199 + 1;
            console.log((Math.abs(100 - num)%100).toString() + "px");
        }, 20);
}

function dispM1() {

    document.getElementById('m1').style.display = "block"; 
    document.getElementById('m2').style.display = "none"; 
}

function dispM2() {

    document.getElementById('m1').style.display = "none"; 
    document.getElementById('m2').style.display = "block"; 
}