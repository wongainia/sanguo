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

public final class MsgReqDungeonReward implements Externalizable, Message<MsgReqDungeonReward>, Schema<MsgReqDungeonReward>
{

    public static Schema<MsgReqDungeonReward> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqDungeonReward getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqDungeonReward DEFAULT_INSTANCE = new MsgReqDungeonReward();

    
    private Integer actid;

    public MsgReqDungeonReward()
    {
        
    }

    // getters and setters

    // actid

    public boolean hasActid(){
        return actid != null;
    }


    public Integer getActid()
    {
        return actid == null ? 0 : actid;
    }

    public MsgReqDungeonReward setActid(Integer actid)
    {
        this.actid = actid;
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

    public Schema<MsgReqDungeonReward> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqDungeonReward newMessage()
    {
        return new MsgReqDungeonReward();
    }

    public Class<MsgReqDungeonReward> typeClass()
    {
        return MsgReqDungeonReward.class;
    }

    public String messageName()
    {
        return MsgReqDungeonReward.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqDungeonReward.class.getName();
    }

    public boolean isInitialized(MsgReqDungeonReward message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqDungeonReward message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.actid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqDungeonReward message) throws IOException
    {
        if(message.actid != null)
            output.writeUInt32(10, message.actid, false);
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
