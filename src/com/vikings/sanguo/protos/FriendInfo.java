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

public final class FriendInfo implements Externalizable, Message<FriendInfo>, Schema<FriendInfo>
{

    public static Schema<FriendInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static FriendInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final FriendInfo DEFAULT_INSTANCE = new FriendInfo();

    
    private BaseFriendInfo bi;

    public FriendInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseFriendInfo getBi()
    {
        return bi == null ? new BaseFriendInfo() : bi;
    }

    public FriendInfo setBi(BaseFriendInfo bi)
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

    public Schema<FriendInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public FriendInfo newMessage()
    {
        return new FriendInfo();
    }

    public Class<FriendInfo> typeClass()
    {
        return FriendInfo.class;
    }

    public String messageName()
    {
        return FriendInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return FriendInfo.class.getName();
    }

    public boolean isInitialized(FriendInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, FriendInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseFriendInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, FriendInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseFriendInfo.getSchema(), false);

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