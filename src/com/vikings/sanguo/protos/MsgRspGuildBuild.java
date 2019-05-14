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

public final class MsgRspGuildBuild implements Externalizable, Message<MsgRspGuildBuild>, Schema<MsgRspGuildBuild>
{

    public static Schema<MsgRspGuildBuild> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspGuildBuild getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspGuildBuild DEFAULT_INSTANCE = new MsgRspGuildBuild();

    
    private ReturnInfo ri;
    private Integer guildid;

    public MsgRspGuildBuild()
    {
        
    }

    // getters and setters

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspGuildBuild setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // guildid

    public boolean hasGuildid(){
        return guildid != null;
    }


    public Integer getGuildid()
    {
        return guildid == null ? 0 : guildid;
    }

    public MsgRspGuildBuild setGuildid(Integer guildid)
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

    public Schema<MsgRspGuildBuild> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspGuildBuild newMessage()
    {
        return new MsgRspGuildBuild();
    }

    public Class<MsgRspGuildBuild> typeClass()
    {
        return MsgRspGuildBuild.class;
    }

    public String messageName()
    {
        return MsgRspGuildBuild.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspGuildBuild.class.getName();
    }

    public boolean isInitialized(MsgRspGuildBuild message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspGuildBuild message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 20:
                    message.guildid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspGuildBuild message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.guildid != null)
            output.writeUInt32(20, message.guildid, false);
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