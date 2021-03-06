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

public final class MsgReqFiefHeroSelect implements Externalizable, Message<MsgReqFiefHeroSelect>, Schema<MsgReqFiefHeroSelect>
{

    public static Schema<MsgReqFiefHeroSelect> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqFiefHeroSelect getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqFiefHeroSelect DEFAULT_INSTANCE = new MsgReqFiefHeroSelect();

    
    private Long fiefid;
    private List<HeroIdInfo> heroInfos;

    public MsgReqFiefHeroSelect()
    {
        
    }

    // getters and setters

    // fiefid

    public boolean hasFiefid(){
        return fiefid != null;
    }


    public Long getFiefid()
    {
        return fiefid == null ? 0L : fiefid;
    }

    public MsgReqFiefHeroSelect setFiefid(Long fiefid)
    {
        this.fiefid = fiefid;
        return this;
    }

    // heroInfos

    public boolean hasHeroInfos(){
        return heroInfos != null;
    }


    public List<HeroIdInfo> getHeroInfosList()
    {
        return heroInfos == null?  new ArrayList<HeroIdInfo>():heroInfos;
    }

    public int getHeroInfosCount()
    {
        return heroInfos == null?0:heroInfos.size();
    }

    public HeroIdInfo getHeroInfos(int i)
    {
        return heroInfos == null?null:heroInfos.get(i);
    }


    public MsgReqFiefHeroSelect setHeroInfosList(List<HeroIdInfo> heroInfos)
    {
        this.heroInfos = heroInfos;
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

    public Schema<MsgReqFiefHeroSelect> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqFiefHeroSelect newMessage()
    {
        return new MsgReqFiefHeroSelect();
    }

    public Class<MsgReqFiefHeroSelect> typeClass()
    {
        return MsgReqFiefHeroSelect.class;
    }

    public String messageName()
    {
        return MsgReqFiefHeroSelect.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqFiefHeroSelect.class.getName();
    }

    public boolean isInitialized(MsgReqFiefHeroSelect message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqFiefHeroSelect message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.fiefid = input.readUInt64();
                    break;
                case 20:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroIdInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqFiefHeroSelect message) throws IOException
    {
        if(message.fiefid != null)
            output.writeUInt64(10, message.fiefid, false);

        if(message.heroInfos != null)
        {
            for(HeroIdInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(20, heroInfos, HeroIdInfo.getSchema(), true);
            }
        }

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
