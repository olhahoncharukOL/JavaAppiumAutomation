import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Test;

/**
 * Created by User on 05.12.2018.
 Написать тест, который:
 1. Сохраняет две статьи в одну папку
 2. Удаляет одну из статей
 3. Убеждается, что вторая осталась
 4. Переходит в неё и убеждается, что title совпадает
 */
public class FourthTest extends CoreTestCase {

    private static String searchText = "MayDay";
    private static String readingBookName = "My first list";

    private SearchPageObject searchPageObject;
    private ArticlePageObject articlePageObject;
    private NavigationUI navigationUI;
    private MyListsPageObject myListsPageObject;

    @Test
    public void testTwoArticlesSaving() {
        searchPageObject = new SearchPageObject(driver);
        articlePageObject = new ArticlePageObject(driver);
        navigationUI = new NavigationUI(driver);
        myListsPageObject = new MyListsPageObject(driver);

        searchPageObject.findSearchResultsAndClickOneOfThem(searchText,0);
        String firstArticleTitle = articlePageObject.saveArticleTitle();
        articlePageObject.addArticleToReadingListFirstTime(readingBookName);

        searchPageObject.findSearchResultsAndClickOneOfThem(searchText,2);
        String secondArticleTitle =  articlePageObject.saveArticleTitle();
        articlePageObject.addArticleToReadingList(readingBookName);

        navigationUI.clickMyLists();

        myListsPageObject.openReadingListFolderByName(readingBookName);
        myListsPageObject.SwipeByArticleAndDelete(secondArticleTitle);

        myListsPageObject.openArticleInReadingList(firstArticleTitle);
        String actualArticleTitle = articlePageObject.saveArticleTitle();

       assertTrue("titles not equal",actualArticleTitle.equals(firstArticleTitle));
    }
}
