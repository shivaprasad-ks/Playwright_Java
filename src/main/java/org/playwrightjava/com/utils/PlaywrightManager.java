package org.playwrightjava.com.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightManager {
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    /**
     * Initialize Playwright with defaults from system properties:
     * -Dbrowser=chromium|firefox|webkit (default: chromium)
     * -Dheadless=true|false (default: false)
     * -Dtrace=true|false (default: true)
     */
    public static void init() {
        String browser = System.getProperty("browser", "chromium");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean trace = Boolean.parseBoolean(System.getProperty("trace", "true"));
        init(browser, headless, trace);
    }

    public static void init(String browserName, boolean headless, boolean startTracing) {
        Playwright pw = Playwright.create();
        PLAYWRIGHT.set(pw);

        BrowserType browserType = getBrowserType(pw, browserName);
        Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
        BROWSER.set(browser);

        BrowserContext context = browser.newContext();
        if (startTracing) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }
        CONTEXT.set(context);

        Page page = context.newPage();
        PAGE.set(page);
    }

    private static BrowserType getBrowserType(Playwright pw, String browserName) {
        if (browserName == null) return pw.chromium();
        switch (browserName.toLowerCase()) {
            case "firefox":
                return pw.firefox();
            case "webkit":
                return pw.webkit();
            case "chromium":
            default:
                return pw.chromium();
        }
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

    public static Page getPage() {
        return PAGE.get();
    }

    public static void navigate(String url) {
        Page p = PAGE.get();
        if (p != null) p.navigate(url);
    }

    /**
     * Stop tracing (if enabled for the context) and save to target/traces.
     * Returns the Path to the saved trace or null if none was saved.
     */
    public static Path stopAndSaveTrace() {
        BrowserContext ctx = CONTEXT.get();
        if (ctx == null) return null;
        try {
            Path tracesDir = Paths.get("target", "traces");
            Files.createDirectories(tracesDir);
            Path traceFile = tracesDir.resolve("trace-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis() + ".zip");
            try {
                ctx.tracing().stop(new Tracing.StopOptions().setPath(traceFile));
                return traceFile;
            } catch (Exception ignored) {
                // ignore tracing stop errors
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * Close page, context, browser and playwright and clear thread locals.
     */
    public static void closeAll() {
        Page p = PAGE.get();
        if (p != null) {
            try { p.close(); } catch (Exception ignored) {}
        }
        BrowserContext ctx = CONTEXT.get();
        if (ctx != null) {
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

    /**
     * Convenience: stop trace (if any) then close everything.
     */
    public static void stopAndCleanup() {
        stopAndSaveTrace();
        closeAll();
    }
}
