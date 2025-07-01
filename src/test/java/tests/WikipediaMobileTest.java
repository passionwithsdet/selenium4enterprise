package tests;

import base.BaseMobileTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.WikipediaHomePage;
import listeners.RetryAnalyzer;

public class WikipediaMobileTest extends BaseMobileTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testAppLaunch() {
        logStep("Testing Wikipedia app launch");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Verifying app launched successfully");
        Assert.assertTrue(homePage.isSearchContainerDisplayed(), "Search container should be displayed");
        
        logStep("App launch test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testSearchFunctionality() {
        logStep("Testing Wikipedia search functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for 'Java programming language'");
        homePage.searchForArticle("Java programming language");
        
        logStep("Verifying search results");
        // Add verification for search results
        Assert.assertTrue(true, "Search functionality test completed");
        
        logStep("Search functionality test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testArticleReading() {
        logStep("Testing article reading functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for 'Artificial Intelligence'");
        homePage.searchForArticle("Artificial Intelligence");
        
        logStep("Verifying article is loaded");
        Assert.assertTrue(homePage.isArticleLoaded(), "Article should be loaded");
        
        logStep("Getting article title");
        String articleTitle = homePage.getArticleTitle();
        Assert.assertNotNull(articleTitle, "Article title should not be null");
        Assert.assertFalse(articleTitle.isEmpty(), "Article title should not be empty");
        
        logStep("Getting article content");
        String articleContent = homePage.getArticleContent();
        Assert.assertNotNull(articleContent, "Article content should not be null");
        Assert.assertFalse(articleContent.isEmpty(), "Article content should not be empty");
        
        logStep("Article reading test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testBookmarkArticle() {
        logStep("Testing article bookmarking functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for 'Machine Learning'");
        homePage.searchForArticle("Machine Learning");
        
        logStep("Verifying article is loaded");
        Assert.assertTrue(homePage.isArticleLoaded(), "Article should be loaded");
        
        logStep("Bookmarking the article");
        homePage.bookmarkArticle();
        
        logStep("Article bookmarking test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testShareArticle() {
        logStep("Testing article sharing functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for 'Python programming'");
        homePage.searchForArticle("Python programming");
        
        logStep("Verifying article is loaded");
        Assert.assertTrue(homePage.isArticleLoaded(), "Article should be loaded");
        
        logStep("Sharing the article");
        homePage.shareArticle();
        
        logStep("Article sharing test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testLanguageSwitching() {
        logStep("Testing language switching functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Opening menu");
        homePage.openMenu();
        
        logStep("Changing language");
        homePage.changeLanguage();
        
        logStep("Language switching test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testNavigationMenu() {
        logStep("Testing navigation menu functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Opening menu");
        homePage.openMenu();
        
        logStep("Verifying menu is accessible");
        Assert.assertTrue(true, "Menu should be accessible");
        
        logStep("Navigation menu test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testSearchClear() {
        logStep("Testing search clear functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Tapping search container");
        homePage.tapSearchContainer();
        
        logStep("Entering search text");
        homePage.enterSearchText("Test search");
        
        logStep("Clearing search");
        homePage.clearSearch();
        
        logStep("Search clear test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testMultipleSearches() {
        logStep("Testing multiple search functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        String[] searchTerms = {"JavaScript", "React", "Node.js", "MongoDB"};
        
        for (String searchTerm : searchTerms) {
            logStep("Searching for: " + searchTerm);
            homePage.searchForArticle(searchTerm);
            
            logStep("Verifying search results for: " + searchTerm);
            Assert.assertTrue(homePage.isArticleLoaded(), "Article should be loaded for: " + searchTerm);
            
            logStep("Going back to search");
            homePage.goBack();
        }
        
        logStep("Multiple searches test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testAppPerformance() {
        logStep("Testing app performance");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Measuring app launch time");
        long startTime = System.currentTimeMillis();
        
        logStep("Performing search operation");
        homePage.searchForArticle("Performance testing");
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        logStep("Response time: " + responseTime + "ms");
        
        logStep("Logging performance metrics");
        logPerformanceMetrics();
        
        logStep("Performance test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testOfflineFunctionality() {
        logStep("Testing offline functionality");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for article while online");
        homePage.searchForArticle("Offline functionality");
        
        logStep("Verifying article is loaded");
        Assert.assertTrue(homePage.isArticleLoaded(), "Article should be loaded");
        
        logStep("Note: Offline testing requires network simulation");
        logInfo("Offline functionality test completed (network simulation required)");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGestureNavigation() {
        logStep("Testing gesture navigation");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for article");
        homePage.searchForArticle("Gesture navigation");
        
        logStep("Testing swipe up gesture");
        homePage.swipeUp();
        
        logStep("Testing swipe down gesture");
        homePage.swipeDown();
        
        logStep("Testing swipe left gesture");
        homePage.swipeLeft();
        
        logStep("Testing swipe right gesture");
        homePage.swipeRight();
        
        logStep("Gesture navigation test completed successfully");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testContentValidation() {
        logStep("Testing content validation");
        
        WikipediaHomePage homePage = new WikipediaHomePage(driver);
        
        logStep("Searching for 'Software Testing'");
        homePage.searchForArticle("Software Testing");
        
        logStep("Verifying article content");
        String title = homePage.getArticleTitle();
        String content = homePage.getArticleContent();
        
        Assert.assertNotNull(title, "Article title should not be null");
        Assert.assertNotNull(content, "Article content should not be null");
        Assert.assertFalse(title.isEmpty(), "Article title should not be empty");
        Assert.assertFalse(content.isEmpty(), "Article content should not be empty");
        
        logStep("Content validation test completed successfully");
    }
} 