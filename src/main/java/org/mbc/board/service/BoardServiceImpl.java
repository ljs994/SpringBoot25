package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service                    // 스프링에게 서비스 계층임을 알린다.
@Log4j2
@RequiredArgsConstructor    // 필드 값을 보고 생성자를 만든다. final 필드나 @Nonnull이 붙은 필드용
@Transactional              // commit용 (여러개의 테이블이 조합될때 해결 역할을 한다.)
public class BoardServiceImpl implements BoardService{
    
    private final ModelMapper modelMapper;          // 엔티티와 dto를 변환

    private final BoardRepository boardRepository;  // jpa용 클래스 (CRUD, 페이징, 정렬, 다중검색)
    
    @Override
    public Long register(BoardDTO boardDTO) {       // 조원이 실행코드를 만든다.
        //  폼에서 넘어온 DTO가 DB에 기록되어야 한다.

        Board board = modelMapper.map(boardDTO, Board.class);   // 엔티티가 dto로 변환

        Long bno = boardRepository.save(board).getBno();
        //                    insert into board ~~~~ -> bno를 받는다.

        return bno; // 프론트에 게시물 저장 후 번호가 전달 된다.
    }   // register 메서드 종료

    @Override
    public BoardDTO readone(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        return boardDTO;
    }   // BoardDTO 메서드 종료

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        boardRepository.save(board);

    }   // modify 메서드 종료

    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }
}
