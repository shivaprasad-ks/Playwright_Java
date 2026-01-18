package org.playwrightjava.com.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = {"org.playwrightjava.com"},
        plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber.json"},
        monochrome = true
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
    // uses AbstractTestNGCucumberTests implementation to run scenarios with TestNG
}
