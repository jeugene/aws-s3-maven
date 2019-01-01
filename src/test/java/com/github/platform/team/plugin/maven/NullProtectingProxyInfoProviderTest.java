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

package com.github.platform.team.plugin.maven;

import org.apache.maven.wagon.proxy.ProxyInfo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class NullProtectingProxyInfoProviderTest {

    @Test
    public void getProxyInfoNullProtocol() {
        // GIVEN
        ProxyInfo proxyInfo = mock(ProxyInfo.class);
        NullProtectingProxyInfoProvider provider = new NullProtectingProxyInfoProvider(proxyInfo);
        String protocol = null;

        // WHEN
        ProxyInfo actual = provider.getProxyInfo(protocol);

        // THEN
        assertThat(actual, equalTo(proxyInfo));
    }

    @Test
    public void getProxyInfoNullProxy() {
        // GIVEN
        ProxyInfo proxyInfo = null;
        NullProtectingProxyInfoProvider provider = new NullProtectingProxyInfoProvider(proxyInfo);
        String protocol = "foo";

        // WHEN
        ProxyInfo actual = provider.getProxyInfo(protocol);

        // THEN
        assertThat(actual, nullValue());
    }

    @Test
    public void getProxyInfoMatchingProtocol() {
        // GIVEN
        ProxyInfo proxyInfo = mock(ProxyInfo.class);
        given(proxyInfo.getType()).willReturn("FOO");
        NullProtectingProxyInfoProvider provider = new NullProtectingProxyInfoProvider(proxyInfo);
        String protocol = "foo";

        // WHEN
        ProxyInfo actual = provider.getProxyInfo(protocol);

        // THEN
        assertThat(actual, equalTo(proxyInfo));
    }

    @Test
    public void getProxyInfoNonMatchingProtocol() {
        // GIVEN
        ProxyInfo proxyInfo = mock(ProxyInfo.class);
        given(proxyInfo.getType()).willReturn("FOO");
        NullProtectingProxyInfoProvider provider = new NullProtectingProxyInfoProvider(proxyInfo);
        String protocol = "bar";

        // WHEN
        ProxyInfo actual = provider.getProxyInfo(protocol);

        // THEN
        assertThat(actual, nullValue());
    }
}
