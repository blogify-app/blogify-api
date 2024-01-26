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

### About the App
Uses the [Java](https://phoenixnap.com/kb/install-java-windows) [Spring Boot](https://spring.io/projects/spring-boot/) framework and [Postgresql](https://www.postgresql.org/) with [POJA](https://github.com/hei-school/poja-cli).

#### How to use the app ?
The principle of __POJA__ is that it deploys directly the application So you can check in the Github Actions and search for the deployed app link.

#### Requirements:
   - JDK 17 installation
   - JAVA_HOME configuration
   - Database creation on PostgreSQL {database}
   - List of environment variables

#### Linter and formatter:
The linter used is Checkstyle, configured with Gradle.

#### Usage:
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
Set Environment variables on CLI and:
```shell
    ./gradlew bootRun
```

### Upcoming Features
- Recommandation system AI
- Blogify posts list
- A more structured and well designed Blogify
   
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
