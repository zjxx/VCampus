package app.vcampus.enums;

/**
 * Enum representing different user types in the system.
 * Each enum constant is associated with a role string.
 */
public enum UserType {
    STUDENT("student"), // Represents a student user
    TEACHER("teacher"), // Represents a teacher user
    ADMIN("admin"); // Represents an admin user

    private final String role; // Role associated with the user type

    /**
     * Constructor for UserType enum.
     *
     * @param role the role string associated with the user type
     */
    UserType(String role) {
        this.role = role;
    }

    /**
     * Gets the role string associated with the user type.
     *
     * @return the role string
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the role string based on the index.
     *
     * @param index the index of the user type
     * @return the role string
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public static String fromIndex(int index) {
        UserType[] values = UserType.values();
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return values[index].getRole();
    }
}