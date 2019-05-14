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

public final class TroopEffectInfo implements Externalizable, Message<TroopEffectInfo>, Schema<TroopEffectInfo>
{

    public static Schema<TroopEffectInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TroopEffectInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TroopEffectInfo DEFAULT_INSTANCE = new TroopEffectInfo();

    
    private Integer armid;
    private Integer attr;
    private Integer value;

    public TroopEffectInfo()
    {
        
    }

    // getters and setters

    // armid

    public boolean hasArmid(){
        return armid != null;
    }


    public Integer getArmid()
    {
        return armid == null ? 0 : armid;
    }

    public TroopEffectInfo setArmid(Integer armid)
    {
        this.armid = armid;
        return this;
    }

    // attr

    public boolean hasAttr(){
        return attr != null;
    }


    public Integer getAttr()
    {
        return attr == null ? 0 : attr;
    }

    public TroopEffectInfo setAttr(Integer attr)
    {
        this.attr = attr;
        return this;
    }

    // value

    public boolean hasValue(){
        return value != null;
    }


    public Integer getValue()
    {
        return value == null ? 0 : value;
    }

    public TroopEffectInfo setValue(Integer value)
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

    public Schema<TroopEffectInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TroopEffectInfo newMessage()
    {
        return new TroopEffectInfo();
    }

    public Class<TroopEffectInfo> typeClass()
    {
        return TroopEffectInfo.class;
    }

    public String messageName()
    {
        return TroopEffectInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TroopEffectInfo.class.getName();
    }

    public boolean isInitialized(TroopEffectInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, TroopEffectInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.armid = input.readUInt32();
                    break;
                case 20:
                    message.attr = input.readUInt32();
                    break;
                case 30:
                    message.value = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TroopEffectInfo message) throws IOException
    {
        if(message.armid != null)
            output.writeUInt32(10, message.armid, false);

        if(message.attr != null)
            output.writeUInt32(20, message.attr, false);

        if(message.value != null)
            output.writeUInt32(30, message.value, false);
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
