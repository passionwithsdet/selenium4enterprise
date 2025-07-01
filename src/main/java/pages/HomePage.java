package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage {
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = ".card-body h5")
    List<WebElement> cards;
    public void clickOnCard(String cardName) {
        for (WebElement card : cards) {
            if (card.getText().equalsIgnoreCase(cardName)) {
                card.click();
                return;
            }
        }
    }
}

