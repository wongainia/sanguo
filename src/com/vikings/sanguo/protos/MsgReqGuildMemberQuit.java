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

public final class MsgReqGuildMemberQuit implements Externalizable, Message<MsgReqGuildMemberQuit>, Schema<MsgReqGuildMemberQuit>
{

    public static Schema<MsgReqGuildMemberQuit> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqGuildMemberQuit getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqGuildMemberQuit DEFAULT_INSTANCE = new MsgReqGuildMemberQuit();

    
    private Integer guildid;

    public MsgReqGuildMemberQuit()
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

    public MsgReqGuildMemberQuit setGuildid(Integer guildid)
    {
        this.guildid = guildid;
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

    public Schema<MsgReqGuildMemberQuit> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqGuildMemberQuit newMessage()
    {
        return new MsgReqGuildMemberQuit();
    }

    public Class<MsgReqGuildMemberQuit> typeClass()
    {
        return MsgReqGuildMemberQuit.class;
    }

    public String messageName()
    {
        return MsgReqGuildMemberQuit.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqGuildMemberQuit.class.getName();
    }

    public boolean isInitialized(MsgReqGuildMemberQuit message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqGuildMemberQuit message) throws IOException
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
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqGuildMemberQuit message) throws IOException
    {
        if(message.guildid != null)
            output.writeUInt32(10, message.guildid, false);
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
