import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class Product {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        // Đăng nhập trước khi test
        driver.get("https://www.saucedemo.com/v1/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", driver.getCurrentUrl());
    }

    @Test
    public void testProductCount() {
        List<WebElement> products = driver.findElements(By.className("inventory_item"));
        System.out.println("Số lượng sản phẩm: " + products.size());
        Assert.assertEquals(6, products.size()); // Số lượng mong đợi là 6 (tùy số thực tế)
    }

    @Test
    public void testAddToCartFirstProduct() {
        WebElement firstAddBtn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        Assert.assertEquals("ADD TO CART", firstAddBtn.getText());
        firstAddBtn.click();
        WebElement removeBtn = driver.findElement(By.xpath("(//button[contains(text(),'REMOVE')])[1]"));
        Assert.assertEquals("REMOVE", removeBtn.getText());
    }

    @Test
    public void testRemoveFromCartFirstProduct() {
        // Thêm trước
        WebElement firstAddBtn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        firstAddBtn.click();
        // Xóa
        WebElement removeBtn = driver.findElement(By.xpath("(//button[contains(text(),'REMOVE')])[1]"));
        removeBtn.click();
        // Kiểm tra nút trở lại "ADD TO CART"
        WebElement addBtn = driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]"));
        Assert.assertEquals("ADD TO CART", addBtn.getText());
    }

    @Test
    public void testSortProductsByPriceLowToHigh() {
        Select sortDropdown = new Select(driver.findElement(By.className("product_sort_container")));
        sortDropdown.selectByVisibleText("Price (low to high)");
        // Lấy giá sản phẩm sau khi sort
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        double prev = 0.0;
        for (WebElement priceEl : priceElements) {
            String priceText = priceEl.getText().replace("$", "");
            double price = Double.parseDouble(priceText);
            Assert.assertTrue("Sắp xếp chưa đúng thứ tự tăng dần!", price >= prev);
            prev = price;
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
