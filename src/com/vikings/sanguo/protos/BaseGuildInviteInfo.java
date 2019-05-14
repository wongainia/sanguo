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

public final class BaseGuildInviteInfo implements Externalizable, Message<BaseGuildInviteInfo>, Schema<BaseGuildInviteInfo>
{

    public static Schema<BaseGuildInviteInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BaseGuildInviteInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BaseGuildInviteInfo DEFAULT_INSTANCE = new BaseGuildInviteInfo();

    
    private Integer userid;
    private Integer time;

    public BaseGuildInviteInfo()
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

    public BaseGuildInviteInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // time

    public boolean hasTime(){
        return time != null;
    }


    public Integer getTime()
    {
        return time == null ? 0 : time;
    }

    public BaseGuildInviteInfo setTime(Integer time)
    {
        this.time = time;
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

    public Schema<BaseGuildInviteInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BaseGuildInviteInfo newMessage()
    {
        return new BaseGuildInviteInfo();
    }

    public Class<BaseGuildInviteInfo> typeClass()
    {
        return BaseGuildInviteInfo.class;
    }

    public String messageName()
    {
        return BaseGuildInviteInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BaseGuildInviteInfo.class.getName();
    }

    public boolean isInitialized(BaseGuildInviteInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BaseGuildInviteInfo message) throws IOException
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
                    message.time = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BaseGuildInviteInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.time != null)
            output.writeUInt32(20, message.time, false);
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