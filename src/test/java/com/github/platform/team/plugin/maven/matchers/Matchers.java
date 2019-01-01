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

import com.amazonaws.services.s3.model.ListObjectsRequest;
import org.apache.maven.wagon.events.SessionEvent;
import org.apache.maven.wagon.events.TransferEvent;
import org.mockito.ArgumentMatchers;

public abstract class Matchers {

    public static ListObjectsRequest eq(ListObjectsRequest listObjectsRequest) {
        return ArgumentMatchers.argThat(new ListObjectsRequestMatcher(listObjectsRequest));
    }

    public static SessionEvent eq(SessionEvent sessionEvent) {
        return ArgumentMatchers.argThat(new SessionEventMatcher(sessionEvent));
    }

    public static TransferEvent eq(TransferEvent transferEvent) {
        return ArgumentMatchers.argThat(new TransferEventMatcher(transferEvent));
    }
}
