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

public final class MsgRspEquipmentReplace implements Externalizable, Message<MsgRspEquipmentReplace>, Schema<MsgRspEquipmentReplace>
{

    public static Schema<MsgRspEquipmentReplace> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspEquipmentReplace getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspEquipmentReplace DEFAULT_INSTANCE = new MsgRspEquipmentReplace();

    
    private HeroInfo targetHeroInfo;
    private HeroInfo srcHeroInfo;
    private EquipmentInfo replacedEquipmentInfo;

    public MsgRspEquipmentReplace()
    {
        
    }

    // getters and setters

    // targetHeroInfo

    public boolean hasTargetHeroInfo(){
        return targetHeroInfo != null;
    }


    public HeroInfo getTargetHeroInfo()
    {
        return targetHeroInfo == null ? new HeroInfo() : targetHeroInfo;
    }

    public MsgRspEquipmentReplace setTargetHeroInfo(HeroInfo targetHeroInfo)
    {
        this.targetHeroInfo = targetHeroInfo;
        return this;
    }

    // srcHeroInfo

    public boolean hasSrcHeroInfo(){
        return srcHeroInfo != null;
    }


    public HeroInfo getSrcHeroInfo()
    {
        return srcHeroInfo == null ? new HeroInfo() : srcHeroInfo;
    }

    public MsgRspEquipmentReplace setSrcHeroInfo(HeroInfo srcHeroInfo)
    {
        this.srcHeroInfo = srcHeroInfo;
        return this;
    }

    // replacedEquipmentInfo

    public boolean hasReplacedEquipmentInfo(){
        return replacedEquipmentInfo != null;
    }


    public EquipmentInfo getReplacedEquipmentInfo()
    {
        return replacedEquipmentInfo == null ? new EquipmentInfo() : replacedEquipmentInfo;
    }

    public MsgRspEquipmentReplace setReplacedEquipmentInfo(EquipmentInfo replacedEquipmentInfo)
    {
        this.replacedEquipmentInfo = replacedEquipmentInfo;
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

    public Schema<MsgRspEquipmentReplace> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspEquipmentReplace newMessage()
    {
        return new MsgRspEquipmentReplace();
    }

    public Class<MsgRspEquipmentReplace> typeClass()
    {
        return MsgRspEquipmentReplace.class;
    }

    public String messageName()
    {
        return MsgRspEquipmentReplace.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspEquipmentReplace.class.getName();
    }

    public boolean isInitialized(MsgRspEquipmentReplace message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspEquipmentReplace message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.targetHeroInfo = input.mergeObject(message.targetHeroInfo, HeroInfo.getSchema());
                    break;

                case 20:
                    message.srcHeroInfo = input.mergeObject(message.srcHeroInfo, HeroInfo.getSchema());
                    break;

                case 30:
                    message.replacedEquipmentInfo = input.mergeObject(message.replacedEquipmentInfo, EquipmentInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspEquipmentReplace message) throws IOException
    {
        if(message.targetHeroInfo != null)
             output.writeObject(10, message.targetHeroInfo, HeroInfo.getSchema(), false);


        if(message.srcHeroInfo != null)
             output.writeObject(20, message.srcHeroInfo, HeroInfo.getSchema(), false);


        if(message.replacedEquipmentInfo != null)
             output.writeObject(30, message.replacedEquipmentInfo, EquipmentInfo.getSchema(), false);

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