package com.virtukch.nest.post.model;

import com.virtukch.nest.common.model.BaseTimeEntity;
import com.virtukch.nest.member.model.Member;
import com.virtukch.nest.post_tag.model.PostTag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private Integer viewCount = 0; // 조회수

    @Builder
    public Post(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    // 연관관계 편의 메서드
    public void addPostTag(PostTag postTag) {
        postTags.add(postTag);
        postTag.setPost(this);
    }

    public void removePostTag(PostTag postTag) {
        postTags.remove(postTag);
        postTag.setPost(null); //orphanRemoval = true 이므로 자동으로 postTag 필드도 삭제됨.
    }
}
