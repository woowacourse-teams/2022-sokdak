SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE post_hashtag;
TRUNCATE TABLE likes;
TRUNCATE TABLE comment;
TRUNCATE TABLE hashtag;
TRUNCATE TABLE post;
TRUNCATE TABLE auth_code;
TRUNCATE TABLE ticket;
TRUNCATE TABLE member;
TRUNCATE TABLE board;
TRUNCATE TABLE post_board;
TRUNCATE TABLE post_report;
TRUNCATE TABLE comment_report;

ALTER TABLE post_hashtag ALTER COLUMN post_hashtag_id RESTART WITH 1;
ALTER TABLE likes ALTER COLUMN likes_id RESTART WITH 1;
ALTER TABLE comment ALTER COLUMN comment_id RESTART WITH 1;
ALTER TABLE hashtag ALTER COLUMN hashtag_id RESTART WITH 1;
ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1;
ALTER TABLE auth_code ALTER COLUMN auth_code_id RESTART WITH 1;
ALTER TABLE ticket ALTER COLUMN ticket_id RESTART WITH 1;
ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1;
ALTER TABLE board ALTER COLUMN board_id RESTART WITH 1;
ALTER TABLE post_board ALTER COLUMN post_board_id RESTART WITH 1;
ALTER TABLE post_report ALTER COLUMN post_report_id RESTART WITH 1;
ALTER TABLE comment_report ALTER COLUMN comment_report_id RESTART WITH 1;

SET
FOREIGN_KEY_CHECKS = 1;

insert into member (username, nickname, password, role_type) values ('chris', 'chrisNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');
insert into member (username, nickname, password, role_type) values ('josh', 'joshNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');
insert into member (username, nickname, password, role_type) values ('east', 'eastNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');
insert into member (username, nickname, password, role_type) values ('thor', 'thorNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');
insert into member (username, nickname, password, role_type) values ('hunch', 'hunchNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');
insert into member (username, nickname, password, role_type) values ('testAdmin', 'adminNick', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');

insert into ticket (serial_number, used) values ('21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279', false);
insert into ticket (serial_number, used) values ('5810c81b6f78b1bcf62b53035d879e1309750ab60d1b4b601dfbc368005645cb', false);
insert into ticket (serial_number, used) values ('694f51c40e1910a86b3f4a655ac874a0511402a6ac347b1cadccf7b9e3678fac', false);
insert into ticket (serial_number, used) values ('a3280648524742e7c891fb472ea3541d4bf73276e01f15b3e73eeba4d0085424', false);
insert into ticket (serial_number, used) values ('774dd927c9c846f2bb21b02fd139d266f7f6fd9d4a0d829e4e6553b8fcd9b53b', false);
insert into ticket (serial_number, used) values ('ce89c8413662dffc17a4644ddf0386432404cd943b18eeee45740be5c35ef03b', false);
insert into ticket (serial_number, used) values ('49d3b5b2d51e0f03cd1c0b85e2312dbd740023856ce16a725a3617f58b91da1c', false);

insert into board (title, user_writable) values ('Hot 게시판', false);
insert into board (title, user_writable) values ('자유게시판', true);
insert into board (title, user_writable) values ('포수타', true);
insert into board (title, user_writable) values ('감동크루', true);

