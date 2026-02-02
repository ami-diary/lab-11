package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.time.Duration;

/**
 * Тесты для проверки функциональности авторизации
 * на сайте the-internet.herokuapp.com
 */
public class LoginTests {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @BeforeMethod
    public void setUp() {
        // WebDriverManager автоматически скачает нужный драйвер!
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("=== Инициализация тестовой среды ===");
    }
    
    @AfterMethod(alwaysRun = true)  // Добавь alwaysRun = true!
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("=== Очистка тестовой среды ===\n");
    }
    
    /**
     * Тест 1: Проверка успешной авторизации
     * Ожидаемый результат: сообщение об успешном входе
     */
    @Test
    public void positiveLoginTest() {
        System.out.println("=== ТЕСТ 1: Успешная авторизация ===");
        
        driver.get("https://the-internet.herokuapp.com/login");
        
        // Ввод данных
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                .sendKeys("tomsmith");
        driver.findElement(By.id("password"))
                .sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button.radius"))
                .click();
        
        // Проверка успешного входа
        String flashMessage = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("flash"))).getText();
        Assert.assertTrue(flashMessage.contains("You logged into a secure area!"),
                "Нет подтверждения успешного входа.");
        
        // Дополнительная проверка - кнопка Logout
        Assert.assertTrue(driver.findElement(By.cssSelector("a.button.secondary.radius"))
                .isDisplayed(), "Кнопка Logout не отображается");
        
        System.out.println("✅ ТЕСТ ПРОЙДЕН: Успешная авторизация подтверждена");
    }
    
    /**
     * Тест 2: Проверка авторизации с неверным паролем
     * Ожидаемый результат: сообщение об ошибке
     */
    @Test
    public void negativeLoginTest_WrongPassword() {
        System.out.println("=== ТЕСТ 2: Авторизация с неверным паролем ===");
        
        driver.get("https://the-internet.herokuapp.com/login");
        
        // Ввод неверных данных
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                .sendKeys("tomsmith");
        driver.findElement(By.id("password"))
                .sendKeys("wrong_password");
        driver.findElement(By.cssSelector("button.radius"))
                .click();
        
        // Проверка ошибки
        String flashMessage = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("flash"))).getText();
        Assert.assertTrue(flashMessage.contains("Your password is invalid!"),
                "Ожидалось сообщение об ошибке аутентификации.");
        
        System.out.println("✅ ТЕСТ ПРОЙДЕН: Ошибка авторизации подтверждена");
    }
}
