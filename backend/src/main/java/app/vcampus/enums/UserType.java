package app.vcampus.enums;

public enum UserType {
    STUDENT("student"),
    TEACHER("teacher"),
    ADMIN("admin");

    private final String role;

    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static String fromIndex(int index) {
        UserType[] values = UserType.values();
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return values[index].getRole();
    }
}