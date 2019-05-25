package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FindElementsTask {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /*
    Задание 7. Сделайте сценарий, проходящий по всем разделам админки
    Сделайте сценарий, который выполняет следующие действия в учебном приложении litecart.
    1) входит в панель администратора http://localhost/litecart/admin
    2) прокликивает последовательно все пункты меню слева, включая вложенные пункты
    3) для каждой страницы проверяет наличие заголовка
     */
    @Test
    public void task7() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.cssSelector(".form-control[name=username]")).sendKeys("admin");
        driver.findElement(By.cssSelector(".form-control[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        wait.until(titleIs("My Store"));

        driver.findElement(By.cssSelector("#app-appearance > a")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-template")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-logotype")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));

        driver.findElement(By.cssSelector("#app-catalog > a")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-catalog")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-product_groups")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-option_groups")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-manufacturers")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-suppliers")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-delivery_statuses")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-sold_out_statuses")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-quantity_units")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));
        driver.findElement(By.id("doc-csv")).click();
        assertTrue(isElementPresent(driver, By.cssSelector("title")));

    }


    /*
    Задание 8. Сделайте сценарий, проверяющий наличие стикеров у товаров
    Сделайте сценарий, проверяющий наличие стикеров у всех товаров в учебном приложении litecart на главной странице.
    Стикеры -- это полоски в левом верхнем углу изображения товара,
    на которых написано New или Sale или что-нибудь другое.
    Сценарий должен проверять, что у каждого товара имеется ровно один стикер.
     */
    @Test
    public void task8() {
        driver.get("http://localhost/litecart");
        driver.findElement(By.cssSelector("a[href='#popular-products']")).click();
        List<WebElement> listOfElements = driver.findElements(By.xpath("//div[@id='box-popular-products']/div/div"));
        for( int i=1; i<=listOfElements.size(); i++ ) {
            assertEquals(driver.findElements(By.xpath("(//div[@id='box-popular-products']//div[contains(@class,'sticker')])["+i+"]")).size(), 1);
        }

    }

    @AfterTest
    public void stop() {
        driver.quit();
        driver = null;
    }

    private boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
