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

public final class MsgRspBloodConf implements Externalizable, Message<MsgRspBloodConf>, Schema<MsgRspBloodConf>
{

    public static Schema<MsgRspBloodConf> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspBloodConf getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspBloodConf DEFAULT_INSTANCE = new MsgRspBloodConf();

    
    private LordInfo li;

    public MsgRspBloodConf()
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

    public MsgRspBloodConf setLi(LordInfo li)
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

    public Schema<MsgRspBloodConf> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspBloodConf newMessage()
    {
        return new MsgRspBloodConf();
    }

    public Class<MsgRspBloodConf> typeClass()
    {
        return MsgRspBloodConf.class;
    }

    public String messageName()
    {
        return MsgRspBloodConf.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspBloodConf.class.getName();
    }

    public boolean isInitialized(MsgRspBloodConf message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspBloodConf message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.li = input.mergeObject(message.li, LordInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspBloodConf message) throws IOException
    {
        if(message.li != null)
             output.writeObject(10, message.li, LordInfo.getSchema(), false);

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