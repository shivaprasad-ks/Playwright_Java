package org.playwrightjava.com;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Main {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://google.com");
        System.out.println(page.title());
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
        browser.close();
        playwright.close();
    }
}