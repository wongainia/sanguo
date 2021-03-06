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

public final class BaseLordInfo implements Externalizable, Message<BaseLordInfo>, Schema<BaseLordInfo>
{

    public static Schema<BaseLordInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BaseLordInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BaseLordInfo DEFAULT_INSTANCE = new BaseLordInfo();

    
    private Long capital;
    private TroopInfo troopInfo;
    private LordBloodInfo bloodInfo;
    private LordReviveInfo reviveInfo;
    private LordArenaInfo arenaInfo;
    private ShopBoughtInfo shopBoughtInfo;
    private HeroShopInfo shopInfo;
    private Integer shopCost;

    public BaseLordInfo()
    {
        
    }

    // getters and setters

    // capital

    public boolean hasCapital(){
        return capital != null;
    }


    public Long getCapital()
    {
        return capital == null ? 0L : capital;
    }

    public BaseLordInfo setCapital(Long capital)
    {
        this.capital = capital;
        return this;
    }

    // troopInfo

    public boolean hasTroopInfo(){
        return troopInfo != null;
    }


    public TroopInfo getTroopInfo()
    {
        return troopInfo == null ? new TroopInfo() : troopInfo;
    }

    public BaseLordInfo setTroopInfo(TroopInfo troopInfo)
    {
        this.troopInfo = troopInfo;
        return this;
    }

    // bloodInfo

    public boolean hasBloodInfo(){
        return bloodInfo != null;
    }


    public LordBloodInfo getBloodInfo()
    {
        return bloodInfo == null ? new LordBloodInfo() : bloodInfo;
    }

    public BaseLordInfo setBloodInfo(LordBloodInfo bloodInfo)
    {
        this.bloodInfo = bloodInfo;
        return this;
    }

    // reviveInfo

    public boolean hasReviveInfo(){
        return reviveInfo != null;
    }


    public LordReviveInfo getReviveInfo()
    {
        return reviveInfo == null ? new LordReviveInfo() : reviveInfo;
    }

    public BaseLordInfo setReviveInfo(LordReviveInfo reviveInfo)
    {
        this.reviveInfo = reviveInfo;
        return this;
    }

    // arenaInfo

    public boolean hasArenaInfo(){
        return arenaInfo != null;
    }


    public LordArenaInfo getArenaInfo()
    {
        return arenaInfo == null ? new LordArenaInfo() : arenaInfo;
    }

    public BaseLordInfo setArenaInfo(LordArenaInfo arenaInfo)
    {
        this.arenaInfo = arenaInfo;
        return this;
    }

    // shopBoughtInfo

    public boolean hasShopBoughtInfo(){
        return shopBoughtInfo != null;
    }


    public ShopBoughtInfo getShopBoughtInfo()
    {
        return shopBoughtInfo == null ? new ShopBoughtInfo() : shopBoughtInfo;
    }

    public BaseLordInfo setShopBoughtInfo(ShopBoughtInfo shopBoughtInfo)
    {
        this.shopBoughtInfo = shopBoughtInfo;
        return this;
    }

    // shopInfo

    public boolean hasShopInfo(){
        return shopInfo != null;
    }


    public HeroShopInfo getShopInfo()
    {
        return shopInfo == null ? new HeroShopInfo() : shopInfo;
    }

    public BaseLordInfo setShopInfo(HeroShopInfo shopInfo)
    {
        this.shopInfo = shopInfo;
        return this;
    }

    // shopCost

    public boolean hasShopCost(){
        return shopCost != null;
    }


    public Integer getShopCost()
    {
        return shopCost == null ? 0 : shopCost;
    }

    public BaseLordInfo setShopCost(Integer shopCost)
    {
        this.shopCost = shopCost;
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

    public Schema<BaseLordInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BaseLordInfo newMessage()
    {
        return new BaseLordInfo();
    }

    public Class<BaseLordInfo> typeClass()
    {
        return BaseLordInfo.class;
    }

    public String messageName()
    {
        return BaseLordInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BaseLordInfo.class.getName();
    }

    public boolean isInitialized(BaseLordInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BaseLordInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 70:
                    message.capital = input.readUInt64();
                    break;
                case 100:
                    message.troopInfo = input.mergeObject(message.troopInfo, TroopInfo.getSchema());
                    break;

                case 110:
                    message.bloodInfo = input.mergeObject(message.bloodInfo, LordBloodInfo.getSchema());
                    break;

                case 120:
                    message.reviveInfo = input.mergeObject(message.reviveInfo, LordReviveInfo.getSchema());
                    break;

                case 130:
                    message.arenaInfo = input.mergeObject(message.arenaInfo, LordArenaInfo.getSchema());
                    break;

                case 140:
                    message.shopBoughtInfo = input.mergeObject(message.shopBoughtInfo, ShopBoughtInfo.getSchema());
                    break;

                case 150:
                    message.shopInfo = input.mergeObject(message.shopInfo, HeroShopInfo.getSchema());
                    break;

                case 160:
                    message.shopCost = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BaseLordInfo message) throws IOException
    {
        if(message.capital != null)
            output.writeUInt64(70, message.capital, false);

        if(message.troopInfo != null)
             output.writeObject(100, message.troopInfo, TroopInfo.getSchema(), false);


        if(message.bloodInfo != null)
             output.writeObject(110, message.bloodInfo, LordBloodInfo.getSchema(), false);


        if(message.reviveInfo != null)
             output.writeObject(120, message.reviveInfo, LordReviveInfo.getSchema(), false);


        if(message.arenaInfo != null)
             output.writeObject(130, message.arenaInfo, LordArenaInfo.getSchema(), false);


        if(message.shopBoughtInfo != null)
             output.writeObject(140, message.shopBoughtInfo, ShopBoughtInfo.getSchema(), false);


        if(message.shopInfo != null)
             output.writeObject(150, message.shopInfo, HeroShopInfo.getSchema(), false);


        if(message.shopCost != null)
            output.writeUInt32(160, message.shopCost, false);
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
