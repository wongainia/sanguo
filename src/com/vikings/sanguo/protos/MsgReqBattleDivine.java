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

public final class MsgReqBattleDivine implements Externalizable, Message<MsgReqBattleDivine>, Schema<MsgReqBattleDivine>
{

    public static Schema<MsgReqBattleDivine> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBattleDivine getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBattleDivine DEFAULT_INSTANCE = new MsgReqBattleDivine();

    
    private Long fiefid;
    private Integer type;

    public MsgReqBattleDivine()
    {
        
    }

    // getters and setters

    // fiefid

    public boolean hasFiefid(){
        return fiefid != null;
    }


    public Long getFiefid()
    {
        return fiefid == null ? 0L : fiefid;
    }

    public MsgReqBattleDivine setFiefid(Long fiefid)
    {
        this.fiefid = fiefid;
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

    public MsgReqBattleDivine setType(Integer type)
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

    public Schema<MsgReqBattleDivine> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBattleDivine newMessage()
    {
        return new MsgReqBattleDivine();
    }

    public Class<MsgReqBattleDivine> typeClass()
    {
        return MsgReqBattleDivine.class;
    }

    public String messageName()
    {
        return MsgReqBattleDivine.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBattleDivine.class.getName();
    }

    public boolean isInitialized(MsgReqBattleDivine message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBattleDivine message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.fiefid = input.readUInt64();
                    break;
                case 30:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBattleDivine message) throws IOException
    {
        if(message.fiefid != null)
            output.writeUInt64(10, message.fiefid, false);

        if(message.type != null)
            output.writeUInt32(30, message.type, false);
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
