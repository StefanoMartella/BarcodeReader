package stefano_martella.barcodereader.roomdatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "items") public class Item {

    @android.support.annotation.NonNull
    @PrimaryKey(autoGenerate = false)
    private String code;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "floor")
    private String floor;

    @ColumnInfo(name = "room")
    private String room;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "number")
    private int number;

    @ColumnInfo(name = "idOperator")
    private int idOperator;

    @ColumnInfo(name = "idWarehouse")
    private int idWarehouse;

    @NonNull
    public String getCode() {
        return code;
    }
    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getIdOperator() {
        return idOperator;
    }
    public void setIdOperator(int idOperator) {
        this.idOperator = idOperator;
    }

    public int getIdWarehouse() {
        return idWarehouse;
    }
    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

}


