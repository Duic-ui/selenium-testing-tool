import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.*;

public class Product {
    static List<String> testLogs = new ArrayList<>();
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/v1/");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", driver.getCurrentUrl());
    }

    @Test
    public void testProductCount() {
        logStart("testProductCount");
        List<WebElement> products = driver.findElements(By.className("inventory_item"));
        Assert.assertEquals("Số lượng sản phẩm không đúng!", 6, products.size());
        testLogs.add("✅ testProductCount: Số lượng sản phẩm đúng là 6.");
    }

    @Test
    public void testProductDetailsDisplayed() {
        logStart("testProductDetailsDisplayed");
        List<WebElement> products = driver.findElements(By.className("inventory_item"));
        for (WebElement product : products) {
            Assert.assertTrue(product.findElement(By.className("inventory_item_name")).isDisplayed());
            Assert.assertTrue(product.findElement(By.className("inventory_item_desc")).isDisplayed());
            Assert.assertTrue(product.findElement(By.className("inventory_item_price")).isDisplayed());
            Assert.assertTrue(product.findElement(By.tagName("img")).isDisplayed());
        }
        testLogs.add("✅ testProductDetailsDisplayed: Tên, mô tả, giá và ảnh sản phẩm hiển thị đầy đủ.");
    }

    @Test
    public void testAddToCartButtonChangesToRemove() {
        logStart("testAddToCartButtonChangesToRemove");
        WebElement btn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        btn.click();
        WebElement removeBtn = driver.findElement(By.xpath("(//button[contains(text(),'REMOVE')])[1]"));
        Assert.assertEquals("REMOVE", removeBtn.getText());
        testLogs.add("✅ testAddToCartButtonChangesToRemove: Nút chuyển sang REMOVE sau khi ADD.");
    }

    @Test
    public void testRemoveButtonChangesBackToAddToCart() {
        logStart("testRemoveButtonChangesBackToAddToCart");
        WebElement btn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        btn.click();
        WebElement removeBtn = driver.findElement(By.xpath("(//button[contains(text(),'REMOVE')])[1]"));
        removeBtn.click();
        WebElement addBtn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        Assert.assertEquals("ADD TO CART", addBtn.getText());
        testLogs.add("✅ testRemoveButtonChangesBackToAddToCart: Nút quay lại ADD TO CART sau khi REMOVE.");
    }

    @Test
    public void testSortByNameAZ() {
        logStart("testSortByNameAZ");
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText("Name (A to Z)");
        List<WebElement> names = driver.findElements(By.className("inventory_item_name"));
        List<String> actual = names.stream().map(WebElement::getText).toList();
        List<String> sorted = new ArrayList<>(actual);
        Collections.sort(sorted);
        Assert.assertEquals("Tên chưa được sắp xếp A → Z", sorted, actual);
        testLogs.add("✅ testSortByNameAZ: Sắp xếp tên A-Z thành công.");
    }

    @Test
    public void testSortByNameZA() {
        logStart("testSortByNameZA");
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText("Name (Z to A)");
        List<WebElement> names = driver.findElements(By.className("inventory_item_name"));
        List<String> actual = names.stream().map(WebElement::getText).toList();
        List<String> sorted = new ArrayList<>(actual);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals("Tên chưa được sắp xếp Z → A", sorted, actual);
        testLogs.add("✅ testSortByNameZA: Sắp xếp tên Z-A thành công.");
    }

    @Test
    public void testSortByPriceLowToHigh() {
        logStart("testSortByPriceLowToHigh");
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText("Price (low to high)");
        List<Double> prices = driver.findElements(By.className("inventory_item_price"))
                .stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", ""))).toList();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals("Giá chưa được sắp xếp tăng", sorted, prices);
        testLogs.add("✅ testSortByPriceLowToHigh: Sắp xếp giá tăng thành công.");
    }

    @Test
    public void testSortByPriceHighToLow() {
        logStart("testSortByPriceHighToLow");
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText("Price (high to low)");
        List<Double> prices = driver.findElements(By.className("inventory_item_price"))
                .stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", ""))).toList();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals("Giá chưa được sắp xếp giảm", sorted, prices);
        testLogs.add("✅ testSortByPriceHighToLow: Sắp xếp giá giảm thành công.");
    }

    public void logStart(String testName) {
        System.out.println("\n==========▶️ ĐANG CHẠY TESTCASE: " + testName + " ==========");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public static void printSummary() {
        System.out.println("\n===== 📝 TỔNG KẾT CÁC TESTCASE PRODUCT =====");
        for (String log : testLogs) {
            System.out.println(log);
        }
        System.out.println("============================================\n");
    }
}