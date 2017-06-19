package com.example.android.miwok;


/**
 * Created by Edmunds_TDL on 09/04/2017.
 */

public class Word {


    private String mDefaultTranslation;
    private String mMivokTranslation;
    private int mImageResourceID = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceID;
    private int mPlayIcon;

    public Word (String defaultTranslation, String mivokTranslation, int audioResourceID, int playIconID){
        mDefaultTranslation = defaultTranslation;
        mMivokTranslation = mivokTranslation;
        mAudioResourceID = audioResourceID;
        mPlayIcon = playIconID;
    }

    public Word (String defaultTranslation, String mivokTranslation, int imageResourceID, int audioResourceID,int mPlayIconID ){
        mDefaultTranslation = defaultTranslation;
        mMivokTranslation = mivokTranslation;
        mImageResourceID = imageResourceID;
        mAudioResourceID = audioResourceID;
        mPlayIcon = mPlayIconID;
    }

    public String getMivokTranslation() {
        return mMivokTranslation;
    }

    public String getDdefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getImageResourceID() { return mImageResourceID; }

    public int getAudioResourceID() { return mAudioResourceID; }

    public boolean hasImage (){
        return mImageResourceID != NO_IMAGE_PROVIDED;
    }

    public int getPlayIconID() {return mPlayIcon;}
}
