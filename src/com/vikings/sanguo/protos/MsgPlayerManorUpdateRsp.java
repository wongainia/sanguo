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

public final class MsgPlayerManorUpdateRsp implements Externalizable, Message<MsgPlayerManorUpdateRsp>, Schema<MsgPlayerManorUpdateRsp>
{

    public static Schema<MsgPlayerManorUpdateRsp> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgPlayerManorUpdateRsp getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgPlayerManorUpdateRsp DEFAULT_INSTANCE = new MsgPlayerManorUpdateRsp();

    
    private ManorInfo mi;

    public MsgPlayerManorUpdateRsp()
    {
        
    }

    // getters and setters

    // mi

    public boolean hasMi(){
        return mi != null;
    }


    public ManorInfo getMi()
    {
        return mi == null ? new ManorInfo() : mi;
    }

    public MsgPlayerManorUpdateRsp setMi(ManorInfo mi)
    {
        this.mi = mi;
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

    public Schema<MsgPlayerManorUpdateRsp> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgPlayerManorUpdateRsp newMessage()
    {
        return new MsgPlayerManorUpdateRsp();
    }

    public Class<MsgPlayerManorUpdateRsp> typeClass()
    {
        return MsgPlayerManorUpdateRsp.class;
    }

    public String messageName()
    {
        return MsgPlayerManorUpdateRsp.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgPlayerManorUpdateRsp.class.getName();
    }

    public boolean isInitialized(MsgPlayerManorUpdateRsp message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgPlayerManorUpdateRsp message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.mi = input.mergeObject(message.mi, ManorInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgPlayerManorUpdateRsp message) throws IOException
    {
        if(message.mi != null)
             output.writeObject(10, message.mi, ManorInfo.getSchema(), false);

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
