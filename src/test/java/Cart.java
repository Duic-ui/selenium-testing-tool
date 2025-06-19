
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Cart {
    WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        Helper.login(driver, "standard_user", "secret_sauce");
        //driver.manage().window().maximize(); // Tối ưu hóa cửa sổ trình duyệt
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

    @Test
    public void testAddAndRemoveTwoProducts() throws InterruptedException {
        // ===== B1: Thêm 2 sản phẩm vào giỏ =====
        String[] productNames = {"Sauce Labs Backpack", "Sauce Labs Bike Light"};

        for (String productName : productNames) {
            WebElement addBtn = driver.findElement(
                    By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button")
            );
            addBtn.click();
            Thread.sleep(1000);

            WebElement removeBtn = driver.findElement(
                    By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button")
            );
            Assert.assertEquals("REMOVE", removeBtn.getText());
            Thread.sleep(1000);
        }

        // ===== B2: Vào trang giỏ hàng =====
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertEquals("https://www.saucedemo.com/v1/cart.html", driver.getCurrentUrl());
        Thread.sleep(1000);

        // ===== B3: Kiểm tra cả 2 sản phẩm có trong giỏ hàng =====
        for (String productName : productNames) {
            boolean isPresent = driver.findElements(By.className("inventory_item_name"))
                    .stream()
                    .anyMatch(el -> el.getText().equals(productName));
            Assert.assertTrue("Không tìm thấy sản phẩm: " + productName, isPresent);
            Thread.sleep(1000);
        }

        // ===== B4: Xóa cả 2 sản phẩm =====
        for (String productName : productNames) {
            WebElement removeBtnInCart = driver.findElement(
                    By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button[text()='REMOVE']")
            );
            removeBtnInCart.click();
            Thread.sleep(1000);
        }

        // ===== B5: Đảm bảo cả 2 sản phẩm đã bị xóa khỏi giỏ hàng =====
        for (String productName : productNames) {
            boolean stillExists = driver.findElements(By.className("inventory_item_name"))
                    .stream()
                    .anyMatch(el -> el.getText().equals(productName));
            Assert.assertFalse("Sản phẩm vẫn còn trong giỏ hàng: " + productName, stillExists);
            Thread.sleep(1000);
        }
    }

    @Test
    public void testCheckoutFlow() throws InterruptedException {
        // B1: Thêm sản phẩm "Sauce Labs Bike Light" vào giỏ
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button")
        );
        addBtn.click();
        Thread.sleep(1000);

        // B2: Click vào biểu tượng giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(1000);

        // B3: Click vào nút "CHECKOUT"
        driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
        Thread.sleep(1000);

        // B4: Nhập thông tin
        driver.findElement(By.id("first-name")).sendKeys("Nguyen");
        driver.findElement(By.id("last-name")).sendKeys("Huynh");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(1000);

        // B5: Click nút "CONTINUE"
        driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
        Thread.sleep(1000);

        // B6: Click nút "FINISH"
        driver.findElement(By.xpath("//a[text()='FINISH']")).click();
        Thread.sleep(1000);

        // B7: Kiểm tra hiện trang cảm ơn
        WebElement thankYou = driver.findElement(By.className("complete-header"));
        Assert.assertEquals("THANK YOU FOR YOUR ORDER", thankYou.getText());
    }

    @Test
    public void testCheckoutCancelAtFinishStep() throws InterruptedException {
        // B1: Thêm sản phẩm "Sauce Labs Bike Light" vào giỏ
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button")
        );
        addBtn.click();
        Thread.sleep(1000);

        // B2: Click vào biểu tượng giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(1000);

        // B3: Click vào nút "CHECKOUT"
        driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
        Thread.sleep(1000);

        // B4: Nhập thông tin
        driver.findElement(By.id("first-name")).sendKeys("Nguyen");
        driver.findElement(By.id("last-name")).sendKeys("Huynh");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        Thread.sleep(1000);

        // B5: Click nút "CONTINUE"
        driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
        Thread.sleep(1000);

        // B6: Click nút "CANCEL" ở bước cuối
        driver.findElement(By.xpath("//a[text()='CANCEL']")).click();
        Thread.sleep(1000);

        // B7: Kiểm tra có trở lại trang chính (inventory)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://www.saucedemo.com/v1/inventory.html", currentUrl);
    }


    @Test
    public void testCheckoutValidationEmptyFields() throws InterruptedException {
        // B1: Thêm sản phẩm bất kỳ vào giỏ
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button")
        );
        addBtn.click();
        Thread.sleep(1000);

        // B2: Mở giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(1000);

        // B3: Click CHECKOUT
        driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
        Thread.sleep(1000);

        // B4: Không nhập gì cả, nhấn CONTINUE
        driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
        Thread.sleep(1000);

        // B5: Kiểm tra thông báo lỗi hiển thị
        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String errorMessage = error.getText();
        System.out.println("Error Message: " + errorMessage);

        // B6: Kiểm tra nội dung lỗi là yêu cầu nhập First Name
        Assert.assertTrue(errorMessage.contains("Error: First Name is required"));
    }

    @Test
    public void testCheckoutCancelReturnsToMainPage() throws InterruptedException {
        // B1: Thêm sản phẩm vào giỏ
        WebElement addBtn = driver.findElement(
                By.xpath("//div[text()='Sauce Labs Bike Light']/ancestor::div[@class='inventory_item']//button")
        );
        addBtn.click();
        Thread.sleep(1000);

        // B2: Mở giỏ hàng
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(1000);

        // B3: Nhấn CHECKOUT
        driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
        Thread.sleep(1000);

        // B4: Nhấn CANCEL
        driver.findElement(By.xpath("//a[text()='CANCEL']")).click();
        Thread.sleep(1000);

        // B5: Kiểm tra đã quay về trang chính
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://www.saucedemo.com/v1/cart.html", currentUrl);
    }



    @After
    public void close() {
        if (driver != null) {
            driver.quit(); // Ensure the browser is closed after the test
        }
    }

//    @After
//    public void close() {
//        if (driver != null) {
//            driver.quit(); // Ensure the browser is closed after the test
//        }
//    }

}
