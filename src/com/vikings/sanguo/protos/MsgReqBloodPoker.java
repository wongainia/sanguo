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

public final class MsgReqBloodPoker implements Externalizable, Message<MsgReqBloodPoker>, Schema<MsgReqBloodPoker>
{

    public static Schema<MsgReqBloodPoker> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBloodPoker getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBloodPoker DEFAULT_INSTANCE = new MsgReqBloodPoker();

    
    private Integer pos;

    public MsgReqBloodPoker()
    {
        
    }

    // getters and setters

    // pos

    public boolean hasPos(){
        return pos != null;
    }


    public Integer getPos()
    {
        return pos == null ? 0 : pos;
    }

    public MsgReqBloodPoker setPos(Integer pos)
    {
        this.pos = pos;
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

    public Schema<MsgReqBloodPoker> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBloodPoker newMessage()
    {
        return new MsgReqBloodPoker();
    }

    public Class<MsgReqBloodPoker> typeClass()
    {
        return MsgReqBloodPoker.class;
    }

    public String messageName()
    {
        return MsgReqBloodPoker.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBloodPoker.class.getName();
    }

    public boolean isInitialized(MsgReqBloodPoker message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBloodPoker message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.pos = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBloodPoker message) throws IOException
    {
        if(message.pos != null)
            output.writeUInt32(10, message.pos, false);
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
