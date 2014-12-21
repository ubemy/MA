package com.ma.schiffeversenken.android.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GamePreferences implements Parcelable{
	
	public static final String GAME_PREFERENCES_TAG = "GamePreferences";
	
	public static final int MAX_ZERSTOERER_NUMBER = 1;
	public static final int MIN_ZERSTOERER_NUMBER = 0;
	public static final int MAX_SCHLACHTSCHIFF_NUMBER = 2;
	public static final int MIN_SCHLACHTSCHIFF_NUMBER = 0;
	public static final int MAX_UBOOT_NUMBER = 3;
	public static final int MIN_UBOOT_NUMBER = 0;
	public static final int MAX_KREUZER_NUMBER = 4;
	public static final int MIN_KREUZER_NUMBER = 0;
	public static final int MAX_SCHWIERIGKEIT_NUMBER = 3;
	public static final int MIN_SCHWIERIGKEIT_NUMBER = 1;
	
	public static final int DEFAULT_ZERSTOERER_NUMBER = 1;
	public static final int DEFAULT_SCHLACHTSCHIFF_NUMBER = 2;
	public static final int DEFAULT_UBOOT_NUMBER = 3;
	public static final int DEFAULT_KREUZER_NUMBER = 4;
	public static final int DEFAULT_SCHWIERIGKEIT_NUMBER = 1;
	
	private int mZerstoererNumber;
	private int mSchlachtschiffNumber;
	private int mUbootNumber;
	private int mKreuzerNumber;
	private int mSchwierigkeitStufe;
	
	public GamePreferences () {
		mZerstoererNumber = DEFAULT_ZERSTOERER_NUMBER;
		mSchlachtschiffNumber = DEFAULT_SCHLACHTSCHIFF_NUMBER;
		mUbootNumber = DEFAULT_UBOOT_NUMBER;
		mKreuzerNumber = DEFAULT_KREUZER_NUMBER;
		mSchwierigkeitStufe = DEFAULT_SCHWIERIGKEIT_NUMBER;
	}
	
	private GamePreferences (Parcel in) {
		mZerstoererNumber = in.readInt();
		mSchlachtschiffNumber = in.readInt();
		mUbootNumber = in.readInt();
		mKreuzerNumber = in.readInt();
		mSchwierigkeitStufe = in.readInt();
	}

	public int getZerstoererNumber() {
		return mZerstoererNumber;
	}

	public void setZerstoererNumber(int s) {
		this.mZerstoererNumber = s;
	}
	
	

	public int getSchlachtschiffNumber() {
		return mSchlachtschiffNumber;
	}

	public void setSchlachtschiffNumber(int s) {
		this.mSchlachtschiffNumber = s;
	}

	public int getUbootNumber() {
		return mUbootNumber;
	}

	public void setUbootNumber(int s) {
		this.mUbootNumber = s;
	}

	public int getKreuzerNumber() {
		return mKreuzerNumber;
	}

	public void setKreuzerNumber(int s) {
		this.mKreuzerNumber = s;
	}

	public int getSchwierigkeitStufe() {
		return mSchwierigkeitStufe;
	}

	public void setSchwierigkeitStufe(int s) {
		this.mSchwierigkeitStufe = s;
	}

	public void writeToParcel(Parcel out, int flags)  {
		out.writeInt(mZerstoererNumber);
		out.writeInt(mSchlachtschiffNumber);
		out.writeInt(mUbootNumber);
		out.writeInt(mKreuzerNumber);
		out.writeInt(mSchwierigkeitStufe);
	}
	
	public static final Parcelable.Creator<GamePreferences> CREATOR
		= new Parcelable.Creator<GamePreferences>() {
			
			public GamePreferences createFromParcel (Parcel in) {
				return new GamePreferences(in);
			}

			public GamePreferences[] newArray(int size) {
				return new GamePreferences[size];
			}
		};
		
	public int describeContents() {
		return 0;
	}
	
	public int getTotalShips() {
		return mZerstoererNumber +
				mSchlachtschiffNumber + 
				mUbootNumber + 
				mKreuzerNumber;
	}
		
}
