package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    @Test
    void validUserAccountAndPassword() {
        UserAccount user = new UserAccount("ABC123", "Password1!");
        assertEquals("ABC123", user.getAccount());
        assertEquals("Password1!", user.getPassword());
    }

    @Test
    void invalidUserAccountThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("AB123", "Password1!"); // Invalid user account format
        });
        assertEquals("UserAccount mal formado. Debe ser 3 letras mayúsculas seguidas de 3 números (longitud 6).", exception.getMessage());
    }

    @Test
    void nullUserAccountThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserAccount(null, "Password1!"); // User account is null
        });
        assertEquals("UserAccount no puede ser nulo.", exception.getMessage());
    }

    @Test
    void invalidPasswordThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("ABC123", "password1"); // Password doesn't contain uppercase letter
        });
        assertEquals("Password mal formada. Debe tener al menos 6 caracteres, incluir una letra mayúscula, un número.", exception.getMessage());
    }

    @Test
    void nullPasswordThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserAccount("ABC123", null); // Password is null
        });
        assertEquals("Password no puede ser nulo.", exception.getMessage());
    }

    @Test
    void equalsAndHashCode() {
        UserAccount user1 = new UserAccount("ABC123", "Password1!");
        UserAccount user2 = new UserAccount("ABC123", "Password1!");
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

}
