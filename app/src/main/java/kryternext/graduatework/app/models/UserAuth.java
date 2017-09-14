package kryternext.graduatework.app.models;


import android.support.annotation.NonNull;

public class UserAuth {
    private String username;
    private String password;

    public UserAuth(String username, String password) {
        this.username = username.toLowerCase();
        this.password = encryptPass(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    static String encryptPass(String password) {
        StringBuilder encryptedPassword = new StringBuilder("");
        for (char ch : password.toCharArray()) {
            int decryptedCharId = (int) ch;
            int encryptedCharId = decryptedCharId + 5;
            encryptedPassword.append((char) encryptedCharId);
        }
        return encryptedPassword.toString();
    }
}
