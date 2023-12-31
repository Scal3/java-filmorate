CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_value VARCHAR(5) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS film (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name VARCHAR(55) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    releaseDate TIMESTAMP NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER NOT NULL REFERENCES mpa (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS filmorate_user (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(75) NOT NULL UNIQUE,
    login VARCHAR(20) NOT NULL UNIQUE,
    user_name VARCHAR(50) NOT NULL,
    birthday TIMESTAMP NOT NULL
);


CREATE TABLE IF NOT EXISTS genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(15) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS film_genre (
    film_id INTEGER NOT NULL REFERENCES film (id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genre (id) ON DELETE CASCADE,
    CONSTRAINT PK_FILM_GENRE PRIMARY KEY(film_id, genre_id)
);


CREATE TABLE IF NOT EXISTS film_user_like (
    film_id INTEGER NOT NULL REFERENCES film (id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES filmorate_user (id) ON DELETE CASCADE,
    CONSTRAINT PK_FILM_USER_LIKE PRIMARY KEY(film_id, user_id)
);


CREATE TABLE IF NOT EXISTS user_friend (
    following_user_id INTEGER NOT NULL REFERENCES filmorate_user (id) ON DELETE CASCADE,
    followed_user_id INTEGER NOT NULL REFERENCES filmorate_user (id) ON DELETE CASCADE,
    friendship_status VARCHAR(10) NOT NULL,
    CONSTRAINT PK_USER_FRIEND PRIMARY KEY(following_user_id, followed_user_id)
);