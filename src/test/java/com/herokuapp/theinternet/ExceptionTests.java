package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ExceptionTests {
    private WebDriver driver;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser){
        //      Create driver
        switch (browser){
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "C:/Users/nkljajic/IdeaProjects/Selenium for Beginners/resources/chromedriver.exe" );
                driver = new ChromeDriver();
                break;

            case "firefox":
                System.setProperty("webdriver.gecko.driver", "C:/Users/nkljajic/IdeaProjects/Selenium for Beginners/resources/geckodriver.exe" );
                driver = new FirefoxDriver();
                break;

            default:
                System.out.println("Do not know how to open: " + browser + ", starting chrome");
                System.setProperty("webdriver.chrome.driver", "C:/Users/nkljajic/IdeaProjects/Selenium for Beginners/resources/chromedriver.exe" );
                driver = new ChromeDriver();
                break;

        }

        //sleep for 3 seconds
        sleep(3000);

        //Maximize browser window
        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }
    @Parameters({"text"})
    @Test
    public void notVisibleTest(String text){
        //Open page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        System.out.println("Page is opened.");

        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        WebElement loadingIndicator = driver.findElement(By.xpath("//div[@id='loading']"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(loadingIndicator));
        System.out.println("Click on button");
        //sleep(5000);



        //Text is visible
        WebElement textElement = driver.findElement(By.id("finish"));
        String actualText = textElement.getText();
        Assert.assertTrue(actualText.contains(text), "Actual message does not match the expected text" +
                "\nActual message: " + actualText +
                "\nExpected message: " + text);



    }

    @Parameters({"text"})
    @Test
    public void timeOutTest(String text){
        //Open page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        System.out.println("Page is opened.");

        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        WebElement textElement = driver.findElement(By.id("finish"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));

        try {
            wait.until(ExpectedConditions.visibilityOf(textElement));
        }catch (TimeoutException exception){
            System.out.println("Exception catched: " + exception.getMessage());

        }

        System.out.println("Click on button");
        //sleep(5000);

        String finishText = textElement.getText();

        //Text is visible
        Assert.assertTrue(finishText.contains(text), "Actual message does not match the expected text" +
                "\nActual message: " + finishText +
                "\nExpected message: " + text);



    }


    @Parameters({"text"})
    @Test
    public void noSuchElementTest(String text){
        //Open page
        String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
        driver.get(url);
        System.out.println("Page is opened.");

        WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
        startButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        Assert.assertTrue(wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), text)),
                "\nCould not verify expected text: " + text);


        /*WebElement textElement = driver.findElement(By.id("finish"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));

        try {
            wait.until(ExpectedConditions.visibilityOf(textElement));
        }catch (TimeoutException exception){
            System.out.println("Exception catched: " + exception.getMessage());

        }

        System.out.println("Click on button");
        //sleep(5000);

        String finishText = textElement.getText();

        //Text is visible
        Assert.assertTrue(finishText.contains(text), "Actual message does not match the expected text" +
                "\nActual message: " + finishText +
                "\nExpected message: " + text);*/



    }

    @Test
    public void staleElement(){
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");
        WebElement checkBox = driver.findElement(By.id("checkbox"));
        WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        removeButton.click();
        Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkBox)),
                "Checkbox is still visible, but should not be");


    }

    @Parameters({"text"})
    @Test
    public void disabledElementTest(String text) {
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");
        WebElement buttonEnable = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
        WebElement textField = driver.findElement(By.xpath("(//input)[2]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        buttonEnable.click();
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(textField));
        textField.sendKeys(text);
        Assert.assertEquals(textField.getAttribute("value"), text);
    }


    private void sleep(long m) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    private void tearDown() {
        //close browser
        driver.quit();
    }
}
