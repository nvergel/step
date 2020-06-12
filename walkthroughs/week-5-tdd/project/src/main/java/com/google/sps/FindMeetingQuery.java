// Copyright 2020 Google LLC
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

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

public final class FindMeetingQuery {
  private int beginSlot;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> timesForMeeting = new ArrayList<TimeRange>();

    // Meeting is too long  
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return timesForMeeting;
    }
    
    getTimeSlots(events, request, timesForMeeting);

    // Check for open slot at end of day
    if ( beginSlot < TimeRange.END_OF_DAY) {
      timesForMeeting.add(TimeRange.fromStartEnd(beginSlot, TimeRange.END_OF_DAY, true));
    }
    return timesForMeeting;
  }
  
  // Loop through list of events to find time slots that fit request requirenment
  private void getTimeSlots(Collection<Event> events, MeetingRequest request, ArrayList<TimeRange> timesForMeeting) {
    // Assume optional attendees can't be included until slot is found
    boolean optionalsIncluded = request.getAttendees().isEmpty();

    // Begin search at start of day
    beginSlot = TimeRange.START_OF_DAY;
    int beginSlotOptional = beginSlot;
    for (Event event : events) {
      // Check for conflicts
      boolean noConflict = checkForConflict(event, request.getAttendees());
      boolean noOptionalConflict = checkForConflict(event, request.getOptionalAttendees());

      if (slotFits(event, request, beginSlotOptional)) {
        optionalsIncluded = true;
      } else if (!noOptionalConflict) {
        beginSlotOptional = event.getWhen().end();
      }
     
      noConflict = optionalsIncluded ? noConflict && noOptionalConflict : noConflict;
      if (noConflict) {
        continue;
      }
      addSlotIfItFits(event, request, timesForMeeting);
    }
  }
  
  /* Checks if the set of an event's attendees does not intersect the
   * set of the request's attendees
   */
  private boolean checkForConflict(Event event, Collection<String> attendees) {
    return Collections.disjoint(event.getAttendees(), attendees);
  }

  private void addSlotIfItFits(Event event, MeetingRequest request,
                              ArrayList<TimeRange> timesForMeeting) {
    // On conflict end time slot before event
    int endSlot = event.getWhen().start();

    // Check that request fits in time slot and add time slot
    if ( slotFits(event, request, beginSlot) ) {
      timesForMeeting.add(TimeRange.fromStartEnd(beginSlot, endSlot, false));
    }

    // Start new slot after event ends
    if (beginSlot < event.getWhen().end()) {
      beginSlot = event.getWhen().end();
    }
  }

  // Returns false if the current slot is too short for the request
  private boolean slotFits(Event event, MeetingRequest request, int beginTimeSlot) {
    int eventLength = event.getWhen().start() - beginTimeSlot;
    return eventLength >= request.getDuration();
  }
}
