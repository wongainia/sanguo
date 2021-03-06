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

public final class MsgReqBattleRiseTroop implements Externalizable, Message<MsgReqBattleRiseTroop>, Schema<MsgReqBattleRiseTroop>
{

    public static Schema<MsgReqBattleRiseTroop> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBattleRiseTroop getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBattleRiseTroop DEFAULT_INSTANCE = new MsgReqBattleRiseTroop();

    
    private Long dstFiefid;
    private Integer type;
    private Integer target;
    private Integer battleType;
    private TroopInfo info;
    private List<HeroIdInfo> heroInfos;
    private Integer cost;

    public MsgReqBattleRiseTroop()
    {
        
    }

    // getters and setters

    // dstFiefid

    public boolean hasDstFiefid(){
        return dstFiefid != null;
    }


    public Long getDstFiefid()
    {
        return dstFiefid == null ? 0L : dstFiefid;
    }

    public MsgReqBattleRiseTroop setDstFiefid(Long dstFiefid)
    {
        this.dstFiefid = dstFiefid;
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

    public MsgReqBattleRiseTroop setType(Integer type)
    {
        this.type = type;
        return this;
    }

    // target

    public boolean hasTarget(){
        return target != null;
    }


    public Integer getTarget()
    {
        return target == null ? 0 : target;
    }

    public MsgReqBattleRiseTroop setTarget(Integer target)
    {
        this.target = target;
        return this;
    }

    // battleType

    public boolean hasBattleType(){
        return battleType != null;
    }


    public Integer getBattleType()
    {
        return battleType == null ? 0 : battleType;
    }

    public MsgReqBattleRiseTroop setBattleType(Integer battleType)
    {
        this.battleType = battleType;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public TroopInfo getInfo()
    {
        return info == null ? new TroopInfo() : info;
    }

    public MsgReqBattleRiseTroop setInfo(TroopInfo info)
    {
        this.info = info;
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


    public MsgReqBattleRiseTroop setHeroInfosList(List<HeroIdInfo> heroInfos)
    {
        this.heroInfos = heroInfos;
        return this;    
    }

    // cost

    public boolean hasCost(){
        return cost != null;
    }


    public Integer getCost()
    {
        return cost == null ? 0 : cost;
    }

    public MsgReqBattleRiseTroop setCost(Integer cost)
    {
        this.cost = cost;
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

    public Schema<MsgReqBattleRiseTroop> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBattleRiseTroop newMessage()
    {
        return new MsgReqBattleRiseTroop();
    }

    public Class<MsgReqBattleRiseTroop> typeClass()
    {
        return MsgReqBattleRiseTroop.class;
    }

    public String messageName()
    {
        return MsgReqBattleRiseTroop.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBattleRiseTroop.class.getName();
    }

    public boolean isInitialized(MsgReqBattleRiseTroop message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBattleRiseTroop message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 20:
                    message.dstFiefid = input.readUInt64();
                    break;
                case 30:
                    message.type = input.readUInt32();
                    break;
                case 40:
                    message.target = input.readUInt32();
                    break;
                case 50:
                    message.battleType = input.readUInt32();
                    break;
                case 100:
                    message.info = input.mergeObject(message.info, TroopInfo.getSchema());
                    break;

                case 110:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroIdInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 120:
                    message.cost = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBattleRiseTroop message) throws IOException
    {
        if(message.dstFiefid != null)
            output.writeUInt64(20, message.dstFiefid, false);

        if(message.type != null)
            output.writeUInt32(30, message.type, false);

        if(message.target != null)
            output.writeUInt32(40, message.target, false);

        if(message.battleType != null)
            output.writeUInt32(50, message.battleType, false);

        if(message.info != null)
             output.writeObject(100, message.info, TroopInfo.getSchema(), false);


        if(message.heroInfos != null)
        {
            for(HeroIdInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(110, heroInfos, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.cost != null)
            output.writeUInt32(120, message.cost, false);
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
