// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class MsgReqHeroDevour implements Externalizable, Message<MsgReqHeroDevour>, Schema<MsgReqHeroDevour>
{

    public static Schema<MsgReqHeroDevour> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqHeroDevour getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqHeroDevour DEFAULT_INSTANCE = new MsgReqHeroDevour();

    
    private Long hero;
    private List<Long> bagids;
    private Integer type;

    public MsgReqHeroDevour()
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

    public MsgReqHeroDevour setHero(Long hero)
    {
        this.hero = hero;
        return this;
    }

    // bagids

    public boolean hasBagids(){
        return bagids != null;
    }


    public List<Long> getBagidsList()
    {
        return bagids == null?  new ArrayList<Long>():bagids;
    }

    public int getBagidsCount()
    {
        return bagids == null?0:bagids.size();
    }

    public Long getBagids(int i)
    {
        return bagids == null?null:bagids.get(i);
    }


    public MsgReqHeroDevour setBagidsList(List<Long> bagids)
    {
        this.bagids = bagids;
        return this;    
    }

    // type

    public boolean hasType(){
        return type != null;
    }


    public Integer getType()
    {
        return type == null ? 0 : type;
    }

    public MsgReqHeroDevour setType(Integer type)
    {
        this.type = type;
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

    public Schema<MsgReqHeroDevour> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqHeroDevour newMessage()
    {
        return new MsgReqHeroDevour();
    }

    public Class<MsgReqHeroDevour> typeClass()
    {
        return MsgReqHeroDevour.class;
    }

    public String messageName()
    {
        return MsgReqHeroDevour.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqHeroDevour.class.getName();
    }

    public boolean isInitialized(MsgReqHeroDevour message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqHeroDevour message) throws IOException
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
                    if(message.bagids == null)
                        message.bagids = new ArrayList<Long>();
                    message.bagids.add(input.readUInt64());
                    break;
                case 30:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqHeroDevour message) throws IOException
    {
        if(message.hero != null)
            output.writeUInt64(10, message.hero, false);

        if(message.bagids != null)
        {
            for(Long bagids : message.bagids)
            {
                if(bagids != null)
                    output.writeUInt64(20, bagids, true);
            }
        }

        if(message.type != null)
            output.writeUInt32(30, message.type, false);
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
