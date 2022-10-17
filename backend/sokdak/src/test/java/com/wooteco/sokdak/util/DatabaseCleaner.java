package com.wooteco.sokdak.util;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@ActiveProfiles("test")
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.tableNames = entityManager.getMetamodel()
                .getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager
                    .createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_id RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void insertInitialData() {
        // 멤버 추가
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('25f43b1486ad95a1398e3eeb3d83bc4010015fcc9bedb35b432e00298d5021f7', 'admin1Nickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('1c142b2d01aa34e9a36bde480645a57fd69e14155dacfab5a3f9257b77fdc8d8', 'admin2Nickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'ADMIN');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('5d7f15f2fce8ddb2dbef5c38be896c238ba7e0a432e396759030a853fa6b1151', 'chrisNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('386a85d8c88778b00b1355608363c7e3078857f3e9633cfd0802d3bf1c0b5b83', 'joshNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('7d445240c97cb8b39b22030981d77679608f91c7a4000e41a1794cde953a1846', 'thorNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('977ef51fa0b1154296309099ca189c7a4970ed2c0bd47dc9cf50606e4412c250', 'hunchNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('6bdb262b0201799aefb0ae4116c6fb88f549425e30cd85a7f75a06be246751cf', 'eastNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('8a6ba32c9bed6ce703f999f9af6ec23686d44e144e4da572d94c8daca4a9cbab', 'movieNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into member (username, nickname, password, role_type) values ('cc74da4ce54e8fb6d80c7768c0268090f537838a5535d0ceef9d2ff3fd4f5978', 'dongkeyNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'USER');")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "insert into member (username, nickname, password, role_type) values ('42aef2f386567bd5a6a3c753ad0b0debc280a3b6231c084618992fc4c91b7644', 'applicantNickname', '6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e', 'APPLICANT');")
                .executeUpdate();

        //티켓 추가
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('5810c81b6f78b1bcf62b53035d879e1309750ab60d1b4b601dfbc368005645cb', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('694f51c40e1910a86b3f4a655ac874a0511402a6ac347b1cadccf7b9e3678fac', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('a3280648524742e7c891fb472ea3541d4bf73276e01f15b3e73eeba4d0085424', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('774dd927c9c846f2bb21b02fd139d266f7f6fd9d4a0d829e4e6553b8fcd9b53b', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('ce89c8413662dffc17a4644ddf0386432404cd943b18eeee45740be5c35ef03b', false);")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into ticket (serial_number, used) values ('277da9dd2879115a42e90b5ac344315ac5fc0222378d5c8b6c110f5e3539b743', false);")
                .executeUpdate();

        entityManager.createNativeQuery(
                "insert into auth_code (code, serial_number, created_at) values ('123456', '277da9dd2879115a42e90b5ac344315ac5fc0222378d5c8b6c110f5e3539b743', CURRENT_TIMESTAMP());")
                .executeUpdate();

        // 게시판 추가
        entityManager.createNativeQuery(
                "insert into board (title, board_type) values ('Hot 게시판', 'NON_WRITABLE');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into board (title, board_type) values ('자유게시판', 'WRITABLE');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into board (title, board_type) values ('포수타', 'WRITABLE');")
                .executeUpdate();
        entityManager.createNativeQuery(
                "insert into board (title, board_type) values ('감동크루', 'WRITABLE');")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "insert into board (title, board_type) values ('5기 게시판', 'WRITABLE');")
                .executeUpdate();
    }
}
