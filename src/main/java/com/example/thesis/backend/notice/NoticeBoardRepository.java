package com.example.thesis.backend.notice;

import com.example.thesis.backend.floor.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
    NoticeBoard findByOwner(Floor owner);

    NoticeBoard findByName(String name);
}
