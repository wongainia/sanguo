// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class BriefUserInfo implements Externalizable, Message<BriefUserInfo>, Schema<BriefUserInfo>
{

    public static Schema<BriefUserInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BriefUserInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BriefUserInfo DEFAULT_INSTANCE = new BriefUserInfo();

    
    private Integer id;
    private Integer image;
    private String nick;
    private Integer sex;
    private Integer birthday;
    private Integer province;
    private Integer city;
    private Integer level;
    private Integer guildid;
    private Integer country;
    private Integer charge;
    private Integer lastLoginTime;

    public BriefUserInfo()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Integer getId()
    {
        return id == null ? 0 : id;
    }

    public BriefUserInfo setId(Integer id)
    {
        this.id = id;
        return this;
    }

    // image

    public boolean hasImage(){
        return image != null;
    }


    public Integer getImage()
    {
        return image == null ? 0 : image;
    }

    public BriefUserInfo setImage(Integer image)
    {
        this.image = image;
        return this;
    }

    // nick

    public boolean hasNick(){
        return nick != null;
    }


    public String getNick()
    {
        return nick == null ? "" : nick;
    }

    public BriefUserInfo setNick(String nick)
    {
        this.nick = nick;
        return this;
    }

    // sex

    public boolean hasSex(){
        return sex != null;
    }


    public Integer getSex()
    {
        return sex == null ? 0 : sex;
    }

    public BriefUserInfo setSex(Integer sex)
    {
        this.sex = sex;
        return this;
    }

    // birthday

    public boolean hasBirthday(){
        return birthday != null;
    }


    public Integer getBirthday()
    {
        return birthday == null ? 0 : birthday;
    }

    public BriefUserInfo setBirthday(Integer birthday)
    {
        this.birthday = birthday;
        return this;
    }

    // province

    public boolean hasProvince(){
        return province != null;
    }


    public Integer getProvince()
    {
        return province == null ? 0 : province;
    }

    public BriefUserInfo setProvince(Integer province)
    {
        this.province = province;
        return this;
    }

    // city

    public boolean hasCity(){
        return city != null;
    }


    public Integer getCity()
    {
        return city == null ? 0 : city;
    }

    public BriefUserInfo setCity(Integer city)
    {
        this.city = city;
        return this;
    }

    // level

    public boolean hasLevel(){
        return level != null;
    }


    public Integer getLevel()
    {
        return level == null ? 0 : level;
    }

    public BriefUserInfo setLevel(Integer level)
    {
        this.level = level;
        return this;
    }

    // guildid

    public boolean hasGuildid(){
        return guildid != null;
    }


    public Integer getGuildid()
    {
        return guildid == null ? 0 : guildid;
    }

    public BriefUserInfo setGuildid(Integer guildid)
    {
        this.guildid = guildid;
        return this;
    }

    // country

    public boolean hasCountry(){
        return country != null;
    }


    public Integer getCountry()
    {
        return country == null ? 0 : country;
    }

    public BriefUserInfo setCountry(Integer country)
    {
        this.country = country;
        return this;
    }

    // charge

    public boolean hasCharge(){
        return charge != null;
    }


    public Integer getCharge()
    {
        return charge == null ? 0 : charge;
    }

    public BriefUserInfo setCharge(Integer charge)
    {
        this.charge = charge;
        return this;
    }

    // lastLoginTime

    public boolean hasLastLoginTime(){
        return lastLoginTime != null;
    }


    public Integer getLastLoginTime()
    {
        return lastLoginTime == null ? 0 : lastLoginTime;
    }

    public BriefUserInfo setLastLoginTime(Integer lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<BriefUserInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BriefUserInfo newMessage()
    {
        return new BriefUserInfo();
    }

    public Class<BriefUserInfo> typeClass()
    {
        return BriefUserInfo.class;
    }

    public String messageName()
    {
        return BriefUserInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BriefUserInfo.class.getName();
    }

    public boolean isInitialized(BriefUserInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BriefUserInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt32();
                    break;
                case 20:
                    message.image = input.readUInt32();
                    break;
                case 30:
                    message.nick = input.readString();
                    break;
                case 40:
                    message.sex = input.readUInt32();
                    break;
                case 50:
                    message.birthday = input.readUInt32();
                    break;
                case 60:
                    message.province = input.readUInt32();
                    break;
                case 70:
                    message.city = input.readUInt32();
                    break;
                case 80:
                    message.level = input.readUInt32();
                    break;
                case 100:
                    message.guildid = input.readUInt32();
                    break;
                case 110:
                    message.country = input.readUInt32();
                    break;
                case 120:
                    message.charge = input.readUInt32();
                    break;
                case 130:
                    message.lastLoginTime = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BriefUserInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt32(10, message.id, false);

        if(message.image != null)
            output.writeUInt32(20, message.image, false);

        if(message.nick != null)
            output.writeString(30, message.nick, false);

        if(message.sex != null)
            output.writeUInt32(40, message.sex, false);

        if(message.birthday != null)
            output.writeUInt32(50, message.birthday, false);

        if(message.province != null)
            output.writeUInt32(60, message.province, false);

        if(message.city != null)
            output.writeUInt32(70, message.city, false);

        if(message.level != null)
            output.writeUInt32(80, message.level, false);

        if(message.guildid != null)
            output.writeUInt32(100, message.guildid, false);

        if(message.country != null)
            output.writeUInt32(110, message.country, false);

        if(message.charge != null)
            output.writeUInt32(120, message.charge, false);

        if(message.lastLoginTime != null)
            output.writeUInt32(130, message.lastLoginTime, false);
    }

    public String getFieldName(int number)
    {
        return Integer.toString(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }
    
}
