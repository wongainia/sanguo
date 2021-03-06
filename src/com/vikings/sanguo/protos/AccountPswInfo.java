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

public final class AccountPswInfo implements Externalizable, Message<AccountPswInfo>, Schema<AccountPswInfo>
{

    public static Schema<AccountPswInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static AccountPswInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final AccountPswInfo DEFAULT_INSTANCE = new AccountPswInfo();

    
    private Integer userid;
    private String psw;
    private String nick;
    private Integer zoneid;
    private Integer level;

    public AccountPswInfo()
    {
        
    }

    // getters and setters

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public AccountPswInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // psw

    public boolean hasPsw(){
        return psw != null;
    }


    public String getPsw()
    {
        return psw == null ? "" : psw;
    }

    public AccountPswInfo setPsw(String psw)
    {
        this.psw = psw;
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

    public AccountPswInfo setNick(String nick)
    {
        this.nick = nick;
        return this;
    }

    // zoneid

    public boolean hasZoneid(){
        return zoneid != null;
    }


    public Integer getZoneid()
    {
        return zoneid == null ? 0 : zoneid;
    }

    public AccountPswInfo setZoneid(Integer zoneid)
    {
        this.zoneid = zoneid;
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

    public AccountPswInfo setLevel(Integer level)
    {
        this.level = level;
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

    public Schema<AccountPswInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public AccountPswInfo newMessage()
    {
        return new AccountPswInfo();
    }

    public Class<AccountPswInfo> typeClass()
    {
        return AccountPswInfo.class;
    }

    public String messageName()
    {
        return AccountPswInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return AccountPswInfo.class.getName();
    }

    public boolean isInitialized(AccountPswInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, AccountPswInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.userid = input.readUInt32();
                    break;
                case 20:
                    message.psw = input.readString();
                    break;
                case 30:
                    message.nick = input.readString();
                    break;
                case 60:
                    message.zoneid = input.readUInt32();
                    break;
                case 70:
                    message.level = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, AccountPswInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.psw != null)
            output.writeString(20, message.psw, false);

        if(message.nick != null)
            output.writeString(30, message.nick, false);

        if(message.zoneid != null)
            output.writeUInt32(60, message.zoneid, false);

        if(message.level != null)
            output.writeUInt32(70, message.level, false);
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
