INSERT INTO genre (genre_name) VALUES ('Комедия')
ON CONFLICT (genre_name) DO NOTHING;

INSERT INTO genre (genre_name) VALUES ('Драма')
ON CONFLICT (genre_name) DO NOTHING;

INSERT INTO genre (genre_name) VALUES ('Мультфильм')
ON CONFLICT (genre_name) DO NOTHING;

INSERT INTO genre (genre_name) VALUES ('Триллер')
ON CONFLICT (genre_name) DO NOTHING;

INSERT INTO genre (genre_name) VALUES ('Документальный')
ON CONFLICT (genre_name) DO NOTHING;

INSERT INTO genre (genre_name) VALUES ('Боевик')
ON CONFLICT (genre_name) DO NOTHING;



INSERT INTO mpa (mpa_value) VALUES ('G')
ON CONFLICT (mpa_value) DO NOTHING;

INSERT INTO mpa (mpa_value) VALUES ('PG')
ON CONFLICT (mpa_value) DO NOTHING;

INSERT INTO mpa (mpa_value) VALUES ('PG-13')
ON CONFLICT (mpa_value) DO NOTHING;

INSERT INTO mpa (mpa_value) VALUES ('R')
ON CONFLICT (mpa_value) DO NOTHING;

INSERT INTO mpa (mpa_value) VALUES ('NC-17')
ON CONFLICT (mpa_value) DO NOTHING;