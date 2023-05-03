package com.example.testchart.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Locale;


public class ShotData implements Parcelable {
    private short index;
    private String shot_date;
    private String shot_time;
    private short mode;
    private short club_code;
    private String club_nickname;
    private float loft_angle;
    private short unit;
    private float air_pressure;
    private float temperature;
    private float ball_speed;
    private float club_speed;
    private float launch_angle;
    private float carry;
    private float total;
    private float apex;
    private float spin_rate;

    private ShotData(Parcel in) {
        index = (short) in.readInt();
        shot_time = in.readString();
        mode = (short) in.readInt();
        club_code = (short) in.readInt();
        loft_angle = in.readFloat();
        unit = (short) in.readInt();
        air_pressure = in.readFloat();
        temperature = in.readFloat();
        ball_speed = in.readFloat();
        club_speed = in.readFloat();
        launch_angle = in.readFloat();
        carry = in.readFloat();
        total = in.readFloat();
        apex = in.readFloat();
        spin_rate = in.readFloat();
        club_nickname = "";
    }

    public ShotData() {
        carry = 0;
    }

    public ShotData(ShotData shotData) {
        this.index = shotData.getIndex();
        this.shot_date = shotData.getShot_date();
        this.shot_time = shotData.getShot_time();
        this.mode = shotData.getMode();
        this.club_code = shotData.getClub_code();
        this.loft_angle = shotData.getLoft_angle();
        this.unit = shotData.getUnit();
        this.air_pressure = shotData.getAir_pressure();
        this.temperature = shotData.getTemperature();
        this.ball_speed = shotData.getBall_speed();
        this.club_speed = shotData.getClub_speed();
        this.launch_angle = shotData.getLaunch_angle();
        this.carry = shotData.getCarry();
        this.total = shotData.getTotal();
        this.apex = shotData.getApex();
        this.spin_rate = shotData.getSpin_rate();
        this.club_nickname = shotData.getClub_nickname();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(shot_time);
        dest.writeInt(mode);
        dest.writeInt(club_code);
        dest.writeFloat(loft_angle);
        dest.writeInt(unit);
        dest.writeFloat(air_pressure);
        dest.writeFloat(temperature);
        dest.writeFloat(ball_speed);
        dest.writeFloat(club_speed);
        dest.writeFloat(launch_angle);
        dest.writeFloat(carry);
        dest.writeFloat(total);
        dest.writeFloat(apex);
        dest.writeFloat(spin_rate);
    }

    public static final Creator<ShotData> CREATOR = new Creator<ShotData>() {
        @Override
        public ShotData createFromParcel(Parcel source) {
            return new ShotData(source);
        }

        @Override
        public ShotData[] newArray(int size) {
            return new ShotData[0];
        }
    };

//    public void setData(BleDevToApp_ShotDataReq shotDataReq) {
//        int seq = shotDataReq.getSeq();
//
//        switch (seq) {
//            case 1:
//                index = shotDataReq.getIndex();
//                shot_date = shotDataReq.getShotDate();
//                shot_time = shotDataReq.getShotTime();
//                mode = shotDataReq.getMode();
//                club_code = shotDataReq.getClub();
//                loft_angle = shotDataReq.getLoftAngle();
//                unit = shotDataReq.getUnit();
//                break;
//            case 2:
//                air_pressure = shotDataReq.getPressure();
//                temperature = shotDataReq.getTemp();
//                ball_speed = shotDataReq.getBallSpeed();
//                break;
//            case 3:
//                club_speed = shotDataReq.getClubSpeed();
//                launch_angle = shotDataReq.getLaunchAngle();
//                carry = shotDataReq.getCarry();
//                break;
//            case 4:
//                total = shotDataReq.getTotal();
//                apex = shotDataReq.getApex();
//                spin_rate = shotDataReq.getSpinRate();
//                break;
//        }
//    }


    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s index : %d, club_id : %d, club_nickname : %s, mode : %d, unit : %d, loft : %f, pressure : %f, temp : %f, ball_spd : %f, club_speed : %f, launch : %f, carry : %f, total : %f, apex : %f, spin : %f"
        , shot_time, index, club_code, club_nickname, mode, unit, loft_angle, air_pressure, temperature, ball_speed, club_speed, launch_angle, carry, total, apex, spin_rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        ShotData shotData = (ShotData) obj;

        return this.carry == shotData.getCarry() && this.ball_speed == shotData.getBall_speed() && this.club_speed == shotData.getClub_speed() &&
            this.apex == shotData.getApex() && this.air_pressure == shotData.getAir_pressure();
    }

    public short getIndex() {
        return index;
    }

    public void setIndex(short index) {
        this.index = index;
    }

    public String getShot_time() {
        return shot_time;
    }

    public void setShot_time(String shot_time) {
        this.shot_time = shot_time;
    }

    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public short getClub_code() {
        return club_code;
    }

    public void setClub_code(short club_code) {
        this.club_code = club_code;
    }

    public float getLoft_angle() {
        return loft_angle;
    }

    public void setLoft_angle(float loft_angle) {
        this.loft_angle = loft_angle;
    }

    public short getUnit() {
        return unit;
    }

    public void setUnit(short unit) {
        this.unit = unit;
    }

    public float getAir_pressure() {
        return air_pressure;
    }

    public void setAir_pressure(float air_pressure) {
        this.air_pressure = air_pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getBall_speed() {
        return ball_speed;
    }

    public void setBall_speed(float ball_speed) {
        this.ball_speed = ball_speed;
    }

    public float getClub_speed() {
        return club_speed;
    }

    public void setClub_speed(float club_speed) {
        this.club_speed = club_speed;
    }

    public float getLaunch_angle() {
        return launch_angle;
    }

    public void setLaunch_angle(float launch_angle) {
        this.launch_angle = launch_angle;
    }

    public float getCarry() {
        return carry;
    }

    public void setCarry(float carry) {
        this.carry = carry;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getApex() {
        return apex;
    }

    public void setApex(float apex) {
        this.apex = apex;
    }

    public float getSpin_rate() {
        return spin_rate;
    }

    public void setSpin_rate(float spin_rate) {
        this.spin_rate = spin_rate;
    }

    public String getShot_date() {
        return shot_date;
    }

    public void setShot_date(String shot_date) {
        this.shot_date = shot_date;
    }

    public String getClub_nickname() {
        if(club_nickname == null || club_nickname.isEmpty())
            return "Generic";
        return club_nickname;
    }

    public void setClub_nickname(String club_nickname) {
        this.club_nickname = club_nickname;
    }

    public float getSmashFactor(){return ball_speed/club_speed;}
}
