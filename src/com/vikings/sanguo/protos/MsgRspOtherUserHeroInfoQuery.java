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

public final class MsgRspOtherUserHeroInfoQuery implements Externalizable, Message<MsgRspOtherUserHeroInfoQuery>, Schema<MsgRspOtherUserHeroInfoQuery>
{

    public static Schema<MsgRspOtherUserHeroInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspOtherUserHeroInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspOtherUserHeroInfoQuery DEFAULT_INSTANCE = new MsgRspOtherUserHeroInfoQuery();

    
    private List<OtherHeroInfo> infos;

    public MsgRspOtherUserHeroInfoQuery()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<OtherHeroInfo> getInfosList()
    {
        return infos == null?  new ArrayList<OtherHeroInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public OtherHeroInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgRspOtherUserHeroInfoQuery setInfosList(List<OtherHeroInfo> infos)
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

    public Schema<MsgRspOtherUserHeroInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspOtherUserHeroInfoQuery newMessage()
    {
        return new MsgRspOtherUserHeroInfoQuery();
    }

    public Class<MsgRspOtherUserHeroInfoQuery> typeClass()
    {
        return MsgRspOtherUserHeroInfoQuery.class;
    }

    public String messageName()
    {
        return MsgRspOtherUserHeroInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspOtherUserHeroInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgRspOtherUserHeroInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspOtherUserHeroInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<OtherHeroInfo>();
                    message.infos.add(input.mergeObject(null, OtherHeroInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspOtherUserHeroInfoQuery message) throws IOException
    {
        if(message.infos != null)
        {
            for(OtherHeroInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, OtherHeroInfo.getSchema(), true);
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