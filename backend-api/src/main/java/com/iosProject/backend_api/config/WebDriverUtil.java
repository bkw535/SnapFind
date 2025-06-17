package com.iosProject.backend_api.config;

import jakarta.annotation.PostConstruct;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebDriverUtil {

    @Value("${driver.chrome.driver_path}")
    private String chromeDriverPath;

    private static String staticChromeDriverPath;

    @PostConstruct
    public void init() {
        staticChromeDriverPath = chromeDriverPath;
    }

    public static WebDriver getChromeDriver() {
        if (System.getProperty("webdriver.chrome.driver") == null) {
            System.setProperty("webdriver.chrome.driver", staticChromeDriverPath);
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    public static void quit(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void close(WebDriver driver) {
        if (driver != null) {
            driver.close();
        }
    }
}