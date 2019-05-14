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

public final class MsgReqBloodConf implements Externalizable, Message<MsgReqBloodConf>, Schema<MsgReqBloodConf>
{

    public static Schema<MsgReqBloodConf> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBloodConf getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBloodConf DEFAULT_INSTANCE = new MsgReqBloodConf();

    
    private List<HeroIdInfo> heroInfos;
    private TroopInfo troop;

    public MsgReqBloodConf()
    {
        
    }

    // getters and setters

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


    public MsgReqBloodConf setHeroInfosList(List<HeroIdInfo> heroInfos)
    {
        this.heroInfos = heroInfos;
        return this;    
    }

    // troop

    public boolean hasTroop(){
        return troop != null;
    }


    public TroopInfo getTroop()
    {
        return troop == null ? new TroopInfo() : troop;
    }

    public MsgReqBloodConf setTroop(TroopInfo troop)
    {
        this.troop = troop;
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

    public Schema<MsgReqBloodConf> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBloodConf newMessage()
    {
        return new MsgReqBloodConf();
    }

    public Class<MsgReqBloodConf> typeClass()
    {
        return MsgReqBloodConf.class;
    }

    public String messageName()
    {
        return MsgReqBloodConf.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBloodConf.class.getName();
    }

    public boolean isInitialized(MsgReqBloodConf message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBloodConf message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroIdInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 20:
                    message.troop = input.mergeObject(message.troop, TroopInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBloodConf message) throws IOException
    {
        if(message.heroInfos != null)
        {
            for(HeroIdInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(10, heroInfos, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.troop != null)
             output.writeObject(20, message.troop, TroopInfo.getSchema(), false);

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
