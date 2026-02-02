package tests;

// 1. Импорты
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTests {
    private WebDriver driver; // Переменная для управления браузером
    private WebDriverWait wait; // Переменная для явных ожиданий

    // 2. Метод, который запускается ПЕРЕД каждым тестом
    @BeforeMethod
    public void setUp() {
        System.out.println("=== Настраиваем браузер для теста ===");
        // ВАЖНО: WebDriverManager сам найдет и скачает нужный драйвер Chrome
        WebDriverManager.chromedriver().setup();

        // Создаем новый экземпляр браузера Chrome
        driver = new ChromeDriver();

        // Настраиваем браузер: разворачиваем на весь экран
        driver.manage().window().maximize();

        // Создаем объект для явных ожиданий (максимум 10 секунд)
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("Браузер запущен и готов.");
    }

    // 3. Метод, который запускается ПОСЛЕ каждого теста
    @AfterMethod(alwaysRun = true) // alwaysRun гарантирует закрытие, даже если тест упал
    public void tearDown() {
        System.out.println("=== Закрываем браузер ===");
        if (driver != null) {
            driver.quit(); // Закрываем браузер и освобождаем ресурсы
        }
    }

    // 4. ТЕСТ 1: Успешный вход (позитивный сценарий)
    @Test
    public void positiveLoginTest() {
        System.out.println(">>> Запуск ТЕСТА 1: Проверка успешного входа.");

        // 4.1. Открываем страницу логина
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("   Открыта страница входа.");

        // 4.2. Вводим логин. Ждем, пока поле станет видимым.
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("tomsmith");
        System.out.println("   Введен логин: tomsmith");

        // 4.3. Вводим правильный пароль
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        System.out.println("   Введен пароль.");

        // 4.4. Нажимаем кнопку Login
        driver.findElement(By.cssSelector("button.radius")).click();
        System.out.println("   Нажата кнопка Login.");

        // 4.5. Ждем сообщение об успехе и проверяем его текст
        WebElement flashMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("flash"))
        );
        String actualText = flashMessage.getText();
        System.out.println("   Получено сообщение: " + actualText);

        Assert.assertTrue(actualText.contains("You logged into a secure area!"),
                "ОШИБКА: Нет сообщения об успешном входе. Текст был: " + actualText);

        // 4.6. Дополнительная проверка: видна ли кнопка Logout
        boolean isLogoutButtonVisible = driver.findElement(
                By.cssSelector("a.button.secondary.radius")
        ).isDisplayed();

        Assert.assertTrue(isLogoutButtonVisible,
                "ОШИБКА: Кнопка Logout не отображается после входа.");

        System.out.println(">>> ТЕСТ 1 ПРОЙДЕН УСПЕШНО! Пользователь вошел в систему.\n");
    }

    // 5. ТЕСТ 2: Неуспешный вход с неверным паролем (негативный сценарий)
    @Test
    public void negativeLoginTest_WrongPassword() {
        System.out.println(">>> Запуск ТЕСТА 2: Проверка входа с неверным паролем.");

        // 5.1. Открываем страницу логина
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("   Открыта страница входа.");

        // 5.2. Вводим логин
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("tomsmith");
        System.out.println("   Введен логин: tomsmith");

        // 5.3. Вводим НЕВЕРНЫЙ пароль
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        System.out.println("   Введен неверный пароль.");

        // 5.4. Нажимаем кнопку Login
        driver.findElement(By.cssSelector("button.radius")).click();
        System.out.println("   Нажата кнопка Login.");

        // 5.5. Ждем сообщение об ошибке и проверяем его текст
        WebElement flashMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("flash"))
        );
        String actualText = flashMessage.getText();
        System.out.println("   Получено сообщение: " + actualText);

        Assert.assertTrue(actualText.contains("Your password is invalid!"),
                "ОШИБКА: Нет сообщения об ошибке пароля. Текст был: " + actualText);

        System.out.println(">>> ТЕСТ 2 ПРОЙДЕН УСПЕШНО! Ошибка пароля корректно отображена.\n");
    }
}
