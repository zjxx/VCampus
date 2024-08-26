
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


  private int gender;
    @Column(length = 13)
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
