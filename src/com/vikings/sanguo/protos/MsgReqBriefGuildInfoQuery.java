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

public final class MsgReqBriefGuildInfoQuery implements Externalizable, Message<MsgReqBriefGuildInfoQuery>, Schema<MsgReqBriefGuildInfoQuery>
{

    public static Schema<MsgReqBriefGuildInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqBriefGuildInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqBriefGuildInfoQuery DEFAULT_INSTANCE = new MsgReqBriefGuildInfoQuery();

    
    private List<Integer> ids;

    public MsgReqBriefGuildInfoQuery()
    {
        
    }

    // getters and setters

    // ids

    public boolean hasIds(){
        return ids != null;
    }


    public List<Integer> getIdsList()
    {
        return ids == null?  new ArrayList<Integer>():ids;
    }

    public int getIdsCount()
    {
        return ids == null?0:ids.size();
    }

    public Integer getIds(int i)
    {
        return ids == null?null:ids.get(i);
    }


    public MsgReqBriefGuildInfoQuery setIdsList(List<Integer> ids)
    {
        this.ids = ids;
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

    public Schema<MsgReqBriefGuildInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqBriefGuildInfoQuery newMessage()
    {
        return new MsgReqBriefGuildInfoQuery();
    }

    public Class<MsgReqBriefGuildInfoQuery> typeClass()
    {
        return MsgReqBriefGuildInfoQuery.class;
    }

    public String messageName()
    {
        return MsgReqBriefGuildInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqBriefGuildInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgReqBriefGuildInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqBriefGuildInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.ids == null)
                        message.ids = new ArrayList<Integer>();
                    message.ids.add(input.readUInt32());
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqBriefGuildInfoQuery message) throws IOException
    {
        if(message.ids != null)
        {
            for(Integer ids : message.ids)
            {
                if(ids != null)
                    output.writeUInt32(10, ids, true);
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