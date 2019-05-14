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

public final class MsgReqTrainingComplete implements Externalizable, Message<MsgReqTrainingComplete>, Schema<MsgReqTrainingComplete>
{

    public static Schema<MsgReqTrainingComplete> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqTrainingComplete getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqTrainingComplete DEFAULT_INSTANCE = new MsgReqTrainingComplete();

    
    private Integer trainingId;

    public MsgReqTrainingComplete()
    {
        
    }

    // getters and setters

    // trainingId

    public boolean hasTrainingId(){
        return trainingId != null;
    }


    public Integer getTrainingId()
    {
        return trainingId == null ? 0 : trainingId;
    }

    public MsgReqTrainingComplete setTrainingId(Integer trainingId)
    {
        this.trainingId = trainingId;
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

    public Schema<MsgReqTrainingComplete> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqTrainingComplete newMessage()
    {
        return new MsgReqTrainingComplete();
    }

    public Class<MsgReqTrainingComplete> typeClass()
    {
        return MsgReqTrainingComplete.class;
    }

    public String messageName()
    {
        return MsgReqTrainingComplete.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqTrainingComplete.class.getName();
    }

    public boolean isInitialized(MsgReqTrainingComplete message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqTrainingComplete message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 20:
                    message.trainingId = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqTrainingComplete message) throws IOException
    {
        if(message.trainingId != null)
            output.writeUInt32(20, message.trainingId, false);
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
