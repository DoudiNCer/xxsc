package com.sipc.xxsc.pojo.dto.result.recommend;

import com.sipc.xxsc.pojo.domain.Book;
import lombok.Data;

@Data
public class BooksResult {
    private Integer id;
    private String author;
    private String resourceUrl;
    private String photoUrl;
    private String name;
    private String introduce;

    public BooksResult(Book book) {
        this.id = book.getId();
        this.author = book.getAuthor();
        this.resourceUrl = book.getResourceUrl();
        this.photoUrl = book.getPhotoUrl();
        this.name = book.getName();
        this.introduce = book.getIntroduce();
    }
}
