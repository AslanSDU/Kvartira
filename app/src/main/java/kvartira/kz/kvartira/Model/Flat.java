package kvartira.kz.kvartira.Model;

/**
 * Created by Aslan on 29.03.2016.
 */
public class Flat {

    private int id;
    private String name;
    private String surname;
    private String photo;
    private String city;
    private String phone_number;
    private String address;
    private int room;
    private int price;
    private int floor;
    private int floors;
    private int type;
    private String notation;
    private int residence;
    private String date;
    private String time;

    public Flat(int id, String name, String surname, String photo, String city, String phone_number, String address, int room, int price, String notation, int residence, String date, String time) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.photo = photo;
        this.city = city;
        this.phone_number = phone_number;
        this.address = address;
        this.room = room;
        this.price = price;
        this.notation = notation;
        this.residence = residence;
        this.date = date;
        this.time = time;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public int getResidence() {
        return residence;
    }

    public void setResidence(int residence) {
        this.residence = residence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
