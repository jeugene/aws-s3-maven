package com.github.platform.team.plugin.aws;

import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AWSMavenCredentialsTest {

    @Test
    public void getAWSAccessKeyId() {
        // GIVEN
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setUserName("username");
        AWSMavenCredentials awsMavenCredentials = new AWSMavenCredentials(authenticationInfo);

        // WHEN
        String actual = awsMavenCredentials.getAWSAccessKeyId();

        // THEN
        assertThat(actual, equalTo(authenticationInfo.getUserName()));
    }

    @Test
    public void getAWSSecretKey() {
        // GIVEN
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setPassword("password");
        AWSMavenCredentials awsMavenCredentials = new AWSMavenCredentials(authenticationInfo);

        // WHEN
        String actual = awsMavenCredentials.getAWSSecretKey();

        // THEN
        assertThat(actual, equalTo(authenticationInfo.getPassword()));
    }
}