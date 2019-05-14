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

public final class OtherFiefInfo implements Externalizable, Message<OtherFiefInfo>, Schema<OtherFiefInfo>
{

    public static Schema<OtherFiefInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static OtherFiefInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final OtherFiefInfo DEFAULT_INSTANCE = new OtherFiefInfo();

    
    private Long id;
    private Integer propid;
    private Integer userid;
    private BuildingInfo building;
    private Integer battleState;
    private Integer battleTime;
    private Integer attacker;
    private Integer nextExtraBattleTime;
    private TroopInfo info;
    private List<HeroIdInfo> firstHeroInfos;
    private List<HeroIdInfo> secondHeroInfos;
    private UserTroopEffectInfo troopEffectInfo;

    public OtherFiefInfo()
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

    public OtherFiefInfo setId(Long id)
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

    public OtherFiefInfo setPropid(Integer propid)
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

    public OtherFiefInfo setUserid(Integer userid)
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

    public OtherFiefInfo setBuilding(BuildingInfo building)
    {
        this.building = building;
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

    public OtherFiefInfo setBattleState(Integer battleState)
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

    public OtherFiefInfo setBattleTime(Integer battleTime)
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

    public OtherFiefInfo setAttacker(Integer attacker)
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

    public OtherFiefInfo setNextExtraBattleTime(Integer nextExtraBattleTime)
    {
        this.nextExtraBattleTime = nextExtraBattleTime;
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

    public OtherFiefInfo setInfo(TroopInfo info)
    {
        this.info = info;
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


    public OtherFiefInfo setFirstHeroInfosList(List<HeroIdInfo> firstHeroInfos)
    {
        this.firstHeroInfos = firstHeroInfos;
        return this;    
    }

    // secondHeroInfos

    public boolean hasSecondHeroInfos(){
        return secondHeroInfos != null;
    }


    public List<HeroIdInfo> getSecondHeroInfosList()
    {
        return secondHeroInfos == null?  new ArrayList<HeroIdInfo>():secondHeroInfos;
    }

    public int getSecondHeroInfosCount()
    {
        return secondHeroInfos == null?0:secondHeroInfos.size();
    }

    public HeroIdInfo getSecondHeroInfos(int i)
    {
        return secondHeroInfos == null?null:secondHeroInfos.get(i);
    }


    public OtherFiefInfo setSecondHeroInfosList(List<HeroIdInfo> secondHeroInfos)
    {
        this.secondHeroInfos = secondHeroInfos;
        return this;    
    }

    // troopEffectInfo

    public boolean hasTroopEffectInfo(){
        return troopEffectInfo != null;
    }


    public UserTroopEffectInfo getTroopEffectInfo()
    {
        return troopEffectInfo == null ? new UserTroopEffectInfo() : troopEffectInfo;
    }

    public OtherFiefInfo setTroopEffectInfo(UserTroopEffectInfo troopEffectInfo)
    {
        this.troopEffectInfo = troopEffectInfo;
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

    public Schema<OtherFiefInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public OtherFiefInfo newMessage()
    {
        return new OtherFiefInfo();
    }

    public Class<OtherFiefInfo> typeClass()
    {
        return OtherFiefInfo.class;
    }

    public String messageName()
    {
        return OtherFiefInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return OtherFiefInfo.class.getName();
    }

    public boolean isInitialized(OtherFiefInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, OtherFiefInfo message) throws IOException
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

                case 80:
                    message.battleState = input.readUInt32();
                    break;
                case 90:
                    message.battleTime = input.readUInt32();
                    break;
                case 100:
                    message.attacker = input.readUInt32();
                    break;
                case 110:
                    message.nextExtraBattleTime = input.readUInt32();
                    break;
                case 200:
                    message.info = input.mergeObject(message.info, TroopInfo.getSchema());
                    break;

                case 210:
                    if(message.firstHeroInfos == null)
                        message.firstHeroInfos = new ArrayList<HeroIdInfo>();
                    message.firstHeroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 220:
                    if(message.secondHeroInfos == null)
                        message.secondHeroInfos = new ArrayList<HeroIdInfo>();
                    message.secondHeroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 240:
                    message.troopEffectInfo = input.mergeObject(message.troopEffectInfo, UserTroopEffectInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, OtherFiefInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.propid != null)
            output.writeUInt32(20, message.propid, false);

        if(message.userid != null)
            output.writeUInt32(30, message.userid, false);

        if(message.building != null)
             output.writeObject(40, message.building, BuildingInfo.getSchema(), false);


        if(message.battleState != null)
            output.writeUInt32(80, message.battleState, false);

        if(message.battleTime != null)
            output.writeUInt32(90, message.battleTime, false);

        if(message.attacker != null)
            output.writeUInt32(100, message.attacker, false);

        if(message.nextExtraBattleTime != null)
            output.writeUInt32(110, message.nextExtraBattleTime, false);

        if(message.info != null)
             output.writeObject(200, message.info, TroopInfo.getSchema(), false);


        if(message.firstHeroInfos != null)
        {
            for(HeroIdInfo firstHeroInfos : message.firstHeroInfos)
            {
                if(firstHeroInfos != null)
                    output.writeObject(210, firstHeroInfos, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.secondHeroInfos != null)
        {
            for(HeroIdInfo secondHeroInfos : message.secondHeroInfos)
            {
                if(secondHeroInfos != null)
                    output.writeObject(220, secondHeroInfos, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.troopEffectInfo != null)
             output.writeObject(240, message.troopEffectInfo, UserTroopEffectInfo.getSchema(), false);

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