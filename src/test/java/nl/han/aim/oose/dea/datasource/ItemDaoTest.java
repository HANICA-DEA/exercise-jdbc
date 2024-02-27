package nl.han.aim.oose.dea.datasource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import nl.han.aim.oose.dea.datasource.exceptions.DuplicateItemException;
import nl.han.aim.oose.dea.datasource.util.DatabaseProperties;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import nl.han.aim.oose.dea.domain.Item;
import nl.han.aim.oose.dea.datasource.exceptions.ItemNotFoundException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemDaoTest {

    private ItemDao sut;

    private List<Item> testItems;

    private DatabaseProperties dbProperties;

    private Logger logger;

    final String CREATE_DB_SCRIPT = "src/sql/create-script.sql";

    @BeforeAll
    void beforeAll() {
        // Connect met de H2 in-memory database.
        logger = Logger.getLogger(getClass().getName());

        logger.info("Initialiseren test dataset.");
        dbProperties = new H2DatabaseProperties();

        var connectionString = dbProperties.getConnectionString();
        var dbUser = dbProperties.getUser();
        var dbPassword = dbProperties.getPassword();
        var driver = dbProperties.getDriver();
        logger.info("Db user '" + dbUser + "' en password: '" + dbPassword + "', driver: '" + driver + "'");
        logger.info("Aanmaken H2 database met schema uit '" + CREATE_DB_SCRIPT + "' en connectionstring: '" + connectionString + "'");

        try {
            var dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
            RunScript.execute(dbConnection, new FileReader(CREATE_DB_SCRIPT, StandardCharsets.UTF_8));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sut = new ItemDao(dbProperties, logger);

        testItems = new ArrayList<Item>(3);
        testItems.add(new Item("test sku1", "test category 1", "test title 1"));
        testItems.add(new Item("test sku2", "test category 2", "test title 2"));
        testItems.add(new Item("test sku3", "test category 3", "test title 3"));
        sut.create(testItems.get(0));
        sut.create(testItems.get(1));
        sut.create(testItems.get(2));
    }

    @BeforeEach
    void beforeEach() {
    }

    @Test
    void findAllReturnsCorrectTestItems() {
        // Arrange.
        var expected = testItems;

        // Act
        var actual = sut.findAll();

        // Assert.
        Assertions.assertEquals(actual.size(), 3);
        Assertions.assertEquals(expected.get(0), actual.get(0));
        Assertions.assertEquals(expected.get(1), actual.get(1));
        Assertions.assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    void create() {
        // Arrange.
        var testItem4 = new Item("test sku 4", "test category 4", "test title 4");

        // Act
        var actual = sut.create(testItem4);
        var actualReadBack = sut.readItem(testItem4.getSku());

        // Assert.
        Assertions.assertEquals(actual, testItem4);

        // TODO: Dit test eigenlijk readItem ook...
        Assertions.assertEquals(actualReadBack, testItem4);
    }

    @Test
    void createDuplicateItemThrowsException() {
        // Arrange.
        var duplicateItem = testItems.get(0);

        // Act.
        Assertions.assertThrows(DuplicateItemException.class,
                () -> sut.create(duplicateItem));
    }

    @Test
    void createItemWithoutSkuThrowsException() {
        // Arrange.
        var invalidItem = new Item("", null, "test title 3");

        // Act.
        Assertions.assertThrows(DuplicateItemException.class,
                () -> sut.create(invalidItem));
    }


    @Test
    void readExistingItem() {
        // Arrange.
        var expected = testItems.get(0);

        // Act.
        var actual = sut.readItem(expected.getSku());

        // Assert.
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readNonExistingItem() {
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> sut.readItem("testsku"));
    }

    @Test
    void updateExistingItem() {
        // Arrange.
        var itemToChange = testItems.get(1);
        itemToChange.setTitle("new title");
        itemToChange.setCategory("new category");
        var expected = new Item(itemToChange.getSku(), itemToChange.getCategory(), itemToChange.getTitle());

        // Act.
        sut.update(itemToChange);
        var actual = sut.readItem(itemToChange.getSku());

        // Assert.
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateNonExistingItemThrowsException() {
        // Arrange.
        var itemToChange = new Item("new sku", "new category", "new title");

        // Act + Assert.
        Assertions.assertThrows(ItemUpdateException.class,
                () -> sut.update(itemToChange));
    }

    @Test
    void updateWithEmptyCategoryThrowsException() {
        // Arrange.
        var itemToChange = new Item(null, null, "new title");

        // Act + Assert.
        Assertions.assertThrows(ItemUpdateException.class,
                () -> sut.update(itemToChange));
    }
    @Test
    void updateWithEmptySkuThrowsException() {
        // Arrange.
        var itemToChange = new Item(null, "category", "new title");

        // Act + Assert.
        Assertions.assertThrows(ItemUpdateException.class,
                () -> sut.update(itemToChange));
    }

    @Test
    void delete() {
        // Arrange.
        var itemToDelete = testItems.get(2);

        // Act.
        sut.delete(itemToDelete.getSku());

        // Assert.
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> sut.readItem(itemToDelete.getSku())
        );
    }
}