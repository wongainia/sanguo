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

public final class MsgReqQuestFinish implements Externalizable, Message<MsgReqQuestFinish>, Schema<MsgReqQuestFinish>
{

    public static Schema<MsgReqQuestFinish> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqQuestFinish getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqQuestFinish DEFAULT_INSTANCE = new MsgReqQuestFinish();

    
    private Integer questid;

    public MsgReqQuestFinish()
    {
        
    }

    // getters and setters

    // questid

    public boolean hasQuestid(){
        return questid != null;
    }


    public Integer getQuestid()
    {
        return questid == null ? 0 : questid;
    }

    public MsgReqQuestFinish setQuestid(Integer questid)
    {
        this.questid = questid;
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

    public Schema<MsgReqQuestFinish> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqQuestFinish newMessage()
    {
        return new MsgReqQuestFinish();
    }

    public Class<MsgReqQuestFinish> typeClass()
    {
        return MsgReqQuestFinish.class;
    }

    public String messageName()
    {
        return MsgReqQuestFinish.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqQuestFinish.class.getName();
    }

    public boolean isInitialized(MsgReqQuestFinish message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqQuestFinish message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.questid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqQuestFinish message) throws IOException
    {
        if(message.questid != null)
            output.writeUInt32(10, message.questid, false);
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
