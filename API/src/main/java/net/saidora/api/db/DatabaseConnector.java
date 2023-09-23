package net.saidora.api.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import net.saidora.api.db.api.DataObjectScanner;
import net.saidora.api.db.api.ItemSerializer;

import java.sql.Connection;
import java.util.*;

public class DatabaseConnector {

    private HikariDataSource dataSource;
    private final DatabaseGetter getter;

    protected final HikariConfig configuration;

    public DatabaseConnector(DatabaseConfiguration dataBaseConfiguration) {
        this.configuration = this.getHikariConfig(dataBaseConfiguration);
        this.dataSource = new HikariDataSource(this.configuration);
        this.getter = new DatabaseGetter(this);
    }

    public DatabaseGetter getGetter() {
        return getter;
    }

    private final Map<Class<?>, DataObjectScanner<?>> scannerMap = new HashMap<>();
    private final Set<ItemSerializer> itemSerializerSet = new HashSet<>();

    public void registerScanner(DataObjectScanner<?> scanner){
        scannerMap.put(scanner.getClazz(), scanner);
    }

    public <T> void registerSerializer(ItemSerializer<T> itemSerializer){
        itemSerializerSet.add(itemSerializer);
    }

    public ItemSerializer getSerializer(Class type){
        return itemSerializerSet.stream().filter(itemSerializer -> itemSerializer.supports(type)).findFirst().orElse(null);
    }

    public <T> void registerDataObjectToScan(Class<T> clazz){
        DataObjectScanner<T> scanner = new DataObjectScanner<>(clazz, this);
        registerScanner(scanner);
    }

    public Map<Class<?>, DataObjectScanner<?>> getScannerMap() {
        return scannerMap;
    }

    public static Connection connection;

    @SneakyThrows
    public Connection getConnection()  {
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    private HikariConfig getHikariConfig(DatabaseConfiguration dataBaseConfiguration) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=" + dataBaseConfiguration.isSsl(),
                dataBaseConfiguration.getHost(),
                dataBaseConfiguration.getPort(),
                dataBaseConfiguration.getTable()));
        hikariConfig.setUsername(dataBaseConfiguration.getUsername());
        hikariConfig.setPassword(dataBaseConfiguration.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
        hikariConfig.addDataSourceProperty("maintainTimeStats", "false");
        hikariConfig.addDataSourceProperty("autoReconnect", "true");
        return hikariConfig;
    }

    public <T> Optional<DataObjectScanner<T>> getScanner(Class<T> aClass) {
        return Optional.ofNullable((DataObjectScanner<T>) scannerMap.get(aClass));
    }

    public void reconnect() {
        this.dataSource = new HikariDataSource(this.configuration);
    }
}