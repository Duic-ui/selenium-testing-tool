import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;

public class LoginLogoutTestcase {
    static List<String> testLogs = new ArrayList<>();
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/v1/");
    }

    @Test
    public void testLoginSuccess() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", driver.getCurrentUrl());
        String log = "testLoginSuccess: Đăng nhập thành công với user hợp lệ.";
        testLogs.add(log);
        showPopup("testLoginSuccess", "Đăng nhập thành công với user hợp lệ.");
    }

    @Test
    public void testLoginLockedOutUser() {
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().contains("locked out"));
        String log = "testLoginLockedOutUser: Đăng nhập với user bị khóa, kiểm tra thông báo lỗi.";
        testLogs.add(log);
        showPopup("testLoginLockedOutUser", "Đăng nhập với user bị khóa, kiểm tra thông báo lỗi.");
    }

    @Test
    public void testLoginWrongPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().toLowerCase().contains("do not match"));
        String log = "testLoginWrongPassword: Đăng nhập với mật khẩu sai, kiểm tra thông báo lỗi.";
        testLogs.add(log);
        showPopup("testLoginWrongPassword", "Đăng nhập với mật khẩu sai, kiểm tra thông báo lỗi.");
    }

    @Test
    public void testLoginEmptyUsername() {
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().toLowerCase().contains("username is required"));
        String log = "testLoginEmptyUsername: Đăng nhập thiếu username, kiểm tra thông báo lỗi.";
        testLogs.add(log);
        showPopup("testLoginEmptyUsername", "Đăng nhập thiếu username, kiểm tra thông báo lỗi.");
    }

    @Test
    public void testLoginEmptyPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("login-button")).click();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().toLowerCase().contains("password is required"));
        String log = "testLoginEmptyPassword: Đăng nhập thiếu password, kiểm tra thông báo lỗi.";
        testLogs.add(log);
        showPopup("testLoginEmptyPassword", "Đăng nhập thiếu password, kiểm tra thông báo lỗi.");
    }

    @Test
    public void testLoginProblemUser() {
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", driver.getCurrentUrl());
        String log = "testLoginProblemUser: Đăng nhập với problem_user, kiểm tra chuyển trang inventory.";
        testLogs.add(log);
        showPopup("testLoginProblemUser", "Đăng nhập với problem_user, kiểm tra chuyển trang inventory.");
    }

    @Test
    public void testLoginPerformanceGlitchUser() {
        driver.findElement(By.id("user-name")).sendKeys("performance_glitch_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", driver.getCurrentUrl());
        String log = "testLoginPerformanceGlitchUser: Đăng nhập với performance_glitch_user, kiểm tra chuyển trang inventory.";
        testLogs.add(log);
        showPopup("testLoginPerformanceGlitchUser", "Đăng nhập với performance_glitch_user, kiểm tra chuyển trang inventory.");
    }

    @Test
    public void testSidebarDisplay() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
        System.out.println("Current URL (sidebar): " + driver.getCurrentUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu_button_container")));
        WebElement menuBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@id='menu_button_container']//button[contains(text(),'Open Menu')]")
        ));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", menuBtn);
        WebElement sidebar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu_button_container")));
        Assert.assertTrue(sidebar.isDisplayed());
        System.out.println(sidebar.getAttribute("innerHTML"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_sidebar_link")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("about_sidebar_link")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reset_sidebar_link")));
        Assert.assertTrue(driver.findElement(By.id("inventory_sidebar_link")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("about_sidebar_link")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("logout_sidebar_link")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("reset_sidebar_link")).isDisplayed());
        String log = "testSidebarDisplay: Kiểm tra hiển thị và các mục trong sidebar sau khi đăng nhập.";
        testLogs.add(log);
        showPopup("testSidebarDisplay", "Kiểm tra hiển thị và các mục trong sidebar sau khi đăng nhập.");
    }

    @Test
    public void testLogoutFunction() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/v1/inventory.html"));
        System.out.println("Current URL (logout): " + driver.getCurrentUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu_button_container")));
        WebElement menuBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@id='menu_button_container']//button[contains(text(),'Open Menu')]")
        ));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", menuBtn);
        WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));
        logoutBtn.click();
        wait.until(driver -> {
            String url = driver.getCurrentUrl();
            return url.equals("https://www.saucedemo.com/v1/") || url.equals("https://www.saucedemo.com/v1/index.html");
        });
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.equals("https://www.saucedemo.com/v1/") || url.equals("https://www.saucedemo.com/v1/index.html"));
        String log = "testLogoutFunction: Đăng xuất thành công từ sidebar, kiểm tra quay lại trang login.";
        testLogs.add(log);
        showPopup("testLogoutFunction", "Đăng xuất thành công từ sidebar, kiểm tra quay lại trang login.");
    }

    public void showPopup(String testName, String message) {
        String script = "alert('Testcase: " + testName + " đã thực hiện thành công!\\nChức năng: " + message + "');";
        ((JavascriptExecutor) driver).executeScript(script);
        try { Thread.sleep(10000); } catch (InterruptedException e) {}
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @AfterClass
    public static void printSummary() {
        System.out.println("\n===== TỔNG KẾT CÁC TESTCASE ĐÃ CHẠY =====");
        for (String log : testLogs) {
            System.out.println(log);
        }
        System.out.println("==========================================\n");
    }
} 