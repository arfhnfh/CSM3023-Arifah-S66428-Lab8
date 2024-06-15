CREATE DATABASE carshop; 
USE carshop;

CREATE TABLE carPricelist( 
Car_id INT NOT NULL AUTO_INCREMENT, 
Brand VARCHAR(15),
Model VARCHAR(30),
Cyclinder INT, Price DOUBLE,
PRIMARY KEY (Car_id)
);
