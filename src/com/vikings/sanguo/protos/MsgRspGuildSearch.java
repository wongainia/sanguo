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

public final class MsgRspGuildSearch implements Externalizable, Message<MsgRspGuildSearch>, Schema<MsgRspGuildSearch>
{

    public static Schema<MsgRspGuildSearch> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspGuildSearch getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspGuildSearch DEFAULT_INSTANCE = new MsgRspGuildSearch();

    
    private List<GuildSearchInfo> infos;

    public MsgRspGuildSearch()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<GuildSearchInfo> getInfosList()
    {
        return infos == null?  new ArrayList<GuildSearchInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public GuildSearchInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgRspGuildSearch setInfosList(List<GuildSearchInfo> infos)
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

    public Schema<MsgRspGuildSearch> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspGuildSearch newMessage()
    {
        return new MsgRspGuildSearch();
    }

    public Class<MsgRspGuildSearch> typeClass()
    {
        return MsgRspGuildSearch.class;
    }

    public String messageName()
    {
        return MsgRspGuildSearch.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspGuildSearch.class.getName();
    }

    public boolean isInitialized(MsgRspGuildSearch message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspGuildSearch message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<GuildSearchInfo>();
                    message.infos.add(input.mergeObject(null, GuildSearchInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspGuildSearch message) throws IOException
    {
        if(message.infos != null)
        {
            for(GuildSearchInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, GuildSearchInfo.getSchema(), true);
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