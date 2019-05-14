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

public final class MsgRspRichBattleInfoQuery implements Externalizable, Message<MsgRspRichBattleInfoQuery>, Schema<MsgRspRichBattleInfoQuery>
{

    public static Schema<MsgRspRichBattleInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspRichBattleInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspRichBattleInfoQuery DEFAULT_INSTANCE = new MsgRspRichBattleInfoQuery();

    
    private RichBattleInfo info;
    private List<OtherHeroInfo> heroInfos;

    public MsgRspRichBattleInfoQuery()
    {
        
    }

    // getters and setters

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public RichBattleInfo getInfo()
    {
        return info == null ? new RichBattleInfo() : info;
    }

    public MsgRspRichBattleInfoQuery setInfo(RichBattleInfo info)
    {
        this.info = info;
        return this;
    }

    // heroInfos

    public boolean hasHeroInfos(){
        return heroInfos != null;
    }


    public List<OtherHeroInfo> getHeroInfosList()
    {
        return heroInfos == null?  new ArrayList<OtherHeroInfo>():heroInfos;
    }

    public int getHeroInfosCount()
    {
        return heroInfos == null?0:heroInfos.size();
    }

    public OtherHeroInfo getHeroInfos(int i)
    {
        return heroInfos == null?null:heroInfos.get(i);
    }


    public MsgRspRichBattleInfoQuery setHeroInfosList(List<OtherHeroInfo> heroInfos)
    {
        this.heroInfos = heroInfos;
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

    public Schema<MsgRspRichBattleInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspRichBattleInfoQuery newMessage()
    {
        return new MsgRspRichBattleInfoQuery();
    }

    public Class<MsgRspRichBattleInfoQuery> typeClass()
    {
        return MsgRspRichBattleInfoQuery.class;
    }

    public String messageName()
    {
        return MsgRspRichBattleInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspRichBattleInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgRspRichBattleInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspRichBattleInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.info = input.mergeObject(message.info, RichBattleInfo.getSchema());
                    break;

                case 20:
                    if(message.heroInfos == null)
                        message.heroInfos = new ArrayList<OtherHeroInfo>();
                    message.heroInfos.add(input.mergeObject(null, OtherHeroInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspRichBattleInfoQuery message) throws IOException
    {
        if(message.info != null)
             output.writeObject(10, message.info, RichBattleInfo.getSchema(), false);


        if(message.heroInfos != null)
        {
            for(OtherHeroInfo heroInfos : message.heroInfos)
            {
                if(heroInfos != null)
                    output.writeObject(20, heroInfos, OtherHeroInfo.getSchema(), true);
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