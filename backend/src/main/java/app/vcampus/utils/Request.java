package app.vcampus.utils;

/**
 * Represents a request with a type, data, and role.
 */
public class Request {
    private String type; // Type of the request
    private String data; // Data associated with the request
    public String role; // Role associated with the request

    /**
     * Gets the type of the request.
     *
     * @return the type of the request
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the request.
     *
     * @param type the type of the request
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the data associated with the request.
     *
     * @return the data associated with the request
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data associated with the request.
     *
     * @param data the data associated with the request
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets the role associated with the request.
     *
     * @return the role associated with the request
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role associated with the request.
     *
     * @param role the role associated with the request
     */
    public void setRole(String role) {
        this.role = role;
    }
}