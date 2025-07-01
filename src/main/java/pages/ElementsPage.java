package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtil;

import java.util.ArrayList;
import java.util.List;

public class ElementsPage {
    private WebDriver driver;
    private Actions actions;

    public ElementsPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // Alerts
    @FindBy(id = "alertButton")
    private WebElement alertButton;
    @FindBy(id = "timerAlertButton")
    private WebElement timerAlertButton;
    @FindBy(id = "confirmButton")
    private WebElement confirmButton;
    @FindBy(id = "promtButton")
    private WebElement promptButton;

    public void clickAlertButton() {
        alertButton.click();
    }
    public void clickTimerAlertButton() {
        timerAlertButton.click();
    }
    public void clickConfirmButton() {
        confirmButton.click();
    }
    public void clickPromptButton() {
        promptButton.click();
    }

    // Popups (using modal dialogs)
    @FindBy(css = ".modal-dialog")
    private WebElement modalDialog;
    @FindBy(css = ".modal-footer button")
    private List<WebElement> modalButtons;
    public boolean isModalDisplayed() {
        return modalDialog.isDisplayed();
    }
    public void closeModal() {
        for (WebElement btn : modalButtons) {
            if (btn.getText().equalsIgnoreCase("Close") || btn.getText().equalsIgnoreCase("OK")) {
                btn.click();
                break;
            }
        }
    }

    // Multiple Windows/Tabs
    @FindBy(id = "tabButton")
    private WebElement tabButton;
    @FindBy(id = "windowButton")
    private WebElement windowButton;
    public void openNewTab() {
        tabButton.click();
    }
    public void openNewWindow() {
        windowButton.click();
    }
    public void switchToNewWindow() {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(handles.size() - 1));
    }
    public void switchToMainWindow() {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(0));
    }

    // Actions (drag and drop, right click, double click)
    @FindBy(id = "draggable")
    private WebElement draggable;
    @FindBy(id = "droppable")
    private WebElement droppable;
    @FindBy(id = "rightClickBtn")
    private WebElement rightClickBtn;
    @FindBy(id = "doubleClickBtn")
    private WebElement doubleClickBtn;
    public void dragAndDrop() {
        actions.dragAndDrop(draggable, droppable).perform();
    }
    public void rightClick() {
        actions.contextClick(rightClickBtn).perform();
    }
    public void doubleClick() {
        actions.doubleClick(doubleClickBtn).perform();
    }

    // DropDown/Select
    @FindBy(id = "oldSelectMenu")
    private WebElement selectMenu;
    public void selectByVisibleText(String text) {
        Select select = new Select(selectMenu);
        select.selectByVisibleText(text);
    }
    public void selectByValue(String value) {
        Select select = new Select(selectMenu);
        select.selectByValue(value);
    }
    public void selectByIndex(int index) {
        Select select = new Select(selectMenu);
        select.selectByIndex(index);
    }
} 