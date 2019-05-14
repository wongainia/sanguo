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

public final class MsgRspQuestFinish implements Externalizable, Message<MsgRspQuestFinish>, Schema<MsgRspQuestFinish>
{

    public static Schema<MsgRspQuestFinish> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspQuestFinish getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspQuestFinish DEFAULT_INSTANCE = new MsgRspQuestFinish();

    
    private ReturnInfo ri;

    public MsgRspQuestFinish()
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

    public MsgRspQuestFinish setRi(ReturnInfo ri)
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

    public Schema<MsgRspQuestFinish> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspQuestFinish newMessage()
    {
        return new MsgRspQuestFinish();
    }

    public Class<MsgRspQuestFinish> typeClass()
    {
        return MsgRspQuestFinish.class;
    }

    public String messageName()
    {
        return MsgRspQuestFinish.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspQuestFinish.class.getName();
    }

    public boolean isInitialized(MsgRspQuestFinish message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspQuestFinish message) throws IOException
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


    public void writeTo(Output output, MsgRspQuestFinish message) throws IOException
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
