package com.example.yszm.learningword.model;

import android.os.Parcelable;
import android.os.Parcel;
/**
 * @author 佐达.
 * on 2019/5/20 15:51
 */
public class Word  implements Parcelable{


    private int id;
    //值
    private String Key;
    //音标
    private String Phono;
    //解释
    private String Trans;
    //示例
    private String Example;
    private int Unit;
    private int status;
    public Word(int id, String key, String phono, String trans, String example, int unit) {
        this.id = id;
        Key = key;
        Phono = phono;
        Trans = trans;
        Example = example;
        Unit = unit;
    }
    public Word (String key)
    {
        Key = key;
    }

    public Word(String key, String phono, String trans,int status) {
        Key = key;
        Phono = phono;
        Trans = trans;
        this.status = status;
    }

    public Word(){

    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return Key;
    }

    public String getPhono() {
        return Phono;
    }

    public String getTrans() {
        return Trans;
    }

    public String getExample() {
        return Example;
    }

    public int getUnit() {
        return Unit;
    }
    public int getStatus() {
        return status;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setPhono(String phono) {
        Phono = phono;
    }

    public void setTrans(String trans) {
        Trans = trans;
    }

    public void setExample(String example) {
        Example = example;
    }

    public void setUnit(int unit) {
        Unit = unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.Key);
        dest.writeString(this.Phono);
        dest.writeString(this.Trans);
        dest.writeString(this.Example);
        dest.writeInt(this.Unit);
    }

    protected Word(Parcel in) {
        this.id = in.readInt();
        this.Key = in.readString();
        this.Phono = in.readString();
        this.Trans = in.readString();
        this.Example = in.readString();
        this.Unit = in.readInt();
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };


}
