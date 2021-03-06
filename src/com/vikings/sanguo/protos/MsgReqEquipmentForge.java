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

public final class MsgReqEquipmentForge implements Externalizable, Message<MsgReqEquipmentForge>, Schema<MsgReqEquipmentForge>
{

    public static Schema<MsgReqEquipmentForge> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqEquipmentForge getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqEquipmentForge DEFAULT_INSTANCE = new MsgReqEquipmentForge();

    
    private Long id;
    private Long hero;
    private Integer effectType;
    private Boolean useCurrency;

    public MsgReqEquipmentForge()
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

    public MsgReqEquipmentForge setId(Long id)
    {
        this.id = id;
        return this;
    }

    // hero

    public boolean hasHero(){
        return hero != null;
    }


    public Long getHero()
    {
        return hero == null ? 0L : hero;
    }

    public MsgReqEquipmentForge setHero(Long hero)
    {
        this.hero = hero;
        return this;
    }

    // effectType

    public boolean hasEffectType(){
        return effectType != null;
    }


    public Integer getEffectType()
    {
        return effectType == null ? 0 : effectType;
    }

    public MsgReqEquipmentForge setEffectType(Integer effectType)
    {
        this.effectType = effectType;
        return this;
    }

    // useCurrency

    public boolean hasUseCurrency(){
        return useCurrency != null;
    }


    public Boolean getUseCurrency()
    {
        return useCurrency == null ? false : useCurrency;
    }

    public MsgReqEquipmentForge setUseCurrency(Boolean useCurrency)
    {
        this.useCurrency = useCurrency;
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

    public Schema<MsgReqEquipmentForge> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqEquipmentForge newMessage()
    {
        return new MsgReqEquipmentForge();
    }

    public Class<MsgReqEquipmentForge> typeClass()
    {
        return MsgReqEquipmentForge.class;
    }

    public String messageName()
    {
        return MsgReqEquipmentForge.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqEquipmentForge.class.getName();
    }

    public boolean isInitialized(MsgReqEquipmentForge message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqEquipmentForge message) throws IOException
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
                case 20:
                    message.hero = input.readUInt64();
                    break;
                case 30:
                    message.effectType = input.readUInt32();
                    break;
                case 40:
                    message.useCurrency = input.readBool();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqEquipmentForge message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.hero != null)
            output.writeUInt64(20, message.hero, false);

        if(message.effectType != null)
            output.writeUInt32(30, message.effectType, false);

        if(message.useCurrency != null)
            output.writeBool(40, message.useCurrency, false);
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
