import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetails {
    WebDriver driver;

    @Before
    public void setUp() {
        driver = Helper.createDriver();
        Helper.login(driver, "standard_user", "secret_sauce");
    }

//    @Test
//    public void testProductImageClickNavigatesToDetails() {
//        Helper.clickProductImage(driver, 4);
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("id=4"));
//        System.out.println("✅ Click image --> Navigated to: " + currentUrl);
//    }

    @Test
    public void testProductTitleClickNavigatesToDetails() {
        Helper.clickProductTitle(driver, 0);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("id=0"));
        System.out.println("✅ Click title --> Navigated to: " + currentUrl);
    }

    @Test
    public void testBackButton(){
        Helper.clickProductTitle(driver, 0);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.className("inventory_details_back_button"))).click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", currentUrl);
        System.out.println("✅ Back button --> Navigated to: " + currentUrl);
    }

//    @After
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit(); // Đảm bảo đóng browser sau mỗi test
//        }
//    }
}