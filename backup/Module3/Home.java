package QKART_SANITY_LOGIN.Module1;

import QKART_SANITY_LOGIN.Module1.SearchResult;
import java.util.ArrayList;
import java.util.List;
// import org.junit.internal.runners.statements.ExpectException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    public boolean searchForProduct(String product) {
        try {
            WebElement srcBox = driver.findElement(By.xpath("//input[@name='search']"));
            srcBox.clear();
            srcBox.sendKeys(product);
            // Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(driver, 3);

            ExpectedCondition con1 = ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//h4[text()=' No products found ']"));
            ExpectedCondition con2 = ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"));
            // ExpectException con2=ExpectedConditions.urlContains(fraction);
            wait.until(ExpectedConditions.or(con1, con2));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public List<WebElement> getSearchResults() {
        List<WebElement> Items = new ArrayList<WebElement>();
        try {
            Items = driver
                    .findElements(By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"));

            return Items;
        } catch (Exception e) {
            System.out.println(e);
            return Items;
        }
    }

    public boolean isNoResultFound() {
        WebElement we = driver.findElement(By.xpath("//h4[text()=' No products found ']"));
        if (we.isDisplayed()) {
            if (we.getText().equals("No products found")) {
                return true;
            }
        }
        return false;
    }

    public boolean addProductToCart(String text) {
        try {
            List<WebElement> results = getSearchResults();
            // for(WebElement we: results){
            // String str=we.findElement(By.xpath("//p[contains(@class,'MuiTypography-root
            // MuiTypography-body1 css-yg30e6')]")).getText();
            // System.out.println(str);
            // if(str.equals(text)){
            // Thread.sleep(2000);
            // WebElement product=driver.findElement(By.xpath("//button[text()='Add to cart']"));
            // product.click();
            // }
            // }
            Thread.sleep(2000);
            List<WebElement> resultsBtn =
                    driver.findElements(By.xpath("//button[text()='Add to cart']"));
            for (int i = 0; i < results.size(); i++) {
                WebElement we = results.get(i);
                String str = we.getText();
                if (str.contains(text)) {
                    Thread.sleep(2000);
                    WebElement btn = resultsBtn.get(i);
                    Thread.sleep(1000);
                    btn.click();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void clickCheckout() {
        // Thread.sleep(2000);
        WebElement checkOut = driver.findElement(By.xpath("//button[text()='Checkout']"));

        checkOut.click();
    }

    public void changeProductQuantityinCart(String str, int count) {
        try {
            List<WebElement> cartProducts =
                    driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']"));
            for (int i = 0; i < cartProducts.size(); i++) {
                WebElement parent = cartProducts.get(i);
                WebElement product = parent.findElement(By.xpath("./div[1]"));
                String tittle = product.getText();
                if (tittle.equals(str)) {
                    while (true) {
                        WebElement actualCount =
                                parent.findElement(By.xpath(".//div[@data-testid='item-qty']"));
                        String actualCountText = actualCount.getText();
                        int actualCountInt = Integer.parseInt(actualCountText);
                        if (count > actualCountInt) {
                            WebElement plus = parent
                                    .findElement(By.xpath(".//*[@data-testid='AddOutlinedIcon']"));
                            plus.click();
                            WebDriverWait wait = new WebDriverWait(driver, 5);
                            // wait.until(ExpectedConditions.textToBe(By.xpath(".//*[@data-testid='AddOutlinedIcon']"),
                            // String.valueOf(actualCountInt+1)));
                            // it is always going to pick first element so we use parent
                            wait.until(ExpectedConditions.textToBePresentInElement(
                                    parent.findElement(By.xpath(".//div[@data-testid='item-qty']")),
                                    String.valueOf(actualCountInt + 1)));
                        } else if (count < actualCountInt) {
                            WebElement minus = parent.findElement(
                                    By.xpath(".//*[@data-testid='RemoveOutlinedIcon']"));
                            minus.click();
                            WebDriverWait wait = new WebDriverWait(driver, 5);
                            wait.until(ExpectedConditions.textToBePresentInElement(
                                parent.findElement(By.xpath(".//div[@data-testid='item-qty']")),
                                String.valueOf(actualCountInt - 1)));
                        } else if (count == actualCountInt) {
                            break;
                        }
                        // Thread.sleep(3000);
                    }
                }
            }
            // String actualCount=product.
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {};
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(
                        cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}

