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

create table if not exists comment_likes
(
    comment_likes_id  bigint generated by default as identity,
    member_id bigint,
    comment_id   bigint,
    primary key (comment_likes_id)
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
