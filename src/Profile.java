// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.io.Serializable;
import java.util.Objects;

public class Profile implements Serializable {
    private String username;
    private String password;
    private String name;
    private String status;

    public Profile(String username, String password, String name) {
            this.username = username;
            this.password = password;
            this.name = name;
            status = "offline";
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + getName() + '\'' +
                ", status:'" + getStatus() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return username.equals(profile.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = password;
        } else System.out.println("Password must contain something.");
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else System.out.println("Name must contain something.");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        switch (status){
            case "Online":
                this.status = "Online";
                break;
            case "Busy":
                this.status = "Busy";
                break;
            case "Offline":
                this.status = "Offline";
                break;
            default:

        }
    }
}
