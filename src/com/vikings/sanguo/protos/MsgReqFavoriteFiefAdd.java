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

public final class MsgReqFavoriteFiefAdd implements Externalizable, Message<MsgReqFavoriteFiefAdd>, Schema<MsgReqFavoriteFiefAdd>
{

    public static Schema<MsgReqFavoriteFiefAdd> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqFavoriteFiefAdd getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqFavoriteFiefAdd DEFAULT_INSTANCE = new MsgReqFavoriteFiefAdd();

    
    private Long fiefid;

    public MsgReqFavoriteFiefAdd()
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

    public MsgReqFavoriteFiefAdd setFiefid(Long fiefid)
    {
        this.fiefid = fiefid;
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

    public Schema<MsgReqFavoriteFiefAdd> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqFavoriteFiefAdd newMessage()
    {
        return new MsgReqFavoriteFiefAdd();
    }

    public Class<MsgReqFavoriteFiefAdd> typeClass()
    {
        return MsgReqFavoriteFiefAdd.class;
    }

    public String messageName()
    {
        return MsgReqFavoriteFiefAdd.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqFavoriteFiefAdd.class.getName();
    }

    public boolean isInitialized(MsgReqFavoriteFiefAdd message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqFavoriteFiefAdd message) throws IOException
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
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqFavoriteFiefAdd message) throws IOException
    {
        if(message.fiefid != null)
            output.writeUInt64(10, message.fiefid, false);
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