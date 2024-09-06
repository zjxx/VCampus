package app.vcampus.domain;



import jakarta.persistence.*;//为什么换成*:因为jpa的注解都在这个包里，所以可以省略包名

@Entity
@Table(name = "book") //指定表名
public class Book {
    @Id //主键
    @Column(length = 30)
    private String BookName;//书名

    @Column(length = 50)
    private String Author;

    @Column(length = 50)
    private String Publisher;

    @Column(length = 13)
    private String ISBN;

//    @Column(length = 20)
//    private String Cover;

    @Column(length = 15)
    private String Language;

    @Column(length = 20)
    private String Kind;

    @Column
    private Integer PublishedYear;

    @Column
    private String Description;

    @Column
    private Integer Quantity;

    @Column
    private Integer Valid_Quantity;


    public Book() {
        this.BookName = "";
        this.Author = "";
        this.Publisher = "";
        this.ISBN = "";
        //this.Cover = "";
        this.Language = "";
        this.Kind = "";
        this.PublishedYear = 0;
        this.Description = "";
        this.Quantity = 0;
        this.Valid_Quantity = 0;
    }

    public Book(String BookName, String Author, String Publisher, String ISBN, String Language, String Kind, Integer PublishedYear, String Description, int Quantity, int Valid_Quantity) {
        this.BookName = BookName;
        this.Author = Author;
        this.Publisher = Publisher;
        this.ISBN = ISBN;
        //this.Cover = Cover;
        this.Language = Language;
        this.Kind = Kind;
        this.PublishedYear = PublishedYear;
        this.Description = Description;
        this.Quantity = Quantity;
        this.Valid_Quantity = Quantity;
    }



    public String getBookName() {return BookName;}
    public void setBookName(String BookName) {this.BookName = BookName;}


    public String getAuthor() {return Author;}
    public void setAuthor(String Author) {this.Author = Author;}


    public String getPublisher() {return Publisher;}
    public void setPublisher(String Publisher) {this.Publisher = Publisher;}


    public String getISBN() {return ISBN;}
    public void setISBN(String ISBN) {this.ISBN = ISBN;}


//    public String getCover() {return Cover;}
//    public void setCover(String Cover) {this.Cover = Cover;}


    public String getLanguage() {return Language;}
    public void setLanguage(String Language) {this.Language = Language;}


    public String getKind() {return Kind;}
    public void setKind(String Kind) {this.Kind = Kind;}

    public Integer getPublishedYear() {return PublishedYear;}
    public void setPublishedYear(Integer PublishedYear) {this.PublishedYear = PublishedYear;}

    public String getDescription() {return Description;}
    public void setDescription(String Description) {this.Description = Description;}

    public int getQuantity() {return Quantity;}
    public void setQuantity(int Quantity) {this.Quantity = Quantity;}

    public int getValid_Quantity() {return Valid_Quantity;}
    public void setValid_Quantity(int Valid_Quantity) {this.Valid_Quantity = Valid_Quantity;}
}
