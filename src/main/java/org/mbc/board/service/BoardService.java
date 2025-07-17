package org.mbc.board.service;

import org.mbc.board.dto.BoardDTO;

public interface BoardService {
    // 조장용 코드 -> 시그니처만 필요 -> Impl 구현클래스 -> 실행문 만든다.
    Long register(BoardDTO boardDTO);

    BoardDTO readone(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

}
