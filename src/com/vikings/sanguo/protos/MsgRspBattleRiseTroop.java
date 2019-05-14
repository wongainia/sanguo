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

public final class MsgRspBattleRiseTroop implements Externalizable, Message<MsgRspBattleRiseTroop>, Schema<MsgRspBattleRiseTroop>
{

    public static Schema<MsgRspBattleRiseTroop> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspBattleRiseTroop getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspBattleRiseTroop DEFAULT_INSTANCE = new MsgRspBattleRiseTroop();

    
    private BriefFiefInfo fiefInfo;
    private ManorInfo manorInfo;
    private ReturnInfo ri;
    private BriefBattleInfo battleInfo;
    private List<HeroInfo> heroInfos;

    public MsgRspBattleRiseTroop()
    {
        
    }

    // getters and setters

    // fiefInfo

    public boolean hasFiefInfo(){
        return fiefInfo != null;
    }


    public BriefFiefInfo getFiefInfo()
    {
        return fiefInfo == null ? new BriefFiefInfo() : fiefInfo;
    }

    public MsgRspBattleRiseTroop setFiefInfo(BriefFiefInfo fiefInfo)
    {
        this.fiefInfo = fiefInfo;
        return this;
    }

    // manorInfo

    public boolean hasManorInfo(){
        return manorInfo != null;
    }


    public ManorInfo getManorInfo()
    {
        return manorInfo == null ? new ManorInfo() : manorInfo;
    }

    public MsgRspBattleRiseTroop setManorInfo(ManorInfo manorInfo)
    {
        this.manorInfo = manorInfo;
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

    public MsgRspBattleRiseTroop setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // battleInfo

    public boolean hasBattleInfo(){
        return battleInfo != null;
    }


    public BriefBattleInfo getBattleInfo()
    {
        return battleInfo == null ? new BriefBattleInfo() : battleInfo;
    }

    public MsgRspBattleRiseTroop setBattleInfo(BriefBattleInfo battleInfo)
    {
        this.battleInfo = battleInfo;
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


    public MsgRspBattleRiseTroop setHeroInfosList(List<HeroInfo> heroInfos)
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

    public Schema<MsgRspBattleRiseTroop> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspBattleRiseTroop newMessage()
    {
        return new MsgRspBattleRiseTroop();
    }

    public Class<MsgRspBattleRiseTroop> typeClass()
    {
        return MsgRspBattleRiseTroop.class;
    }

    public String messageName()
    {
        return MsgRspBattleRiseTroop.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspBattleRiseTroop.class.getName();
    }

    public boolean isInitialized(MsgRspBattleRiseTroop message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspBattleRiseTroop message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.fiefInfo = input.mergeObject(message.fiefInfo, BriefFiefInfo.getSchema());
                    break;

                case 20:
                    message.manorInfo = input.mergeObject(message.manorInfo, ManorInfo.getSchema());
                    break;

                case 30:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 40:
                    message.battleInfo = input.mergeObject(message.battleInfo, BriefBattleInfo.getSchema());
                    break;

                case 50:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspBattleRiseTroop message) throws IOException
    {
        if(message.fiefInfo != null)
             output.writeObject(10, message.fiefInfo, BriefFiefInfo.getSchema(), false);


        if(message.manorInfo != null)
             output.writeObject(20, message.manorInfo, ManorInfo.getSchema(), false);


        if(message.ri != null)
             output.writeObject(30, message.ri, ReturnInfo.getSchema(), false);


        if(message.battleInfo != null)
             output.writeObject(40, message.battleInfo, BriefBattleInfo.getSchema(), false);


        if(message.heroInfos != null)
        {
            for(HeroInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(50, heroInfos, HeroInfo.getSchema(), true);
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
