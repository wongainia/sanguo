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

public final class MsgReqUserStatusUpdate implements Externalizable, Message<MsgReqUserStatusUpdate>, Schema<MsgReqUserStatusUpdate>
{

    public static Schema<MsgReqUserStatusUpdate> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqUserStatusUpdate getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqUserStatusUpdate DEFAULT_INSTANCE = new MsgReqUserStatusUpdate();

    
    private Integer statusid;
    private Integer type;

    public MsgReqUserStatusUpdate()
    {
        
    }

    // getters and setters

    // statusid

    public boolean hasStatusid(){
        return statusid != null;
    }


    public Integer getStatusid()
    {
        return statusid == null ? 0 : statusid;
    }

    public MsgReqUserStatusUpdate setStatusid(Integer statusid)
    {
        this.statusid = statusid;
        return this;
    }

    // type

    public boolean hasType(){
        return type != null;
    }


    public Integer getType()
    {
        return type == null ? 0 : type;
    }

    public MsgReqUserStatusUpdate setType(Integer type)
    {
        this.type = type;
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

    public Schema<MsgReqUserStatusUpdate> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqUserStatusUpdate newMessage()
    {
        return new MsgReqUserStatusUpdate();
    }

    public Class<MsgReqUserStatusUpdate> typeClass()
    {
        return MsgReqUserStatusUpdate.class;
    }

    public String messageName()
    {
        return MsgReqUserStatusUpdate.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqUserStatusUpdate.class.getName();
    }

    public boolean isInitialized(MsgReqUserStatusUpdate message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqUserStatusUpdate message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.statusid = input.readUInt32();
                    break;
                case 20:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqUserStatusUpdate message) throws IOException
    {
        if(message.statusid != null)
            output.writeUInt32(10, message.statusid, false);

        if(message.type != null)
            output.writeUInt32(20, message.type, false);
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
