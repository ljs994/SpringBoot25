package org.mbc.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        log.info("등록용 테스트 서비스 실행중");
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("서비스님")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("테스트 결과 bno : " + bno);

    } // testRegister 메서드 종료

    @Test
    public void  testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("수정된 제목101")
                .content("수정된 내용101")
                .build();

        boardService.modify(boardDTO);
    } // testModify 메서드 종료

}
