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

public final class MsgRspItemBuy implements Externalizable, Message<MsgRspItemBuy>, Schema<MsgRspItemBuy>
{

    public static Schema<MsgRspItemBuy> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspItemBuy getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspItemBuy DEFAULT_INSTANCE = new MsgRspItemBuy();

    
    private ReturnInfo ri;

    public MsgRspItemBuy()
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

    public MsgRspItemBuy setRi(ReturnInfo ri)
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

    public Schema<MsgRspItemBuy> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspItemBuy newMessage()
    {
        return new MsgRspItemBuy();
    }

    public Class<MsgRspItemBuy> typeClass()
    {
        return MsgRspItemBuy.class;
    }

    public String messageName()
    {
        return MsgRspItemBuy.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspItemBuy.class.getName();
    }

    public boolean isInitialized(MsgRspItemBuy message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspItemBuy message) throws IOException
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


    public void writeTo(Output output, MsgRspItemBuy message) throws IOException
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