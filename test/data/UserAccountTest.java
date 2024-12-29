package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    @Test
    void validUserAccountAndPassword() {
        UserAccount user = new UserAccount("ABC123", "John Doe", "Password1!", "john.doe@example.com");
        assertEquals("ABC123", user.getUserAccount());
        assertEquals("John Doe", user.getName());
        assertEquals("Password1!", user.getPassword());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void invalidUserAccountThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("AB123", "John Doe", "Password1!", "john.doe@example.com"); // Invalid user account format
        });
        assertEquals("UserAccount mal formado. Debe ser 3 letras mayúsculas seguidas de 3 números (longitud 6).", exception.getMessage());
    }

    @Test
    void nullUserAccountThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserAccount(null, "John Doe", "Password1!", "john.doe@example.com"); // User account is null
        });
        assertEquals("UserAccount no puede ser nulo.", exception.getMessage());
    }

    @Test
    void invalidPasswordThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("ABC123", "John Doe", "password1", "john.doe@example.com"); // Password doesn't contain uppercase letter
        });
        assertEquals("Password mal formada. Debe tener al menos 6 caracteres, incluir una letra mayúscula, un número.", exception.getMessage());
    }

    @Test
    void nullPasswordThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserAccount("ABC123", "John Doe", null, "john.doe@example.com"); // Password is null
        });
        assertEquals("Password no puede ser nulo.", exception.getMessage());
    }

    @Test
    void invalidEmailThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("ABC123", "John Doe", "Password1!", "invalid-email"); // Email format is invalid
        });
        assertEquals("Password mal formada. Debe tener al menos 6 caracteres, incluir una letra mayúscula, un número.", exception.getMessage());
    }

    @Test
    void nullEmailThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserAccount("ABC123", "John Doe", "Password1!", null); // Email is null
        });
        assertEquals("Password no puede ser nulo.", exception.getMessage());
    }

    @Test
    void equalsAndHashCode() {
        UserAccount user1 = new UserAccount("ABC123", "John Doe", "Password1!", "john.doe@example.com");
        UserAccount user2 = new UserAccount("ABC123", "Jane Doe", "Password2@", "jane.doe@example.com"); // Same userAccount but different details
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
