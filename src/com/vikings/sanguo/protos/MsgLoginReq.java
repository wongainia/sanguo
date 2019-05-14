// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class MsgLoginReq implements Externalizable, Message<MsgLoginReq>, Schema<MsgLoginReq>
{

    public static Schema<MsgLoginReq> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgLoginReq getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgLoginReq DEFAULT_INSTANCE = new MsgLoginReq();

    
    private String psw;
    private ByteString aesKey;
    private Long reqid;

    public MsgLoginReq()
    {
        
    }

    // getters and setters

    // psw

    public boolean hasPsw(){
        return psw != null;
    }


    public String getPsw()
    {
        return psw == null ? "" : psw;
    }

    public MsgLoginReq setPsw(String psw)
    {
        this.psw = psw;
        return this;
    }

    // aesKey

    public boolean hasAesKey(){
        return aesKey != null;
    }


    public ByteString getAesKey()
    {
        return aesKey == null ? ByteString.bytesDefaultValue("") : aesKey;
    }

    public MsgLoginReq setAesKey(ByteString aesKey)
    {
        this.aesKey = aesKey;
        return this;
    }

    // reqid

    public boolean hasReqid(){
        return reqid != null;
    }


    public Long getReqid()
    {
        return reqid == null ? 0L : reqid;
    }

    public MsgLoginReq setReqid(Long reqid)
    {
        this.reqid = reqid;
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

    public Schema<MsgLoginReq> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgLoginReq newMessage()
    {
        return new MsgLoginReq();
    }

    public Class<MsgLoginReq> typeClass()
    {
        return MsgLoginReq.class;
    }

    public String messageName()
    {
        return MsgLoginReq.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgLoginReq.class.getName();
    }

    public boolean isInitialized(MsgLoginReq message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgLoginReq message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.psw = input.readString();
                    break;
                case 20:
                    message.aesKey = input.readBytes();
                    break;
                case 30:
                    message.reqid = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgLoginReq message) throws IOException
    {
        if(message.psw != null)
            output.writeString(10, message.psw, false);

        if(message.aesKey != null)
            output.writeBytes(20, message.aesKey, false);

        if(message.reqid != null)
            output.writeUInt64(30, message.reqid, false);
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
