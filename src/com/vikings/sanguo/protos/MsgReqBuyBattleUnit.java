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

public final class MsgReqBuyBattleUnit implements Externalizable, Message<MsgReqBuyBattleUnit>, Schema<MsgReqBuyBattleUnit>
{

    public static Schema<MsgReqBuyBattleUnit> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBuyBattleUnit getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBuyBattleUnit DEFAULT_INSTANCE = new MsgReqBuyBattleUnit();

    
    private Long battleid;
    private Integer index;

    public MsgReqBuyBattleUnit()
    {
        
    }

    // getters and setters

    // battleid

    public boolean hasBattleid(){
        return battleid != null;
    }


    public Long getBattleid()
    {
        return battleid == null ? 0L : battleid;
    }

    public MsgReqBuyBattleUnit setBattleid(Long battleid)
    {
        this.battleid = battleid;
        return this;
    }

    // index

    public boolean hasIndex(){
        return index != null;
    }


    public Integer getIndex()
    {
        return index == null ? 0 : index;
    }

    public MsgReqBuyBattleUnit setIndex(Integer index)
    {
        this.index = index;
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

    public Schema<MsgReqBuyBattleUnit> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBuyBattleUnit newMessage()
    {
        return new MsgReqBuyBattleUnit();
    }

    public Class<MsgReqBuyBattleUnit> typeClass()
    {
        return MsgReqBuyBattleUnit.class;
    }

    public String messageName()
    {
        return MsgReqBuyBattleUnit.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBuyBattleUnit.class.getName();
    }

    public boolean isInitialized(MsgReqBuyBattleUnit message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBuyBattleUnit message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.battleid = input.readUInt64();
                    break;
                case 20:
                    message.index = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBuyBattleUnit message) throws IOException
    {
        if(message.battleid != null)
            output.writeUInt64(10, message.battleid, false);

        if(message.index != null)
            output.writeUInt32(20, message.index, false);
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
