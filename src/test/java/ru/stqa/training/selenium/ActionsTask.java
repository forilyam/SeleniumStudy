package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class ActionsTask {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /*
    Задание 11. Сделайте сценарий регистрации пользователя
    Сделайте сценарий для регистрации нового пользователя в учебном приложении litecart (не в админке, а в клиентской части магазина).
    Сценарий должен состоять из следующих частей:
    1) регистрация новой учётной записи с достаточно уникальным адресом электронной почты (чтобы не конфликтовало с ранее созданными пользователями),
    2) выход (logout), потому что после успешной регистрации автоматически происходит вход,
    3) повторный вход в только что созданную учётную запись,
    4) и ещё раз выход.
     */
    @Test
    public void task11() {
        driver.get("http://litecart.stqa.ru/en/");
        driver.findElement(By.xpath("//a[contains(text(),'New customers click here')]")).click();
        driver.findElement(By.name("firstname")).sendKeys("IlyaTest");
        driver.findElement(By.name("lastname")).sendKeys("IlyaTest");
        driver.findElement(By.name("address1")).sendKeys("IlyaTest");
        driver.findElement(By.name("postcode")).sendKeys("12345-123");
        driver.findElement(By.name("city")).sendKeys("IlyaTest");
        driver.findElement(By.cssSelector(".select2-selection__rendered")).click();
        driver.findElement(By.xpath("//li[contains(text(),'Brazil')]")).click();
        driver.findElement(By.name("email")).sendKeys("IlyaTest@IlyaTest.com");
        driver.findElement(By.name("phone")).sendKeys("+71234567890");
        driver.findElement(By.name("password")).sendKeys("IlyaTest");
        driver.findElement(By.name("confirmed_password")).sendKeys("IlyaTest");
        driver.findElement(By.name("newsletter")).click();
        driver.findElement(By.name("create_account")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        driver.findElement(By.name("email")).sendKeys("IlyaTest@IlyaTest.com");
        driver.findElement(By.name("password")).sendKeys("IlyaTest");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
    }

    /*
    Задание 12. Сделайте сценарий добавления товара
    Сделайте сценарий для добавления нового товара (продукта) в учебном приложении litecart (в админке).
    Для добавления товара нужно открыть меню Catalog, в правом верхнем углу нажать кнопку "Add New Product",
    заполнить поля с информацией о товаре и сохранить.
    Достаточно заполнить только информацию на вкладках General, Information и Prices.
    Скидки (Campains) на вкладке Prices можно не добавлять.
    После сохранения товара нужно убедиться, что он появился в каталоге (в админке).
    Клиентскую часть магазина можно не проверять.
    Можно оформить сценарий либо как тест, либо как отдельный исполняемый файл.
     */
    @Test
    public void task12() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.cssSelector(".form-control[name=username]")).sendKeys("admin");
        driver.findElement(By.cssSelector(".form-control[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        wait.until(titleIs("My Store"));
        driver.findElement(By.cssSelector("#app-catalog")).click();
        driver.findElement(By.cssSelector("a[href='http://localhost/litecart/admin/?category_id=0&app=catalog&doc=edit_product']")).click();

        driver.findElement(By.cssSelector("label.btn.btn-default")).click();
        driver.findElement(By.name("name[en]")).sendKeys("Test Duck");
        driver.findElement(By.name("code")).sendKeys("rd126");
        driver.findElement(By.name("sku")).sendKeys("RD126");
        driver.findElement(By.name("mpn")).sendKeys("12340126");
        Select manufacturer = new Select(driver.findElement(By.name("manufacturer_id")));
        manufacturer.selectByVisibleText("ACME Corp.");
        driver.findElement(By.name("date_valid_from")).sendKeys("30052019");
        driver.findElement(By.name("date_valid_to")).sendKeys("01062019");
        driver.findElement(By.cssSelector("a[href='#tab-information']")).click();
        driver.findElement(By.name("short_description[en]")).sendKeys("Test" + Keys.TAB  + "Test");
        driver.findElement(By.name("attributes[en]")).sendKeys("Test");
        driver.findElement(By.cssSelector("a[href='#tab-prices']")).click();
        driver.findElement(By.name("purchase_price")).clear();
        driver.findElement(By.name("purchase_price")).sendKeys("15");
        driver.findElement(By.name("prices[USD]")).sendKeys("30");
        driver.findElement(By.name("save")).click();
        List<WebElement> products = driver.findElements(By.xpath("//table/tbody/tr/td[3]"));
        int lastAddedProduct = products.size();
        String nameOfLastAddedProduct = products.get(lastAddedProduct-1).getText();
        Assert.assertEquals(nameOfLastAddedProduct, "Test Duck");
    }

    @AfterTest
    public void stop() {
        driver.quit();
        driver = null;
    }
}
