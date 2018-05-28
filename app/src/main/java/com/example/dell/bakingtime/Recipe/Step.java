package com.example.dell.bakingtime.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Step implements Parcelable, Serializable{

    private int id;
    private String shortDescription;
    private String longDescription;
    private String videoUrl;
    private String thumbnailUrl;

    public Step(int id, String shortDescription, String longDescription, String videoUrl, String thumbnailUrl){
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        longDescription = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public boolean compare(Step stepToCompare){
        if(this.id != stepToCompare.id)
            return false;
        if(!this.shortDescription.equals(stepToCompare.shortDescription))
            return false;
        if(!this.longDescription.equals(stepToCompare.longDescription))
            return false;
        if(!this.videoUrl.equals(stepToCompare.videoUrl))
            return false;
        if(!this.thumbnailUrl.equals(stepToCompare.thumbnailUrl))
            return false;
        return true;
    }
}
