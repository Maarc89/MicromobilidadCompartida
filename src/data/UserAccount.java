package data;

import micromobility.PMVehicle;
import micromobility.payment.Payment;

import java.util.Objects;

public class UserAccount {

    private final String userAccount;
    private final String name;
    private final String password;
    private final String email;

    public static final char GatewayPayment = 'G';
    public static final char CardPayment = 'C';
    public static final char BizumPayment = 'B';
    public static final char WalletPayment = 'W';


    public UserAccount(String userAccount, String name, String password, String email) {

        if (userAccount == null) {
            throw new NullPointerException("UserAccount no puede ser nulo.");
        }
        if (!isValid(userAccount)) {
            throw new IllegalArgumentException("UserAccount mal formado. Debe ser 3 letras mayúsculas seguidas de 3 números (longitud 6).");
        }

        if (password == null) {
            throw new NullPointerException("Password no puede ser nulo.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password mal formada. Debe tener al menos 6 caracteres, incluir una letra mayúscula, un número.");
        }

        if (email == null) {
            throw new NullPointerException("Password no puede ser nulo.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Password mal formada. Debe tener al menos 6 caracteres, incluir una letra mayúscula, un número.");
        }

        this.userAccount = userAccount;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private boolean isValid(String userAccount) {
        return userAccount.matches("^[A-Z]{3}\\d{3}$"); // 3 letras mayúsculas + 5 números
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$"); // 1 letra mayúsculas + 1 letra miniscula + 1 número
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[^@\\s]+@[^@\\s]+\\.com$");
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(userAccount, that.userAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAccount);
    }

    // toString
    @Override
    public String toString() {
        return "UserAccount{" +
                "account='" + userAccount + '\'' + '}';
    }
}

