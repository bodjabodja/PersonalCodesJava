import javax.persistence.*;

/**
 * Created by Богдан on 11.10.2016.
 */


@Entity
@Table(name="Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "bookname", insertable = false, updatable = false)
    private String author;
    private String bookname;

    public Book(){

    }

    public Book(String bookname, String author){
        this.bookname=bookname;
        this.author=author;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getBookname(){
        return bookname;
    }

    public void setBookname(String bookname){
        this.bookname=bookname;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    @Override
    public String toString(){
        return "Book{"+"id="+id+
        ", author= "+author+
                ", bookname= "+bookname +"}";
    }

}
