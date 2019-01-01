package com.github.platform.team.plugin.aws;

import com.amazonaws.auth.AWSCredentials;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class AWSMavenCredentialsProviderTest {

    @Test
    public void getCredentialsIfNull() {
        // GIVEN
        AWSMavenCredentialsProvider provider = new AWSMavenCredentialsProvider(null);

        // WHEN
        AWSCredentials actual = provider.getCredentials();

        // THEN
        assertThat(actual, nullValue());
    }

    @Test
    public void getCredentials() {
        // GIVEN
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setUserName("username");
        authenticationInfo.setPassword("password");
        AWSMavenCredentialsProvider provider = new AWSMavenCredentialsProvider(authenticationInfo);

        // WHEN
        AWSCredentials actual = provider.getCredentials();

        // THEN
        assertThat(actual.getAWSAccessKeyId(), equalTo(authenticationInfo.getUserName()));
        assertThat(actual.getAWSSecretKey(), equalTo(authenticationInfo.getPassword()));
    }
}