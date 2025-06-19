import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Cart {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/v1/");
        login("standard_user", "secret_sauce");
        //driver.manage().window().maximize(); // Tối ưu hóa cửa sổ trình duyệt
    }

    public void login(String username, String password) {
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();

        // Đợi tới khi URL chuyển sang trang inventory (login thành công)
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
    }

    @Test
    public void testAddBackpackToCart() {
        // B1: Tìm nút ADD TO CART của sản phẩm Sauce Labs Backpack
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Backpack']/ancestor::div[@class='inventory_item']//button")
        );

        // B2: Click để thêm sản phẩm vào giỏ
        addBtn.click();

        // B3: Kiểm tra sau khi thêm, nút chuyển thành "REMOVE"
        WebElement removeBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Backpack']/ancestor::div[@class='inventory_item']//button")
        );
        Assert.assertEquals("REMOVE", removeBtn.getText());

        // B4: Click vào biểu tượng giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();

        // B5: Kiểm tra đã chuyển đúng URL trang giỏ hàng
        Assert.assertEquals("https://www.saucedemo.com/v1/cart.html", driver.getCurrentUrl());

        // B6: Kiểm tra sản phẩm "Sauce Labs Backpack" đã hiển thị trong giỏ
        WebElement product = driver.findElement(By.className("inventory_item_name"));
        Assert.assertEquals("Sauce Labs Backpack", product.getText());
    }

    @Test
    public void testRemoveBackpackFromCart() {
        // B1: Thêm sản phẩm trước (giống như trong testAddBackpackToCart)
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Backpack']/ancestor::div[@class='inventory_item']//button")
        );
        addBtn.click();

        // B2: Mở trang giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();

        // B3: Tìm và kiểm tra nút REMOVE hiển thị
        WebElement removeBtn = driver.findElement(By.xpath("//button[text()='REMOVE']"));
        Assert.assertTrue(removeBtn.isDisplayed());

        // B4: Click nút REMOVE để xoá sản phẩm khỏi giỏ
        removeBtn.click();

        // B5: Kiểm tra sản phẩm không còn trong danh sách giỏ hàng
        boolean isProductPresent = driver.findElements(By.className("inventory_item_name"))
                .stream()
                .anyMatch(el -> el.getText().equals("Sauce Labs Backpack"));

        Assert.assertFalse("Sản phẩm vẫn còn trong giỏ hàng!", isProductPresent);
    }
}
