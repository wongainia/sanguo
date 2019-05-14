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

public final class GuildMemberInfo implements Externalizable, Message<GuildMemberInfo>, Schema<GuildMemberInfo>
{

    public static Schema<GuildMemberInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildMemberInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildMemberInfo DEFAULT_INSTANCE = new GuildMemberInfo();

    
    private BaseGuildMemberInfo bi;

    public GuildMemberInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseGuildMemberInfo getBi()
    {
        return bi == null ? new BaseGuildMemberInfo() : bi;
    }

    public GuildMemberInfo setBi(BaseGuildMemberInfo bi)
    {
        this.bi = bi;
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

    public Schema<GuildMemberInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildMemberInfo newMessage()
    {
        return new GuildMemberInfo();
    }

    public Class<GuildMemberInfo> typeClass()
    {
        return GuildMemberInfo.class;
    }

    public String messageName()
    {
        return GuildMemberInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildMemberInfo.class.getName();
    }

    public boolean isInitialized(GuildMemberInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildMemberInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseGuildMemberInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildMemberInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseGuildMemberInfo.getSchema(), false);

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