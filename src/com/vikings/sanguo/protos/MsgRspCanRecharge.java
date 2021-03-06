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

public final class MsgRspCanRecharge implements Externalizable, Message<MsgRspCanRecharge>, Schema<MsgRspCanRecharge>
{

    public static Schema<MsgRspCanRecharge> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspCanRecharge getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspCanRecharge DEFAULT_INSTANCE = new MsgRspCanRecharge();

    
    private Integer canRecharge;

    public MsgRspCanRecharge()
    {
        
    }

    // getters and setters

    // canRecharge

    public boolean hasCanRecharge(){
        return canRecharge != null;
    }


    public Integer getCanRecharge()
    {
        return canRecharge == null ? 0 : canRecharge;
    }

    public MsgRspCanRecharge setCanRecharge(Integer canRecharge)
    {
        this.canRecharge = canRecharge;
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

    public Schema<MsgRspCanRecharge> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspCanRecharge newMessage()
    {
        return new MsgRspCanRecharge();
    }

    public Class<MsgRspCanRecharge> typeClass()
    {
        return MsgRspCanRecharge.class;
    }

    public String messageName()
    {
        return MsgRspCanRecharge.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspCanRecharge.class.getName();
    }

    public boolean isInitialized(MsgRspCanRecharge message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspCanRecharge message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.canRecharge = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspCanRecharge message) throws IOException
    {
        if(message.canRecharge != null)
            output.writeUInt32(10, message.canRecharge, false);
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
