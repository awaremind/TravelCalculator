CREATE TABLE countries.country (
    ID int(10) NOT NULL auto_increment,
    name varchar(255),
    code varchar(255),
    currency varchar(255),
    rate double,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;