// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

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

public final class BriefFiefInfo implements Externalizable, Message<BriefFiefInfo>, Schema<BriefFiefInfo>
{

    public static Schema<BriefFiefInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BriefFiefInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BriefFiefInfo DEFAULT_INSTANCE = new BriefFiefInfo();

    
    private Long id;
    private Integer propid;
    private Integer userid;
    private BuildingInfo building;
    private Integer unitCount;
    private Integer battleState;
    private Integer battleTime;
    private Integer attacker;
    private List<HeroIdInfo> firstHeroInfos;
    private Integer secondHeroCount;
    private Integer battleType;

    public BriefFiefInfo()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Long getId()
    {
        return id == null ? 0L : id;
    }

    public BriefFiefInfo setId(Long id)
    {
        this.id = id;
        return this;
    }

    // propid

    public boolean hasPropid(){
        return propid != null;
    }


    public Integer getPropid()
    {
        return propid == null ? 0 : propid;
    }

    public BriefFiefInfo setPropid(Integer propid)
    {
        this.propid = propid;
        return this;
    }

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public BriefFiefInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // building

    public boolean hasBuilding(){
        return building != null;
    }


    public BuildingInfo getBuilding()
    {
        return building == null ? new BuildingInfo() : building;
    }

    public BriefFiefInfo setBuilding(BuildingInfo building)
    {
        this.building = building;
        return this;
    }

    // unitCount

    public boolean hasUnitCount(){
        return unitCount != null;
    }


    public Integer getUnitCount()
    {
        return unitCount == null ? 0 : unitCount;
    }

    public BriefFiefInfo setUnitCount(Integer unitCount)
    {
        this.unitCount = unitCount;
        return this;
    }

    // battleState

    public boolean hasBattleState(){
        return battleState != null;
    }


    public Integer getBattleState()
    {
        return battleState == null ? 0 : battleState;
    }

    public BriefFiefInfo setBattleState(Integer battleState)
    {
        this.battleState = battleState;
        return this;
    }

    // battleTime

    public boolean hasBattleTime(){
        return battleTime != null;
    }


    public Integer getBattleTime()
    {
        return battleTime == null ? 0 : battleTime;
    }

    public BriefFiefInfo setBattleTime(Integer battleTime)
    {
        this.battleTime = battleTime;
        return this;
    }

    // attacker

    public boolean hasAttacker(){
        return attacker != null;
    }


    public Integer getAttacker()
    {
        return attacker == null ? 0 : attacker;
    }

    public BriefFiefInfo setAttacker(Integer attacker)
    {
        this.attacker = attacker;
        return this;
    }

    // firstHeroInfos

    public boolean hasFirstHeroInfos(){
        return firstHeroInfos != null;
    }


    public List<HeroIdInfo> getFirstHeroInfosList()
    {
        return firstHeroInfos == null?  new ArrayList<HeroIdInfo>():firstHeroInfos;
    }

    public int getFirstHeroInfosCount()
    {
        return firstHeroInfos == null?0:firstHeroInfos.size();
    }

    public HeroIdInfo getFirstHeroInfos(int i)
    {
        return firstHeroInfos == null?null:firstHeroInfos.get(i);
    }


    public BriefFiefInfo setFirstHeroInfosList(List<HeroIdInfo> firstHeroInfos)
    {
        this.firstHeroInfos = firstHeroInfos;
        return this;    
    }

    // secondHeroCount

    public boolean hasSecondHeroCount(){
        return secondHeroCount != null;
    }


    public Integer getSecondHeroCount()
    {
        return secondHeroCount == null ? 0 : secondHeroCount;
    }

    public BriefFiefInfo setSecondHeroCount(Integer secondHeroCount)
    {
        this.secondHeroCount = secondHeroCount;
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

    public BriefFiefInfo setBattleType(Integer battleType)
    {
        this.battleType = battleType;
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

    public Schema<BriefFiefInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BriefFiefInfo newMessage()
    {
        return new BriefFiefInfo();
    }

    public Class<BriefFiefInfo> typeClass()
    {
        return BriefFiefInfo.class;
    }

    public String messageName()
    {
        return BriefFiefInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BriefFiefInfo.class.getName();
    }

    public boolean isInitialized(BriefFiefInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BriefFiefInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt64();
                    break;
                case 20:
                    message.propid = input.readUInt32();
                    break;
                case 30:
                    message.userid = input.readUInt32();
                    break;
                case 40:
                    message.building = input.mergeObject(message.building, BuildingInfo.getSchema());
                    break;

                case 70:
                    message.unitCount = input.readUInt32();
                    break;
                case 80:
                    message.battleState = input.readUInt32();
                    break;
                case 90:
                    message.battleTime = input.readUInt32();
                    break;
                case 100:
                    message.attacker = input.readUInt32();
                    break;
                case 130:
                    if(message.firstHeroInfos == null)
                        message.firstHeroInfos = new ArrayList<HeroIdInfo>();
                    message.firstHeroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 140:
                    message.secondHeroCount = input.readUInt32();
                    break;
                case 150:
                    message.battleType = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BriefFiefInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.propid != null)
            output.writeUInt32(20, message.propid, false);

        if(message.userid != null)
            output.writeUInt32(30, message.userid, false);

        if(message.building != null)
             output.writeObject(40, message.building, BuildingInfo.getSchema(), false);


        if(message.unitCount != null)
            output.writeUInt32(70, message.unitCount, false);

        if(message.battleState != null)
            output.writeUInt32(80, message.battleState, false);

        if(message.battleTime != null)
            output.writeUInt32(90, message.battleTime, false);

        if(message.attacker != null)
            output.writeUInt32(100, message.attacker, false);

        if(message.firstHeroInfos != null)
        {
            for(HeroIdInfo firstHeroInfos : message.firstHeroInfos)
            {
                if(firstHeroInfos != null)
                    output.writeObject(130, firstHeroInfos, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.secondHeroCount != null)
            output.writeUInt32(140, message.secondHeroCount, false);

        if(message.battleType != null)
            output.writeUInt32(150, message.battleType, false);
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
