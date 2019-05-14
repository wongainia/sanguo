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

public final class MsgRspManorReviveClean implements Externalizable, Message<MsgRspManorReviveClean>, Schema<MsgRspManorReviveClean>
{

    public static Schema<MsgRspManorReviveClean> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspManorReviveClean getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspManorReviveClean DEFAULT_INSTANCE = new MsgRspManorReviveClean();

    
    private LordInfo li;

    public MsgRspManorReviveClean()
    {
        
    }

    // getters and setters

    // li

    public boolean hasLi(){
        return li != null;
    }


    public LordInfo getLi()
    {
        return li == null ? new LordInfo() : li;
    }

    public MsgRspManorReviveClean setLi(LordInfo li)
    {
        this.li = li;
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

    public Schema<MsgRspManorReviveClean> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspManorReviveClean newMessage()
    {
        return new MsgRspManorReviveClean();
    }

    public Class<MsgRspManorReviveClean> typeClass()
    {
        return MsgRspManorReviveClean.class;
    }

    public String messageName()
    {
        return MsgRspManorReviveClean.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspManorReviveClean.class.getName();
    }

    public boolean isInitialized(MsgRspManorReviveClean message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspManorReviveClean message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 30:
                    message.li = input.mergeObject(message.li, LordInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspManorReviveClean message) throws IOException
    {
        if(message.li != null)
             output.writeObject(30, message.li, LordInfo.getSchema(), false);

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
