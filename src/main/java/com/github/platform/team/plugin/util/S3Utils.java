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
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.appendIfMissing;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringAfter;

public abstract class S3Utils {
    private static final String KEY_FORMAT = "%s%s";

    public static String getBucketName(Repository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        return repository.getHost();
    }

    public static String getBaseDirectory(Repository repository) {
        Objects.requireNonNull(repository, "repository must not be null");
        String basedir = substringAfter(repository.getBasedir(), "/");

        return isNotBlank(basedir) ? appendIfMissing(basedir, "/", "/") : basedir;
    }

    public static ClientConfiguration getClientConfiguration(ProxyInfoProvider proxyInfoProvider) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        if (proxyInfoProvider != null) {
            ProxyInfo proxyInfo = proxyInfoProvider.getProxyInfo("s3");
            if (proxyInfo != null) {
                clientConfiguration.withProxyHost(proxyInfo.getHost()).withProxyPort(proxyInfo.getPort());
            }
        }

        return clientConfiguration;
    }
}
