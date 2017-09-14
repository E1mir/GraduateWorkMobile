package kryternext.graduatework.app.models;


public class UserAuth {
    private String username;
    private String password;

    public UserAuth(String username, String password) {
        this.username = username;
        this.password = encryptPass(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private String encryptPass(String password) {
        StringBuilder encryptedPassword = new StringBuilder("");
        for (char ch : password.toCharArray()) {
            int decryptedCharId = (int) ch;
            int encryptedCharId = decryptedCharId + 5;
            encryptedPassword.append((char) encryptedCharId);
        }
        return encryptedPassword.toString();
    }
}