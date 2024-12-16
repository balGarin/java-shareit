
CREATE TABLE IF NOT EXISTS users (
user_id INTEGER  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
name VARCHAR(255)  NOT NULL,
email VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests(
request_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
description VARCHAR(100),
requestor_id INTEGER REFERENCES users(user_id),
created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items(
item_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR(100),
description VARCHAR(500) NOT NULL,
available boolean ,
owner_id INTEGER REFERENCES users(user_id),
request_id INTEGER REFERENCES requests(request_id) NULL
);


CREATE TABLE IF NOT EXISTS bookings(
booking_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
item_id INTEGER REFERENCES items(item_id),
user_id INTEGER REFERENCES users(user_id),
status VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS comments(
comment_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
item_id INTEGER REFERENCES items(item_id),
user_id INTEGER REFERENCES users(user_id),
text VARCHAR(500),
created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);