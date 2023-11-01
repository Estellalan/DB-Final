package com.wangpeng.bms.model;

import java.math.BigDecimal;

public class BookInfo {
    private Integer bookId;

    private String bookname;

    private String bookauthor;

    private BigDecimal bookprice;

    private Integer booktypeid;

    private String booktypename;

    private String bookdesc;

    private Byte isborrowed;

    private String bookimg;

    private int bookNum;

    private double bookSore;

    private int bookSoreCount;

    public int getBookNum() {
        return bookNum;
    }

    public void setBookNum(int bookNum) {
        this.bookNum = bookNum;
    }

    public double getBookSore() {
        return bookSore;
    }

    public void setBookSore(double bookSore) {
        this.bookSore = bookSore;
    }

    public int getBookSoreCount() {
        return bookSoreCount;
    }

    public void setBookSoreCount(int bookSoreCount) {
        this.bookSoreCount = bookSoreCount;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookid) {
        this.bookId = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public BigDecimal getBookprice() {
        return bookprice;
    }

    public void setBookprice(BigDecimal bookprice) {
        this.bookprice = bookprice;
    }

    public Integer getBooktypeid() {
        return booktypeid;
    }

    public void setBooktypeid(Integer booktypeid) {
        this.booktypeid = booktypeid;
    }

    public String getBooktypename() {
        return booktypename;
    }

    public void setBooktypename(String booktypename) {
        this.booktypename = booktypename;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    public Byte getIsborrowed() {
        return isborrowed;
    }

    public void setIsborrowed(Byte isborrowed) {
        this.isborrowed = isborrowed;
    }

    public String getBookimg() {
        return bookimg;
    }

    public void setBookimg(String bookimg) {
        this.bookimg = bookimg;
    }
}