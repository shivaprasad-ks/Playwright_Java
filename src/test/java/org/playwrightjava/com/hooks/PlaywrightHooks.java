package org.playwrightjava.com.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class PlaywrightHooks {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    @Before
    public void beforeScenario() {
        Playwright pw = Playwright.create();
        PLAYWRIGHT.set(pw);
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BROWSER.set(browser);
        BrowserContext context = browser.newContext();
        // start tracing for this context so we can open the trace viewer after the scenario
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        CONTEXT.set(context);
        Page page = context.newPage();
        PAGE.set(page);
    }

    public static Playwright getPlaywright() {
        return PLAYWRIGHT.get();
    }

    public static Browser getBrowser() {
        return BROWSER.get();
    }

    public static BrowserContext getContext() {
        return CONTEXT.get();
    }

    public static Page getpage() {
        return PAGE.get();
    }

    public static void navigate(String url) {
        Page p = PAGE.get();
        if (p != null) {
            p.navigate(url);
        }
    }


    @After
    public void afterScenario() {
        Page p = PAGE.get();
        if (p != null) {
            try { p.close(); } catch (Exception ignored) {}
        }
        BrowserContext ctx = CONTEXT.get();
        if (ctx != null) {
            // stop tracing and save a trace file for this scenario/thread
            try {
                Path tracesDir = Paths.get("target", "traces");
                Files.createDirectories(tracesDir);
                Path traceFile = tracesDir.resolve("trace-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis() + ".zip");
                ctx.tracing().stop(new Tracing.StopOptions().setPath(traceFile));
            } catch (Exception ignored) {
                // ignore trace saving errors to avoid hiding test results
            }
            try { ctx.close(); } catch (Exception ignored) {}
        }
        Browser b = BROWSER.get();
        if (b != null) {
            try { b.close(); } catch (Exception ignored) {}
        }
        Playwright pw = PLAYWRIGHT.get();
        if (pw != null) {
            try { pw.close(); } catch (Exception ignored) {}
        }

        PAGE.remove();
        CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }
}
