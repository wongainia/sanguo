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

public final class MsgRspArenaQuery implements Externalizable, Message<MsgRspArenaQuery>, Schema<MsgRspArenaQuery>
{

    public static Schema<MsgRspArenaQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspArenaQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspArenaQuery DEFAULT_INSTANCE = new MsgRspArenaQuery();

    
    private List<ArenaUserRankInfo> attackableInfos;
    private List<ArenaUserRankInfo> otherInfos;

    public MsgRspArenaQuery()
    {
        
    }

    // getters and setters

    // attackableInfos

    public boolean hasAttackableInfos(){
        return attackableInfos != null;
    }


    public List<ArenaUserRankInfo> getAttackableInfosList()
    {
        return attackableInfos == null?  new ArrayList<ArenaUserRankInfo>():attackableInfos;
    }

    public int getAttackableInfosCount()
    {
        return attackableInfos == null?0:attackableInfos.size();
    }

    public ArenaUserRankInfo getAttackableInfos(int i)
    {
        return attackableInfos == null?null:attackableInfos.get(i);
    }


    public MsgRspArenaQuery setAttackableInfosList(List<ArenaUserRankInfo> attackableInfos)
    {
        this.attackableInfos = attackableInfos;
        return this;    
    }

    // otherInfos

    public boolean hasOtherInfos(){
        return otherInfos != null;
    }


    public List<ArenaUserRankInfo> getOtherInfosList()
    {
        return otherInfos == null?  new ArrayList<ArenaUserRankInfo>():otherInfos;
    }

    public int getOtherInfosCount()
    {
        return otherInfos == null?0:otherInfos.size();
    }

    public ArenaUserRankInfo getOtherInfos(int i)
    {
        return otherInfos == null?null:otherInfos.get(i);
    }


    public MsgRspArenaQuery setOtherInfosList(List<ArenaUserRankInfo> otherInfos)
    {
        this.otherInfos = otherInfos;
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

    public Schema<MsgRspArenaQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspArenaQuery newMessage()
    {
        return new MsgRspArenaQuery();
    }

    public Class<MsgRspArenaQuery> typeClass()
    {
        return MsgRspArenaQuery.class;
    }

    public String messageName()
    {
        return MsgRspArenaQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspArenaQuery.class.getName();
    }

    public boolean isInitialized(MsgRspArenaQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspArenaQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.attackableInfos == null)
                        message.attackableInfos = new ArrayList<ArenaUserRankInfo>();
                    message.attackableInfos.add(input.mergeObject(null, ArenaUserRankInfo.getSchema()));
                    break;

                case 20:
                    if(message.otherInfos == null)
                        message.otherInfos = new ArrayList<ArenaUserRankInfo>();
                    message.otherInfos.add(input.mergeObject(null, ArenaUserRankInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspArenaQuery message) throws IOException
    {
        if(message.attackableInfos != null)
        {
            for(ArenaUserRankInfo attackableInfos : message.attackableInfos)
            {
                if(attackableInfos != null)
                    output.writeObject(10, attackableInfos, ArenaUserRankInfo.getSchema(), true);
            }
        }


        if(message.otherInfos != null)
        {
            for(ArenaUserRankInfo otherInfos : message.otherInfos)
            {
                if(otherInfos != null)
                    output.writeObject(20, otherInfos, ArenaUserRankInfo.getSchema(), true);
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