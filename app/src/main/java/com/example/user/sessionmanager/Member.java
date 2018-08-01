package com.example.user.sessionmanager;

/**
 * Created by User on 2018/4/3.
 */

public class Member {

    private int mId;
    private String mName;
    private int mAge;
    private Gender mGender;

    public int getId() {
        return mId;
    }

    public Member setId(int id) {
        mId = id;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Member setName(String name) {
        mName = name;
        return this;
    }

    public int getAge() {
        return mAge;
    }

    public Member setAge(int age) {
        mAge = age;
        return this;
    }

    public Gender getGender() {
        return mGender;
    }

    public Member setGender(Gender gender) {
        mGender = gender;
        return this;
    }
}
