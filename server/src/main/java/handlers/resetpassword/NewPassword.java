package handlers.resetpassword;

public class NewPassword {
    public final String password;
    public final String confirmPassword;

    public NewPassword(String password, String confirmPassword){
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public boolean checkMatchingPasswords() {
        return password.equals(confirmPassword);
    }
}
