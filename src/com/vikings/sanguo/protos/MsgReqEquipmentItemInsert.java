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

public final class MsgReqEquipmentItemInsert implements Externalizable, Message<MsgReqEquipmentItemInsert>, Schema<MsgReqEquipmentItemInsert>
{

    public static Schema<MsgReqEquipmentItemInsert> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqEquipmentItemInsert getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqEquipmentItemInsert DEFAULT_INSTANCE = new MsgReqEquipmentItemInsert();

    
    private Long id;
    private Long hero;
    private Integer itemid;

    public MsgReqEquipmentItemInsert()
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

    public MsgReqEquipmentItemInsert setId(Long id)
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

    public MsgReqEquipmentItemInsert setHero(Long hero)
    {
        this.hero = hero;
        return this;
    }

    // itemid

    public boolean hasItemid(){
        return itemid != null;
    }


    public Integer getItemid()
    {
        return itemid == null ? 0 : itemid;
    }

    public MsgReqEquipmentItemInsert setItemid(Integer itemid)
    {
        this.itemid = itemid;
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

    public Schema<MsgReqEquipmentItemInsert> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqEquipmentItemInsert newMessage()
    {
        return new MsgReqEquipmentItemInsert();
    }

    public Class<MsgReqEquipmentItemInsert> typeClass()
    {
        return MsgReqEquipmentItemInsert.class;
    }

    public String messageName()
    {
        return MsgReqEquipmentItemInsert.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqEquipmentItemInsert.class.getName();
    }

    public boolean isInitialized(MsgReqEquipmentItemInsert message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqEquipmentItemInsert message) throws IOException
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
                    message.itemid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqEquipmentItemInsert message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.hero != null)
            output.writeUInt64(20, message.hero, false);

        if(message.itemid != null)
            output.writeUInt32(30, message.itemid, false);
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
