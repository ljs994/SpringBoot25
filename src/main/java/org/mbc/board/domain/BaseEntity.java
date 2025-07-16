package org.mbc.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass                                           // 공통적인 최상위 Class
@EntityListeners(value = {AuditingEntityListener.class})    // 감시용 Class 명시
@Getter                                                     // 날짜용 처리만(sysdate) ->  DB 날짜를 가져오기만 하겠다.(보안때문)
abstract class BaseEntity {                                 // abstract : 추상적인 -> 자체적으로 실행X
    // 모든 테이블에 공통적으로 사용되는 필드를 만든다.

    @CreatedDate                                            // 등록일 용
    @Column(name = "regdate", updatable = false)            // updatable = false 수정 못하게 만듬
    private LocalDateTime regDate;                          // 등록일

    @LastModifiedDate                                       // 수정일 용
    @Column(name = "moddate")                               // DB 필드명만 지정
    private LocalDateTime modDate;                          // 수정일
}
