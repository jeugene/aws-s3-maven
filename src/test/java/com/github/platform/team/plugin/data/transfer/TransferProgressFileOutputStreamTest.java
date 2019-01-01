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
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TransferProgressFileOutputStreamTest {

    private static final int START_POSITION = 10;
    private static final int END_POSITION = 30;
    private static final int SIZE = 20;
    private static final int BIG_SIZE = 1024;

    private final StubTransferProgress transferProgress = new StubTransferProgress();
    private TransferProgressFileOutputStream outputStream;

    @Before
    public void before() throws Exception {
        this.outputStream = new TransferProgressFileOutputStream(new File("target/test.txt"), this.transferProgress);
    }

    @After
    public void after() {
        IOUtils.closeQuietly(this.outputStream);
    }

    @Test
    public void write() throws Exception {
        // GIVEN
        byte[] expected = {(byte) 1};

        // WHEN
        this.outputStream.write(1);

        // THEN
        assertThat(this.transferProgress.getLength(), equalTo(1));
        assertThat(this.transferProgress.getBuffer(), equalTo(expected));
    }

    @Test
    public void readByteArray() throws Exception {
        // GIVEN
        byte[] expected = new byte[SIZE];
        Arrays.fill(expected, (byte) 1);

        // WHEN
        this.outputStream.write(expected);

        // THEN
        assertThat(this.transferProgress.getLength(), equalTo(SIZE));
        assertThat(this.transferProgress.getBuffer(), equalTo(expected));
    }

    @Test
    public void readyByteArrayLength() throws Exception {
        // GIVEN
        byte[] expected = new byte[SIZE];
        Arrays.fill(expected, (byte) 1);

        // WHEN
        this.outputStream.write(expected, 0, SIZE);

        // THEN
        assertThat(this.transferProgress.getLength(), equalTo(SIZE));
        assertThat(this.transferProgress.getBuffer(), equalTo(expected));
    }

    @Test
    public void readyByteArrayOffsetLength() throws Exception {
        // GIVEN
        byte[] buffer = new byte[BIG_SIZE];
        Arrays.fill(buffer, START_POSITION, END_POSITION, (byte) 1);

        // WHEN
        this.outputStream.write(buffer, START_POSITION, SIZE);

        // THEN
        byte[] expected = new byte[SIZE];
        System.arraycopy(buffer, START_POSITION, expected, 0, SIZE);

        assertThat(this.transferProgress.getLength(), equalTo(SIZE));
        assertThat(this.transferProgress.getBuffer(), equalTo(expected));
    }
}
