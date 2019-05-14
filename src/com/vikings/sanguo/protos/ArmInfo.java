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

public final class ArmInfo implements Externalizable, Message<ArmInfo>, Schema<ArmInfo>
{

    public static Schema<ArmInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ArmInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ArmInfo DEFAULT_INSTANCE = new ArmInfo();

    
    private Integer id;
    private Integer count;

    public ArmInfo()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Integer getId()
    {
        return id == null ? 0 : id;
    }

    public ArmInfo setId(Integer id)
    {
        this.id = id;
        return this;
    }

    // count

    public boolean hasCount(){
        return count != null;
    }


    public Integer getCount()
    {
        return count == null ? 0 : count;
    }

    public ArmInfo setCount(Integer count)
    {
        this.count = count;
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

    public Schema<ArmInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ArmInfo newMessage()
    {
        return new ArmInfo();
    }

    public Class<ArmInfo> typeClass()
    {
        return ArmInfo.class;
    }

    public String messageName()
    {
        return ArmInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ArmInfo.class.getName();
    }

    public boolean isInitialized(ArmInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ArmInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt32();
                    break;
                case 20:
                    message.count = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ArmInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt32(10, message.id, false);

        if(message.count != null)
            output.writeUInt32(20, message.count, false);
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
