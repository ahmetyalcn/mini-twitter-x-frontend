package com.workintech.twitter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post", schema = "twitter")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tweet_type")
    private TweetType tweetType;

    @Column(name = "post_date")
    @CreatedDate
    private LocalDateTime postDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replies ;

    public void addLike(Like like){
        if (likes==null){
            likes = new HashSet<>();
        }
        likes.add(like);
    }

    public void addReply(Reply reply){
        if (replies==null){
            replies = new ArrayList<>();
        }
        replies.add(reply);
    }
}
