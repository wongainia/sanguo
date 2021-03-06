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

public final class BaseGuildMemberInfo implements Externalizable, Message<BaseGuildMemberInfo>, Schema<BaseGuildMemberInfo>
{

    public static Schema<BaseGuildMemberInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BaseGuildMemberInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BaseGuildMemberInfo DEFAULT_INSTANCE = new BaseGuildMemberInfo();

    
    private Integer userid;

    public BaseGuildMemberInfo()
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

    public BaseGuildMemberInfo setUserid(Integer userid)
    {
        this.userid = userid;
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

    public Schema<BaseGuildMemberInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BaseGuildMemberInfo newMessage()
    {
        return new BaseGuildMemberInfo();
    }

    public Class<BaseGuildMemberInfo> typeClass()
    {
        return BaseGuildMemberInfo.class;
    }

    public String messageName()
    {
        return BaseGuildMemberInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BaseGuildMemberInfo.class.getName();
    }

    public boolean isInitialized(BaseGuildMemberInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BaseGuildMemberInfo message) throws IOException
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
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BaseGuildMemberInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);
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
