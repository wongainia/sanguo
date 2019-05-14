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

public final class MsgRspRouletteSacrifice implements Externalizable, Message<MsgRspRouletteSacrifice>, Schema<MsgRspRouletteSacrifice>
{

    public static Schema<MsgRspRouletteSacrifice> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspRouletteSacrifice getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspRouletteSacrifice DEFAULT_INSTANCE = new MsgRspRouletteSacrifice();

    
    private ReturnInfo ri;

    public MsgRspRouletteSacrifice()
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

    public MsgRspRouletteSacrifice setRi(ReturnInfo ri)
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

    public Schema<MsgRspRouletteSacrifice> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspRouletteSacrifice newMessage()
    {
        return new MsgRspRouletteSacrifice();
    }

    public Class<MsgRspRouletteSacrifice> typeClass()
    {
        return MsgRspRouletteSacrifice.class;
    }

    public String messageName()
    {
        return MsgRspRouletteSacrifice.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspRouletteSacrifice.class.getName();
    }

    public boolean isInitialized(MsgRspRouletteSacrifice message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspRouletteSacrifice message) throws IOException
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


    public void writeTo(Output output, MsgRspRouletteSacrifice message) throws IOException
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