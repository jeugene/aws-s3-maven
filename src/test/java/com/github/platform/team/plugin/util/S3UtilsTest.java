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

package com.github.platform.team.plugin.util;

import com.amazonaws.ClientConfiguration;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
public class S3UtilsTest {

    @Test(expected = NullPointerException.class)
    public void getBucketNameIfRepositoryIsNull() {
        // GIVEN
        Repository repository = null;

        // WHEN
        try {
            S3Utils.getBucketName(repository);
            fail("Exception must occur");
        } catch (NullPointerException e) {
            // THEN
            assertThat(e.getMessage(), containsString("repository must not be null"));
            throw e;
        }
    }

    @Test
    @Parameters(method = "buckets")
    public void getBucketName(Repository repository, String expected) {
        // GIVEN

        // WHEN
        String actual = S3Utils.getBucketName(repository);

        // THEN
        assertThat(actual, equalTo(expected));
    }

    @Test(expected = NullPointerException.class)
    public void getBaseDirectoryIfRepositoryIsNull() {
        // GIVEN
        Repository repository = null;

        // WHEN
        try {
            S3Utils.getBaseDirectory(repository);
            fail("Exception must occur");
        } catch (NullPointerException e) {
            // THEN
            assertThat(e.getMessage(), containsString("repository must not be null"));
            throw e;
        }
    }

    @Test
    @Parameters(method = "baseDirectories")
    public void getBaseDirectory(Repository repository, String expected) {
        // GIVEN

        // WHEN
        String actual = S3Utils.getBaseDirectory(repository);

        // THEN
        assertThat(actual, equalTo(expected));
    }

    @Test
    @Parameters(method = "proxyProviders")
    public void getClientConfigurationIfProxyInfoProviderIsNull(ProxyInfoProvider provider, String expectedHost, int expectedPort) {
        // GIVEN

        // WHEN
        ClientConfiguration actual = S3Utils.getClientConfiguration(provider);

        // THEN
        assertThat(actual.getProxyHost(), equalTo(expectedHost));
        assertThat(actual.getProxyPort(), equalTo(expectedPort));
    }

    public Object[][] buckets() {
        return new Object[][]{
                {new Repository("id", ""), "localhost"},
                {new Repository("id", "s3://bucket"), "bucket"},
                {new Repository("id", "s3://bucket/folder"), "bucket"},
        };
    }

    public Object[][] baseDirectories() {
        return new Object[][]{
                {new Repository("id", ""), ""},
                {new Repository("id", "s3://bucket"), ""},
                {new Repository("id", "s3://bucket/"), ""},
                {new Repository("id", "s3://bucket/folder"), "folder/"},
                {new Repository("id", "s3://bucket/folder/subfolder"), "folder/subfolder/"},
                {new Repository("id", "s3://bucket/folder/subfolder/"), "folder/subfolder/"},
        };
    }

    public Object[][] proxyProviders() {
        return new Object[][]{
                {null, null, -1},
                {(ProxyInfoProvider) protocol -> null, null, -1},
                {(ProxyInfoProvider) protocol -> {
                    ProxyInfo proxyInfo = new ProxyInfo();
                    proxyInfo.setHost("host");
                    proxyInfo.setPort(8888);
                    return proxyInfo;
                }, "host", 8888},
        };
    }
}