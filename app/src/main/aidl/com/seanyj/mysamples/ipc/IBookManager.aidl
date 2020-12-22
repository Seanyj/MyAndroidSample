// IBookManager.aidl
package com.seanyj.mysamples.ipc;

// Declare any non-default types here with import statements
import com.seanyj.mysamples.ipc.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
