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

public final class MsgReqEquipmentReplace implements Externalizable, Message<MsgReqEquipmentReplace>, Schema<MsgReqEquipmentReplace>
{

    public static Schema<MsgReqEquipmentReplace> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqEquipmentReplace getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqEquipmentReplace DEFAULT_INSTANCE = new MsgReqEquipmentReplace();

    
    private Long id;
    private Long srcHero;
    private Long targetHero;

    public MsgReqEquipmentReplace()
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

    public MsgReqEquipmentReplace setId(Long id)
    {
        this.id = id;
        return this;
    }

    // srcHero

    public boolean hasSrcHero(){
        return srcHero != null;
    }


    public Long getSrcHero()
    {
        return srcHero == null ? 0L : srcHero;
    }

    public MsgReqEquipmentReplace setSrcHero(Long srcHero)
    {
        this.srcHero = srcHero;
        return this;
    }

    // targetHero

    public boolean hasTargetHero(){
        return targetHero != null;
    }


    public Long getTargetHero()
    {
        return targetHero == null ? 0L : targetHero;
    }

    public MsgReqEquipmentReplace setTargetHero(Long targetHero)
    {
        this.targetHero = targetHero;
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

    public Schema<MsgReqEquipmentReplace> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqEquipmentReplace newMessage()
    {
        return new MsgReqEquipmentReplace();
    }

    public Class<MsgReqEquipmentReplace> typeClass()
    {
        return MsgReqEquipmentReplace.class;
    }

    public String messageName()
    {
        return MsgReqEquipmentReplace.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqEquipmentReplace.class.getName();
    }

    public boolean isInitialized(MsgReqEquipmentReplace message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqEquipmentReplace message) throws IOException
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
                    message.srcHero = input.readUInt64();
                    break;
                case 30:
                    message.targetHero = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqEquipmentReplace message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.srcHero != null)
            output.writeUInt64(20, message.srcHero, false);

        if(message.targetHero != null)
            output.writeUInt64(30, message.targetHero, false);
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
