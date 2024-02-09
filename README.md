# BLOGIFY API 📑✨

Welcome to Blogify, where words come to life and stories find their digital home! __Blogify__ is more than just a blog app; it's a vibrant community of passionate writers, storytellers, and readers coming together to share, explore, and engage in the art of blogging.

## Overview

This repository contains essential information and guidelines for understanding and contributing to our project. Please read through this README to familiarize yourself with our Definitions Of Done (DoD) and find links to our frontend and backend repositories.

## Definitions Of Done (DoD)

The Definitions Of Done (DoD) for our project are aligned with the professor's requirements:

1. **Authentication & Authorization System:**
   - a) Visitors can access the blog without authentication.
   - b) Users must be logged in to publish an article.

2. **Data Collection:**
   - Implement a robust system for collecting and managing data.

3. **Recommendation System:**
   - Develop a recommendation system to enhance the user experience.

### AI Features

The AI feature enhances user experience by providing personalized topic suggestions. Here's how it works:

- Only authenticated users have access to the AI feature.
- The AI suggests topics based on two criteria:
  1. **Personal Preferences:** This aspect is tailored to the individual user, considering their posts, visited posts, and the categories of topics they've chosen during their subscription.
  2. **Community Preferences:** This aspect considers the overall community's preferences, analyzing existing categories, as well as the engagement metrics like likes and dislikes on posts.

### About the App

Uses the [Java](https://phoenixnap.com/kb/install-java-windows) [Spring Boot](https://spring.io/projects/spring-boot/) framework and [Postgresql](https://www.postgresql.org/) with [POJA](https://github.com/hei-school/poja-cli).

#### How to use the app ?

The principle of __POJA__ is that it deploys the application directly. So you can check in the GitHub Actions and search for the deployed app link.

#### Requirements

##### JDK installation

- Depending on the OS, [download and install](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html) (Java) JDK 17

- Configure JAVA_HOME

##### PostgreSQL database creation

```shell
    # In postgreSQL CLI, run:
    CREATE DATABASE <DB_NAME>;
    # Then update the env variable "spring_datasource_url" value to : jdbc:postgresql://localhost:5432/{DB_NAME}
```

##### List of environment variables

- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY
- aws_eventBridge_bus
- aws_region
- aws_s3_bucket
- aws_ses_source
- aws_sqs_queue_url
- firebase_private_key
- spring_datasource_password
- spring_datasource_url
- spring_datasource_username

#### Linter and formatter

##### Coding standard

The coding standard used is Google java style guide

##### Linter

The linter used is checkstyle, configured with gradle.

##### Formatter

The formatter used is Google-java-format, executed with the script format.sh as follows:

```shell
    ./format.sh
```

#### Usage

- To clean previous builds:

```shell
    ./gradlew clean
```

- To build:

```shell
    ./gradlew assemble
```

- To run tests:

```shell
    ./gradlew test
```

- To run the application:

```shell
    # Set Environment variables on CLI and then run:
    ./gradlew bootRun
```

### Upcoming Features

- Recommendation system AI
- Blogify posts list
- A more structured and well-designed Blogify

## Known Issues

Help us to find bugs !
Put it in the issues :

- [https://github.com/blogify-app/blogify-api/issues](https://github.com/blogify-app/blogify-api/issues)

## How to Contribute

We welcome contributions from the community! If you're interested in contributing to our project, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and ensure they meet the Definitions Of Done.
4. Test your changes thoroughly.
5. Create a pull request, detailing the changes you've made.

## Getting Help

If you have any questions or need assistance, feel free to reach out to us through the project's GitHub issues.

__Thank you for being a part of The Maestro Project - Blogify!__ 🚀
