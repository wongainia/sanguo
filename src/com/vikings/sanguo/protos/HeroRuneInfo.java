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

public final class HeroRuneInfo implements Externalizable, Message<HeroRuneInfo>, Schema<HeroRuneInfo>
{

    public static Schema<HeroRuneInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static HeroRuneInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final HeroRuneInfo DEFAULT_INSTANCE = new HeroRuneInfo();

    
    private Integer id;
    private Integer weight;
    private Integer skillid;

    public HeroRuneInfo()
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

    public HeroRuneInfo setId(Integer id)
    {
        this.id = id;
        return this;
    }

    // weight

    public boolean hasWeight(){
        return weight != null;
    }


    public Integer getWeight()
    {
        return weight == null ? 0 : weight;
    }

    public HeroRuneInfo setWeight(Integer weight)
    {
        this.weight = weight;
        return this;
    }

    // skillid

    public boolean hasSkillid(){
        return skillid != null;
    }


    public Integer getSkillid()
    {
        return skillid == null ? 0 : skillid;
    }

    public HeroRuneInfo setSkillid(Integer skillid)
    {
        this.skillid = skillid;
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

    public Schema<HeroRuneInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public HeroRuneInfo newMessage()
    {
        return new HeroRuneInfo();
    }

    public Class<HeroRuneInfo> typeClass()
    {
        return HeroRuneInfo.class;
    }

    public String messageName()
    {
        return HeroRuneInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return HeroRuneInfo.class.getName();
    }

    public boolean isInitialized(HeroRuneInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, HeroRuneInfo message) throws IOException
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
                    message.weight = input.readUInt32();
                    break;
                case 30:
                    message.skillid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, HeroRuneInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt32(10, message.id, false);

        if(message.weight != null)
            output.writeUInt32(20, message.weight, false);

        if(message.skillid != null)
            output.writeUInt32(30, message.skillid, false);
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
