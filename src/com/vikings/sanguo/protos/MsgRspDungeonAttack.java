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

public final class MsgRspDungeonAttack implements Externalizable, Message<MsgRspDungeonAttack>, Schema<MsgRspDungeonAttack>
{

    public static Schema<MsgRspDungeonAttack> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspDungeonAttack getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspDungeonAttack DEFAULT_INSTANCE = new MsgRspDungeonAttack();

    
    private BattleLogInfo battleLogInfo;
    private ReturnInfo ri;
    private CampaignInfo campaignInfo;
    private List<HeroInfo> heroInfos;

    public MsgRspDungeonAttack()
    {
        
    }

    // getters and setters

    // battleLogInfo

    public boolean hasBattleLogInfo(){
        return battleLogInfo != null;
    }


    public BattleLogInfo getBattleLogInfo()
    {
        return battleLogInfo == null ? new BattleLogInfo() : battleLogInfo;
    }

    public MsgRspDungeonAttack setBattleLogInfo(BattleLogInfo battleLogInfo)
    {
        this.battleLogInfo = battleLogInfo;
        return this;
    }

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspDungeonAttack setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // campaignInfo

    public boolean hasCampaignInfo(){
        return campaignInfo != null;
    }


    public CampaignInfo getCampaignInfo()
    {
        return campaignInfo == null ? new CampaignInfo() : campaignInfo;
    }

    public MsgRspDungeonAttack setCampaignInfo(CampaignInfo campaignInfo)
    {
        this.campaignInfo = campaignInfo;
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


    public MsgRspDungeonAttack setHeroInfosList(List<HeroInfo> heroInfos)
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

    public Schema<MsgRspDungeonAttack> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspDungeonAttack newMessage()
    {
        return new MsgRspDungeonAttack();
    }

    public Class<MsgRspDungeonAttack> typeClass()
    {
        return MsgRspDungeonAttack.class;
    }

    public String messageName()
    {
        return MsgRspDungeonAttack.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspDungeonAttack.class.getName();
    }

    public boolean isInitialized(MsgRspDungeonAttack message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspDungeonAttack message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.battleLogInfo = input.mergeObject(message.battleLogInfo, BattleLogInfo.getSchema());
                    break;

                case 20:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 30:
                    message.campaignInfo = input.mergeObject(message.campaignInfo, CampaignInfo.getSchema());
                    break;

                case 40:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspDungeonAttack message) throws IOException
    {
        if(message.battleLogInfo != null)
             output.writeObject(10, message.battleLogInfo, BattleLogInfo.getSchema(), false);


        if(message.ri != null)
             output.writeObject(20, message.ri, ReturnInfo.getSchema(), false);


        if(message.campaignInfo != null)
             output.writeObject(30, message.campaignInfo, CampaignInfo.getSchema(), false);


        if(message.heroInfos != null)
        {
            for(HeroInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(40, heroInfos, HeroInfo.getSchema(), true);
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