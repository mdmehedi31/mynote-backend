package com.mynoteservice.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_posts")
@CompoundIndexes(
        {
                @CompoundIndex(name = "created_by_created_at_idx",def = "{'createdById':1, 'createdAt':-1}"),
                @CompoundIndex(name = "global_created_at_desc_idx", def = "{'createdAt':-1}")
        }
)
public class UserPostEntity {

    @Id
    private String id;
    private String content;
    private String createdById;
    private LocalDateTime createdAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
