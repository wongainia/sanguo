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

public final class MsgRspAccountBinding2 implements Externalizable, Message<MsgRspAccountBinding2>, Schema<MsgRspAccountBinding2>
{

    public static Schema<MsgRspAccountBinding2> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspAccountBinding2 getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspAccountBinding2 DEFAULT_INSTANCE = new MsgRspAccountBinding2();

    
    private Integer inviter;
    private ReturnInfo ri;

    public MsgRspAccountBinding2()
    {
        
    }

    // getters and setters

    // inviter

    public boolean hasInviter(){
        return inviter != null;
    }


    public Integer getInviter()
    {
        return inviter == null ? 0 : inviter;
    }

    public MsgRspAccountBinding2 setInviter(Integer inviter)
    {
        this.inviter = inviter;
        return this;
    }

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspAccountBinding2 setRi(ReturnInfo ri)
    {
        this.ri = ri;
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

    public Schema<MsgRspAccountBinding2> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspAccountBinding2 newMessage()
    {
        return new MsgRspAccountBinding2();
    }

    public Class<MsgRspAccountBinding2> typeClass()
    {
        return MsgRspAccountBinding2.class;
    }

    public String messageName()
    {
        return MsgRspAccountBinding2.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspAccountBinding2.class.getName();
    }

    public boolean isInitialized(MsgRspAccountBinding2 message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspAccountBinding2 message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.inviter = input.readUInt32();
                    break;
                case 20:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspAccountBinding2 message) throws IOException
    {
        if(message.inviter != null)
            output.writeUInt32(10, message.inviter, false);

        if(message.ri != null)
             output.writeObject(20, message.ri, ReturnInfo.getSchema(), false);

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
