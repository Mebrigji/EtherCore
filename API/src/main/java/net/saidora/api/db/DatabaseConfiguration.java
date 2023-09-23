package net.saidora.api.db;

public class DatabaseConfiguration {

    private final String host, username, password, table;
    private final int port;
    private final boolean ssl;

    public DatabaseConfiguration(String host, String username, String password, String table, int port, boolean ssl) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.table = table;
        this.port = port;
        this.ssl = ssl;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTable() {
        return table;
    }

    public int getPort() {
        return port;
    }

    public boolean isSsl() {
        return ssl;
    }
}
