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

public final class MsgRspBattleHotInfo implements Externalizable, Message<MsgRspBattleHotInfo>, Schema<MsgRspBattleHotInfo>
{

    public static Schema<MsgRspBattleHotInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspBattleHotInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspBattleHotInfo DEFAULT_INSTANCE = new MsgRspBattleHotInfo();

    
    private List<BattleHotInfo> infos;

    public MsgRspBattleHotInfo()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<BattleHotInfo> getInfosList()
    {
        return infos == null?  new ArrayList<BattleHotInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public BattleHotInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgRspBattleHotInfo setInfosList(List<BattleHotInfo> infos)
    {
        this.infos = infos;
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

    public Schema<MsgRspBattleHotInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspBattleHotInfo newMessage()
    {
        return new MsgRspBattleHotInfo();
    }

    public Class<MsgRspBattleHotInfo> typeClass()
    {
        return MsgRspBattleHotInfo.class;
    }

    public String messageName()
    {
        return MsgRspBattleHotInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspBattleHotInfo.class.getName();
    }

    public boolean isInitialized(MsgRspBattleHotInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspBattleHotInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<BattleHotInfo>();
                    message.infos.add(input.mergeObject(null, BattleHotInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspBattleHotInfo message) throws IOException
    {
        if(message.infos != null)
        {
            for(BattleHotInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, BattleHotInfo.getSchema(), true);
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
