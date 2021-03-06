// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

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

public final class MsgRspAccountRestore implements Externalizable, Message<MsgRspAccountRestore>, Schema<MsgRspAccountRestore>
{

    public static Schema<MsgRspAccountRestore> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspAccountRestore getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspAccountRestore DEFAULT_INSTANCE = new MsgRspAccountRestore();

    
    private Integer remainCount;

    public MsgRspAccountRestore()
    {
        
    }

    // getters and setters

    // remainCount

    public boolean hasRemainCount(){
        return remainCount != null;
    }


    public Integer getRemainCount()
    {
        return remainCount == null ? 0 : remainCount;
    }

    public MsgRspAccountRestore setRemainCount(Integer remainCount)
    {
        this.remainCount = remainCount;
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

    public Schema<MsgRspAccountRestore> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspAccountRestore newMessage()
    {
        return new MsgRspAccountRestore();
    }

    public Class<MsgRspAccountRestore> typeClass()
    {
        return MsgRspAccountRestore.class;
    }

    public String messageName()
    {
        return MsgRspAccountRestore.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspAccountRestore.class.getName();
    }

    public boolean isInitialized(MsgRspAccountRestore message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspAccountRestore message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 20:
                    message.remainCount = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspAccountRestore message) throws IOException
    {
        if(message.remainCount != null)
            output.writeUInt32(20, message.remainCount, false);
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
