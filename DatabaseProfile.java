/**
 * DatabaseProfile
 * 
 * Contains database information:
 * Host (IP address, port, and database name)
 */

public class DatabaseProfile {
    String host, port, name;

    public DatabaseProfile() {
        this.host = "";
        this.port = "";
        this.name = "";
    }
    
    public DatabaseProfile(String host, String port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    // Getters, Setters
    public void setHost(String host) { this.host = host; }
    public String getHost() { return this.host; }
    public void setPort(String port) { this.port = port; }
    public String getPort() { return this.port; }
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    // toString
    @Override
    public String toString() { return host + ":" + port + "/" + name; }
}
