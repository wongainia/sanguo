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

public final class MsgRspTrainingComplete implements Externalizable, Message<MsgRspTrainingComplete>, Schema<MsgRspTrainingComplete>
{

    public static Schema<MsgRspTrainingComplete> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspTrainingComplete getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspTrainingComplete DEFAULT_INSTANCE = new MsgRspTrainingComplete();

    
    private ReturnInfo info;

    public MsgRspTrainingComplete()
    {
        
    }

    // getters and setters

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public ReturnInfo getInfo()
    {
        return info == null ? new ReturnInfo() : info;
    }

    public MsgRspTrainingComplete setInfo(ReturnInfo info)
    {
        this.info = info;
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

    public Schema<MsgRspTrainingComplete> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspTrainingComplete newMessage()
    {
        return new MsgRspTrainingComplete();
    }

    public Class<MsgRspTrainingComplete> typeClass()
    {
        return MsgRspTrainingComplete.class;
    }

    public String messageName()
    {
        return MsgRspTrainingComplete.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspTrainingComplete.class.getName();
    }

    public boolean isInitialized(MsgRspTrainingComplete message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspTrainingComplete message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.info = input.mergeObject(message.info, ReturnInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspTrainingComplete message) throws IOException
    {
        if(message.info != null)
             output.writeObject(10, message.info, ReturnInfo.getSchema(), false);

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