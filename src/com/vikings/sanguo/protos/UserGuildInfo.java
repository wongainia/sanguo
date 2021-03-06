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

public final class UserGuildInfo implements Externalizable, Message<UserGuildInfo>, Schema<UserGuildInfo>
{

    public static Schema<UserGuildInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UserGuildInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UserGuildInfo DEFAULT_INSTANCE = new UserGuildInfo();

    
    private BaseUserGuildInfo bi;

    public UserGuildInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseUserGuildInfo getBi()
    {
        return bi == null ? new BaseUserGuildInfo() : bi;
    }

    public UserGuildInfo setBi(BaseUserGuildInfo bi)
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

    public Schema<UserGuildInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UserGuildInfo newMessage()
    {
        return new UserGuildInfo();
    }

    public Class<UserGuildInfo> typeClass()
    {
        return UserGuildInfo.class;
    }

    public String messageName()
    {
        return UserGuildInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UserGuildInfo.class.getName();
    }

    public boolean isInitialized(UserGuildInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, UserGuildInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseUserGuildInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UserGuildInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseUserGuildInfo.getSchema(), false);

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
