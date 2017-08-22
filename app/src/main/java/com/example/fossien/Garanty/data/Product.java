package com.example.fossien.Garanty.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by Fossien on 19/07/2017.
 */

public class Product implements Parcelable{

    private String marque;
    private String name;
    private int price;
    private String image;
    private String Category;
    private String createdAt;
    private int qte;

    public Product(String marque, String name, int price, String image, String category, int qte) {
        this.marque = marque;
        this.name = name;
        this.price = price;
        this.image = image;
        this.Category = category;
        this.qte = qte;
        this.createdAt = Calendar.getInstance().getTime().toString();
    }

    public Product() {

    }

    public Product(String marque, String name, int price, String image) {
        this.marque = marque;
        this.name = name;
        this.price = price;
        this.image = image;
        this.qte = 0;
        this.Category = "";
        this.createdAt = Calendar.getInstance().getTime().toString();
    }

    protected Product(Parcel in) {
        marque = in.readString();
        name = in.readString();
        price = in.readInt();
        image = in.readString();
        Category = in.readString();
        qte = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getCreatedAt() {return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(marque);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeString(image);
        parcel.writeString(Category);
        parcel.writeString(createdAt);
        parcel.writeInt(qte);
    }
}
