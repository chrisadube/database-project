/**
 * DatabaseCredentials.java
 */

public class DatabaseCredentials {
    String username, password;

    public DatabaseCredentials() {
        this.username = "";
        this.password = "";
    }
    
    public DatabaseCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters, Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
