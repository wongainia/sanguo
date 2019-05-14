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

public final class MsgRspEquipmentItemRemove implements Externalizable, Message<MsgRspEquipmentItemRemove>, Schema<MsgRspEquipmentItemRemove>
{

    public static Schema<MsgRspEquipmentItemRemove> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspEquipmentItemRemove getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspEquipmentItemRemove DEFAULT_INSTANCE = new MsgRspEquipmentItemRemove();

    
    private ReturnInfo ri;
    private HeroInfo heroInfo;
    private EquipmentInfo equipmentInfo;

    public MsgRspEquipmentItemRemove()
    {
        
    }

    // getters and setters

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspEquipmentItemRemove setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // heroInfo

    public boolean hasHeroInfo(){
        return heroInfo != null;
    }


    public HeroInfo getHeroInfo()
    {
        return heroInfo == null ? new HeroInfo() : heroInfo;
    }

    public MsgRspEquipmentItemRemove setHeroInfo(HeroInfo heroInfo)
    {
        this.heroInfo = heroInfo;
        return this;
    }

    // equipmentInfo

    public boolean hasEquipmentInfo(){
        return equipmentInfo != null;
    }


    public EquipmentInfo getEquipmentInfo()
    {
        return equipmentInfo == null ? new EquipmentInfo() : equipmentInfo;
    }

    public MsgRspEquipmentItemRemove setEquipmentInfo(EquipmentInfo equipmentInfo)
    {
        this.equipmentInfo = equipmentInfo;
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

    public Schema<MsgRspEquipmentItemRemove> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspEquipmentItemRemove newMessage()
    {
        return new MsgRspEquipmentItemRemove();
    }

    public Class<MsgRspEquipmentItemRemove> typeClass()
    {
        return MsgRspEquipmentItemRemove.class;
    }

    public String messageName()
    {
        return MsgRspEquipmentItemRemove.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspEquipmentItemRemove.class.getName();
    }

    public boolean isInitialized(MsgRspEquipmentItemRemove message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspEquipmentItemRemove message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 20:
                    message.heroInfo = input.mergeObject(message.heroInfo, HeroInfo.getSchema());
                    break;

                case 30:
                    message.equipmentInfo = input.mergeObject(message.equipmentInfo, EquipmentInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspEquipmentItemRemove message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.heroInfo != null)
             output.writeObject(20, message.heroInfo, HeroInfo.getSchema(), false);


        if(message.equipmentInfo != null)
             output.writeObject(30, message.equipmentInfo, EquipmentInfo.getSchema(), false);

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