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

public final class ArenaLogInfo implements Externalizable, Message<ArenaLogInfo>, Schema<ArenaLogInfo>
{

    public static Schema<ArenaLogInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ArenaLogInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ArenaLogInfo DEFAULT_INSTANCE = new ArenaLogInfo();

    
    private Long id;
    private Integer attacker;
    private Integer attackerPos;
    private Integer defender;
    private Integer defenderPos;
    private Integer battleResult;
    private Integer time;
    private List<HeroIdInfo> attackHeros;
    private List<HeroIdInfo> defendHeros;
    private Long battleLog;

    public ArenaLogInfo()
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

    public ArenaLogInfo setId(Long id)
    {
        this.id = id;
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

    public ArenaLogInfo setAttacker(Integer attacker)
    {
        this.attacker = attacker;
        return this;
    }

    // attackerPos

    public boolean hasAttackerPos(){
        return attackerPos != null;
    }


    public Integer getAttackerPos()
    {
        return attackerPos == null ? 0 : attackerPos;
    }

    public ArenaLogInfo setAttackerPos(Integer attackerPos)
    {
        this.attackerPos = attackerPos;
        return this;
    }

    // defender

    public boolean hasDefender(){
        return defender != null;
    }


    public Integer getDefender()
    {
        return defender == null ? 0 : defender;
    }

    public ArenaLogInfo setDefender(Integer defender)
    {
        this.defender = defender;
        return this;
    }

    // defenderPos

    public boolean hasDefenderPos(){
        return defenderPos != null;
    }


    public Integer getDefenderPos()
    {
        return defenderPos == null ? 0 : defenderPos;
    }

    public ArenaLogInfo setDefenderPos(Integer defenderPos)
    {
        this.defenderPos = defenderPos;
        return this;
    }

    // battleResult

    public boolean hasBattleResult(){
        return battleResult != null;
    }


    public Integer getBattleResult()
    {
        return battleResult == null ? 0 : battleResult;
    }

    public ArenaLogInfo setBattleResult(Integer battleResult)
    {
        this.battleResult = battleResult;
        return this;
    }

    // time

    public boolean hasTime(){
        return time != null;
    }


    public Integer getTime()
    {
        return time == null ? 0 : time;
    }

    public ArenaLogInfo setTime(Integer time)
    {
        this.time = time;
        return this;
    }

    // attackHeros

    public boolean hasAttackHeros(){
        return attackHeros != null;
    }


    public List<HeroIdInfo> getAttackHerosList()
    {
        return attackHeros == null?  new ArrayList<HeroIdInfo>():attackHeros;
    }

    public int getAttackHerosCount()
    {
        return attackHeros == null?0:attackHeros.size();
    }

    public HeroIdInfo getAttackHeros(int i)
    {
        return attackHeros == null?null:attackHeros.get(i);
    }


    public ArenaLogInfo setAttackHerosList(List<HeroIdInfo> attackHeros)
    {
        this.attackHeros = attackHeros;
        return this;    
    }

    // defendHeros

    public boolean hasDefendHeros(){
        return defendHeros != null;
    }


    public List<HeroIdInfo> getDefendHerosList()
    {
        return defendHeros == null?  new ArrayList<HeroIdInfo>():defendHeros;
    }

    public int getDefendHerosCount()
    {
        return defendHeros == null?0:defendHeros.size();
    }

    public HeroIdInfo getDefendHeros(int i)
    {
        return defendHeros == null?null:defendHeros.get(i);
    }


    public ArenaLogInfo setDefendHerosList(List<HeroIdInfo> defendHeros)
    {
        this.defendHeros = defendHeros;
        return this;    
    }

    // battleLog

    public boolean hasBattleLog(){
        return battleLog != null;
    }


    public Long getBattleLog()
    {
        return battleLog == null ? 0L : battleLog;
    }

    public ArenaLogInfo setBattleLog(Long battleLog)
    {
        this.battleLog = battleLog;
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

    public Schema<ArenaLogInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ArenaLogInfo newMessage()
    {
        return new ArenaLogInfo();
    }

    public Class<ArenaLogInfo> typeClass()
    {
        return ArenaLogInfo.class;
    }

    public String messageName()
    {
        return ArenaLogInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ArenaLogInfo.class.getName();
    }

    public boolean isInitialized(ArenaLogInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ArenaLogInfo message) throws IOException
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
                    message.attacker = input.readUInt32();
                    break;
                case 30:
                    message.attackerPos = input.readUInt32();
                    break;
                case 40:
                    message.defender = input.readUInt32();
                    break;
                case 50:
                    message.defenderPos = input.readUInt32();
                    break;
                case 60:
                    message.battleResult = input.readUInt32();
                    break;
                case 70:
                    message.time = input.readUInt32();
                    break;
                case 71:
                    if(message.attackHeros == null)
                        message.attackHeros = new ArrayList<HeroIdInfo>();
                    message.attackHeros.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 72:
                    if(message.defendHeros == null)
                        message.defendHeros = new ArrayList<HeroIdInfo>();
                    message.defendHeros.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                case 80:
                    message.battleLog = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ArenaLogInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.attacker != null)
            output.writeUInt32(20, message.attacker, false);

        if(message.attackerPos != null)
            output.writeUInt32(30, message.attackerPos, false);

        if(message.defender != null)
            output.writeUInt32(40, message.defender, false);

        if(message.defenderPos != null)
            output.writeUInt32(50, message.defenderPos, false);

        if(message.battleResult != null)
            output.writeUInt32(60, message.battleResult, false);

        if(message.time != null)
            output.writeUInt32(70, message.time, false);

        if(message.attackHeros != null)
        {
            for(HeroIdInfo attackHeros : message.attackHeros)
            {
                if(attackHeros != null)
                    output.writeObject(71, attackHeros, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.defendHeros != null)
        {
            for(HeroIdInfo defendHeros : message.defendHeros)
            {
                if(defendHeros != null)
                    output.writeObject(72, defendHeros, HeroIdInfo.getSchema(), true);
            }
        }


        if(message.battleLog != null)
            output.writeUInt64(80, message.battleLog, false);
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
