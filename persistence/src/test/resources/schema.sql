CREATE TABLE IF NOT EXISTS countries (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75)
);

CREATE TABLE IF NOT EXISTS provinces (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75),
    countryId INTEGER REFERENCES countries(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS cities (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75),
    countryId INTEGER REFERENCES countries(id) ON DELETE SET NULL,
    provinceId INTEGER REFERENCES countries(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS neighbourhoods (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75),
    cityId INTEGER REFERENCES cities(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS properties (
    id INTEGER IDENTITY PRIMARY KEY,
    description varchar(3000),
    caption varchar(250),
    image varchar(300),
    propertyType varchar(100),
    neighbourhoodId integer references neighbourhoods(id) ON DELETE SET NULL,
    privacyLevel boolean,
    capacity integer,
    price float,
    mainimageid integer --THIS SHOULD BE A FOREIGN KEY! BUT THE ADD COLUMN IF NOT EXISTS WASN'T WORKING TODO FIX
);


CREATE TABLE IF NOT EXISTS universities (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75)
);

CREATE TABLE IF NOT EXISTS careers (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(75)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER IDENTITY PRIMARY KEY,
    email varchar(250),
    username varchar(250),
    passwordHash varchar(300),
    name varchar(50),
    lastName varchar(100),
    birthDate date,
    gender integer,
    bio varchar(1000),
    contactNumber varchar(25),
    universityId INTEGER REFERENCES universities(id),
    careerId INTEGER REFERENCES careers(id)
);

CREATE TABLE IF NOT EXISTS interests (
    id INTEGER IDENTITY PRIMARY KEY,
    propertyId INTEGER REFERENCES properties(id),
    userId INTEGER REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS rules (
    id INTEGER IDENTITY PRIMARY KEY,
    name varchar(250)
);

CREATE TABLE IF NOT EXISTS propertyRules (
    id INTEGER IDENTITY PRIMARY KEY,
    propertyId INTEGER REFERENCES properties(id),
    ruleId INTEGER REFERENCES rules(id)
);


CREATE TABLE IF NOT EXISTS images (
    id INTEGER IDENTITY PRIMARY KEY,
    propertyId INTEGER REFERENCES properties(id) ON DELETE CASCADE,
    image binary
);

-- seeding database
-- REMOVE WHEN MOVING ENVIRONMENT TO PROD
-- INSERT ... ON CONFLICT DO NOTHING/UPDATE statement was added in 9.5, watch out for version to use

INSERT INTO countries(
    id, name)
SELECT * FROM (VALUES (1, 'Argentina'))
WHERE NOT EXISTS (SELECT * FROM countries WHERE id=1);

INSERT INTO cities(
    id, name, countryid, provinceid)
SELECT * FROM (VALUES (1, 'CABA', 1, null))
WHERE NOT EXISTS (SELECT * FROM cities WHERE id=1);

INSERT INTO neighbourhoods(
    id, name, cityid)
SELECT * FROM (VALUES (1, 'Palermo', 1))
WHERE NOT EXISTS (SELECT * FROM neighbourhoods WHERE id=1);

INSERT INTO images(
    id, propertyid, image)
SELECT * FROM (VALUES (1, null, decode('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', 'base64', '')))
WHERE NOT EXISTS (SELECT * FROM images WHERE id=1);


INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (1, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=1);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (2, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=2);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (3, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=3);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (4, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=4);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (5, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=5);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (6, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=6);

INSERT INTO properties(
    id, description, caption, propertytype, neighbourhoodid, privacylevel, capacity, price, mainimageid)
SELECT * FROM (VALUES (7, 'posta que el mejor depto', 'el mejor depto', 'APARTMENT', 1, true, 5, 100, 1))
WHERE NOT EXISTS (SELECT * FROM properties WHERE id=7);
