package com.wooteco.sokdak.util.fixture;

import com.wooteco.sokdak.board.dto.NewBoardRequest;

public class BoardFixture {

    public static NewBoardRequest BOARD_REQUEST_1 = new NewBoardRequest("Hot 게시판");
    public static NewBoardRequest BOARD_REQUEST_2 = new NewBoardRequest("자유게시판");
    public static NewBoardRequest BOARD_REQUEST_3 = new NewBoardRequest("포수타");
    public static NewBoardRequest BOARD_REQUEST_4 = new NewBoardRequest("감동크루");

    public static final long HOT_BOARD_ID = 1;
    public static final long FREE_BOARD_ID = 2;
    public static final long POSUTA_BOARD_ID = 3;
}
