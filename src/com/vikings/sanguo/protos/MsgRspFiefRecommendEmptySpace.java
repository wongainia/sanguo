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

public final class MsgRspFiefRecommendEmptySpace implements Externalizable, Message<MsgRspFiefRecommendEmptySpace>, Schema<MsgRspFiefRecommendEmptySpace>
{

    public static Schema<MsgRspFiefRecommendEmptySpace> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspFiefRecommendEmptySpace getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspFiefRecommendEmptySpace DEFAULT_INSTANCE = new MsgRspFiefRecommendEmptySpace();

    
    private Long zoneid;

    public MsgRspFiefRecommendEmptySpace()
    {
        
    }

    // getters and setters

    // zoneid

    public boolean hasZoneid(){
        return zoneid != null;
    }


    public Long getZoneid()
    {
        return zoneid == null ? 0L : zoneid;
    }

    public MsgRspFiefRecommendEmptySpace setZoneid(Long zoneid)
    {
        this.zoneid = zoneid;
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

    public Schema<MsgRspFiefRecommendEmptySpace> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspFiefRecommendEmptySpace newMessage()
    {
        return new MsgRspFiefRecommendEmptySpace();
    }

    public Class<MsgRspFiefRecommendEmptySpace> typeClass()
    {
        return MsgRspFiefRecommendEmptySpace.class;
    }

    public String messageName()
    {
        return MsgRspFiefRecommendEmptySpace.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspFiefRecommendEmptySpace.class.getName();
    }

    public boolean isInitialized(MsgRspFiefRecommendEmptySpace message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspFiefRecommendEmptySpace message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.zoneid = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspFiefRecommendEmptySpace message) throws IOException
    {
        if(message.zoneid != null)
            output.writeUInt64(10, message.zoneid, false);
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
