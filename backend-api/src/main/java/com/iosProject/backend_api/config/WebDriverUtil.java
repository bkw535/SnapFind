package com.iosProject.backend_api.config;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class WebDriverUtil {

    private static String webDriverPath;

    @Value("${driver.chrome.driver_path}") // application.properties 또는 application.yml에서 가져옴
    public void setWebDriverPath(String path) {
        webDriverPath = path;
    }

    public static WebDriver getChromeDriver() {
        if (ObjectUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
            System.setProperty("webdriver.chrome.driver", webDriverPath);
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new"); // 최신 Chrome용 headless
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.setCapability("ignoreProtectedModeSettings", true);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    public static void quit(WebDriver driver) {
        if (!ObjectUtils.isEmpty(driver)) {
            driver.quit();
        }
    }

    public static void close(WebDriver driver) {
        if (!ObjectUtils.isEmpty(driver)) {
            driver.close();
        }
    }
}