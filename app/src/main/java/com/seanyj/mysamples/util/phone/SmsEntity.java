package com.seanyj.mysamples.util.phone;

public class SmsEntity {
    private long smsId;
    private String address;
    private String body;
    private transient long person;
    private long date;
    private int type;
    private long thread_id;

    @Override
    public String toString() {
        return "smsId: " + smsId + ", "
                + "address: " + address + ", "
                + "person: " + person + ", "
                + "body: " + body + ", "
                + "date: " + date + ", "
                + "type: " + type + ", "
                + "thread_id: " + thread_id;
    }

    public long getSmsId() {
        return smsId;
    }

    public void setSmsId(long smsId) {
        this.smsId = smsId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getPerson() {
        return person;
    }

    public void setPerson(long person) {
        this.person = person;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getThread_id() {
        return thread_id;
    }

    public void setThread_id(long thread_id) {
        this.thread_id = thread_id;
    }
}


