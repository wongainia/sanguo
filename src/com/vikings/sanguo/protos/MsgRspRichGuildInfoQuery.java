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

public final class MsgRspRichGuildInfoQuery implements Externalizable, Message<MsgRspRichGuildInfoQuery>, Schema<MsgRspRichGuildInfoQuery>
{

    public static Schema<MsgRspRichGuildInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspRichGuildInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspRichGuildInfoQuery DEFAULT_INSTANCE = new MsgRspRichGuildInfoQuery();

    
    private RichGuildInfo info;

    public MsgRspRichGuildInfoQuery()
    {
        
    }

    // getters and setters

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public RichGuildInfo getInfo()
    {
        return info == null ? new RichGuildInfo() : info;
    }

    public MsgRspRichGuildInfoQuery setInfo(RichGuildInfo info)
    {
        this.info = info;
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

    public Schema<MsgRspRichGuildInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspRichGuildInfoQuery newMessage()
    {
        return new MsgRspRichGuildInfoQuery();
    }

    public Class<MsgRspRichGuildInfoQuery> typeClass()
    {
        return MsgRspRichGuildInfoQuery.class;
    }

    public String messageName()
    {
        return MsgRspRichGuildInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspRichGuildInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgRspRichGuildInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspRichGuildInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 20:
                    message.info = input.mergeObject(message.info, RichGuildInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspRichGuildInfoQuery message) throws IOException
    {
        if(message.info != null)
             output.writeObject(20, message.info, RichGuildInfo.getSchema(), false);

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
