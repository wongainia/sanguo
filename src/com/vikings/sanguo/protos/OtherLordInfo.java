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

public final class OtherLordInfo implements Externalizable, Message<OtherLordInfo>, Schema<OtherLordInfo>
{

    public static Schema<OtherLordInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static OtherLordInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final OtherLordInfo DEFAULT_INSTANCE = new OtherLordInfo();

    
    private Integer id;
    private Integer unitCount;
    private Integer fiefCount;
    private Integer siteCount;
    private Integer exploit;
    private TroopInfo arenaTroopInfo;
    private List<HeroIdInfo> arenaHeroInfos;

    public OtherLordInfo()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Integer getId()
    {
        return id == null ? 0 : id;
    }

    public OtherLordInfo setId(Integer id)
    {
        this.id = id;
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

    public OtherLordInfo setUnitCount(Integer unitCount)
    {
        this.unitCount = unitCount;
        return this;
    }

    // fiefCount

    public boolean hasFiefCount(){
        return fiefCount != null;
    }


    public Integer getFiefCount()
    {
        return fiefCount == null ? 0 : fiefCount;
    }

    public OtherLordInfo setFiefCount(Integer fiefCount)
    {
        this.fiefCount = fiefCount;
        return this;
    }

    // siteCount

    public boolean hasSiteCount(){
        return siteCount != null;
    }


    public Integer getSiteCount()
    {
        return siteCount == null ? 0 : siteCount;
    }

    public OtherLordInfo setSiteCount(Integer siteCount)
    {
        this.siteCount = siteCount;
        return this;
    }

    // exploit

    public boolean hasExploit(){
        return exploit != null;
    }


    public Integer getExploit()
    {
        return exploit == null ? 0 : exploit;
    }

    public OtherLordInfo setExploit(Integer exploit)
    {
        this.exploit = exploit;
        return this;
    }

    // arenaTroopInfo

    public boolean hasArenaTroopInfo(){
        return arenaTroopInfo != null;
    }


    public TroopInfo getArenaTroopInfo()
    {
        return arenaTroopInfo == null ? new TroopInfo() : arenaTroopInfo;
    }

    public OtherLordInfo setArenaTroopInfo(TroopInfo arenaTroopInfo)
    {
        this.arenaTroopInfo = arenaTroopInfo;
        return this;
    }

    // arenaHeroInfos

    public boolean hasArenaHeroInfos(){
        return arenaHeroInfos != null;
    }


    public List<HeroIdInfo> getArenaHeroInfosList()
    {
        return arenaHeroInfos == null?  new ArrayList<HeroIdInfo>():arenaHeroInfos;
    }

    public int getArenaHeroInfosCount()
    {
        return arenaHeroInfos == null?0:arenaHeroInfos.size();
    }

    public HeroIdInfo getArenaHeroInfos(int i)
    {
        return arenaHeroInfos == null?null:arenaHeroInfos.get(i);
    }


    public OtherLordInfo setArenaHeroInfosList(List<HeroIdInfo> arenaHeroInfos)
    {
        this.arenaHeroInfos = arenaHeroInfos;
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

    public Schema<OtherLordInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public OtherLordInfo newMessage()
    {
        return new OtherLordInfo();
    }

    public Class<OtherLordInfo> typeClass()
    {
        return OtherLordInfo.class;
    }

    public String messageName()
    {
        return OtherLordInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return OtherLordInfo.class.getName();
    }

    public boolean isInitialized(OtherLordInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, OtherLordInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt32();
                    break;
                case 20:
                    message.unitCount = input.readUInt32();
                    break;
                case 30:
                    message.fiefCount = input.readUInt32();
                    break;
                case 40:
                    message.siteCount = input.readUInt32();
                    break;
                case 50:
                    message.exploit = input.readUInt32();
                    break;
                case 70:
                    message.arenaTroopInfo = input.mergeObject(message.arenaTroopInfo, TroopInfo.getSchema());
                    break;

                case 80:
                    if(message.arenaHeroInfos == null)
                        message.arenaHeroInfos = new ArrayList<HeroIdInfo>();
                    message.arenaHeroInfos.add(input.mergeObject(null, HeroIdInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, OtherLordInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt32(10, message.id, false);

        if(message.unitCount != null)
            output.writeUInt32(20, message.unitCount, false);

        if(message.fiefCount != null)
            output.writeUInt32(30, message.fiefCount, false);

        if(message.siteCount != null)
            output.writeUInt32(40, message.siteCount, false);

        if(message.exploit != null)
            output.writeUInt32(50, message.exploit, false);

        if(message.arenaTroopInfo != null)
             output.writeObject(70, message.arenaTroopInfo, TroopInfo.getSchema(), false);


        if(message.arenaHeroInfos != null)
        {
            for(HeroIdInfo arenaHeroInfos : message.arenaHeroInfos)
            {
                if(arenaHeroInfos != null)
                    output.writeObject(80, arenaHeroInfos, HeroIdInfo.getSchema(), true);
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