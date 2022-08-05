create table if not exists auth_code
(
    auth_code_id  bigint generated by default as identity,
    code          varchar(255),
    created_at    timestamp,
    serial_number varchar(255),
    primary key (auth_code_id)
    );

create table if not exists board
(
    board_id   bigint generated by default as identity,
    board_type varchar(255),
    created_at timestamp,
    title      varchar(255),
    primary key (board_id)
    );

create table if not exists comment
(
    comment_id bigint generated by default as identity,
    created_at timestamp,
    message    varchar(255) not null,
    nickname   varchar(255),
    member_id  bigint,
    post_id    bigint,
    primary key (comment_id)
    );

create table if not exists comment_report
(
    comment_report_id bigint generated by default as identity,
    created_at        timestamp,
    report_message    varchar(255) not null,
    comment_id        bigint,
    member_id         bigint,
    primary key (comment_report_id)
    );

create table if not exists hashtag
(
    hashtag_id bigint generated by default as identity,
    name       varchar(255),
    primary key (hashtag_id)
    );

create table if not exists likes
(
    likes_id  bigint generated by default as identity,
    member_id bigint,
    post_id   bigint,
    primary key (likes_id)
    );

create table if not exists member
(
    member_id bigint generated by default as identity,
    nickname  varchar(255) unique,
    password  varchar(255),
    role_type varchar(255),
    username  varchar(255),
    primary key (member_id)
    );

create table if not exists post
(
    post_id         bigint generated by default as identity,
    content         clob         not null,
    created_at      timestamp,
    modified_at     timestamp,
    title           varchar(255) not null,
    writer_nickname varchar(255),
    member_id       bigint,
    primary key (post_id)
    );

create table if not exists post_board
(
    post_board_id bigint generated by default as identity,
    created_at    timestamp,
    board_id      bigint,
    post_id       bigint,
    primary key (post_board_id)
    );

create table if not exists post_hashtag
(
    post_hashtag_id bigint generated by default as identity,
    hashtag_id      bigint,
    post_id         bigint,
    primary key (post_hashtag_id)
    );

create table if not exists post_report
(
    post_report_id bigint generated by default as identity,
    created_at     timestamp,
    report_message varchar(255) not null,
    post_id        bigint,
    member_id      bigint,
    primary key (post_report_id)
    );

create table if not exists refresh_token
(
    refresh_token_id bigint generated by default as identity,
    member_id        bigint,
    token            varchar(255),
    primary key (refresh_token_id)
    );

create table if not exists ticket
(
    ticket_id     bigint generated by default as identity,
    serial_number varchar(255),
    used          boolean not null,
    primary key (ticket_id)
    );

-- insert into member (username, nickname, password, role_type) values ('chris', 'chris', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('east', 'east', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('hunch', 'hunch', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('josh', 'josh', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('dongkey', 'dongkey', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('movie', 'movie', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');
-- insert into member (username, nickname, password, role_type) values ('thor', 'thor', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');

insert into ticket (serial_number, used) values ('21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279', false);
insert into ticket (serial_number, used) values ('5810c81b6f78b1bcf62b53035d879e1309750ab60d1b4b601dfbc368005645cb', false);
insert into ticket (serial_number, used) values ('694f51c40e1910a86b3f4a655ac874a0511402a6ac347b1cadccf7b9e3678fac', false);
insert into ticket (serial_number, used) values ('a3280648524742e7c891fb472ea3541d4bf73276e01f15b3e73eeba4d0085424', false);
insert into ticket (serial_number, used) values ('774dd927c9c846f2bb21b02fd139d266f7f6fd9d4a0d829e4e6553b8fcd9b53b', false);
insert into ticket (serial_number, used) values ('ce89c8413662dffc17a4644ddf0386432404cd943b18eeee45740be5c35ef03b', false);
insert into ticket (serial_number, used) values ('49d3b5b2d51e0f03cd1c0b85e2312dbd740023856ce16a725a3617f58b91da1c', false);

insert into board (title, board_type) values ('Hot 게시판', 'NON_WRITABLE');
insert into board (title, board_type) values ('자유게시판', 'WRITABLE');
insert into board (title, board_type) values ('포수타', 'WRITABLE');
insert into board (title, board_type) values ('감동크루', 'WRITABLE');