import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Helper {


    public static WebDriver createDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        return new ChromeDriver(options);
    }


    public static void login(WebDriver driver, String username, String password) {
        driver.get("https://www.saucedemo.com/v1/");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        // Tự động click nút "OK" nếu popup hiện
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
//            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='OK']")));
//            okButton.click();
//        } catch (Exception e) {
//            // Không thấy popup thì thôi, không làm gì
//        }

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("inventory.html"));
    }

    public static void clickProductTitle(WebDriver driver, int productId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("item_" + productId + "_title_link"))).click();
    }

    public static void clickProductImage(WebDriver driver, int productId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("item_" + productId + "_img_link"))).click();
    }
}