package com.example.lansiasmart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserData extends ViewModel {
    private MutableLiveData<String> name = new MutableLiveData<String>() ;
    private int age;
    private String gender;

    public UserData(String name, int age, String gender) {
        this.name.setValue(name);
        this.age = age;
        this.gender = gender;
    }

    public UserData() {
        super();
    }

    void setUserData(String name, int age, String gender) {
        this.name.setValue(name);
        this.age = age;
        this.gender = gender;
    }

    public MutableLiveData<String> getName () {
        if (name == null) {
            name = new MutableLiveData<String>();
        }
        return this.name;
    }

    int getAge () {
        return this.age;
    }

    String getGender () {
        return this.gender;
    }

    void removeUserData () {
        this.name.setValue(null);
        this.age = 0;
        this.gender = null;
    }
}
