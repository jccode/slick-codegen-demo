CREATE TABLE user (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  password VARCHAR(128) NOT NULL ,
  salt VARCHAR(128) NOT NULL ,
  mobile VARCHAR(20),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX user_name ON user (name);

CREATE TABLE account (
  id   INTEGER not null primary key auto_increment,
  user_id INTEGER NOT NULL ,
  name VARCHAR(50) NOT NULL ,
  balance INTEGER,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX account_name ON account (name);

CREATE TABLE product (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL comment '商品名',
  price INTEGER comment '价格',
  stock INTEGER comment '库存',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX product_name ON PRODUCT (name);