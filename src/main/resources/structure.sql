create DATABASE santa;

\connect santa;

CREATE table users (
  mail VARCHAR(255) PRIMARY KEY,
  lastName VARCHAR(255) NOT NULL,
  hash VARCHAR(255) NOT NULL,
  accepted bool DEFAULT FALSE
);

CREATE table usersSanta (
  userMail VARCHAR(255) PRIMARY KEY,
  santaMail VARCHAR(255) NOT NULL
);