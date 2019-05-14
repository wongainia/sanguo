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

public final class MsgRspItemSell implements Externalizable, Message<MsgRspItemSell>, Schema<MsgRspItemSell>
{

    public static Schema<MsgRspItemSell> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspItemSell getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspItemSell DEFAULT_INSTANCE = new MsgRspItemSell();

    
    private ReturnInfo ri;

    public MsgRspItemSell()
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

    public MsgRspItemSell setRi(ReturnInfo ri)
    {
        this.ri = ri;
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

    public Schema<MsgRspItemSell> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspItemSell newMessage()
    {
        return new MsgRspItemSell();
    }

    public Class<MsgRspItemSell> typeClass()
    {
        return MsgRspItemSell.class;
    }

    public String messageName()
    {
        return MsgRspItemSell.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspItemSell.class.getName();
    }

    public boolean isInitialized(MsgRspItemSell message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspItemSell message) throws IOException
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

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspItemSell message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);

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
