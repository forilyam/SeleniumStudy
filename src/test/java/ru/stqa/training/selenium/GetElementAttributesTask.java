package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.testng.Assert.assertTrue;

public class GetElementAttributesTask {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /*
    Задание 9. Проверить сортировку стран и геозон в админке
    Сделайте сценарии, которые проверяют сортировку стран и геозон (штатов) в учебном приложении litecart.
    1) на странице http://localhost/litecart/admin/?app=countries&doc=countries
    а) проверить, что страны расположены в алфавитном порядке
    б) для тех стран, у которых количество зон отлично от нуля -- открыть страницу этой страны и там проверить, что зоны расположены в алфавитном порядке
    2) на странице http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones
    зайти в каждую из стран и проверить, что зоны расположены в алфавитном порядке
     */
    @Test
    public void task91a() {
        openPage();

        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr/td[5]"));
        boolean alpha = true;
        String previous = "";
        Collator collator = Collator.getInstance(Locale.ENGLISH);
        collator.setStrength(Collator.PRIMARY);
        for (WebElement e: elements) {
            String current = e.getText();
            if (collator.compare(current, previous) < 0) {
                alpha = false;
                System.out.println(previous);
                System.out.println(current);
                break;
            }
            previous = current;
        }
        assertTrue(alpha);

    }

    @Test
    public void task91b() {
        openPage();

        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));

        for (WebElement e: elements) {
            String str = e.findElement(By.xpath("td[6]")).getText();
            System.out.println(str);
            int result = Integer.parseInt(str);
            if (result != 0) {
                e.findElement(By.xpath("td[5]/a")).click();
                List<WebElement> zones = driver.findElements(By.xpath("//*[@name='country_form']/table/tbody/tr/td[3]/input"));
                boolean alpha = true;
                String previous = "";
                Collator collator = Collator.getInstance(Locale.ENGLISH);
                collator.setStrength(Collator.PRIMARY);
                for (WebElement z: zones) {
                    String current = z.getText();
                    if (collator.compare(current, previous) < 0) {
                        alpha = false;
                        System.out.println(previous);
                        System.out.println(current);
                        break;
                    }
                    previous = current;
                }
                assertTrue(alpha);
                driver.navigate().back();
            }
        }
    }

    @Test
    public void task92() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.cssSelector(".form-control[name=username]")).sendKeys("admin");
        driver.findElement(By.cssSelector(".form-control[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        wait.until(titleIs("My Store"));
        driver.findElement(By.cssSelector("#app-geo_zones")).click();

        driver.findElement(By.xpath("//table/tbody/tr/td[3]/a")).click();

        List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));
        boolean alpha = true;
        String previous = "";
        Collator collator = Collator.getInstance(Locale.ENGLISH);
        collator.setStrength(Collator.PRIMARY);
        for (WebElement e: elements) {
            String current = e.getText();
            if (collator.compare(current, previous) < 0) {
                alpha = false;
                System.out.println(previous);
                System.out.println(current);
                break;
            }
            previous = current;
        }
        assertTrue(alpha);
    }

    /*
    Задание 10. Проверить, что открывается правильная страница товара
    Сделайте сценарий, который проверяет, что при клике на товар открывается правильная страница товара
    в учебном приложении litecart.
    1) Открыть главную страницу
    2) Кликнуть по первому товару в категории Campaigns
    3) Проверить, что открывается страница правильного товара
    Более точно, проверить, что
    а) совпадает текст названия товара
    б) совпадает цена (обе цены)
    Кроме того, проверить стили цены на главной странице и на странице товара -- первая цена серая,
    зачёркнутая, маленькая, вторая цена красная жирная, крупная.
     */
    @Test
    public void task10() {
        driver.get("http://localhost/litecart");

        driver.findElement(By.cssSelector("a[href='#campaign-products']")).click();
        String titleOfFirstProduct = driver.findElement(By.cssSelector(".name")).getText();
        String regularPrice = driver.findElement(By.cssSelector(".regular-price")).getText();
        String regularPriceColor = driver.findElement(By.cssSelector(".regular-price")).getCssValue("color");
        String regularPriceTag = driver.findElement(By.cssSelector(".regular-price")).getTagName();
        String regularPriceFont = driver.findElement(By.cssSelector(".regular-price")).getCssValue("font-size");
        Assert.assertEquals(regularPriceColor, "rgba(51, 51, 51, 1)");
        Assert.assertEquals(regularPriceTag, "s");
        Assert.assertEquals(regularPriceFont, "14.0625px");

        String campaignPrice = driver.findElement(By.cssSelector(".campaign-price")).getText();
        String campaignPriceColor = driver.findElement(By.cssSelector(".campaign-price")).getCssValue("color");
        String campaignPriceTag = driver.findElement(By.cssSelector(".campaign-price")).getTagName();
        String campaignPriceFont = driver.findElement(By.cssSelector(".campaign-price")).getCssValue("font-weight");
        Assert.assertEquals(campaignPriceColor, "rgba(204, 0, 0, 1)");
        Assert.assertEquals(campaignPriceTag, "strong");
        Assert.assertEquals(campaignPriceFont, "700");

        String href = driver.findElement(By.cssSelector("a.link")).getAttribute("href");
        System.out.println(href);
        driver.get(href);

        String titleOnPageOfFirstProduct = driver.findElement(By.cssSelector("h1.title")).getText();
        System.out.println(titleOfFirstProduct);
        System.out.println(titleOnPageOfFirstProduct);
        Assert.assertEquals(titleOfFirstProduct, titleOnPageOfFirstProduct);
        String regularPriceOnPageOfFirstProduct = driver.findElement(By.cssSelector(".regular-price")).getText();
        String regularPriceTagOnPageOfFirstProduct = driver.findElement(By.cssSelector(".regular-price")).getTagName();
        String regularPriceFontOnPageOfFirstProduct = driver.findElement(By.cssSelector(".regular-price")).getCssValue("font-size");
        Assert.assertEquals(regularPriceTagOnPageOfFirstProduct, "del");
        Assert.assertEquals(regularPriceFontOnPageOfFirstProduct, "22.5px");

        String campaignPriceOnPageOfFirstProduct = driver.findElement(By.cssSelector(".campaign-price")).getText();
        String campaignPriceColorOnPageOfFirstProduct = driver.findElement(By.cssSelector(".campaign-price")).getCssValue("color");
        String campaignPriceTagOnPageOfFirstProduct = driver.findElement(By.cssSelector(".campaign-price")).getTagName();
        Assert.assertEquals(campaignPriceColorOnPageOfFirstProduct, "rgba(204, 0, 0, 1)");
        Assert.assertEquals(campaignPriceTagOnPageOfFirstProduct, "strong");

        Assert.assertEquals(regularPrice, regularPriceOnPageOfFirstProduct);
        Assert.assertEquals(campaignPrice, campaignPriceOnPageOfFirstProduct);
    }


    @AfterTest
    public void stop() {
        driver.quit();
        driver = null;
    }


    private void openPage () {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.cssSelector(".form-control[name=username]")).sendKeys("admin");
        driver.findElement(By.cssSelector(".form-control[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        wait.until(titleIs("My Store"));
        driver.findElement(By.cssSelector("#app-countries")).click();
    }


//    private boolean compareStrings(List thelist) {
//        String previous = ""; // empty string: guaranteed to be less than or equal to any other
//
//        for (final String current: thelist) {
//            if (current.compareTo(previous) < 0)
//                return false;
//            previous = current;
//        }
//
//        return true;
//    }
}
