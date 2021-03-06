// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

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

public final class MsgRspHeroRefresh implements Externalizable, Message<MsgRspHeroRefresh>, Schema<MsgRspHeroRefresh>
{

    public static Schema<MsgRspHeroRefresh> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspHeroRefresh getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspHeroRefresh DEFAULT_INSTANCE = new MsgRspHeroRefresh();

    
    private HeroShopInfo shopInfo;
    private ReturnInfo ri;
    private HeroInfo heroInfo;

    public MsgRspHeroRefresh()
    {
        
    }

    // getters and setters

    // shopInfo

    public boolean hasShopInfo(){
        return shopInfo != null;
    }


    public HeroShopInfo getShopInfo()
    {
        return shopInfo == null ? new HeroShopInfo() : shopInfo;
    }

    public MsgRspHeroRefresh setShopInfo(HeroShopInfo shopInfo)
    {
        this.shopInfo = shopInfo;
        return this;
    }

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspHeroRefresh setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // heroInfo

    public boolean hasHeroInfo(){
        return heroInfo != null;
    }


    public HeroInfo getHeroInfo()
    {
        return heroInfo == null ? new HeroInfo() : heroInfo;
    }

    public MsgRspHeroRefresh setHeroInfo(HeroInfo heroInfo)
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

    public Schema<MsgRspHeroRefresh> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspHeroRefresh newMessage()
    {
        return new MsgRspHeroRefresh();
    }

    public Class<MsgRspHeroRefresh> typeClass()
    {
        return MsgRspHeroRefresh.class;
    }

    public String messageName()
    {
        return MsgRspHeroRefresh.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspHeroRefresh.class.getName();
    }

    public boolean isInitialized(MsgRspHeroRefresh message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspHeroRefresh message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.shopInfo = input.mergeObject(message.shopInfo, HeroShopInfo.getSchema());
                    break;

                case 20:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 30:
                    message.heroInfo = input.mergeObject(message.heroInfo, HeroInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspHeroRefresh message) throws IOException
    {
        if(message.shopInfo != null)
             output.writeObject(10, message.shopInfo, HeroShopInfo.getSchema(), false);


        if(message.ri != null)
             output.writeObject(20, message.ri, ReturnInfo.getSchema(), false);


        if(message.heroInfo != null)
             output.writeObject(30, message.heroInfo, HeroInfo.getSchema(), false);

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
