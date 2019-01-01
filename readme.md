# AWS Maven Wagon
[![GitHub version](https://badge.fury.io/gh/platform-team%2Faws-maven.svg)](http://badge.fury.io/gh/platform-team%2Faws-maven)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[![Dependency Status](https://www.versioneye.com/user/projects/5a8ab8e30fb24f3a2ef5b4be/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5a8ab8e30fb24f3a2ef5b4be)
[![Build Status](https://travis-ci.org/platform-team/aws-maven.svg?branch=master)](https://travis-ci.org/platform-team/aws-maven)
[![Coverage Status](https://coveralls.io/repos/github/platform-team/aws-maven/badge.svg?branch=master)](https://coveralls.io/github/platform-team/aws-maven?branch=master)


## Description
This project is a fork of a [Maven Wagon](https://github.com/spring-projects/aws-maven) for [Amazon S3](http://aws.amazon.com/s3/).  In order to to publish artifacts to an S3 bucket, the user (as identified by their access key) must be listed as an owner on the bucket.


## Why this fork?
- original repo not maintained for a long time but we updated fork to the latest libs.
- we fixed some of issues that blocks others and us.
- no support from maintainers of original repo. 


## Usage
To publish Maven artifacts to S3 a build extension must be defined in a project's `pom.xml`.  The latest version of the wagon can be found on the [`aws-maven`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.platform-team%22%20AND%20a%3A%22aws-maven%22) page in Maven Central.

```xml
<project>
  ...
  <build>
    ...
    <extensions>
      ...
      <extension>
        <groupId>com.github.platform-team</groupId>
        <artifactId>aws-maven</artifactId>
        <version>6.0.0</version>
      </extension>
      ...
    </extensions>
    ...
  </build>
  ...
</project>
```

Once the build extension is configured distribution management repositories can be defined in the `pom.xml` with an `s3://` scheme.

```xml
<project>
  ...
  <distributionManagement>
    <repository>
      <id>aws-release</id>
      <name>AWS Release Repository</name>
      <url>s3://<BUCKET>/release</url>
    </repository>
    <snapshotRepository>
      <id>aws-snapshot</id>
      <name>AWS Snapshot Repository</name>
      <url>s3://<BUCKET>/snapshot</url>
    </snapshotRepository>
  </distributionManagement>
  ...
</project>
```

Finally the `~/.m2/settings.xml` must be updated to include access and secret keys for the account. The access key should be used to populate the `username` element, and the secret access key should be used to populate the `password` element.

```xml
<settings>
  ...
  <servers>
    ...
    <server>
      <id>aws-release</id>
      <username>0123456789ABCDEFGHIJ</username>
      <password>0123456789abcdefghijklmnopqrstuvwxyzABCD</password>
      <configuration>
        <wagonProvider>s3</wagonProvider>
      </configuration>
    </server>
    <server>
      <id>aws-snapshot</id>
      <username>0123456789ABCDEFGHIJ</username>
      <password>0123456789abcdefghijklmnopqrstuvwxyzABCD</password>
      <configuration>
        <wagonProvider>s3</wagonProvider>
      </configuration>
    </server>
    ...
  </servers>
  ...
</settings>
```

### Connecting through a Proxy
For being able to connect behind an HTTP proxy you need to add the following configuration to `~/.m2/settings.xml`:

```xml
<settings>
  ...
  <proxies>
     ...
     <proxy>
         <active>true</active>
         <protocol>s3</protocol>
         <host>myproxy.host.com</host>
         <port>8080</port>
         <username>proxyuser</username>
         <password>somepassword</password>
         <nonProxyHosts>www.google.com|*.somewhere.com</nonProxyHosts>
     </proxy>
     ...
    </proxies>
  ...
</settings>
```

Alternatively, the access and secret keys for the account can be provided using (applied in order below)

* `aws.accessKeyId` and `aws.secretKey` [system properties](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/SystemPropertiesCredentialsProvider.html)
* `AWS_ACCESS_KEY_ID` (or `AWS_ACCESS_KEY`) and `AWS_SECRET_KEY` (or `AWS_SECRET_ACCESS_KEY`) [environment variables](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/EnvironmentVariableCredentialsProvider.html)
* `aws_access_key_id` and `aws_secret_access_key` of [aws cli](https://docs.aws.amazon.com/cli/latest/userguide/cli-config-files.html)
* The Amazon EC2 [Instance Metadata Service](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/EC2ContainerCredentialsProviderWrapper.html)

## Making Artifacts Public
This wagon doesn't set an explict ACL for each artifact that is uploaded. Instead you should create an AWS Bucket Policy to set permissions on objects. A bucket policy can be set in the [AWS Console](https://console.aws.amazon.com/s3) and can be generated using the [AWS Policy Generator](http://awspolicygen.s3.amazonaws.com/policygen.html).

In order to make the contents of a bucket public you need to add statements with the following details to your policy:

| Effect  | Principal | Action       | Amazon Resource Name (ARN)
| ------- | --------- | ------------ | --------------------------
| `Allow` | `*`       | `ListBucket` | `arn:aws:s3:::<BUCKET>`
| `Allow` | `*`       | `GetObject`  | `arn:aws:s3:::<BUCKET>/*`

If your policy is setup properly it should look something like:

```json
{
  "Id": "Policy1397027253868",
  "Statement": [
    {
      "Sid": "Stmt1397027243665",
      "Action": [
        "s3:ListBucket"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::<BUCKET>",
      "Principal": {
        "AWS": [
          "*"
        ]
      }
    },
    {
      "Sid": "Stmt1397027177153",
      "Action": [
        "s3:GetObject"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::<BUCKET>/*",
      "Principal": {
        "AWS": [
          "*"
        ]
      }
    }
  ]
}
```

If you prefer to use the [command line](http://aws.amazon.com/documentation/cli/), you can use the following script to make the contents of a bucket public:

```bash
BUCKET=<BUCKET>
TIMESTAMP=$(date +%Y%m%d%H%M)
POLICY=$(cat<<EOF
{
  "Id": "public-read-policy-$TIMESTAMP",
  "Statement": [
    {
      "Sid": "list-bucket-$TIMESTAMP",
      "Action": [
        "s3:ListBucket"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::$BUCKET",
      "Principal": {
        "AWS": [
          "*"
        ]
      }
    },
    {
      "Sid": "get-object-$TIMESTAMP",
      "Action": [
        "s3:GetObject"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::$BUCKET/*",
      "Principal": {
        "AWS": [
          "*"
        ]
      }
    }
  ]
}
EOF
)

aws s3api put-bucket-policy --bucket $BUCKET --policy "$POLICY"
```

## Release Notes
* `6.0.0`
    - Updated to the latest versions of aws-sdk and maven-wagon.
    - Changed order of aws credential resolution strategy.
    - Added support of all regions defined in aws-sdk.

## License

Copyright 2018-Present Platform Team.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.