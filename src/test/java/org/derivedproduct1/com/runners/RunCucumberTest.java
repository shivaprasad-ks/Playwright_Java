package org.derivedproduct1.com.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"org.derivedproduct1.com.steps", "org.derivedproduct1.com.hooks"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
    // uses AbstractTestNGCucumberTests implementation to run scenarios with TestNG
}

