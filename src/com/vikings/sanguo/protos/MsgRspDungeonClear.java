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

public final class MsgRspDungeonClear implements Externalizable, Message<MsgRspDungeonClear>, Schema<MsgRspDungeonClear>
{

    public static Schema<MsgRspDungeonClear> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspDungeonClear getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspDungeonClear DEFAULT_INSTANCE = new MsgRspDungeonClear();

    
    private ReturnInfo ri;
    private ActInfo actInfo;
    private List<HeroInfo> heroInfos;
    private BattleLogInfo log;

    public MsgRspDungeonClear()
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

    public MsgRspDungeonClear setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // actInfo

    public boolean hasActInfo(){
        return actInfo != null;
    }


    public ActInfo getActInfo()
    {
        return actInfo == null ? new ActInfo() : actInfo;
    }

    public MsgRspDungeonClear setActInfo(ActInfo actInfo)
    {
        this.actInfo = actInfo;
        return this;
    }

    // heroInfos

    public boolean hasHeroInfos(){
        return heroInfos != null;
    }


    public List<HeroInfo> getHeroInfosList()
    {
        return heroInfos == null?  new ArrayList<HeroInfo>():heroInfos;
    }

    public int getHeroInfosCount()
    {
        return heroInfos == null?0:heroInfos.size();
    }

    public HeroInfo getHeroInfos(int i)
    {
        return heroInfos == null?null:heroInfos.get(i);
    }


    public MsgRspDungeonClear setHeroInfosList(List<HeroInfo> heroInfos)
    {
        this.heroInfos = heroInfos;
        return this;    
    }

    // log

    public boolean hasLog(){
        return log != null;
    }


    public BattleLogInfo getLog()
    {
        return log == null ? new BattleLogInfo() : log;
    }

    public MsgRspDungeonClear setLog(BattleLogInfo log)
    {
        this.log = log;
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

    public Schema<MsgRspDungeonClear> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspDungeonClear newMessage()
    {
        return new MsgRspDungeonClear();
    }

    public Class<MsgRspDungeonClear> typeClass()
    {
        return MsgRspDungeonClear.class;
    }

    public String messageName()
    {
        return MsgRspDungeonClear.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspDungeonClear.class.getName();
    }

    public boolean isInitialized(MsgRspDungeonClear message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspDungeonClear message) throws IOException
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
                    message.actInfo = input.mergeObject(message.actInfo, ActInfo.getSchema());
                    break;

                case 30:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroInfo.getSchema()));
                    break;

                case 40:
                    message.log = input.mergeObject(message.log, BattleLogInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspDungeonClear message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.actInfo != null)
             output.writeObject(20, message.actInfo, ActInfo.getSchema(), false);


        if(message.heroInfos != null)
        {
            for(HeroInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(30, heroInfos, HeroInfo.getSchema(), true);
            }
        }


        if(message.log != null)
             output.writeObject(40, message.log, BattleLogInfo.getSchema(), false);

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