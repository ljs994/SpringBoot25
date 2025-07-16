package org.mbc.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest // 메서드용 테스트 동작
@Log4j2         // 로그용
public class BoardRepositoryTests {
    // 영속성 계층에 테스트용
    
    @Autowired  // 생성자 자동주입
    private  BoardRepository boardRepository;
    
    @Test   // C
    public void testInsert() {
        // 데이터베이스에 데이터 주입(C) 테스트 코드
        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)      // board.setTitle()
                    .content("content..." + i)  // board.setContent()
                    .writer("user" + (i % 10))  // board.setWriter()
                    .build();                   // @Builder 용 (세터 대신 조금더 간단하고 가독성 좋게)
            // log.info(board);
            
            Board result = boardRepository.save(board); // DB에 기록하는 코드
            //                            .save : 메서드는 jpa에서 상속한 메서드로 값을 저장하는 용도
            //                                    이미 값이 있으면 update를 진행한다.
            log.info("BNO: " + result.getBno() + "게시물의 제목" + result.getTitle());
        });
    } // testInsert 메서드 종료

    @Test   // R
    public void testSelect() {

        Long bno = 100L;    // 게시물 번호가 100인 객체를 확인

        Optional<Board> result = boardRepository.findById(bno);
        //                                       findById(bno) : select *from board where bno = bno랑 같은 의미다
        // null 값이 나올 경우를 대비한 객체
        //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?
        
        Board board = result.orElseThrow(); // orElseThrow : 값이 있으면 넣어라

        log.info(bno+ "가 DB에 존재합니다.");
        log.info(board);
    } // testSelect 메서드 종료

    @Test   // U
    public void testUpdate() {

        Long bno = 100L;    // 100번 게시물을 가져와서 수정후 테스트 종료

        Optional<Board> result = boardRepository.findById(bno);     // bno를 찾아서 result에 넣는다

        Board board = result.orElseThrow();                         // 가져온 값이 있으면 board 타입 객체에 넣는다.

        board.change("수정한 제목", "수정한 내용");   // 제목과 내용만 수정할 수 있는 메서드

        boardRepository.save(board);                                // .save 메서드는 pk값이 없으면 insert, pk가 있으면 update 함.
        
        //Hibernate: 
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer 
        //    from
        //        board b1_0 
        //    where
        //        b1_0.bno=?
        //Hibernate: 
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer 
        //    from
        //        board b1_0 
        //    where
        //        b1_0.bno=?
        //Hibernate: 
        //    update
        //        board 
        //    set
        //        content=?,
        //        moddate=?,
        //        title=?,
        //        writer=? 
        //    where
        //        bno=?

    }   // testUpdate 메서드 종료

    @Test   // D
    public void testDelete() {

        Long bno = 1L;

        boardRepository.deleteById(bno);
        //             .deleteById(bno); : delete from board where bno = bno;랑 같은 의미다.
        
        //Hibernate: 
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer 
        //    from
        //        board b1_0 
        //    where
        //        b1_0.bno=?
        //Hibernate: 
        //    delete 
        //    from
        //        board 
        //    where
        //        bno=?

    }   // testDelete 메서드 종료

    @Test
    public void  testPaging() {
        // .findAll(); : 모든 리스트를 출력하는 메서드 select * from board;
        // 전체 리스트에 페이징과 정렬 기법도 추가 해보자.

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        //                                      시작번호, 페이지당 데이터 갯수
        //                                                             번호를 기준으로 내림차순 정렬

        //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    order by
        //        b1_0.bno desc     (bno를 기준으로 내림차순 정렬)
        //    limit
        //        ?, ?              (시작번호, 끝번호)
        //Hibernate:
        //    select
        //        count(b1_0.bno)   (board 전체 리스트 수를 알아옴)
        //    from
        //        board b1_0

        Page<Board> result = boardRepository.findAll(pageable);
        // 1장에 종이에 Board 객체를 가지고 있는 결과는 result에 담긴다.
        // Page 클래스는 다음페이지 존재 여부, 이전페이지 존재 여부, 전체 데이터 개수, 등등...을 계산한다.

        log.info("전체 게시물 수 : " + result.getTotalElements());
        log.info("총 페이지 수 : " + result.getTotalPages());
        log.info("현재 페이지 번호 : " + result.getNumber());
        log.info("페이지당 데이터 갯수 : " + result.getSize());
        log.info("다음 페이지 여부 ; " + result.hasNext());
        log.info("시작 페이지 여부 : " + result.isFirst());

        //2025-07-16T15:18:37.564+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 전체 게시물 수 : 99
        //2025-07-16T15:18:37.573+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 총 페이지 수 : 10
        //2025-07-16T15:18:37.574+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 현재 페이지 번호 : 0
        //2025-07-16T15:18:37.575+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 페이지당 데이터 갯수 : 10
        //2025-07-16T15:18:37.576+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 다음 페이지 여부 ; true
        //2025-07-16T15:18:37.577+09:00  INFO 11096 --- [board] [    Test worker] o.m.b.repository.BoardRepositoryTests    : 시작 페이지 여부 : true
        
        // 콘솔에 결과를 출력해보자
        List<Board> boardList = result.getContent();    // 페이징처리된 내용을 가져와라
        
        boardList.forEach(board -> log.info(board));
        // forEach는 인덱스를 사용하지 않고 앞에서부터 객체를 리턴한다.
        //                  board -> log.info(board);
        //                        -> : 람다식 1개의 명령어가 있을 때 활용
        
    }   // testPaging 메서드 종료

}   // class 종료
