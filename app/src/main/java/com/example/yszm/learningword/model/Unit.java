package com.example.yszm.learningword.model;
/**
 * @author 佐达.
 * on 2019/5/21 13:50
 */
public class Unit {
    private int id ;
    private int unit_key;

    public Unit(int id, int unit_key) {
        this.id = id;
        this.unit_key = unit_key;
    }

    public int getId() {
        return id;
    }

    public int getUnit_key() {
        return unit_key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUnit_key(int unit_key) {
        this.unit_key = unit_key;
    }
}
