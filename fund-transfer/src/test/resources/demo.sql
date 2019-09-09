DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30),
Balance DECIMAL(19,4),
CurrencyCode VARCHAR(30)
);

CREATE UNIQUE INDEX idx_acc on Account(UserName,CurrencyCode);

INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('varun',100.0000,'USD');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('jake',200.0000,'USD');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('varun',500.0000,'EUR');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('jake',500.0000,'EUR');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('varun',500.0000,'GBP');
INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('jake',500.0000,'GBP');
