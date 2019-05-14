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

public final class MsgRspCurrencyBoxOpen implements Externalizable, Message<MsgRspCurrencyBoxOpen>, Schema<MsgRspCurrencyBoxOpen>
{

    public static Schema<MsgRspCurrencyBoxOpen> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspCurrencyBoxOpen getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspCurrencyBoxOpen DEFAULT_INSTANCE = new MsgRspCurrencyBoxOpen();

    
    private ReturnInfo ri;
    private RoleDayInfo dayInfo;

    public MsgRspCurrencyBoxOpen()
    {
        
    }

    // getters and setters

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspCurrencyBoxOpen setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // dayInfo

    public boolean hasDayInfo(){
        return dayInfo != null;
    }


    public RoleDayInfo getDayInfo()
    {
        return dayInfo == null ? new RoleDayInfo() : dayInfo;
    }

    public MsgRspCurrencyBoxOpen setDayInfo(RoleDayInfo dayInfo)
    {
        this.dayInfo = dayInfo;
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

    public Schema<MsgRspCurrencyBoxOpen> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspCurrencyBoxOpen newMessage()
    {
        return new MsgRspCurrencyBoxOpen();
    }

    public Class<MsgRspCurrencyBoxOpen> typeClass()
    {
        return MsgRspCurrencyBoxOpen.class;
    }

    public String messageName()
    {
        return MsgRspCurrencyBoxOpen.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspCurrencyBoxOpen.class.getName();
    }

    public boolean isInitialized(MsgRspCurrencyBoxOpen message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspCurrencyBoxOpen message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 20:
                    message.dayInfo = input.mergeObject(message.dayInfo, RoleDayInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspCurrencyBoxOpen message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.dayInfo != null)
             output.writeObject(20, message.dayInfo, RoleDayInfo.getSchema(), false);

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
