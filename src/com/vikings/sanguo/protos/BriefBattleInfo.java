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

public final class BriefBattleInfo implements Externalizable, Message<BriefBattleInfo>, Schema<BriefBattleInfo>
{

    public static Schema<BriefBattleInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BriefBattleInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BriefBattleInfo DEFAULT_INSTANCE = new BriefBattleInfo();

    
    private Long id;
    private Integer type;
    private Long defendFiefid;
    private Long attackFiefid;
    private Integer attacker;
    private Integer defender;
    private Integer state;
    private Integer time;
    private Integer scale;
    private Integer attackUnit;
    private Integer defendUnit;

    public BriefBattleInfo()
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

    public BriefBattleInfo setId(Long id)
    {
        this.id = id;
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

    public BriefBattleInfo setType(Integer type)
    {
        this.type = type;
        return this;
    }

    // defendFiefid

    public boolean hasDefendFiefid(){
        return defendFiefid != null;
    }


    public Long getDefendFiefid()
    {
        return defendFiefid == null ? 0L : defendFiefid;
    }

    public BriefBattleInfo setDefendFiefid(Long defendFiefid)
    {
        this.defendFiefid = defendFiefid;
        return this;
    }

    // attackFiefid

    public boolean hasAttackFiefid(){
        return attackFiefid != null;
    }


    public Long getAttackFiefid()
    {
        return attackFiefid == null ? 0L : attackFiefid;
    }

    public BriefBattleInfo setAttackFiefid(Long attackFiefid)
    {
        this.attackFiefid = attackFiefid;
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

    public BriefBattleInfo setAttacker(Integer attacker)
    {
        this.attacker = attacker;
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

    public BriefBattleInfo setDefender(Integer defender)
    {
        this.defender = defender;
        return this;
    }

    // state

    public boolean hasState(){
        return state != null;
    }


    public Integer getState()
    {
        return state == null ? 0 : state;
    }

    public BriefBattleInfo setState(Integer state)
    {
        this.state = state;
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

    public BriefBattleInfo setTime(Integer time)
    {
        this.time = time;
        return this;
    }

    // scale

    public boolean hasScale(){
        return scale != null;
    }


    public Integer getScale()
    {
        return scale == null ? 0 : scale;
    }

    public BriefBattleInfo setScale(Integer scale)
    {
        this.scale = scale;
        return this;
    }

    // attackUnit

    public boolean hasAttackUnit(){
        return attackUnit != null;
    }


    public Integer getAttackUnit()
    {
        return attackUnit == null ? 0 : attackUnit;
    }

    public BriefBattleInfo setAttackUnit(Integer attackUnit)
    {
        this.attackUnit = attackUnit;
        return this;
    }

    // defendUnit

    public boolean hasDefendUnit(){
        return defendUnit != null;
    }


    public Integer getDefendUnit()
    {
        return defendUnit == null ? 0 : defendUnit;
    }

    public BriefBattleInfo setDefendUnit(Integer defendUnit)
    {
        this.defendUnit = defendUnit;
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

    public Schema<BriefBattleInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BriefBattleInfo newMessage()
    {
        return new BriefBattleInfo();
    }

    public Class<BriefBattleInfo> typeClass()
    {
        return BriefBattleInfo.class;
    }

    public String messageName()
    {
        return BriefBattleInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BriefBattleInfo.class.getName();
    }

    public boolean isInitialized(BriefBattleInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BriefBattleInfo message) throws IOException
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
                case 15:
                    message.type = input.readUInt32();
                    break;
                case 20:
                    message.defendFiefid = input.readUInt64();
                    break;
                case 30:
                    message.attackFiefid = input.readUInt64();
                    break;
                case 40:
                    message.attacker = input.readUInt32();
                    break;
                case 50:
                    message.defender = input.readUInt32();
                    break;
                case 60:
                    message.state = input.readUInt32();
                    break;
                case 70:
                    message.time = input.readUInt32();
                    break;
                case 80:
                    message.scale = input.readUInt32();
                    break;
                case 100:
                    message.attackUnit = input.readUInt32();
                    break;
                case 110:
                    message.defendUnit = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BriefBattleInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.type != null)
            output.writeUInt32(15, message.type, false);

        if(message.defendFiefid != null)
            output.writeUInt64(20, message.defendFiefid, false);

        if(message.attackFiefid != null)
            output.writeUInt64(30, message.attackFiefid, false);

        if(message.attacker != null)
            output.writeUInt32(40, message.attacker, false);

        if(message.defender != null)
            output.writeUInt32(50, message.defender, false);

        if(message.state != null)
            output.writeUInt32(60, message.state, false);

        if(message.time != null)
            output.writeUInt32(70, message.time, false);

        if(message.scale != null)
            output.writeUInt32(80, message.scale, false);

        if(message.attackUnit != null)
            output.writeUInt32(100, message.attackUnit, false);

        if(message.defendUnit != null)
            output.writeUInt32(110, message.defendUnit, false);
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