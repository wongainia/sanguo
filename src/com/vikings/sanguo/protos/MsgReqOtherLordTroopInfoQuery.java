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

public final class MsgReqOtherLordTroopInfoQuery implements Externalizable, Message<MsgReqOtherLordTroopInfoQuery>, Schema<MsgReqOtherLordTroopInfoQuery>
{

    public static Schema<MsgReqOtherLordTroopInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqOtherLordTroopInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqOtherLordTroopInfoQuery DEFAULT_INSTANCE = new MsgReqOtherLordTroopInfoQuery();

    
    private Integer target;

    public MsgReqOtherLordTroopInfoQuery()
    {
        
    }

    // getters and setters

    // target

    public boolean hasTarget(){
        return target != null;
    }


    public Integer getTarget()
    {
        return target == null ? 0 : target;
    }

    public MsgReqOtherLordTroopInfoQuery setTarget(Integer target)
    {
        this.target = target;
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

    public Schema<MsgReqOtherLordTroopInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqOtherLordTroopInfoQuery newMessage()
    {
        return new MsgReqOtherLordTroopInfoQuery();
    }

    public Class<MsgReqOtherLordTroopInfoQuery> typeClass()
    {
        return MsgReqOtherLordTroopInfoQuery.class;
    }

    public String messageName()
    {
        return MsgReqOtherLordTroopInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqOtherLordTroopInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgReqOtherLordTroopInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqOtherLordTroopInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.target = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqOtherLordTroopInfoQuery message) throws IOException
    {
        if(message.target != null)
            output.writeUInt32(10, message.target, false);
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
