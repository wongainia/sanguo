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

public final class MsgRspUserStaminaReset implements Externalizable, Message<MsgRspUserStaminaReset>, Schema<MsgRspUserStaminaReset>
{

    public static Schema<MsgRspUserStaminaReset> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspUserStaminaReset getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspUserStaminaReset DEFAULT_INSTANCE = new MsgRspUserStaminaReset();

    
    private ReturnInfo ri;

    public MsgRspUserStaminaReset()
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

    public MsgRspUserStaminaReset setRi(ReturnInfo ri)
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

    public Schema<MsgRspUserStaminaReset> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspUserStaminaReset newMessage()
    {
        return new MsgRspUserStaminaReset();
    }

    public Class<MsgRspUserStaminaReset> typeClass()
    {
        return MsgRspUserStaminaReset.class;
    }

    public String messageName()
    {
        return MsgRspUserStaminaReset.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspUserStaminaReset.class.getName();
    }

    public boolean isInitialized(MsgRspUserStaminaReset message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspUserStaminaReset message) throws IOException
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


    public void writeTo(Output output, MsgRspUserStaminaReset message) throws IOException
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