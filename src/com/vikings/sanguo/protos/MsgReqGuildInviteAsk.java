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

public final class MsgReqGuildInviteAsk implements Externalizable, Message<MsgReqGuildInviteAsk>, Schema<MsgReqGuildInviteAsk>
{

    public static Schema<MsgReqGuildInviteAsk> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqGuildInviteAsk getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqGuildInviteAsk DEFAULT_INSTANCE = new MsgReqGuildInviteAsk();

    
    private Integer guildid;
    private Integer target;
    private String message;

    public MsgReqGuildInviteAsk()
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

    public MsgReqGuildInviteAsk setGuildid(Integer guildid)
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

    public MsgReqGuildInviteAsk setTarget(Integer target)
    {
        this.target = target;
        return this;
    }

    // message

    public boolean hasMessage(){
        return message != null;
    }


    public String getMessage()
    {
        return message == null ? "" : message;
    }

    public MsgReqGuildInviteAsk setMessage(String message)
    {
        this.message = message;
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

    public Schema<MsgReqGuildInviteAsk> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqGuildInviteAsk newMessage()
    {
        return new MsgReqGuildInviteAsk();
    }

    public Class<MsgReqGuildInviteAsk> typeClass()
    {
        return MsgReqGuildInviteAsk.class;
    }

    public String messageName()
    {
        return MsgReqGuildInviteAsk.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqGuildInviteAsk.class.getName();
    }

    public boolean isInitialized(MsgReqGuildInviteAsk message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqGuildInviteAsk message) throws IOException
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
                    message.message = input.readString();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqGuildInviteAsk message) throws IOException
    {
        if(message.guildid != null)
            output.writeUInt32(10, message.guildid, false);

        if(message.target != null)
            output.writeUInt32(20, message.target, false);

        if(message.message != null)
            output.writeString(30, message.message, false);
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