package com.uff.pedalauff;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Pattern;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTests {
    protected static WebDriver driver;

    @BeforeClass
    public static void configuraDriver() {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Java\\selenium\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get("http://localhost:8080");
    }

    @Test
    public void step1_testSetup() {
        WebElement homeLink = driver.findElement(By.cssSelector("body > header > a.logo"));
        assertEquals("PedalaUFF", homeLink.getText());
    }

    @Test
    public void step2_testLogin() {
        WebElement loginLink = driver.findElement(By.cssSelector("html > body > section.wrapper > div.inner > div.highlights > section > div.content > header > a.icon[data-target='#myModal']"));
        loginLink.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("myModal"))));
        WebElement email = driver.findElement(By.id("email"));
        WebElement senha = driver.findElement(By.id("senha"));
        WebElement submitBtn = driver.findElement(By.cssSelector("input[type='submit']"));
        email.sendKeys("admin@gmail.com");
        senha.sendKeys("admin");
        submitBtn.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBe(By.cssSelector("#heading > h1"), "PEDALA UFF - ALUGUEL DE BICICLETAS"));
        assertEquals("PEDALA UFF - ALUGUEL DE BICICLETAS", driver.findElement(By.cssSelector("#heading > h1")).getText());
    }

    @Test
    public void step3_testAlugar() {
        WebElement alugarLink = driver.findElement(By.cssSelector("a.icon[data-target='#myModalAlugar']"));
        alugarLink.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("myModalAlugar"))));
        WebElement qrCode = driver.findElement(By.cssSelector("input#name[name='qrCodeBicicleta']"));
        WebElement submitBtn = driver.findElement(By.cssSelector("#myModalAlugar input[type='submit']"));
        qrCode.sendKeys("41ys");
        submitBtn.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBe(By.cssSelector("#heading > h1"), "PEDALA UFF - ALUGUEL DE BICICLETAS"));
        assertEquals("PEDALA UFF - ALUGUEL DE BICICLETAS", driver.findElement(By.cssSelector("#heading > h1")).getText());
    }

    @Test
    public void step4_testDevolver() {
        WebElement alugarLink = driver.findElement(By.cssSelector("a.icon[data-target='#myModalDevolver']"));
        alugarLink.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("myModalDevolver"))));
        WebElement qrCode = driver.findElement(By.cssSelector("input#name[name='qrCodeVaga']"));
        WebElement submitBtn = driver.findElement(By.cssSelector("#myModalDevolver input[type='submit']"));
        qrCode.sendKeys("0zm7");
        submitBtn.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBe(By.cssSelector("#heading > h1"), "PEDALA UFF - ALUGUEL DE BICICLETAS"));
        assertEquals("PEDALA UFF - ALUGUEL DE BICICLETAS", driver.findElement(By.cssSelector("#heading > h1")).getText());
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }
}
