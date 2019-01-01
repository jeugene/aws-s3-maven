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

package com.github.platform.team.plugin.data.transfer;

import com.github.platform.team.plugin.util.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TransferProgressFileInputStreamTest {

    private static final int START_POSITION = 10;
    private static final int BIG_SIZE = 1024;
    private static final int SIZE = 20;

    private final StubTransferProgress transferProgress = new StubTransferProgress();
    private TransferProgressFileInputStream inputStream;

    @Before
    public void before() throws Exception {
        this.inputStream = new TransferProgressFileInputStream(new File("src/test/resources/test.txt"), this.transferProgress);
    }

    @After
    public void after() {
        IOUtils.closeQuietly(this.inputStream);
    }

    @Test
    public void read() throws Exception {
        // GIVEN
        int length = this.inputStream.read();
        byte[] expected = {(byte) length};

        // WHEN
        byte[] actual = this.transferProgress.getBuffer();

        // THEN
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void readByteArray() throws Exception {
        // GIVEN
        byte[] expected = new byte[SIZE];
        this.inputStream.read(expected);

        // WHEN
        byte[] actual = this.transferProgress.getBuffer();

        // THEN
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void readyByteArrayLength() throws Exception {
        // GIVEN
        byte[] expected = new byte[SIZE];
        this.inputStream.read(expected, 0, SIZE);

        // WHEN
        byte[] actual = this.transferProgress.getBuffer();

        // THEN
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void readyByteArrayOffsetLength() throws Exception {
        // GIVEN
        byte[] buffer = new byte[BIG_SIZE];
        int length = this.inputStream.read(buffer, START_POSITION, SIZE);

        byte[] expected = new byte[SIZE];
        System.arraycopy(buffer, START_POSITION, expected, 0, SIZE);

        // WHEN
        byte[] actual = this.transferProgress.getBuffer();

        // THEN
        assertThat(actual, equalTo(expected));
    }
}
