package com.harbor.realm;

import io.realm.RealmObject;

/**
 * Created by Harbor on 17/07/05.
 */

public class User2 extends RealmObject {
    public String name;
    public int age;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}