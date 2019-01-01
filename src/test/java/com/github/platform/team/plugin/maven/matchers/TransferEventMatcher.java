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

import org.apache.maven.wagon.events.TransferEvent;
import org.mockito.ArgumentMatcher;

public class TransferEventMatcher implements ArgumentMatcher<TransferEvent> {

    private final TransferEvent transferEvent;

    TransferEventMatcher(TransferEvent transferEvent) {
        this.transferEvent = transferEvent;
    }

    @Override
    public boolean matches(TransferEvent obj) {
        if (this.transferEvent == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (TransferEvent.class != obj.getClass()) {
            return false;
        }
        TransferEvent other = obj;
        if (this.transferEvent.getEventType() != other.getEventType()) {
            return false;
        }
        if (this.transferEvent.getRequestType() != other.getRequestType()) {
            return false;
        }
        if (this.transferEvent.getWagon() == null) {
            if (other.getWagon() != null) {
                return false;
            }
        } else if (!this.transferEvent.getWagon().equals(other.getWagon())) {
            return false;
        }
        if (this.transferEvent.getResource() == null) {
            if (other.getResource() != null) {
                return false;
            }
        } else if (!this.transferEvent.getResource().equals(other.getResource())) {
            return false;
        }
        if (this.transferEvent.getException() == null) {
            if (other.getException() != null) {
                return false;
            }
        } else if (!this.transferEvent.getException().equals(other.getException())) {
            return false;
        }
        return true;
    }
}
