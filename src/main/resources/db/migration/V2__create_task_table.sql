CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    due_date DATE,
    user_id INTEGER REFERENCES app_user(id),
    state VARCHAR(255) CHECK (state IN ('NOT_COMPLETED', 'IN_PROGRESS', 'COMPLETED'))
);