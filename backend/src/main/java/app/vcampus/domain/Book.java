package app.vcampus.domain;



import jakarta.persistence.*;//为什么换成*:因为jpa的注解都在这个包里，所以可以省略包名

@Entity
@Table(name = "book") //指定表名
public class Book {
    @Id //主键
    @Column(length = 20)
    private String BookName;//书名

    @Column(length = 20)
    private String Author;

    @Column(length = 20)
    private String Publisher;

    @Column(length = 20)
    private String ISBN;

    @Column(length = 20)
    private String Cover;

    @Column(length = 20)
    private String Language;

    @Column(length = 20)
    private String Kind;

    @Column()
    private Integer PublishedYear;

    public String getBookName() {return BookName;}
    public void setBookName(String BookName) {this.BookName = BookName;}


    public String getAuthor() {return Author;}
    public void setAuthor(String Author) {this.Author = Author;}


    public String getPublisher() {return Publisher;}
    public void setPublisher(String Publisher) {this.Publisher = Publisher;}


    public String getISBN() {return ISBN;}
    public void setISBN(String ISBN) {this.ISBN = ISBN;}


    public String getCover() {return Cover;}
    public void setCover(String Cover) {this.Cover = Cover;}


    public String getLanguage() {return Language;}
    public void setLanguage(String Language) {this.Language = Language;}


    public String getKind() {return Kind;}
    public void setKind(String Kind) {this.Kind = Kind;}

    public Integer getPublishedYear() {return PublishedYear;}
    public void setPublishedYear(Integer PublishedYear) {this.PublishedYear = PublishedYear;}

}
