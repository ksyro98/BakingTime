package com.example.dell.bakingtime.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Step implements Parcelable, Serializable{

    private int id;
    private String shortDescription;
    private String longDescription;
    private String videoUrl;
    private String thumbnailUrl;


    /**
     * Constructor
     */
    public Step(int id, String shortDescription, String longDescription, String videoUrl, String thumbnailUrl){
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }


    /**
     * getters
     */
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


    /**
     * setter
     */
    public void setId(int id) {
        this.id = id;
    }

    private Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        longDescription = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    /**
     * parcelable methods
     */
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


    /**
     * Used instead of equals, because equals doesn't work as intended.
     * I didn't override equals because the I should override hashCode() too.
     * @param stepToCheck the step to check if it is the same
     * @return true if the ingredients are the same, false otherwise
     */
    boolean checkIfEquals(Step stepToCheck){
        if(this.id != stepToCheck.id) {
            return false;
        }
        if(!this.shortDescription.equals(stepToCheck.shortDescription)) {
            return false;
        }
        if(!this.longDescription.equals(stepToCheck.longDescription)) {
            return false;
        }
        if(!this.videoUrl.equals(stepToCheck.videoUrl)) {
            return false;
        }
        if(!this.thumbnailUrl.equals(stepToCheck.thumbnailUrl)) {
            return false;
        }
        return true;
    }
}
