package model;

import java.util.Objects;

public record User(String userId, String password, String name, String email) {

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId(), user.userId()) && Objects.equals(password(), user.password()) && Objects.equals(name(), user.name()) && Objects.equals(email(), user.email());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId(), password(), name(), email());
    }


}
