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
            String url = "https://search.11st.co.kr/Search.tmall?kwd=" + encodedKeyword;
            System.out.println("🔍 11번가 검색 URL: " + url);

            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#footer > div.b_wing > div > div.c_wing_product_ad")));
            List<WebElement> adElements = driver.findElements(By.cssSelector("#footer > div.b_wing > div > div.c_wing_product_ad"));
            int count = 0;
            for (WebElement adElement : adElements) {
                if (count++ >= 5) break;
                try {
                    WebElement linkElement = adElement.findElement(By.cssSelector("a"));
                    WebElement infoElement = adElement.findElement(By.cssSelector("a > div.info"));

                    String name = infoElement.findElement(By.cssSelector(".name")).getText();
                    String price = infoElement.findElement(By.cssSelector(".price")).getText();
                    String shopUrl = linkElement.getAttribute("href");
                    String shop = "11번가";

                    ProductInfo product = ProductInfo.builder()
                            .name(name)
                            .price(price)
                            .shopUrl(shopUrl)
                            .shop(shop)
                            .build();

                    result.add(product);
                    System.out.println("✅ 상품 파싱 성공: " + name);
                } catch (Exception innerEx) {
                    System.out.println("❗ 상품 파싱 실패: " + innerEx.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("❌ 11번가 크롤링 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            WebDriverUtil.quit(driver);
        }

        return result;
    }
}