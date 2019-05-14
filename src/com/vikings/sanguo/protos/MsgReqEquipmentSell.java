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

public final class MsgReqEquipmentSell implements Externalizable, Message<MsgReqEquipmentSell>, Schema<MsgReqEquipmentSell>
{

    public static Schema<MsgReqEquipmentSell> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqEquipmentSell getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqEquipmentSell DEFAULT_INSTANCE = new MsgReqEquipmentSell();

    
    private Long id;

    public MsgReqEquipmentSell()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Long getId()
    {
        return id == null ? 0L : id;
    }

    public MsgReqEquipmentSell setId(Long id)
    {
        this.id = id;
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

    public Schema<MsgReqEquipmentSell> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqEquipmentSell newMessage()
    {
        return new MsgReqEquipmentSell();
    }

    public Class<MsgReqEquipmentSell> typeClass()
    {
        return MsgReqEquipmentSell.class;
    }

    public String messageName()
    {
        return MsgReqEquipmentSell.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqEquipmentSell.class.getName();
    }

    public boolean isInitialized(MsgReqEquipmentSell message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqEquipmentSell message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqEquipmentSell message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);
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
