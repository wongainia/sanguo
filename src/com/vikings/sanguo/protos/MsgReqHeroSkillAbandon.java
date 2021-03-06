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

public final class MsgReqHeroSkillAbandon implements Externalizable, Message<MsgReqHeroSkillAbandon>, Schema<MsgReqHeroSkillAbandon>
{

    public static Schema<MsgReqHeroSkillAbandon> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqHeroSkillAbandon getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqHeroSkillAbandon DEFAULT_INSTANCE = new MsgReqHeroSkillAbandon();

    
    private Long hero;
    private Integer slotid;

    public MsgReqHeroSkillAbandon()
    {
        
    }

    // getters and setters

    // hero

    public boolean hasHero(){
        return hero != null;
    }


    public Long getHero()
    {
        return hero == null ? 0L : hero;
    }

    public MsgReqHeroSkillAbandon setHero(Long hero)
    {
        this.hero = hero;
        return this;
    }

    // slotid

    public boolean hasSlotid(){
        return slotid != null;
    }


    public Integer getSlotid()
    {
        return slotid == null ? 0 : slotid;
    }

    public MsgReqHeroSkillAbandon setSlotid(Integer slotid)
    {
        this.slotid = slotid;
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

    public Schema<MsgReqHeroSkillAbandon> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqHeroSkillAbandon newMessage()
    {
        return new MsgReqHeroSkillAbandon();
    }

    public Class<MsgReqHeroSkillAbandon> typeClass()
    {
        return MsgReqHeroSkillAbandon.class;
    }

    public String messageName()
    {
        return MsgReqHeroSkillAbandon.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqHeroSkillAbandon.class.getName();
    }

    public boolean isInitialized(MsgReqHeroSkillAbandon message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqHeroSkillAbandon message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.hero = input.readUInt64();
                    break;
                case 20:
                    message.slotid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqHeroSkillAbandon message) throws IOException
    {
        if(message.hero != null)
            output.writeUInt64(10, message.hero, false);

        if(message.slotid != null)
            output.writeUInt32(20, message.slotid, false);
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
