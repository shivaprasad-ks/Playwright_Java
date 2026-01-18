package org.playwrightjava.com.hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.playwrightjava.com.utils.PlaywrightManager;

public class PlaywrightHooks {

    @Before
    public void beforeScenario() {
        // Initialize Playwright, browser, context and page via the centralized manager.
        PlaywrightManager.init();
    }

    public static Object getPlaywright() {
        return PlaywrightManager.getPlaywright();
    }

    public static Browser getBrowser() {
        return PlaywrightManager.getBrowser();
    }

    public static BrowserContext getContext() {
        return PlaywrightManager.getContext();
    }

    public static Page getPage() {
        return PlaywrightManager.getPage();
    }

    public static void navigate(String url) {
        PlaywrightManager.navigate(url);
    }

    @After
    public void afterScenario() {
        // stop tracing (saved by manager) and cleanup resources
        PlaywrightManager.stopAndCleanup();
    }
}
