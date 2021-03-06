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

public final class MsgReqGuildPositionAssign implements Externalizable, Message<MsgReqGuildPositionAssign>, Schema<MsgReqGuildPositionAssign>
{

    public static Schema<MsgReqGuildPositionAssign> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqGuildPositionAssign getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqGuildPositionAssign DEFAULT_INSTANCE = new MsgReqGuildPositionAssign();

    
    private Integer guildid;
    private Integer target;
    private Integer position;

    public MsgReqGuildPositionAssign()
    {
        
    }

    // getters and setters

    // guildid

    public boolean hasGuildid(){
        return guildid != null;
    }


    public Integer getGuildid()
    {
        return guildid == null ? 0 : guildid;
    }

    public MsgReqGuildPositionAssign setGuildid(Integer guildid)
    {
        this.guildid = guildid;
        return this;
    }

    // target

    public boolean hasTarget(){
        return target != null;
    }


    public Integer getTarget()
    {
        return target == null ? 0 : target;
    }

    public MsgReqGuildPositionAssign setTarget(Integer target)
    {
        this.target = target;
        return this;
    }

    // position

    public boolean hasPosition(){
        return position != null;
    }


    public Integer getPosition()
    {
        return position == null ? 0 : position;
    }

    public MsgReqGuildPositionAssign setPosition(Integer position)
    {
        this.position = position;
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

    public Schema<MsgReqGuildPositionAssign> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqGuildPositionAssign newMessage()
    {
        return new MsgReqGuildPositionAssign();
    }

    public Class<MsgReqGuildPositionAssign> typeClass()
    {
        return MsgReqGuildPositionAssign.class;
    }

    public String messageName()
    {
        return MsgReqGuildPositionAssign.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqGuildPositionAssign.class.getName();
    }

    public boolean isInitialized(MsgReqGuildPositionAssign message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqGuildPositionAssign message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.guildid = input.readUInt32();
                    break;
                case 20:
                    message.target = input.readUInt32();
                    break;
                case 30:
                    message.position = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqGuildPositionAssign message) throws IOException
    {
        if(message.guildid != null)
            output.writeUInt32(10, message.guildid, false);

        if(message.target != null)
            output.writeUInt32(20, message.target, false);

        if(message.position != null)
            output.writeUInt32(30, message.position, false);
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
