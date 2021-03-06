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

public final class AccountInfoPart3 implements Externalizable, Message<AccountInfoPart3>, Schema<AccountInfoPart3>
{

    public static Schema<AccountInfoPart3> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AccountInfoPart3 getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AccountInfoPart3 DEFAULT_INSTANCE = new AccountInfoPart3();

    
    private Integer image;
    private String nick;
    private Integer sex;
    private Integer birthday;
    private Integer province;
    private Integer city;
    private String mobile;
    private String email;
    private String desc;
    private String partnerId;
    private Integer partnerChannel;
    private String renrenSchool;
    private String idCardNumber;
    private Integer channel;

    public AccountInfoPart3()
    {
        
    }

    // getters and setters

    // image

    public boolean hasImage(){
        return image != null;
    }


    public Integer getImage()
    {
        return image == null ? 0 : image;
    }

    public AccountInfoPart3 setImage(Integer image)
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

    public AccountInfoPart3 setNick(String nick)
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

    public AccountInfoPart3 setSex(Integer sex)
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

    public AccountInfoPart3 setBirthday(Integer birthday)
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

    public AccountInfoPart3 setProvince(Integer province)
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

    public AccountInfoPart3 setCity(Integer city)
    {
        this.city = city;
        return this;
    }

    // mobile

    public boolean hasMobile(){
        return mobile != null;
    }


    public String getMobile()
    {
        return mobile == null ? "" : mobile;
    }

    public AccountInfoPart3 setMobile(String mobile)
    {
        this.mobile = mobile;
        return this;
    }

    // email

    public boolean hasEmail(){
        return email != null;
    }


    public String getEmail()
    {
        return email == null ? "" : email;
    }

    public AccountInfoPart3 setEmail(String email)
    {
        this.email = email;
        return this;
    }

    // desc

    public boolean hasDesc(){
        return desc != null;
    }


    public String getDesc()
    {
        return desc == null ? "" : desc;
    }

    public AccountInfoPart3 setDesc(String desc)
    {
        this.desc = desc;
        return this;
    }

    // partnerId

    public boolean hasPartnerId(){
        return partnerId != null;
    }


    public String getPartnerId()
    {
        return partnerId == null ? "" : partnerId;
    }

    public AccountInfoPart3 setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
        return this;
    }

    // partnerChannel

    public boolean hasPartnerChannel(){
        return partnerChannel != null;
    }


    public Integer getPartnerChannel()
    {
        return partnerChannel == null ? 0 : partnerChannel;
    }

    public AccountInfoPart3 setPartnerChannel(Integer partnerChannel)
    {
        this.partnerChannel = partnerChannel;
        return this;
    }

    // renrenSchool

    public boolean hasRenrenSchool(){
        return renrenSchool != null;
    }


    public String getRenrenSchool()
    {
        return renrenSchool == null ? "" : renrenSchool;
    }

    public AccountInfoPart3 setRenrenSchool(String renrenSchool)
    {
        this.renrenSchool = renrenSchool;
        return this;
    }

    // idCardNumber

    public boolean hasIdCardNumber(){
        return idCardNumber != null;
    }


    public String getIdCardNumber()
    {
        return idCardNumber == null ? "" : idCardNumber;
    }

    public AccountInfoPart3 setIdCardNumber(String idCardNumber)
    {
        this.idCardNumber = idCardNumber;
        return this;
    }

    // channel

    public boolean hasChannel(){
        return channel != null;
    }


    public Integer getChannel()
    {
        return channel == null ? 0 : channel;
    }

    public AccountInfoPart3 setChannel(Integer channel)
    {
        this.channel = channel;
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

    public Schema<AccountInfoPart3> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AccountInfoPart3 newMessage()
    {
        return new AccountInfoPart3();
    }

    public Class<AccountInfoPart3> typeClass()
    {
        return AccountInfoPart3.class;
    }

    public String messageName()
    {
        return AccountInfoPart3.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AccountInfoPart3.class.getName();
    }

    public boolean isInitialized(AccountInfoPart3 message)
    {
        return true;
    }

    public void mergeFrom(Input input, AccountInfoPart3 message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.image = input.readUInt32();
                    break;
                case 20:
                    message.nick = input.readString();
                    break;
                case 30:
                    message.sex = input.readUInt32();
                    break;
                case 40:
                    message.birthday = input.readUInt32();
                    break;
                case 50:
                    message.province = input.readUInt32();
                    break;
                case 60:
                    message.city = input.readUInt32();
                    break;
                case 90:
                    message.mobile = input.readString();
                    break;
                case 100:
                    message.email = input.readString();
                    break;
                case 140:
                    message.desc = input.readString();
                    break;
                case 150:
                    message.partnerId = input.readString();
                    break;
                case 160:
                    message.partnerChannel = input.readUInt32();
                    break;
                case 170:
                    message.renrenSchool = input.readString();
                    break;
                case 180:
                    message.idCardNumber = input.readString();
                    break;
                case 190:
                    message.channel = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AccountInfoPart3 message) throws IOException
    {
        if(message.image != null)
            output.writeUInt32(10, message.image, false);

        if(message.nick != null)
            output.writeString(20, message.nick, false);

        if(message.sex != null)
            output.writeUInt32(30, message.sex, false);

        if(message.birthday != null)
            output.writeUInt32(40, message.birthday, false);

        if(message.province != null)
            output.writeUInt32(50, message.province, false);

        if(message.city != null)
            output.writeUInt32(60, message.city, false);

        if(message.mobile != null)
            output.writeString(90, message.mobile, false);

        if(message.email != null)
            output.writeString(100, message.email, false);

        if(message.desc != null)
            output.writeString(140, message.desc, false);

        if(message.partnerId != null)
            output.writeString(150, message.partnerId, false);

        if(message.partnerChannel != null)
            output.writeUInt32(160, message.partnerChannel, false);

        if(message.renrenSchool != null)
            output.writeString(170, message.renrenSchool, false);

        if(message.idCardNumber != null)
            output.writeString(180, message.idCardNumber, false);

        if(message.channel != null)
            output.writeUInt32(190, message.channel, false);
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
