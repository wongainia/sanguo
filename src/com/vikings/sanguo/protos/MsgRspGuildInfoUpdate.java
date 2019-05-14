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

public final class MsgRspGuildInfoUpdate implements Externalizable, Message<MsgRspGuildInfoUpdate>, Schema<MsgRspGuildInfoUpdate>
{

    public static Schema<MsgRspGuildInfoUpdate> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspGuildInfoUpdate getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspGuildInfoUpdate DEFAULT_INSTANCE = new MsgRspGuildInfoUpdate();

    
    private ReturnInfo ri;
    private GuildInfo gi;

    public MsgRspGuildInfoUpdate()
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

    public MsgRspGuildInfoUpdate setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // gi

    public boolean hasGi(){
        return gi != null;
    }


    public GuildInfo getGi()
    {
        return gi == null ? new GuildInfo() : gi;
    }

    public MsgRspGuildInfoUpdate setGi(GuildInfo gi)
    {
        this.gi = gi;
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

    public Schema<MsgRspGuildInfoUpdate> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspGuildInfoUpdate newMessage()
    {
        return new MsgRspGuildInfoUpdate();
    }

    public Class<MsgRspGuildInfoUpdate> typeClass()
    {
        return MsgRspGuildInfoUpdate.class;
    }

    public String messageName()
    {
        return MsgRspGuildInfoUpdate.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspGuildInfoUpdate.class.getName();
    }

    public boolean isInitialized(MsgRspGuildInfoUpdate message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspGuildInfoUpdate message) throws IOException
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
                    message.gi = input.mergeObject(message.gi, GuildInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspGuildInfoUpdate message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.gi != null)
             output.writeObject(20, message.gi, GuildInfo.getSchema(), false);

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
