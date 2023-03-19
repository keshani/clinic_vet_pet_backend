
INSERT INTO USERS( USER_ID, USER_FULL_NAME, PASSWORD, ENABLED)
VALUES ('TestUserOne','Test User One','$2a$10$U94Y8TT/0pBvzm2/MkHQX.E3Ahqxhuba/Y0ACz47mo1OW7hsi0F6C',true);

INSERT INTO USERS( USER_ID, USER_FULL_NAME, PASSWORD, ENABLED)
VALUES ('TestUserTwo','Test User Two','$2a$10$q3ZTlLzDCjIrVcIp4NvP6uzmhVa./84qV28CO4Xgd/oHSiknGJC3a',true);

INSERT INTO USERS_ROLES(USER_ID, ROLE_ID)
VALUES ('TestUserOne',1);
INSERT INTO USERS_ROLES(USER_ID, ROLE_ID)
VALUES ('TestUserOne',2);

INSERT INTO USERS_ROLES(USER_ID, ROLE_ID)
VALUES ('TestUserTwo',1);

INSERT INTO ANIMAL_DETAIL( ANIMAL_NAME, ANIMAL_TYPE, USER_ID)
VALUES ('Kitty','CAT','TestUserOne');

