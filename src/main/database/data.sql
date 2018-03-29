INSERT INTO public.users(
	username, password, email, firstname, lastname, alias, gender, socialtype)
	VALUES ('admin', '$2a$04$SCCEMlPMLDQuxwET5TFWOev8298omHKsRvjqA/8aZhfJzo92tKozi','dario.frongillo@gmail.com', 'dario','frongillo', 'frongy', 0, 0);

insert into accounts
(id,name,description,is_sharable,IS_DELETABLE,status, DEFAULT_USER)
values('0','Account.default.name','Account.default.description', false, false, 0,'admin');

insert into  USER_ACCOUNT values ( 'admin','0');


INSERT INTO categories VALUES ('0', 0, 'tag.incoming.saving', false, NULL, 1522327674, 1522327981);
INSERT INTO categories VALUES ('1', 0, 'tag.incoming.salary', false, NULL, 1522327674, 1522327981);
INSERT INTO categories VALUES ('2', 0, 'tag.incoming.gift', false, NULL, 1522327674, 1522327981);
INSERT INTO categories VALUES ('3', 1, 'tag.expense.clothes', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('4', 1, 'tag.expense.animal', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('5', 1, 'tag.expense.auto', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('6', 1, 'tag.expense.bills', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('7', 1, 'tag.expense.house', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('8', 1, 'tag.expense.food', false, NULL, 1522327777, 1522327981);
INSERT INTO categories VALUES ('9', 1, 'tag.expense.tel', false, NULL, 1522327857, 1522327981);
INSERT INTO categories VALUES ('10', 1, 'tag.expense.bodyCare', false, NULL, 1522327857, 1522327981);
INSERT INTO categories VALUES ('11', 1, 'tag.expense.dinner', false, NULL, 1522327857, 1522327981);
INSERT INTO categories VALUES ('12', 1, 'tag.expense.gift', false, NULL, 1522327857, 1522327981);
INSERT INTO categories VALUES ('13', 1, 'tag.expense.health', false, NULL, 1522327943, 1522327981);
INSERT INTO categories VALUES ('14', 1, 'tag.tag.expense.sport', false, NULL, 1522327954, 1522327981);
INSERT INTO categories VALUES ('15', 1, 'tag.expense.transportation', false, NULL, 1522327970, 1522327981);
