// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

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

public final class ReturnEffectInfo implements Externalizable, Message<ReturnEffectInfo>, Schema<ReturnEffectInfo>
{

    public static Schema<ReturnEffectInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ReturnEffectInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ReturnEffectInfo DEFAULT_INSTANCE = new ReturnEffectInfo();

    static final Integer DEFAULT_FIELD = new Integer(0);
    static final Long DEFAULT_VALUE = new Long(0l);
    
    private Integer field = DEFAULT_FIELD;
    private Long value = DEFAULT_VALUE;

    public ReturnEffectInfo()
    {
        
    }

    // getters and setters

    // field

    public boolean hasField(){
        return field != null;
    }


    public Integer getField()
    {
        return field == null ? 0 : field;
    }

    public ReturnEffectInfo setField(Integer field)
    {
        this.field = field;
        return this;
    }

    // value

    public boolean hasValue(){
        return value != null;
    }


    public Long getValue()
    {
        return value == null ? 0L : value;
    }

    public ReturnEffectInfo setValue(Long value)
    {
        this.value = value;
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

    public Schema<ReturnEffectInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ReturnEffectInfo newMessage()
    {
        return new ReturnEffectInfo();
    }

    public Class<ReturnEffectInfo> typeClass()
    {
        return ReturnEffectInfo.class;
    }

    public String messageName()
    {
        return ReturnEffectInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ReturnEffectInfo.class.getName();
    }

    public boolean isInitialized(ReturnEffectInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ReturnEffectInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.field = input.readUInt32();
                    break;
                case 20:
                    message.value = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ReturnEffectInfo message) throws IOException
    {
        if(message.field != null && message.field != DEFAULT_FIELD)
            output.writeUInt32(10, message.field, false);

        if(message.value != null && message.value != DEFAULT_VALUE)
            output.writeUInt64(20, message.value, false);
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
