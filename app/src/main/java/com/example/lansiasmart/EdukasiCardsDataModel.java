package com.example.lansiasmart;

class EdukasiCardsDataModel {
    private String title;
    private int id_;
    private int image;

    EdukasiCardsDataModel(String name, int id_, int image) {
        this.title = name;
        this.id_ = id_;
        this.image=image;
    }

    String getTitle() {
        return title;
    }


    int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}
