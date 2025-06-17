package com.iosProject.backend_api.search.service;

import com.iosProject.backend_api.search.domain.ProductInfo;
import com.iosProject.backend_api.config.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
            String url = "https://m.11st.co.kr/search?q=" + encodedKeyword;
            System.out.println("🔍 검색 URL: " + url);

            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.c_card_box > a.c_card_item")));

            List<WebElement> itemElements = driver.findElements(By.cssSelector("div.c_card_box > a.c_card_item"));

            int count = 0;
            for (WebElement item : itemElements) {
                if (count++ >= 5) break;

                try {
                    String name = item.findElement(By.cssSelector("div.c_card_tit")).getText();
                    String price = item.findElement(By.cssSelector("div.c_card_price")).getText();
                    String shopUrl = item.getAttribute("href");

                    ProductInfo product = ProductInfo.builder()
                            .name(name)
                            .price(price)
                            .shopUrl(shopUrl)
                            .shop("11번가")
                            .build();

                    result.add(product);
                } catch (Exception ex) {
                    System.out.println("❗ 상품 파싱 실패: " + ex.getMessage());
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