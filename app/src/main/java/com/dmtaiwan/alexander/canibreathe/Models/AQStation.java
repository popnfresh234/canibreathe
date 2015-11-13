package com.dmtaiwan.alexander.canibreathe.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class AQStation implements Parcelable {

    public AQStation() {

    }

    private String SiteName;
    private String County;
    private String PSI;
    private String MajorPollutant;
    private String Status;
    private String SO2;
    private String CO;
    private String O3;
    private String PM10;
    private String PM25;
    private String NO2;
    private String WindSpeed;
    private String WindDirec;
    private String FPMI;
    private String NOx;
    private String NO;
    private String PublishTime;

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String SiteName) {
        this.SiteName = SiteName;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String County) {
        this.County = County;
    }

    public String getPSI() {
        return PSI;
    }

    public void setPSI(String PSI) {
        this.PSI = PSI;
    }

    public String getMajorPollutant() {
        return MajorPollutant;
    }

    public void setMajorPollutant(String MajorPollutant) {
        this.MajorPollutant = MajorPollutant;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getSO2() {
        return SO2;
    }


    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String O3) {
        this.O3 = O3;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getNO2() {
        return NO2;
    }

    public void setNO2(String NO2) {
        this.NO2 = NO2;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String WindSpeed) {
        this.WindSpeed = WindSpeed;
    }

    public String getWindDirec() {
        return WindDirec;
    }

    public void setWindDirec(String WindDirec) {
        this.WindDirec = WindDirec;
    }

    public String getFPMI() {
        return FPMI;
    }

    public void setFPMI(String FPMI) {
        this.FPMI = FPMI;
    }

    public String getNOx() {
        return NOx;
    }

    public void setNOx(String NOx) {
        this.NOx = NOx;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String PublishTime) {
        this.PublishTime = PublishTime;
    }


    protected AQStation(Parcel in) {
        SiteName = in.readString();
        County = in.readString();
        PSI = in.readString();
        MajorPollutant = in.readString();
        Status = in.readString();
        SO2 = in.readString();
        CO = in.readString();
        O3 = in.readString();
        PM10 = in.readString();
        PM25 = in.readString();
        NO2 = in.readString();
        WindSpeed = in.readString();
        WindDirec = in.readString();
        FPMI = in.readString();
        NOx = in.readString();
        NO = in.readString();
        PublishTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SiteName);
        dest.writeString(County);
        dest.writeString(PSI);
        dest.writeString(MajorPollutant);
        dest.writeString(Status);
        dest.writeString(SO2);
        dest.writeString(CO);
        dest.writeString(O3);
        dest.writeString(PM10);
        dest.writeString(PM25);
        dest.writeString(NO2);
        dest.writeString(WindSpeed);
        dest.writeString(WindDirec);
        dest.writeString(FPMI);
        dest.writeString(NOx);
        dest.writeString(NO);
        dest.writeString(PublishTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AQStation> CREATOR = new Parcelable.Creator<AQStation>() {
        @Override
        public AQStation createFromParcel(Parcel in) {
            return new AQStation(in);
        }

        @Override
        public AQStation[] newArray(int size) {
            return new AQStation[size];
        }
    };
}