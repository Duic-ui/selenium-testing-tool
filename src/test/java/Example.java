import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Example {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/v1/");
        //driver.manage().window().maximize(); // Tối ưu hóa cửa sổ trình duyệt
    }

    @Test
    public void testTitle() {
        String title = driver.getTitle();

        if(title.equals("Swag Labs")) {
            System.out.println("Title is correct: " + title);
        } else {
            System.out.println("Title is incorrect: " + title);
        }

        //Assert.assertTrue(title.equals("Swag Labs"));
        Assert.assertEquals("Swag Labs", title);
    }

    @Test
    public void validateLogin() {
        try {
            Point point = driver.manage().window().getPosition();
            System.out.println("Window position: " + point.x);
            System.out.println("Window position: " + point.y);

            Point point1 = new Point(40 , 20);
            driver.manage().window().setPosition(point1);


            driver.findElement(By.id("user-name")).clear();
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            String fontColor = driver.findElement(By.id("user-name")).getCssValue("color");
            System.out.println("Font color of username field: " + fontColor);

            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys("secret_sauce");

            // Kiểm tra text password
            String passwordText = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/h4")).getText();
            Assert.assertEquals("Password for all users:", passwordText);
            System.out.println("Password text is correct: " + passwordText);

            // Kiểm tra xem nút đăng nhập có hiển thị chữ LOGIN
            String btnText = driver.findElement(By.id("login-button")).getAttribute("value");
            Assert.assertEquals("LOGIN", btnText);
            System.out.println("Login button text is correct: " + btnText);

            // Kiểm tra xem nút đăng nhập có hiển thị và có thể click được
            if(driver.findElement(By.id("login-button")).isDisplayed()) {
                driver.findElement(By.id("login-button")).click();
            } else {
                System.out.println("Login button is not enabled.");
            }


            // Kiểm tra URL sau khi đăng nhập
            String url = driver.getCurrentUrl();
            Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", url);
            System.out.println("Login thành công!");

        } catch (AssertionError e) {
            System.out.println("Login thất bại! URL hiện tại: " + driver.getCurrentUrl());
            throw e; // Để JUnit vẫn báo fail
        }
    }

//    @After
//    public void close() {
//        if (driver != null) {
//            driver.quit(); // Ensure the browser is closed after the test
//        }
//    }

//    public static void main(String[] args) {
//    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//
//        driver.get("https://www.saucedemo.com/v1/");
//        String title = driver.getTitle();
//        if(title.equals("Swag Labs")) {
//            System.out.println("Title is correct: " + title);
//        } else {
//            System.out.println("Title is incorrect: " + title);
//        }
//
//        //Assert.assertTrue(title.equals("Swag Labs"));
//        Assert.assertEquals("Swag Labs", title);
//    }
}
