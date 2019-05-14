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

public final class GuildInviteInfo implements Externalizable, Message<GuildInviteInfo>, Schema<GuildInviteInfo>
{

    public static Schema<GuildInviteInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildInviteInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildInviteInfo DEFAULT_INSTANCE = new GuildInviteInfo();

    
    private BaseGuildInviteInfo bi;

    public GuildInviteInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseGuildInviteInfo getBi()
    {
        return bi == null ? new BaseGuildInviteInfo() : bi;
    }

    public GuildInviteInfo setBi(BaseGuildInviteInfo bi)
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

    public Schema<GuildInviteInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildInviteInfo newMessage()
    {
        return new GuildInviteInfo();
    }

    public Class<GuildInviteInfo> typeClass()
    {
        return GuildInviteInfo.class;
    }

    public String messageName()
    {
        return GuildInviteInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildInviteInfo.class.getName();
    }

    public boolean isInitialized(GuildInviteInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildInviteInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseGuildInviteInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildInviteInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseGuildInviteInfo.getSchema(), false);

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
