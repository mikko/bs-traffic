# Users schema
 
# --- !Ups
 
CREATE TABLE LocationUpdate (
    id int NOT NULL AUTO_INCREMENT,
    journey varchar(255) NOT NULL,
    line int NOT NULL,
    direction int NOT NULL,
    lat double NOT NULL,
    lon double NOT NULL,
    timestamp bigint NOT NULL,
    currentstop int NOT NULL,
    prevstop int NOT NULL,    
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE LocationUpdate;