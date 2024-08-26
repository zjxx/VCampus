
package app.vcampus.domain;

import jakarta.persistence.*;


@Entity
@Table(name="user")
public class User {
  private String username;

  private String password;

  @Id
  private long userId;

  private String gender;

  private String phone;

  private long role;

  private Integer balance;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public long getRole() {
        return role;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(long role) {
        this.role = role;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
