package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.customerCenter.QnAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnARepository extends JpaRepository<QnAEntity, Integer> {
    // 전체 게시글 개수를 조회하는 메서드 추가
    @Query("SELECT COUNT(q) FROM QnAEntity q")
    int countAllQnA();

        /**
         * 로그인한 멤버 email이랑 매치되는 qna 리스트 불러오기
         */
        @Query("SELECT mr FROM QnAEntity mr " +
                "JOIN MemberEntity m ON mr.user_email = m.email " +
                "WHERE m.email = :user_email " +
                "ORDER BY mr.createdAt DESC")  // 최근글부터 나오게 정렬 추가
        Page<QnAEntity> findQnaByEmail(@Param("user_email") String email, Pageable pageable);

    @Query("SELECT count(mr.id) FROM QnAEntity mr " +
            "JOIN MemberEntity m ON mr.user_email = m.email " +
            "WHERE m.email = :user_email " +
            "ORDER BY mr.createdAt DESC")  // 최근글부터 나오게 정렬 추가
    Long findQnaByEmai0lCount(@Param("user_email") String email);

    }
