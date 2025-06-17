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
        chromeOptions.addArguments("--headless=new"); // headless 새 방식
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled"); // 자동화 탐지 우회
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments(
                "user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) " +
                        "Version/17.0 Mobile/15E148 Safari/604.1"
        );
        chromeOptions.addArguments("window-size=430,932");

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