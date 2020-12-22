package com.seanyj.mysamples.util.phone;

import java.util.List;

public class ContactEntity {
    private String contactId;
    private String name;
    private long date;
    private List<String> numbers;
    private transient String address;
    private transient String email;
    private transient String company;
    private transient String photoId;
    private transient String lookupKey;

    @Override
    public String toString() {
        return "contactId: " + contactId + ", " + "name: " + name + ", " + "date: "
                + date + ", " +
                "address: " + address + ", "
                + "email: " + email + ", " + "company: " + company + ", " + "numbers: " + numbers + ", "
                + "photoId: " + photoId + ", " + "lookupKey: " + lookupKey;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }
}
