package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reader2book")
public class Reader2Book {
    @Id
    public UUID uuid = UUID.randomUUID();

    @Column(length = 20)
    private String Book_ISBN;

    @Column
    private Date Borrow_Date;

    @Column
    private Date Return_Date;

    @Column(length = 13)
    private String Reader_ID;

    @Column
    private boolean Book_State;

    @Column
    private boolean Borrow_State;

    @Column
    private int Delay_Times;

    @Column
    private int Fine_Amount;

    public Reader2Book() {
        this.Book_ISBN = "";
        this.Borrow_Date = new Date();
        this.Return_Date = new Date();
        this.Reader_ID = "";
        this.Book_State = true;
        this.Borrow_State = false;
        this.Delay_Times = 0;
        this.Fine_Amount = 0;
    }

    public Reader2Book(String Book_ISBN, Date Borrow_Date, Date Return_Date, String Reader_ID, boolean Book_State, boolean Borrow_State, int Delay_Times, int Fine_Amount) {
        this.Book_ISBN = Book_ISBN;
        this.Borrow_Date = Borrow_Date;
        this.Return_Date = Return_Date;
        this.Reader_ID = Reader_ID;
        this.Book_State = Book_State;
        this.Borrow_State = Borrow_State;
        this.Delay_Times = Delay_Times;
        this.Fine_Amount = Fine_Amount;
    }

    public String getBook_ISBN() {
        return Book_ISBN;
    }

    public void setBook_ISBN(String Book_ISBN) {
        this.Book_ISBN = Book_ISBN;
    }


    public Date getBorrow_Date() {
        return Borrow_Date;
    }

    public void setBorrow_Date(Date Borrow_Date) {
        this.Borrow_Date = Borrow_Date;
    }

    public Date getReturn_Date() {
        return Return_Date;
    }

    public void setReturn_Date(Date Return_Date) {
        this.Return_Date = Return_Date;
    }

    public String getReader_ID() {
        return Reader_ID;
    }

    public void setReader_ID(String Reader_ID) {
        this.Reader_ID = Reader_ID;
    }

    public boolean isBook_State() {
        return Book_State;
    }

    public void setBook_State(boolean Book_State) {
        this.Book_State = Book_State;
    }

    public boolean isBorrow_State() {
        return Borrow_State;
    }

    public void setBorrow_State(boolean Borrow_State) {
        this.Borrow_State = Borrow_State;
    }

    public int getDelay_Times() {
        return Delay_Times;
    }

    public void setDelay_Times(int Delay_Times) {
        this.Delay_Times = Delay_Times;
    }

    public int getFine_Amount() {
        return Fine_Amount;
    }

    public void setFine_Amount(int Fine_Amount) {
        this.Fine_Amount = Fine_Amount;
    }


}
