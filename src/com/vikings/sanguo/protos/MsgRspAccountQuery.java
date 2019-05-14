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

public final class MsgRspAccountQuery implements Externalizable, Message<MsgRspAccountQuery>, Schema<MsgRspAccountQuery>
{

    public static Schema<MsgRspAccountQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspAccountQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspAccountQuery DEFAULT_INSTANCE = new MsgRspAccountQuery();

    
    private Integer userid;
    private String psw;

    public MsgRspAccountQuery()
    {
        
    }

    // getters and setters

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public MsgRspAccountQuery setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // psw

    public boolean hasPsw(){
        return psw != null;
    }


    public String getPsw()
    {
        return psw == null ? "" : psw;
    }

    public MsgRspAccountQuery setPsw(String psw)
    {
        this.psw = psw;
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

    public Schema<MsgRspAccountQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspAccountQuery newMessage()
    {
        return new MsgRspAccountQuery();
    }

    public Class<MsgRspAccountQuery> typeClass()
    {
        return MsgRspAccountQuery.class;
    }

    public String messageName()
    {
        return MsgRspAccountQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspAccountQuery.class.getName();
    }

    public boolean isInitialized(MsgRspAccountQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspAccountQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.userid = input.readUInt32();
                    break;
                case 20:
                    message.psw = input.readString();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspAccountQuery message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.psw != null)
            output.writeString(20, message.psw, false);
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
