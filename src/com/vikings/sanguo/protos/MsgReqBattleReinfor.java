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

public final class MsgReqBattleReinfor implements Externalizable, Message<MsgReqBattleReinfor>, Schema<MsgReqBattleReinfor>
{

    public static Schema<MsgReqBattleReinfor> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBattleReinfor getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBattleReinfor DEFAULT_INSTANCE = new MsgReqBattleReinfor();

    
    private Integer target;
    private Long dstFiefid;
    private Integer holyCost;
    private Integer type;
    private Integer targetType;
    private TroopInfo info;
    private List<HeroIdInfo> heroInfos;

    public MsgReqBattleReinfor()
    {
        
    }

    // getters and setters

    // target

    public boolean hasTarget(){
        return target != null;
    }


    public Integer getTarget()
    {
        return target == null ? 0 : target;
    }

    public MsgReqBattleReinfor setTarget(Integer target)
    {
        this.target = target;
        return this;
    }

    // dstFiefid

    public boolean hasDstFiefid(){
        return dstFiefid != null;
    }


    public Long getDstFiefid()
    {
        return dstFiefid == null ? 0L : dstFiefid;
    }

    public MsgReqBattleReinfor setDstFiefid(Long dstFiefid)
    {
        this.dstFiefid = dstFiefid;
        return this;
    }

    // holyCost

    public boolean hasHolyCost(){
        return holyCost != null;
    }


    public Integer getHolyCost()
    {
        return holyCost == null ? 0 : holyCost;
    }

    public MsgReqBattleReinfor setHolyCost(Integer holyCost)
    {
        this.holyCost = holyCost;
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

    public MsgReqBattleReinfor setType(Integer type)
    {
        this.type = type;
        return this;
    }

    // targetType

    public boolean hasTargetType(){
        return targetType != null;
    }


    public Integer getTargetType()
    {
        return targetType == null ? 0 : targetType;
    }

    public MsgReqBattleReinfor setTargetType(Integer targetType)
    {
        this.targetType = targetType;
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

    public MsgReqBattleReinfor setInfo(TroopInfo info)
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


    public MsgReqBattleReinfor setHeroInfosList(List<HeroIdInfo> heroInfos)
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

    public Schema<MsgReqBattleReinfor> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBattleReinfor newMessage()
    {
        return new MsgReqBattleReinfor();
    }

    public Class<MsgReqBattleReinfor> typeClass()
    {
        return MsgReqBattleReinfor.class;
    }

    public String messageName()
    {
        return MsgReqBattleReinfor.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBattleReinfor.class.getName();
    }

    public boolean isInitialized(MsgReqBattleReinfor message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBattleReinfor message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.target = input.readUInt32();
                    break;
                case 20:
                    message.dstFiefid = input.readUInt64();
                    break;
                case 30:
                    message.holyCost = input.readUInt32();
                    break;
                case 40:
                    message.type = input.readUInt32();
                    break;
                case 60:
                    message.targetType = input.readUInt32();
                    break;
                case 100:
                    message.info = input.mergeObject(message.info, TroopInfo.getSchema());
                    break;

                case 110:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<HeroIdInfo>();
                    message.heroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBattleReinfor message) throws IOException
    {
        if(message.target != null)
            output.writeUInt32(10, message.target, false);

        if(message.dstFiefid != null)
            output.writeUInt64(20, message.dstFiefid, false);

        if(message.holyCost != null)
            output.writeUInt32(30, message.holyCost, false);

        if(message.type != null)
            output.writeUInt32(40, message.type, false);

        if(message.targetType != null)
            output.writeUInt32(60, message.targetType, false);

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
