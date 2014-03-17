CREATE SCHEMA JPANTRY;

CREATE TABLE JPANTRY.ITEM (
		id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
		description varchar(255),
        name varchar(255), 
        upc varchar(255), 
        on_list boolean, 
        quantity int);
      
INSERT INTO JPANTRY.ITEM (description, name, upc, on_list, quantity) VALUES ('Test Description', 'Test Name', '1234567890', false, 1);

CREATE TABLE JPANTRY.ITEM_HISTORY (
		id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
        upc varchar(255), 
        event varchar(255), 
        date date,
        quantity int);
        
