package com.virtukch.nest.notice.repository;

import com.virtukch.nest.notice.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    boolean existsByNoticeTypeAndNumber(String noticeType, Long number);

    Page<Notice> findByNoticeTypeOrderByPostDateDesc(String noticeType, Pageable pageable);
    Page<Notice> findByNoticeTypeAndTitleContainingOrderByPostDateDesc(String noticeType, String title, Pageable pageable);
    Page<Notice> findByNoticeTypeAndWriterContainingOrderByPostDateDesc(String noticeType, String writer, Pageable pageable);

    // 제목과 작성자를 모두 검색 (ALL 타입)
    @Query("SELECT n FROM Notice n WHERE n.noticeType = :noticeType AND " +
            "(UPPER(n.title) LIKE UPPER(CONCAT('%', :keyword, '%')) OR " +
            "UPPER(n.writer) LIKE UPPER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY n.postDate DESC")
    Page<Notice> findByNoticeTypeAndTitleOrWriterContainingOrderByPostDateDesc(
            @Param("noticeType") String noticeType,
            @Param("keyword") String keyword,
            Pageable pageable);
}
