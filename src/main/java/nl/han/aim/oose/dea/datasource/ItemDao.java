package nl.han.aim.oose.dea.datasource;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcConnection;
import nl.han.aim.oose.dea.datasource.exceptions.ItemNotFoundException;
import nl.han.aim.oose.dea.datasource.util.DatabaseProperties;
import nl.han.aim.oose.dea.domain.Item;

public class ItemDao {

    private DatabaseProperties dbProperties;

    private Logger logger;

    public ItemDao(DatabaseProperties dbProperties, Logger logger) {
        this.dbProperties = dbProperties;
        this.logger = logger;
    }

    Connection dbConnection = null;

    private Connection getConnection() throws SQLException {
        if (dbConnection ==null) {
            dbConnection = DriverManager.getConnection(dbProperties.getConnectionString(), dbProperties.getUser(), dbProperties.getPassword());
            dbConnection.setAutoCommit(false);
        }
        return dbConnection;
    }
    public List<Item> findAll() {
        var resultItems = new ArrayList<Item>();
        try {
            ResultSet rsItems = null;
            var st = getConnection().prepareStatement("SELECT * FROM Items;");
            rsItems = st.executeQuery();
            while (rsItems.next() != false) {
                String sku = rsItems.getString("sku");
                String category = rsItems.getString("category");
                String title = rsItems.getString("title");
                resultItems.add(new Item(sku, category, title));
            }
        } catch (SQLException e) {
            logger.log(Level.INFO, "Database error: " + e);
            throw new RuntimeException(e);
        }
        return resultItems;
    }

    public Item readItem(String sku) throws ItemNotFoundException {
        // Lezen item uit database.
        Item resultItem = null;
        try {
            var st = getConnection().prepareStatement("select * from Items where sku = ?");
            st.setString(1, sku);
            var result = st.executeQuery();
            // Verplaats cursor naar de eerste rij (als aanwezig, anders exceptie gooien).
            if (!result.next()) {
                throw new ItemNotFoundException("Item met sku '" + sku + "' niet gevonden in.");
            }
            resultItem = new Item(
                    result.getString("sku"),
                    result.getString("category"),
                    result.getString("title"));
        } catch (SQLException e) {
            logger.warning("Error bij ophalen item met sku " + sku + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return resultItem;
    }

    /**
     * Toevoegen item in database.
     */
    public Item create(Item item) {
        try {
            var connection = getConnection();
            var st = getConnection().prepareStatement("insert into Items values(?, ?, ?);");
            st.setString(1, item.getSku());
            st.setString(2, item.getCategory());
            st.setString(3, item.getTitle());
            var result = st.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning("Error bij aanmaken item " + item.toString() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return item;
    }

    /**
     * De opgave noemt 'insert', maar dat komt overeen met create,
     * dus ik neem aan dat de opgave de missende 'Read' uit CRUD bedoelt.
     * @param sku
     * @return
     */
    public void update(Item item) {
        try {
            var connection = getConnection();
            var st = connection.prepareStatement("update Items set category=?, title=? where sku=?;");
            st.setString(1, item.getCategory());
            st.setString(2, item.getTitle());
            st.setString(3, item.getSku());
            st.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning("Error bij updaten item met sku " + item.getSku() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(String sku) {
        try {
            var connection = getConnection();
            var st = connection.prepareStatement("delete from Items where sku=?;");
            st.setString(1, sku);
            st.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning("Error bij updaten item met sku " + sku + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
