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

public final class BaseFiefInfo implements Externalizable, Message<BaseFiefInfo>, Schema<BaseFiefInfo>
{

    public static Schema<BaseFiefInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BaseFiefInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BaseFiefInfo DEFAULT_INSTANCE = new BaseFiefInfo();

    
    private Long id;
    private Integer propid;
    private Integer userid;
    private BuildingInfo building;
    private Integer unitCount;
    private Integer battleState;
    private Integer battleTime;
    private Integer attacker;
    private Integer nextExtraBattleTime;
    private FiefHeroInfo heroInfo;
    private Integer battleType;

    public BaseFiefInfo()
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

    public BaseFiefInfo setId(Long id)
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

    public BaseFiefInfo setPropid(Integer propid)
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

    public BaseFiefInfo setUserid(Integer userid)
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

    public BaseFiefInfo setBuilding(BuildingInfo building)
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

    public BaseFiefInfo setUnitCount(Integer unitCount)
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

    public BaseFiefInfo setBattleState(Integer battleState)
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

    public BaseFiefInfo setBattleTime(Integer battleTime)
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

    public BaseFiefInfo setAttacker(Integer attacker)
    {
        this.attacker = attacker;
        return this;
    }

    // nextExtraBattleTime

    public boolean hasNextExtraBattleTime(){
        return nextExtraBattleTime != null;
    }


    public Integer getNextExtraBattleTime()
    {
        return nextExtraBattleTime == null ? 0 : nextExtraBattleTime;
    }

    public BaseFiefInfo setNextExtraBattleTime(Integer nextExtraBattleTime)
    {
        this.nextExtraBattleTime = nextExtraBattleTime;
        return this;
    }

    // heroInfo

    public boolean hasHeroInfo(){
        return heroInfo != null;
    }


    public FiefHeroInfo getHeroInfo()
    {
        return heroInfo == null ? new FiefHeroInfo() : heroInfo;
    }

    public BaseFiefInfo setHeroInfo(FiefHeroInfo heroInfo)
    {
        this.heroInfo = heroInfo;
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

    public BaseFiefInfo setBattleType(Integer battleType)
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

    public Schema<BaseFiefInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BaseFiefInfo newMessage()
    {
        return new BaseFiefInfo();
    }

    public Class<BaseFiefInfo> typeClass()
    {
        return BaseFiefInfo.class;
    }

    public String messageName()
    {
        return BaseFiefInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BaseFiefInfo.class.getName();
    }

    public boolean isInitialized(BaseFiefInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BaseFiefInfo message) throws IOException
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

                case 100:
                    message.unitCount = input.readUInt32();
                    break;
                case 110:
                    message.battleState = input.readUInt32();
                    break;
                case 120:
                    message.battleTime = input.readUInt32();
                    break;
                case 130:
                    message.attacker = input.readUInt32();
                    break;
                case 150:
                    message.nextExtraBattleTime = input.readUInt32();
                    break;
                case 160:
                    message.heroInfo = input.mergeObject(message.heroInfo, FiefHeroInfo.getSchema());
                    break;

                case 170:
                    message.battleType = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BaseFiefInfo message) throws IOException
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
            output.writeUInt32(100, message.unitCount, false);

        if(message.battleState != null)
            output.writeUInt32(110, message.battleState, false);

        if(message.battleTime != null)
            output.writeUInt32(120, message.battleTime, false);

        if(message.attacker != null)
            output.writeUInt32(130, message.attacker, false);

        if(message.nextExtraBattleTime != null)
            output.writeUInt32(150, message.nextExtraBattleTime, false);

        if(message.heroInfo != null)
             output.writeObject(160, message.heroInfo, FiefHeroInfo.getSchema(), false);


        if(message.battleType != null)
            output.writeUInt32(170, message.battleType, false);
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
