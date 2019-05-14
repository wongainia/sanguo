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

public final class EquipEffect implements Externalizable, Message<EquipEffect>, Schema<EquipEffect>
{

    public static Schema<EquipEffect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static EquipEffect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final EquipEffect DEFAULT_INSTANCE = new EquipEffect();

    
    private Integer type;
    private Integer minQuality;
    private Integer exp;
    private Integer rewardTime;
    private Integer coolStartTime;

    public EquipEffect()
    {
        
    }

    // getters and setters

    // type

    public boolean hasType(){
        return type != null;
    }


    public Integer getType()
    {
        return type == null ? 0 : type;
    }

    public EquipEffect setType(Integer type)
    {
        this.type = type;
        return this;
    }

    // minQuality

    public boolean hasMinQuality(){
        return minQuality != null;
    }


    public Integer getMinQuality()
    {
        return minQuality == null ? 0 : minQuality;
    }

    public EquipEffect setMinQuality(Integer minQuality)
    {
        this.minQuality = minQuality;
        return this;
    }

    // exp

    public boolean hasExp(){
        return exp != null;
    }


    public Integer getExp()
    {
        return exp == null ? 0 : exp;
    }

    public EquipEffect setExp(Integer exp)
    {
        this.exp = exp;
        return this;
    }

    // rewardTime

    public boolean hasRewardTime(){
        return rewardTime != null;
    }


    public Integer getRewardTime()
    {
        return rewardTime == null ? 0 : rewardTime;
    }

    public EquipEffect setRewardTime(Integer rewardTime)
    {
        this.rewardTime = rewardTime;
        return this;
    }

    // coolStartTime

    public boolean hasCoolStartTime(){
        return coolStartTime != null;
    }


    public Integer getCoolStartTime()
    {
        return coolStartTime == null ? 0 : coolStartTime;
    }

    public EquipEffect setCoolStartTime(Integer coolStartTime)
    {
        this.coolStartTime = coolStartTime;
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

    public Schema<EquipEffect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public EquipEffect newMessage()
    {
        return new EquipEffect();
    }

    public Class<EquipEffect> typeClass()
    {
        return EquipEffect.class;
    }

    public String messageName()
    {
        return EquipEffect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return EquipEffect.class.getName();
    }

    public boolean isInitialized(EquipEffect message)
    {
        return true;
    }

    public void mergeFrom(Input input, EquipEffect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.type = input.readUInt32();
                    break;
                case 20:
                    message.minQuality = input.readUInt32();
                    break;
                case 30:
                    message.exp = input.readUInt32();
                    break;
                case 40:
                    message.rewardTime = input.readUInt32();
                    break;
                case 50:
                    message.coolStartTime = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, EquipEffect message) throws IOException
    {
        if(message.type != null)
            output.writeUInt32(10, message.type, false);

        if(message.minQuality != null)
            output.writeUInt32(20, message.minQuality, false);

        if(message.exp != null)
            output.writeUInt32(30, message.exp, false);

        if(message.rewardTime != null)
            output.writeUInt32(40, message.rewardTime, false);

        if(message.coolStartTime != null)
            output.writeUInt32(50, message.coolStartTime, false);
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
