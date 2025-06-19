import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ProductDetails {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
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

//    @After
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit(); // Đảm bảo đóng browser sau mỗi test
//        }
//    }
}