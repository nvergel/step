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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class MessageTest {

  @Test
  public void testMessage() {
    Message message = new Message("1", "Hi", 1);

    Assert.assertEquals("1", message.userId);
    Assert.assertEquals("Hi", message.userText);
    Assert.assertEquals(1, message.timeCreated);
  }

  @Test
  public void testLessThanAndGreaterThan() {
    Message message1 = new Message("0", "a", 1);
    Message message2 = new Message("1", "b", 2);
    Message message3 = new Message("2", "c", 1);

    Assert.assertTrue(message1.compareTo(message2) > 0);
    Assert.assertTrue(message2.compareTo(message1) < 0);
    Assert.assertEquals(message1.compareTo(message3), 0);
    Assert.assertEquals(message3.compareTo(message1), 0);
  }

@Test
  public void testEqual() {
    Message message1 = new Message("0", "a", 1);
    Message message2 = new Message("0", "a", 1);

    Assert.assertEquals(message1, message2);
  }
}
