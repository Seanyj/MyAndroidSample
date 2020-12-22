package com.seanyj.mysamples.util.phone;

public class CallEntity {
    public static final int CALL_IN = 1;
    public static final int CALL_OUT = 2;

    private String callId;
    private int type;//1 呼入 2呼出
    private String date;
    private long duration;//通话时长
    private String phoneNumber;
    private String name;
    private transient long timeStamp;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("callId: " + callId + ", ");
        sb.append("type: " + (type == CALL_IN ? "in" : type == CALL_OUT ? "out" : "unknown") + ", ");
        sb.append("date: " + date + ", ");
        sb.append("duration: " + duration + ", ");
        sb.append("phoneNumber: " + phoneNumber + ", ");
        sb.append("timeStamp: " + timeStamp + ", ");
        sb.append("name: " + name);

        return sb.toString();
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
