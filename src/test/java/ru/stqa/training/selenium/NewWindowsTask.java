package ru.stqa.training.selenium;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class NewWindowsTask {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    /*
    Задание 14. Проверьте, что ссылки открываются в новом окне
    Сделайте сценарий, который проверяет, что ссылки на странице редактирования страны открываются в новом окне.
    Сценарий должен состоять из следующих частей:
    1) зайти в админку
    2) открыть пункт меню Countries (или страницу http://localhost/litecart/admin/?app=countries&doc=countries)
    3) открыть на редактирование какую-нибудь страну или начать создание новой
    4) возле некоторых полей есть ссылки с иконкой в виде квадратика со стрелкой -- они ведут на внешние страницы
    и открываются в новом окне, именно это и нужно проверить.
    Конечно, можно просто убедиться в том, что у ссылки есть атрибут target="_blank". Но в этом упражнении требуется
    именно кликнуть по ссылке, чтобы она открылась в новом окне, потом переключиться в новое окно, закрыть его, вернуться обратно, и повторить эти действия для всех таких ссылок.
    Не забудьте, что новое окно открывается не мгновенно, поэтому требуется ожидание открытия окна.
     */
    @Test
    public void task14() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.cssSelector(".form-control[name=username]")).sendKeys("admin");
        driver.findElement(By.cssSelector(".form-control[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        wait.until(titleIs("My Store"));
        driver.findElement(By.cssSelector("#app-countries")).click();
        driver.findElement(By.cssSelector(".btn.btn-default")).click();

        String mainWindow = driver.getWindowHandle();
        Set<String> existingWindows = driver.getWindowHandles();
        List<WebElement> externalLinks = driver.findElements(By.cssSelector(".fa.fa-external-link"));
        for (WebElement e: externalLinks) {
            e.click();
            String newWindow = wait.until(anyWindowOtherThan(existingWindows));
            driver.switchTo().window(newWindow);
            driver.close();
            driver.switchTo().window(mainWindow);
        }
    }

    @AfterTest
    public void stop() {
        driver.quit();
        driver = null;
    }

    private ExpectedCondition<String> anyWindowOtherThan(final Set<String> oldWindows){
        return new ExpectedCondition<String>() {
            @NullableDecl
            @Override
            public String apply(@NullableDecl WebDriver webDriver) {
                Set<String> handles = webDriver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
}
