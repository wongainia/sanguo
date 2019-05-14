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

public final class MsgGameEnterReq implements Externalizable, Message<MsgGameEnterReq>, Schema<MsgGameEnterReq>
{

    public static Schema<MsgGameEnterReq> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgGameEnterReq getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgGameEnterReq DEFAULT_INSTANCE = new MsgGameEnterReq();

    
    private Integer clientType;
    private Integer clientVer;
    private Integer channelId;

    public MsgGameEnterReq()
    {
        
    }

    // getters and setters

    // clientType

    public boolean hasClientType(){
        return clientType != null;
    }


    public Integer getClientType()
    {
        return clientType == null ? 0 : clientType;
    }

    public MsgGameEnterReq setClientType(Integer clientType)
    {
        this.clientType = clientType;
        return this;
    }

    // clientVer

    public boolean hasClientVer(){
        return clientVer != null;
    }


    public Integer getClientVer()
    {
        return clientVer == null ? 0 : clientVer;
    }

    public MsgGameEnterReq setClientVer(Integer clientVer)
    {
        this.clientVer = clientVer;
        return this;
    }

    // channelId

    public boolean hasChannelId(){
        return channelId != null;
    }


    public Integer getChannelId()
    {
        return channelId == null ? 0 : channelId;
    }

    public MsgGameEnterReq setChannelId(Integer channelId)
    {
        this.channelId = channelId;
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

    public Schema<MsgGameEnterReq> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgGameEnterReq newMessage()
    {
        return new MsgGameEnterReq();
    }

    public Class<MsgGameEnterReq> typeClass()
    {
        return MsgGameEnterReq.class;
    }

    public String messageName()
    {
        return MsgGameEnterReq.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgGameEnterReq.class.getName();
    }

    public boolean isInitialized(MsgGameEnterReq message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgGameEnterReq message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.clientType = input.readUInt32();
                    break;
                case 20:
                    message.clientVer = input.readUInt32();
                    break;
                case 30:
                    message.channelId = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgGameEnterReq message) throws IOException
    {
        if(message.clientType != null)
            output.writeUInt32(10, message.clientType, false);

        if(message.clientVer != null)
            output.writeUInt32(20, message.clientVer, false);

        if(message.channelId != null)
            output.writeUInt32(30, message.channelId, false);
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