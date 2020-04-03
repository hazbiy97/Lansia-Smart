package com.example.lansiasmart;

import androidx.fragment.app.Fragment;

public interface UserDataInterface {
    void setUserData(String name, int age, String gender);
    UserData getUserData();
    void removeUserData();
    void showTopLevelFragment(Fragment fragment, String TAG);
}
