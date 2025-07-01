package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtil;

import java.time.Duration;

public class WikipediaHomePage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    public WikipediaHomePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Search elements
    @AndroidFindBy(id = "search_container")
    @iOSXCUITFindBy(accessibility = "Search Wikipedia")
    private WebElement searchContainer;

    @AndroidFindBy(id = "search_src_text")
    @iOSXCUITFindBy(accessibility = "Search Wikipedia")
    private WebElement searchInput;

    @AndroidFindBy(id = "search_close_btn")
    @iOSXCUITFindBy(accessibility = "Clear search")
    private WebElement searchCloseButton;

    // Navigation elements
    @AndroidFindBy(id = "nav_menu_button")
    @iOSXCUITFindBy(accessibility = "Menu")
    private WebElement menuButton;

    @AndroidFindBy(id = "main_toolbar")
    @iOSXCUITFindBy(accessibility = "Toolbar")
    private WebElement toolbar;

    // Article elements
    @AndroidFindBy(id = "article_title")
    @iOSXCUITFindBy(accessibility = "Article title")
    private WebElement articleTitle;

    @AndroidFindBy(id = "article_content")
    @iOSXCUITFindBy(accessibility = "Article content")
    private WebElement articleContent;

    // Language elements
    @AndroidFindBy(id = "language_button")
    @iOSXCUITFindBy(accessibility = "Language")
    private WebElement languageButton;

    // Bookmark elements
    @AndroidFindBy(id = "bookmark_button")
    @iOSXCUITFindBy(accessibility = "Bookmark")
    private WebElement bookmarkButton;

    // Share elements
    @AndroidFindBy(id = "share_button")
    @iOSXCUITFindBy(accessibility = "Share")
    private WebElement shareButton;

    // Methods
    public void tapSearchContainer() {
        LogUtil.info("Tapping search container");
        wait.until(ExpectedConditions.elementToBeClickable(searchContainer)).click();
    }

    public void enterSearchText(String searchText) {
        LogUtil.info("Entering search text: " + searchText);
        wait.until(ExpectedConditions.elementToBeClickable(searchInput)).clear();
        searchInput.sendKeys(searchText);
    }

    public void clearSearch() {
        LogUtil.info("Clearing search");
        if (searchCloseButton.isDisplayed()) {
            searchCloseButton.click();
        }
    }

    public void openMenu() {
        LogUtil.info("Opening menu");
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
    }

    public String getArticleTitle() {
        LogUtil.info("Getting article title");
        return wait.until(ExpectedConditions.visibilityOf(articleTitle)).getText();
    }

    public String getArticleContent() {
        LogUtil.info("Getting article content");
        return wait.until(ExpectedConditions.visibilityOf(articleContent)).getText();
    }

    public void changeLanguage() {
        LogUtil.info("Changing language");
        wait.until(ExpectedConditions.elementToBeClickable(languageButton)).click();
    }

    public void bookmarkArticle() {
        LogUtil.info("Bookmarking article");
        wait.until(ExpectedConditions.elementToBeClickable(bookmarkButton)).click();
    }

    public void shareArticle() {
        LogUtil.info("Sharing article");
        wait.until(ExpectedConditions.elementToBeClickable(shareButton)).click();
    }

    public void searchForArticle(String searchText) {
        LogUtil.info("Searching for article: " + searchText);
        tapSearchContainer();
        enterSearchText(searchText);
        // Press Enter using Actions
        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.sendKeys(org.openqa.selenium.Keys.ENTER).perform();
    }

    public boolean isSearchContainerDisplayed() {
        return searchContainer.isDisplayed();
    }

    public boolean isArticleLoaded() {
        return articleTitle.isDisplayed() && articleContent.isDisplayed();
    }

    // Gesture methods
    public void swipeUp() {
        LogUtil.info("Swiping up");
        // Implement swipe up gesture
    }

    public void swipeDown() {
        LogUtil.info("Swiping down");
        // Implement swipe down gesture
    }

    public void swipeLeft() {
        LogUtil.info("Swiping left");
        // Implement swipe left gesture
    }

    public void swipeRight() {
        LogUtil.info("Swiping right");
        // Implement swipe right gesture
    }

    // Navigation methods
    public void goBack() {
        LogUtil.info("Going back");
        driver.navigate().back();
    }

    public void goHome() {
        LogUtil.info("Going home");
        // Navigate to home screen
    }
} 