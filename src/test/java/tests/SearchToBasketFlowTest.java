package tests;


import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.ExcelUtil;
import utils.TextUtil;

@Feature("Search, Favorites and Cart Management")
public class SearchToBasketFlowTest extends BaseTest {

        private static final Logger log = LoggerFactory.getLogger(SearchToBasketFlowTest.class);
        private static final int TARGET_PAGE_NUMBER = 2;
        private static final int TARGET_PRODUCT_INDEX = 5;


        private HomePage homePage;
        private LoginPage loginPage;
        private ProductListPage productListPage;
        private ProductDetailPage productDetailPage;
        private FavoritesPage favoritesPage;
        private CartPage cartPage;

        @BeforeMethod(alwaysRun = true)
        public void initializePages() {

            log.info("İnitialize pages for the test.");
            homePage = new HomePage();
            loginPage = new LoginPage();
            productListPage = new ProductListPage();
            productDetailPage = new ProductDetailPage();
            favoritesPage = new FavoritesPage();
            cartPage = new CartPage();
        }



        @Story("Search a product, add it to favorites, move it to cart and remove it")
        @Description(
                "The user searches for a product, adds it to favorites, moves it to the cart, and then removes it from the cart, verifying each step."
        )
        @Severity(SeverityLevel.CRITICAL)
        @Test(description = "Search, Favorite and Cart Flow Test",
              groups = {"e2e"})
        public void shouldAddFavoriteProductToCartAndRemoveIt() {
            final String[] productIdHolder = new String[1];

            Allure.step(
                    "1. Open Trendyol home page and verify it is displayed",
                    () -> {
                        homePage.open();
                        Assert.assertTrue(
                                homePage.isLoginButtonDisplayed(),
                                "Home page is not displayed correctly, login button is not visible."
                        );
                        log.info("Home page is opened and login button is visible.");
                        homePage.closeGenderPopup().closeLanguagePopup();

                    }
            );

            Allure.step(
                    "2. Login with valid credentials: " ,
                    () -> {

                        log.info("Login step is starting.");
                        homePage.clickLoginButton();
                        log.info("Login button clicked, navigating to login page.");


                        loginPage.validLoginWithEmail(ExcelUtil.getValue("loginEmail"),ExcelUtil.getValue("loginPassword"));

                        Assert.assertTrue(
                                homePage.isLoggedIn(), "Login failed, user is not logged in.");

                        log.info("Kullanıcı başarıyla giriş yaptı.");

                    }

            );

            Allure.step(
                    "Precondition: Clear existing cart items and favorite products",
                    () -> {
                        cartPage.navigateCart();
                        log.info("User is at card page");
                        cartPage.deleteAllProducts();
                        Assert.assertFalse(cartPage.hasProducts(), "Empty card precondition failed.");
                        log.info("All cards removed");

                        favoritesPage.navigateFavourites();
                        log.info("User is at favourites page");
                        favoritesPage.removeAllFavourites();
                        Assert.assertFalse(favoritesPage.hasFavouriteActionIcon(), "Empty favourites precondition failed.");
                        log.info("All favourites removed");

                    }
            );





            Allure.step( "3. Type '" + ExcelUtil.getValue("searchKey") + "' in the search field and click the search button",
                    () -> {
                        homePage.searchFromSuggestion(ExcelUtil.getValue("searchKey"));
                        log.info("Search for '{}' is performed.", ExcelUtil.getValue("searchKey"));

                    }

            );

            Allure.step(

                    "4. Select main category '" + ExcelUtil.getValue("mainCategory") + "' and subcategory '" + ExcelUtil.getValue("subCategory") + "'",

                    () -> {

                        log.info( "Selecting main category '{}' and subcategory '{}'",  ExcelUtil.getValue("mainCategory"), ExcelUtil.getValue("subCategory"));
                        productListPage.selectMatchesSingleItemFromCategoryFilter(ExcelUtil.getValue("subCategory"));

                        String expectedTitle =  "%s %s".formatted( TextUtil.capitalizeFirstLetter(ExcelUtil.getValue("searchKey")), ExcelUtil.getValue("subCategory"));
                        String actualTitle = productListPage.getTextOfSearchResultTitle();
                        Assert.assertEquals(actualTitle, expectedTitle,
                                "Search result title mismatch.%nExpected: %s%nActual: %s".formatted(expectedTitle, actualTitle));



                        log.info( "Subcategory successfully selected - : {}", ExcelUtil.getValue("subCategory"));

                    }

            );


            Allure.step(

                    "5. Navigate to the " + TARGET_PAGE_NUMBER + ". page of the search results",

                    () -> {

                        log.info( "Navigating to the {}. page of the search results.", TARGET_PAGE_NUMBER);

                        productListPage.navigateNthPage(TARGET_PAGE_NUMBER);
                        Assert.assertFalse(productListPage.isNoResultFoundDisplayed(), "User is at valid search results page, but no results are found.");

                    }

            );

            Allure.step(
                    "6. Select the " + TARGET_PRODUCT_INDEX + ". product from the search results",
                    () -> {
                        productIdHolder[0]=
                                productListPage.clickNthProductAndGetProductID(
                                        TARGET_PRODUCT_INDEX
                                );

                        log.info(
                                "Navigated to {}. product detail page. Product ID: {}",
                                TARGET_PRODUCT_INDEX,
                                productIdHolder[0] );

                        Assert.assertTrue(
                                productDetailPage.isUserNavigatedTheRightProductDetailPage(
                                        productIdHolder[0]
                                ),
                                "User is not navigated to the correct product detail page. "
                                        + "Expected Product ID: "
                                        + productIdHolder[0]
                        );

                        log.info(
                                "User is navigated to the correct product detail page. Product ID: {}",
                                productIdHolder[0]
                        );

                        return productIdHolder[0];
                    }
            );

            Allure.step(

                    "7. Press Favorite button to add the product to favorites",

                    () -> {
                        Assert.assertFalse(productDetailPage.isFavoriteButtonSelected());
                        log.info( "Pressing Favorite button to add the product to favorites");
                        productDetailPage.clickFavoriteButton();
                        log.info("Pressed Favorite button to add the product to favorites");
                        Assert.assertTrue(productDetailPage.isFavoriteButtonSelected(), "Favorite button is not selected after clicking. The product may not have been added to favorites.");
                        log.info("The product is successfully added to favorites, Favorite button is selected.");

                    }

            );


            Allure.step(

                    "8.Navigates Favoriler ( Favorites) and verifies that the product is listed in the favorites",

                    () -> {

                        log.info("Navigating to Favorites page to verify the product is listed in favorites.");
                        homePage.clickFavoriteButton();
                        Assert.assertTrue(favoritesPage.isAtFavoritePage(), "User is not navigated to the Favorites page. The product may not have been added to favorites.");
                        log.info("Favorites page is opened successfully. Verifying the product is listed in favorites.");
                    }
            );


            Allure.step(

                    "9. Validate the favorite product is displayed in the favorites list",

                    () -> {

                        Assert.assertTrue( favoritesPage.isTheProductInFavorites(productIdHolder[0]),

                                "The product with ID " + productIdHolder[0] + " is not found in the favorites list. The product may not have been added to favorites."
                        );

                        log.info( "The product with ID {} is successfully found in the favorites list.",productIdHolder[0]);

                    }

            );


            Allure.step(

                    "10.The favorite product is added to the cart by clicking the 'Sepete Ekle' (Add to Cart) button",

                    () -> {

                        favoritesPage.clickAddBasketButtonWithID(productIdHolder[0]);
                        log.info("Clicked 'Sepete Ekle' (Add to Cart) button for the product with ID: {}", productIdHolder[0]);
                        Assert.assertTrue(favoritesPage.isSuccessAlertDisplayed(), "The success alert for adding the product to the cart is not displayed. The product may not have been added to the cart.");
                        log.info("The product with ID {} is successfully added to the cart, and the success alert is displayed.", productIdHolder[0]);

                    }

            );


            Allure.step(

                    "11. Open the cart page and verify that the product is displayed in the cart",
                    () -> {
                        log.info("Navigating to the cart page to verify the product is displayed in the cart.");
                        homePage.clickBasketIcon();
                        Assert.assertTrue(cartPage.isUserNavigatedTheCartPage(), "User is not navigated to the Cart page. The product may not have been added to the cart.");
                        log.info("Cart page is opened successfully. Verifying the product is displayed in the cart.");
                    }

            );



            Allure.step(

                    "12. Validate the product is displayed in the cart than delete it from the cart",

                    () -> {
                        log.info("Validating the product is displayed in the cart.");
                        Assert.assertTrue(cartPage.isProductDisplayedOnBasket(productIdHolder[0]), "The product with ID " + productIdHolder[0] + " is not found in the cart. The product may not have been added to the cart.");
                        log.info("Product with ID {} is successfully found in the cart.", productIdHolder[0]);

                        cartPage.clickDeleteButtonWithProductID(productIdHolder[0]);
                        log.info("Clicked delete button for the product with ID: {}", productIdHolder[0]);

                        Assert.assertFalse(cartPage.isProductDisplayedOnBasket(productIdHolder[0]), "The product with ID " +productIdHolder[0] + " is still found in the cart after, deletion. The product may not have been removed from the cart.");
                        log.info("Product with ID {} is successfully removed from the cart.", productIdHolder[0]);
                    }

            );

            log.info("Search  to Basket flow test completed successfully for product ID: {}", productIdHolder[0] );

        }



    }

