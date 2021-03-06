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

public final class ArenaUserRankInfo implements Externalizable, Message<ArenaUserRankInfo>, Schema<ArenaUserRankInfo>
{

    public static Schema<ArenaUserRankInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ArenaUserRankInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ArenaUserRankInfo DEFAULT_INSTANCE = new ArenaUserRankInfo();

    
    private Integer userid;
    private Integer rank;
    private List<HeroIdInfo> heroInfo;

    public ArenaUserRankInfo()
    {
        
    }

    // getters and setters

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public ArenaUserRankInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // rank

    public boolean hasRank(){
        return rank != null;
    }


    public Integer getRank()
    {
        return rank == null ? 0 : rank;
    }

    public ArenaUserRankInfo setRank(Integer rank)
    {
        this.rank = rank;
        return this;
    }

    // heroInfo

    public boolean hasHeroInfo(){
        return heroInfo != null;
    }


    public List<HeroIdInfo> getHeroInfoList()
    {
        return heroInfo == null?  new ArrayList<HeroIdInfo>():heroInfo;
    }

    public int getHeroInfoCount()
    {
        return heroInfo == null?0:heroInfo.size();
    }

    public HeroIdInfo getHeroInfo(int i)
    {
        return heroInfo == null?null:heroInfo.get(i);
    }


    public ArenaUserRankInfo setHeroInfoList(List<HeroIdInfo> heroInfo)
    {
        this.heroInfo = heroInfo;
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

    public Schema<ArenaUserRankInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ArenaUserRankInfo newMessage()
    {
        return new ArenaUserRankInfo();
    }

    public Class<ArenaUserRankInfo> typeClass()
    {
        return ArenaUserRankInfo.class;
    }

    public String messageName()
    {
        return ArenaUserRankInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ArenaUserRankInfo.class.getName();
    }

    public boolean isInitialized(ArenaUserRankInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ArenaUserRankInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.userid = input.readUInt32();
                    break;
                case 20:
                    message.rank = input.readUInt32();
                    break;
                case 30:
                    if(message.heroInfo == null)
                        message.heroInfo = new ArrayList<HeroIdInfo>();
                    message.heroInfo.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ArenaUserRankInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.rank != null)
            output.writeUInt32(20, message.rank, false);

        if(message.heroInfo != null)
        {
            for(HeroIdInfo heroInfo : message.heroInfo)
            {
                if(heroInfo != null)
                    output.writeObject(30, heroInfo, HeroIdInfo.getSchema(), true);
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
