package projectManagementEntity;

public class Account {
    private String username;
    private String password;
   
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
