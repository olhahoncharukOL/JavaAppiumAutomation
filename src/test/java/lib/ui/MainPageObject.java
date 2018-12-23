package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * Created by User on 12.12.2018.
 */
public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public void waitForElementAndClick(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();
    }
    public void waitForElementAndClick(String locator) {
        waitForElementAndClick(locator, "no such element as " + locator, 5);
    }

    public void waitForElementAndEnterText(String locator, String errorMessage, long timeoutInSeconds, String text) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        wait.until(ExpectedConditions.presenceOfElementLocated(by)).sendKeys(text);
    }
    public void waitForElementAndEnterText(String locator, String text) {
        waitForElementAndEnterText(locator, "element not found", 10, text);
    }

    public boolean waitForElementsDisappear(List<WebElement> list, String errorMessage, long timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfAllElements(list));
    }
    public boolean waitForElementDisappear (String locator, String error_message, int timeOutInSec) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementPresents(String locator, String error_message, long timeOutInSecs){
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSecs);
        wait.withMessage(by.toString()+ error_message);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
    public WebElement waitForElementPresents(By by){
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.withMessage(by.toString()+ " no such element on the page");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForElementAndClear(String locator) {
        By by = getLocatorByString(locator);
        waitForElementPresents(by).clear();
    }

    public void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width/2;
        int start_y = (int)(size.height*0.8);
        int end_y = (int) (size.height*0.2);
        action.press(x,start_y).waitAction(timeOfSwipe).moveTo(x,end_y).release().perform();
    }
    public void swipeUpQuick() {
        swipeUp(200);
    }
    public void swipeUpToFindElement(String locator, String error_message, int max_swipes) {
        int alreadySwiped=0;
        By by = getLocatorByString(locator);
        while (driver.findElements(by).size()==0) {
            if (alreadySwiped>max_swipes) {
                waitForElementPresents(locator, "can't find element with swipe.\n" + error_message, 0 ) ;
                return;
            }
            swipeUpQuick();
            ++alreadySwiped;
        }

    }
    public void swipeElementToLeft (String locator, String error_message) {
        WebElement element = waitForElementPresents(locator,
                error_message ,
                10);
        int left_x = element.getLocation().getX();
        int rigth_x = left_x+ element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y+element.getSize().getHeight();
        int middle_y = (upper_y+lower_y)/2;

        TouchAction action = new TouchAction(driver);
        action.
                press(rigth_x, middle_y).
                waitAction(400).
                moveTo(left_x, middle_y).
                release().
                perform();
    }

    public boolean isElementPresent(String locator) {
        By by = getLocatorByString(locator);
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("Element title not found");
            //  e.printStackTrace();
            return false;
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeOutInSeconds) {
        WebElement element = waitForElementPresents(locator, error_message, timeOutInSeconds);
        return element.getAttribute(attribute);
    }

    protected By getLocatorByString(String locator_with_type) {
        String[] exploded_locator= locator_with_type.split(Pattern.quote(":"),2);
        String by_type = exploded_locator[0];
        String locator = exploded_locator[1];
        if (by_type.equals("xpath")) {
            return By.xpath(locator);
        } else if (by_type.equals("ID")){
            return By.id(locator);
        }
        else{
            throw new IllegalArgumentException("Cannot get type of locator. Locator " +locator_with_type);
        }
    }

}
