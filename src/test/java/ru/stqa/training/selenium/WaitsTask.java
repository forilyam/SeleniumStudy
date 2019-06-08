package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class WaitsTask {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    /*
    Задание 13. Сделайте сценарий работы с корзиной
    Сделайте сценарий для добавления товаров в корзину и удаления товаров из корзины.
    Сценарий должен состоять из следующих частей:
    1) открыть страницу какого-нибудь товара
    2) добавить его в корзину
    3) подождать, пока счётчик товаров в корзине обновится
    4) вернуться на главную страницу, повторить предыдущие шаги ещё два раза, чтобы в общей сложности в корзине было 3 единицы товара
    5) открыть корзину (в правом верхнем углу кликнуть по ссылке Checkout)
    6) удалить все товары из корзины один за другим, после каждого удаления подождать, пока внизу обновится таблица
     */
    @Test
    public void task13() {
        driver.get("http://litecart.stqa.ru/en/");
        for (int i = 1; i < 4; i++) {
            addToCart(i);
            driver.findElement(By.cssSelector("#logotype-wrapper")).click();
        }
        driver.findElement(By.cssSelector("#cart .link")).click();

        String numberOfItems = driver.findElement(By.name("quantity")).getAttribute("value");
        for (int i = Integer.parseInt(numberOfItems); i > 0; i--) {
            removeFromCart(i);
        }

    }

    @AfterTest
    public void stop() {
        driver.quit();
        driver = null;
    }

    private void addToCart(int i) {
        driver.findElement(By.xpath("//img[@alt='Green Duck']")).click();
        driver.findElement(By.name("add_cart_product")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("span.quantity"), ""+i+""));
    }

    private void removeFromCart(int numberOfItems) {
        driver.findElement(By.name("quantity")).clear();
        driver.findElement(By.name("quantity")).sendKeys(""+(numberOfItems-1)+"");
        driver.findElement(By.name("update_cart_item")).click();
        try {
            wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id='order_confirmation-wrapper']//table/tbody/tr/td[1]"), ""+(numberOfItems-1)+""));
        } catch (Exception e) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//em[.='There are no items in your cart.']")));
        }
    }
}
