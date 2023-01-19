package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.Assertion;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LoginTests {
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }




    @Test(priority = 1, groups = {"positiveTest", "smokeTest"})
    public void loginPositiveTest(){
        System.out.println("Starting loginTest");


//      Open test page
        String url = "https://the-internet.herokuapp.com/login";
        driver.get(url);
        System.out.println("Page is opened.");

        //sleep for 2 seconds
        sleep(2000);

//      Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("tomsmith");
        sleep(1000);

//      Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("SuperSecretPassword!");
        sleep(3000);

        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

//      Click login button
        WebElement loginButton = driver.findElement(By.tagName("button"));
        //wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();

//      Verification
//      New Url
        String expectedUrl = "https://the-internet.herokuapp.com/secure";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "Actual url is not the same as expected");

//      Logout button is visible
        WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
        Assert.assertTrue(logOutButton.isDisplayed(), "Log out button is not visible");

//      Successful login message
        WebElement message = driver.findElement(By.cssSelector("#flash"));
        String expectedMessage = "You logged into a secure area!";
        String actualMessage = message.getText();
        //Assert.assertEquals(actualMessage, expectedMessage, "Actual message is not the same as expected");
        Assert.assertTrue(actualMessage.contains(expectedMessage), "Actual message does not contain expected message." +
                "\nActual message: " + actualMessage +
                "\nExpected message: " + expectedMessage);


    }
    @AfterMethod(alwaysRun = true)
    private void tearDown() {
        //close browser
        driver.quit();
    }


    @Parameters({"username", "password", "expectedMessage"})
    @Test(priority = 2, groups = {"negativeTest", "smokeTest"})
    public void loginNegativeTest(String username, String password, String expectedMessage){
        System.out.println("Starting negative username test");


//      Open test page
        String url = "https://the-internet.herokuapp.com/login";
        driver.get(url);
        System.out.println("Page is opened.");

        //sleep 3 seconds
        sleep(3000);

//      Enter username
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys(username);
        sleep(1000);


//      Enter password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        sleep(3000);

//      Click on login button
        WebElement loginButton = driver.findElement(By.tagName("button"));
        loginButton.click();

//      Verification
//      New url
//      Logout button is visible
//      Error message
        WebElement errorMessage = driver.findElement(By.xpath("//div[@id='flash']"));
        String actualMessage = errorMessage.getText();
        Assert.assertTrue(actualMessage.contains(expectedMessage), "Actual message does not contain expected message." +
                "\nActual message: " + actualMessage +
                "\nExpected message: " + expectedMessage);


    }

    private void sleep(long m) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
