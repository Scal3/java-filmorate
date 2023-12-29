package ru.yandex.practicum.filmorate.user.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.user.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        );
    };

    private final RowMapper<User> userFriendRowMapper = (rs, rowNum) -> {
        User user = new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        );

        user.setFriendshipStatus(FriendshipStatus.valueOf(rs.getString("friendship_status")));

        return user;
    };

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getOneById(int id) {
        String sqlQuery =
                        "SELECT " +
                            "id, " +
                            "email, " +
                            "login, " +
                            "user_name, " +
                            "birthday " +
                        "FROM filmorate_user " +
                        "WHERE id = ?;";

        return jdbcTemplate.query(sqlQuery, userRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        String sqlQuery =
                "SELECT " +
                    "id, " +
                    "email, " +
                    "login, " +
                    "user_name, " +
                    "birthday " +
                "FROM filmorate_user;";

        return jdbcTemplate.query(sqlQuery, userRowMapper);
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO filmorate_user (email, login, user_name, birthday)" +
                          "VALUES (?, ?, ?, ?);";

        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        String sqlQuery =
                        "SELECT " +
                            "id, " +
                            "email, " +
                            "login, " +
                            "user_name, " +
                            "birthday " +
                        "FROM filmorate_user " +
                        "WHERE email = ? " +
                                "AND login = ? " +
                                "AND user_name = ? " +
                                "AND birthday = ?;";

        return jdbcTemplate.queryForObject(
                sqlQuery,
                userRowMapper,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
    }

    @Override
    public User update(User user) {
        String sql =
                    "UPDATE filmorate_user " +
                    "SET " +
                        "email = ?, " +
                        "login = ?, " +
                        "user_name = ?, " +
                        "birthday = ? " +
                    "WHERE id = ?;";

        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return getOneById(user.getId()).get();
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM filmorate_user WHERE id = ?;", id);
    }

    @Override
    public boolean isUserFriendOfOtherUser(int userId, int expectedFriendId) {
        String sqlQuery =
                        "SELECT followed_user_id " +
                        "FROM user_friend " +
                        "WHERE following_user_id = ? " +
                        "AND followed_user_id = ?;";

        try {
            jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<>(Object.class), userId, expectedFriendId);

            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void changeFriendshipStatus(int userId, FriendshipStatus friendshipStatus) {
        String sql =
                    "UPDATE user_friend " +
                    "SET friendship_status = ? " +
                    "WHERE following_user_id = ?;";

        jdbcTemplate.update(sql, friendshipStatus, userId);
    }

    @Override
    public void addFriend(int userId, int friendId, FriendshipStatus friendshipStatus) {
        String sql =
                    "INSERT INTO user_friend (following_user_id, followed_user_id, friendship_status) " +
                    "VALUES (?, ?, ?);";

        jdbcTemplate.update(sql, userId, friendId, friendshipStatus.name());
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql =
                    "DELETE FROM user_friend " +
                    "WHERE following_user_id = ? " +
                    "AND followed_user_id = ?;";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        String sqlQuery =
                        "SELECT " +
                            "filmorate_user.id, " +
                            "filmorate_user.email, " +
                            "filmorate_user.login, " +
                            "filmorate_user.user_name, " +
                            "filmorate_user.birthday, " +
                            "user_friend.friendship_status " +
                        "FROM user_friend " +
                        "JOIN filmorate_user ON user_friend.followed_user_id = filmorate_user.id " +
                        "WHERE user_friend.following_user_id = ?;";

        return jdbcTemplate.query(sqlQuery, userFriendRowMapper, userId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int otherUserId) {
        String sqlAllUsersFriendsIdsSubQuery =
                                "(SELECT user_friend.followed_user_id AS friend_id " +
                                "FROM user_friend " +
                                "WHERE user_friend.following_user_id = ? " +
                                    "OR user_friend.following_user_id = ?)";

        String sqlMutualUsersIdsSubQuery =
                                "(SELECT " +
                                    "all_friends.friend_id, " +
                                    "COUNT(all_friends.friend_id) AS friend_repeat " +
                                "FROM " + sqlAllUsersFriendsIdsSubQuery + " AS all_friends " +
                                "GROUP BY all_friends.friend_id " +
                                "HAVING friend_repeat = 2)";

        String sqlGetUsersFriends =
                                "SELECT " +
                                    "filmorate_user.id, " +
                                    "filmorate_user.email, " +
                                    "filmorate_user.login, " +
                                    "filmorate_user.user_name, " +
                                    "filmorate_user.birthday " +
                                "FROM " + sqlMutualUsersIdsSubQuery + " AS mutual_friends " +
                                "JOIN filmorate_user ON mutual_friends.friend_id = filmorate_user.id;";

        return jdbcTemplate.query(sqlGetUsersFriends, userRowMapper, userId, otherUserId);
    }
}
