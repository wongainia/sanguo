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

public final class BattleEventArmInfo implements Externalizable, Message<BattleEventArmInfo>, Schema<BattleEventArmInfo>
{

    public static Schema<BattleEventArmInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BattleEventArmInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BattleEventArmInfo DEFAULT_INSTANCE = new BattleEventArmInfo();

    
    private Integer armid;
    private Long value;
    private Integer ex;

    public BattleEventArmInfo()
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

    public BattleEventArmInfo setArmid(Integer armid)
    {
        this.armid = armid;
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

    public BattleEventArmInfo setValue(Long value)
    {
        this.value = value;
        return this;
    }

    // ex

    public boolean hasEx(){
        return ex != null;
    }


    public Integer getEx()
    {
        return ex == null ? 0 : ex;
    }

    public BattleEventArmInfo setEx(Integer ex)
    {
        this.ex = ex;
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

    public Schema<BattleEventArmInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BattleEventArmInfo newMessage()
    {
        return new BattleEventArmInfo();
    }

    public Class<BattleEventArmInfo> typeClass()
    {
        return BattleEventArmInfo.class;
    }

    public String messageName()
    {
        return BattleEventArmInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BattleEventArmInfo.class.getName();
    }

    public boolean isInitialized(BattleEventArmInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BattleEventArmInfo message) throws IOException
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
                    message.value = input.readInt64();
                    break;
                case 30:
                    message.ex = input.readInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BattleEventArmInfo message) throws IOException
    {
        if(message.armid != null)
            output.writeUInt32(10, message.armid, false);

        if(message.value != null)
            output.writeInt64(20, message.value, false);

        if(message.ex != null)
            output.writeInt32(30, message.ex, false);
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
