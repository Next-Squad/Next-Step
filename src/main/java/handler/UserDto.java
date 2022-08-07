package handler;

import model.User;

public record UserDto(int rowNum, String userId, String password, String name, String email) {
    public static UserDto of(int rowNum, User user) {
        return new UserDto(rowNum,
                user.userId(),
                user.password(),
                user.name(),
                user.email());
    }
}
