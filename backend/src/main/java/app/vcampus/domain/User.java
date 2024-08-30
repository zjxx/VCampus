
package app.vcampus.domain;

import jakarta.persistence.*;


@Entity
@Table(name="user")
public class User {

  @Column(length = 25)
  private String username;

  @Column(length = 25)
  private String password;

  @Id
  @Column(length = 13)
  private String userId;

  private Integer balance;

  private int gender;

  private int role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }




    public Integer getBalance() {
        return balance;
    }




    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
