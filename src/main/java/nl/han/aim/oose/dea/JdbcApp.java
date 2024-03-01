package nl.han.aim.oose.dea;

import nl.han.aim.oose.dea.datasource.ItemDao;
import nl.han.aim.oose.dea.datasource.util.DbProperties;
import nl.han.aim.oose.dea.datasource.util.MySqlDbProperties;
import nl.han.aim.oose.dea.datasource.util.SqlServerDbProperties;
import nl.han.aim.oose.dea.business.ItemDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcApp {
    public static void main(String[] args) {
        System.out.println("Hello JDBC");

        var useSqlServer = true;
        DbProperties dbProperties;
        if (useSqlServer) {
            dbProperties = new SqlServerDbProperties();
        } else {
            dbProperties = new MySqlDbProperties();
        }
        var app = new JdbcApp();
        app.log("Connection string: " + dbProperties.getConnectionString());
        app.log("Driver: " + dbProperties.getDriver());

        var itemDao = new ItemDao(dbProperties, app.logger);
        var items = itemDao.findAll();

        // TODO: Magic number 'test sku'
        var testSku = "test sku";
        var testItem = new ItemDTO(testSku, "test category", "test titel");
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
