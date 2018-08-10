package com.hzyc.csj.ordermealsystem.model;

/**
 * Created by 小柿子 on 2018/8/3.
 */
public class Delicious {
    private int id;
    private String name;
    private String price;
    private float tuij;
    private String photo;
    private String kind;

    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public float getTuij() {
        return tuij;
    }
    public void setTuij(float tuij) {
        this.tuij = tuij;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    @Override
    public String toString() {
        return "Delicious [id=" + id + ", name=" + name + ", price=" + price + ", tuij=" + tuij + ", photo=" + photo
                + ", kind=" + kind + "]";
    }
}
