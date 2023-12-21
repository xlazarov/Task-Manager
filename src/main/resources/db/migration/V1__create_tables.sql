CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    due_date DATE,
    user_id INTEGER REFERENCES app_user(id),
    state VARCHAR(255)
);