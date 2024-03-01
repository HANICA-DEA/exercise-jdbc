package nl.han.aim.oose.dea.datasource.util;

import java.io.IOException;
import java.util.Properties;

public abstract class DbProperties {
    private final static String CONNECTIONSTRING = "connectionstring";
    private final static String DRIVER = "driver";
    private final static String USER = "user";
    private final static String PASSWORD = "password";


    protected Properties properties;

    public String getConnectionString() {
        return properties.getProperty(CONNECTIONSTRING);
    }

    public String getDriver() {
        return properties.getProperty(DRIVER);
    }

    public DbProperties(String propertiesFileName) {
        properties = new Properties();
        System.out.println("Loading '" + propertiesFileName + "'");
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Laad de in de `.properties` ingesteld database driver/klasse met onderstaand stukje magisch `Class.forName`
        // code, zodat DAO-objecten de database connectie kunnen opvragen.
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUser() {
        return properties.getProperty(USER);
    }

    public String getPassword() {
        return properties.getProperty(PASSWORD);
    }
}
