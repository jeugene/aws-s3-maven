package com.github.platform.team.plugin.util;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class IOUtilsTest {

    @Test
    public void copy() throws Exception {
        // GIVEN
        String expected = "expected";
        InputStream in = new ByteArrayInputStream(expected.getBytes());
        OutputStream out = new ByteArrayOutputStream();

        // WHEN
        IOUtils.copy(in, out);

        // THEN
        assertThat(expected, equalTo(out.toString()));
    }

    @Test
    public void closeQuietlyIfNoCloseables() {
        // GIVEN

        // WHEN
        IOUtils.closeQuietly();

        // THEN
        // no exceptions
    }

    @Test
    public void closeQuietlyIfCloseablesIsEmpty() {
        // GIVEN
        Closeable[] closeables = new Closeable[1];

        // WHEN
        IOUtils.closeQuietly(closeables);

        // THEN
        // no exceptions
    }

    @Test
    public void closeQuietlyIfCloseablesThrowsException() throws Exception {
        // GIVEN
        Closeable closeables = mock(Closeable.class);
        doThrow(IOException.class).when(closeables).close();

        // WHEN
        IOUtils.closeQuietly(closeables);

        // THEN
        // no exceptions
    }

    @Test
    public void closeQuietly() throws Exception {
        // GIVEN
        Closeable closeables = mock(Closeable.class);

        // WHEN
        IOUtils.closeQuietly(closeables);

        // THEN
        // no exceptions
    }
}