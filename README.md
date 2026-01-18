# Playwright_Java

Maven Java project that uses Playwright Java (com.microsoft.playwright:playwright:1.57.0) for browser automation.

Summary
- Language: Java (target/source 22)
- Build: Maven
- Main automation library: Playwright Java 1.57.0

Prerequisites
- JDK 22 (set JAVA_HOME)
- Maven 3.6+
- Internet access for downloading dependencies and Playwright browser binaries

Quick build
1. From the project root:
   mvn -B clean package

Run the sample Main class
Option A — using the Maven Exec plugin (no changes to pom required):
   mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="org.playwrightjava.com.Main"

Option B — manual classpath run:
1. Copy dependencies to target/dependency:
   mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
2. Run the main class:
   java -cp target/classes:target/dependency/* org.playwrightjava.com.Main

Playwright browsers
- On first run Playwright will usually download required browser binaries automatically.
- If browsers are missing, run the project (see Run sample) — the Playwright runtime will fetch browsers on demand.

Project structure (relevant parts)
- pom.xml                         Maven project file (Java 22, Playwright dependency)
- src/main/java/.../Main.java     Example entry point
- src/test/java/                  Put test classes here (add test framework deps as needed)
- target/                         Build output

Adding tests
- Add JUnit or TestNG to pom.xml and place test files under src/test/java
- Run tests with:
   mvn test

Notes and troubleshooting
- Ensure JAVA_HOME points to a JDK 22 installation.
- If you get classpath problems when running Main manually, use the Maven Exec command above.
- If Playwright fails to download browsers, check network/proxy settings and rerun the program to trigger the download.

Extending this project
- Add test frameworks (JUnit 5) and a test runner configuration (Surefire/Failsafe) for CI.
- Consider adding the exec-maven-plugin to pom.xml for convenient mvn exec:java runs.

License
- No license specified. Add a LICENSE file if you want to set one.

