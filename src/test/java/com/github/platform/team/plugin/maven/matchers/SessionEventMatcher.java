/*
 * Copyright 2018-Present Platform Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.platform.team.plugin.maven.matchers;

import org.apache.maven.wagon.events.SessionEvent;
import org.mockito.ArgumentMatcher;

public class SessionEventMatcher implements ArgumentMatcher<SessionEvent> {

    private final SessionEvent sessionEvent;

    SessionEventMatcher(SessionEvent sessionEvent) {
        this.sessionEvent = sessionEvent;
    }

    @Override
    public boolean matches(SessionEvent obj) {
        if (this.sessionEvent == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (SessionEvent.class != obj.getClass()) {
            return false;
        }
        SessionEvent other = obj;
        if (this.sessionEvent.getEventType() != other.getEventType()) {
            return false;
        }
        if (this.sessionEvent.getWagon() == null) {
            if (other.getWagon() != null) {
                return false;
            }
        } else if (!this.sessionEvent.getWagon().equals(other.getWagon())) {
            return false;
        }
        if (this.sessionEvent.getException() == null) {
            if (other.getException() != null) {
                return false;
            }
        } else if (!this.sessionEvent.getException().equals(other.getException())) {
            return false;
        }
        return true;
    }
}
