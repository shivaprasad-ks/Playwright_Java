package org.playwrightjava.com.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.playwrightjava.com.hooks.PlaywrightHooks;
import com.microsoft.playwright.Page;
import org.testng.Assert;

public class ExampleSteps {

    @Given("I open the example page")
    public void i_open_the_example_page() {
        Page page = PlaywrightHooks.getpage();
        if (page != null) {
            // ensure a clean starting point
            page.navigate("about:blank");
        }
    }

    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        PlaywrightHooks.navigate(url);
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String text) {
        Page page = PlaywrightHooks.getpage();
        String title = page.title();
        Assert.assertTrue(title.contains(text), "Expected page title to contain: " + text + " but was: " + title);
    }
}

