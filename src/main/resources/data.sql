MERGE INTO genre USING (SELECT 'Комедия' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);

MERGE INTO genre USING (SELECT 'Драма' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);

MERGE INTO genre USING (SELECT 'Мультфильм' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);

MERGE INTO genre USING (SELECT 'Триллер' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);

MERGE INTO genre USING (SELECT 'Документальный' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);

MERGE INTO genre USING (SELECT 'Боевик' AS genre_name) source
ON genre.genre_name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (genre_name) VALUES (source.genre_name);



MERGE INTO mpa USING (SELECT 'G' AS mpa_value) source
ON mpa.mpa_value = source.mpa_value
WHEN NOT MATCHED THEN
  INSERT (mpa_value) VALUES (source.mpa_value);

MERGE INTO mpa USING (SELECT 'PG' AS mpa_value) source
ON mpa.mpa_value = source.mpa_value
WHEN NOT MATCHED THEN
  INSERT (mpa_value) VALUES (source.mpa_value);

MERGE INTO mpa USING (SELECT 'PG-13' AS mpa_value) source
ON mpa.mpa_value = source.mpa_value
WHEN NOT MATCHED THEN
  INSERT (mpa_value) VALUES (source.mpa_value);

MERGE INTO mpa USING (SELECT 'R' AS mpa_value) source
ON mpa.mpa_value = source.mpa_value
WHEN NOT MATCHED THEN
  INSERT (mpa_value) VALUES (source.mpa_value);

MERGE INTO mpa USING (SELECT 'NC-17' AS mpa_value) source
ON mpa.mpa_value = source.mpa_value
WHEN NOT MATCHED THEN
  INSERT (mpa_value) VALUES (source.mpa_value);