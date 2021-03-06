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

public final class MsgRspBriefBattleInfoQuery implements Externalizable, Message<MsgRspBriefBattleInfoQuery>, Schema<MsgRspBriefBattleInfoQuery>
{

    public static Schema<MsgRspBriefBattleInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspBriefBattleInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspBriefBattleInfoQuery DEFAULT_INSTANCE = new MsgRspBriefBattleInfoQuery();

    
    private List<BriefBattleInfo> infos;

    public MsgRspBriefBattleInfoQuery()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<BriefBattleInfo> getInfosList()
    {
        return infos == null?  new ArrayList<BriefBattleInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public BriefBattleInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgRspBriefBattleInfoQuery setInfosList(List<BriefBattleInfo> infos)
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

    public Schema<MsgRspBriefBattleInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspBriefBattleInfoQuery newMessage()
    {
        return new MsgRspBriefBattleInfoQuery();
    }

    public Class<MsgRspBriefBattleInfoQuery> typeClass()
    {
        return MsgRspBriefBattleInfoQuery.class;
    }

    public String messageName()
    {
        return MsgRspBriefBattleInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspBriefBattleInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgRspBriefBattleInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspBriefBattleInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<BriefBattleInfo>();
                    message.infos.add(input.mergeObject(null, BriefBattleInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspBriefBattleInfoQuery message) throws IOException
    {
        if(message.infos != null)
        {
            for(BriefBattleInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, BriefBattleInfo.getSchema(), true);
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
