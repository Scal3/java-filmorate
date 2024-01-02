package ru.yandex.practicum.filmorate.user.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.user.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.user.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testGetUserById() {
        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user);

        User savedUser = storage.getOneById(1).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testFindAllUsers() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);

        List<User> usersList = List.of(user1, user2);
        List<User> usersDbList = storage.getAll();

        assertThat(usersDbList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(usersList);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testCreateUser() {
        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user);

        User createdUser = storage.getOneById(1).get();

        assertThat(createdUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testUpdateUser() {
        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user);

        User createdUser = storage.getOneById(1).get();

        assertThat(createdUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);

        user.setEmail("updatedEmail@gmail.com");
        storage.update(user);

        User updatedUser = storage.getOneById(1).get();

        assertThat(updatedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testDeleteUser() {
        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user);

        User createdUser = storage.getOneById(1).get();

        assertThat(createdUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);

        storage.delete(1);

        boolean deletedUser = storage.getOneById(1).isEmpty();

        assertThat(deletedUser)
                .isEqualTo(true);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testIsUserFriendOfOtherUser() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);

        boolean areTheyFriends = storage.isUserFriendOfOtherUser(1, 2);

        assertThat(areTheyFriends)
                .isEqualTo(false);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testChangeFriendshipStatus() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);
        storage.addFriend(1, 2, FriendshipStatus.UNAPPROVED);
        storage.changeFriendshipStatus(1, 2, FriendshipStatus.APPROVED);

        List<User> userFriends = storage.getUserFriends(1);

        assertThat(userFriends.get(0).getFriendshipStatus())
                .isNotNull()
                .isEqualTo(FriendshipStatus.APPROVED);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testAddFriend() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );
        user2.setFriendshipStatus(FriendshipStatus.UNAPPROVED);

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);
        storage.addFriend(1, 2, FriendshipStatus.UNAPPROVED);

        List<User> userDbFriends = storage.getUserFriends(1);

        assertThat(userDbFriends.size())
                .isEqualTo(1);

        List<User> userFriends = List.of(user2);

        assertThat(userDbFriends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userFriends);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testRemoveFriend() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );
        user2.setFriendshipStatus(FriendshipStatus.UNAPPROVED);

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);
        storage.addFriend(1, 2, FriendshipStatus.UNAPPROVED);

        List<User> userDbFriends = storage.getUserFriends(1);

        assertThat(userDbFriends.size())
                .isEqualTo(1);

        List<User> userFriends = List.of(user2);

        assertThat(userDbFriends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userFriends);

        storage.removeFriend(1, 2);

        List<User> userDbFriendsAfterRemoving = storage.getUserFriends(1);

        assertThat(userDbFriendsAfterRemoving.size())
                .isEqualTo(0);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testGetUserFriends() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );
        user2.setFriendshipStatus(FriendshipStatus.UNAPPROVED);

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);
        storage.addFriend(1, 2, FriendshipStatus.UNAPPROVED);

        List<User> userDbFriends = storage.getUserFriends(1);
        List<User> userFriends = List.of(user2);

        assertThat(userDbFriends.size())
                .isEqualTo(1);

        assertThat(userDbFriends).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userFriends);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @Test
    public void testGetMutualFriends() {
        User user1 = new User(
                1,
                "user1@gmail.com",
                "login1",
                "name1",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user2 = new User(
                2,
                "user2@gmail.com",
                "login2",
                "name2",
                LocalDate.of(2000, Month.JULY, 7)
        );

        User user3 = new User(
                3,
                "user3@gmail.com",
                "login3",
                "name3",
                LocalDate.of(2000, Month.JULY, 7)
        );

        UserDbStorage storage = new UserDbStorage(jdbcTemplate);
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);
        storage.addFriend(1, 3, FriendshipStatus.UNAPPROVED);
        storage.addFriend(2, 3, FriendshipStatus.UNAPPROVED);

        List<User> mutualDbFriends = storage.getMutualFriends(1, 2);
        List<User> mutualFriends = List.of(user3);

        assertThat(mutualDbFriends.size())
                .isEqualTo(1);

        assertThat(mutualDbFriends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mutualFriends);
    }
}