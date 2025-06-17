package com.iosProject.backend_api.search.service;

import com.iosProject.backend_api.search.domain.ProductInfo;
import com.iosProject.backend_api.config.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    public List<ProductInfo> searchProducts(String keyword) {
        List<ProductInfo> result = new ArrayList<>();
        WebDriver driver = WebDriverUtil.getChromeDriver();

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String url = "https://search.danawa.com/dsearch.php?query=" + encodedKeyword;
            System.out.println("🔍 검색 URL: " + url);

            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.main_prodlist > ul > li")));

            List<WebElement> itemElements = driver.findElements(
                    By.cssSelector("div.main_prodlist > ul > li:not(.prod_ad_item)")
            );

            int count = 0;
            for (WebElement item : itemElements) {
                if (count++ >= 5) break;

                try {
                    String name = item.findElement(By.cssSelector("p.prod_name > a")).getText();
                    String price = item.findElement(By.cssSelector("p.price_sect > a")).getText();
                    String shopUrl = item.findElement(By.cssSelector("p.prod_name > a")).getAttribute("href");
                    String shop = "다나와";

                    ProductInfo product = ProductInfo.builder()
                            .name(name)
                            .price(price)
                            .shopUrl(shopUrl)
                            .shop(shop)
                            .build();

                    result.add(product);
                } catch (Exception innerEx) {
                    System.out.println("❗ 상품 파싱 실패: " + innerEx.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("❌ 크롤링 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            WebDriverUtil.quit(driver);
        }

        return result;
    }
}