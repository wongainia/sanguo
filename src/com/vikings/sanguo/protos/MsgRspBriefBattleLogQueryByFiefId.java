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

public final class MsgRspBriefBattleLogQueryByFiefId implements Externalizable, Message<MsgRspBriefBattleLogQueryByFiefId>, Schema<MsgRspBriefBattleLogQueryByFiefId>
{

    public static Schema<MsgRspBriefBattleLogQueryByFiefId> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspBriefBattleLogQueryByFiefId getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspBriefBattleLogQueryByFiefId DEFAULT_INSTANCE = new MsgRspBriefBattleLogQueryByFiefId();

    
    private List<BriefBattleLogInfo> infos;

    public MsgRspBriefBattleLogQueryByFiefId()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<BriefBattleLogInfo> getInfosList()
    {
        return infos == null?  new ArrayList<BriefBattleLogInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public BriefBattleLogInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgRspBriefBattleLogQueryByFiefId setInfosList(List<BriefBattleLogInfo> infos)
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

    public Schema<MsgRspBriefBattleLogQueryByFiefId> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspBriefBattleLogQueryByFiefId newMessage()
    {
        return new MsgRspBriefBattleLogQueryByFiefId();
    }

    public Class<MsgRspBriefBattleLogQueryByFiefId> typeClass()
    {
        return MsgRspBriefBattleLogQueryByFiefId.class;
    }

    public String messageName()
    {
        return MsgRspBriefBattleLogQueryByFiefId.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspBriefBattleLogQueryByFiefId.class.getName();
    }

    public boolean isInitialized(MsgRspBriefBattleLogQueryByFiefId message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspBriefBattleLogQueryByFiefId message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<BriefBattleLogInfo>();
                    message.infos.add(input.mergeObject(null, BriefBattleLogInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspBriefBattleLogQueryByFiefId message) throws IOException
    {
        if(message.infos != null)
        {
            for(BriefBattleLogInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, BriefBattleLogInfo.getSchema(), true);
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