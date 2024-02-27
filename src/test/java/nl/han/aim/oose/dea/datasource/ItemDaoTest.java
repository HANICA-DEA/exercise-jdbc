package nl.han.aim.oose.dea.datasource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import nl.han.aim.oose.dea.domain.Item;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import nl.han.aim.oose.dea.datasource.exceptions.ItemNotFoundException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemDaoTest {

    private ItemDao sut;

    private List<Item> testItems;

    @BeforeAll
    void beforeAll() {

        // Connect to H2 in-memory database.
        final String CREATE_DB_SCRIPT = "src/sql/create-script.sql";

        var dbProperties = new H2DatabaseProperties();
        var connectionString = dbProperties.getConnectionString();
        var dbUser = dbProperties.getUser();
        var dbPassword = dbProperties.getPassword();
        var logger = Logger.getLogger(getClass().getName());
        logger.info("Db user '" + dbUser + "' en password: '" + dbPassword + "'");
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

        // Assert.
        Assertions.assertEquals(actual, testItem4);
    }

    @Test
    void readExistingItem() {
    }

    @Test
    void readNotExistingItem() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> sut.readItem("testsku"));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}