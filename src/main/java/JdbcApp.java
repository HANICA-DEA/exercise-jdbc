import com.mysql.cj.jdbc.JdbcConnection;
import nl.han.aim.oose.dea.datasource.ItemDao;
import nl.han.aim.oose.dea.datasource.exceptions.ItemNotFoundException;
import nl.han.aim.oose.dea.datasource.util.DatabaseProperties;
import nl.han.aim.oose.dea.datasource.util.MySqlDatabaseProperties;
import nl.han.aim.oose.dea.datasource.util.SqlServerDatabaseProperties;
import nl.han.aim.oose.dea.domain.Item;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcApp {
    public static void main(String[] args) {
        System.out.println("Hello JDBC");

        var useSqlServer = false;
        DatabaseProperties dbProperties;
        if (useSqlServer) {
            dbProperties = new SqlServerDatabaseProperties();
        } else {
            dbProperties = new MySqlDatabaseProperties();
        }
        var app = new JdbcApp();
        app.log("Connection string: " + dbProperties.getConnectionString());
        app.log("Driver: " + dbProperties.getDriver());

        var itemDao = new ItemDao(dbProperties, app.logger);
        var items = itemDao.findAll();

        // TODO: Magic number 'test sku'
        var testSku = "test sku";
        var testItem = new Item(testSku, "test category", "test titel");
        itemDao.create(testItem);
        var findTestSKUItem = itemDao.readItem(testSku);

        app.log("Na toevoegen item met sku '" + testSku +"', gevonden: " + findTestSKUItem.getSku().equals(testSku));

        testItem.setCategory("test category 2");
        testItem.setTitle("test titel 2");

        itemDao.update(testItem);
        var findTestSKUItem2 = itemDao.readItem(testSku);
        app.log("Na wijzigen item met sku '" + testSku +"', waarde: " + findTestSKUItem2.toString());

        itemDao.delete(testSku);
        app.log("Alle records na verwijderen item met sku '" + testSku + "'");

        items.forEach(i -> app.log(i));
    }

    private Logger logger;

    public JdbcApp() {
        logger = Logger.getLogger(getClass().getName());
    }
    private void log(String s) {
        logger.log(Level.INFO, s);
    }

    private void log(Object o) {
        logger.log(Level.INFO, o.toString());
    }
}
