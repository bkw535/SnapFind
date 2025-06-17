package com.iosProject.backend_api.search.service;

import com.iosProject.backend_api.config.WebDriverUtil;
import com.iosProject.backend_api.search.domain.ProductInfo;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    public List<ProductInfo> searchProducts(String keyword) {
        WebDriver driver = WebDriverUtil.getChromeDriver();
        List<ProductInfo> result = new ArrayList<>();

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String decodedKeyword = URLDecoder.decode(encodedKeyword, StandardCharsets.UTF_8);
            String url = "https://search.danawa.com/dsearch.php?query=" + decodedKeyword + "&tab=main";
            System.out.println("🔍 검색 URL: " + url);

            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            List<WebElement> itemElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("div.main_prodlist_list ul.product_list li.prod_item:not(.prod_ad_item)")
            ));

            String rawKeyword = keyword;

            result = itemElements.stream()
                    .map(item -> {
                        try {
                            String name = item.findElement(By.cssSelector("p.prod_name > a")).getText();
                            String price = item.findElement(By.cssSelector("p.price_sect > a")).getText();
                            String shopUrl = item.findElement(By.cssSelector("p.prod_name > a")).getAttribute("href");
                            String shop = "다나와";

                            double sim = StringSimilarity.similarity(rawKeyword, name);

                            return new AbstractMap.SimpleEntry<>(sim, ProductInfo.builder()
                                    .name(name)
                                    .price(price)
                                    .shopUrl(shopUrl)
                                    .shop(shop)
                                    .build());

                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted((a, b) -> Double.compare(b.getKey(), a.getKey())) // 유사도 높은 순
                    .limit(2)
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("❌ 크롤링 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            WebDriverUtil.quit(driver);
        }

        return result;
    }

    public static class StringSimilarity {
        public static int levenshteinDistance(String a, String b) {
            int[][] dp = new int[a.length() + 1][b.length() + 1];

            for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
            for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

            for (int i = 1; i <= a.length(); i++) {
                for (int j = 1; j <= b.length(); j++) {
                    int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(
                            dp[i - 1][j] + 1,
                            Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
                    );
                }
            }
            return dp[a.length()][b.length()];
        }

        public static double similarity(String a, String b) {
            int distance = levenshteinDistance(a.toLowerCase(), b.toLowerCase());
            return 1.0 - (double) distance / Math.max(a.length(), b.length());
        }
    }
}