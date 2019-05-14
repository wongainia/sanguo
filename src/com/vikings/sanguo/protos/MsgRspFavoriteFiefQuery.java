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

public final class MsgRspFavoriteFiefQuery implements Externalizable, Message<MsgRspFavoriteFiefQuery>, Schema<MsgRspFavoriteFiefQuery>
{

    public static Schema<MsgRspFavoriteFiefQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspFavoriteFiefQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspFavoriteFiefQuery DEFAULT_INSTANCE = new MsgRspFavoriteFiefQuery();

    
    private List<Long> fiefids;

    public MsgRspFavoriteFiefQuery()
    {
        
    }

    // getters and setters

    // fiefids

    public boolean hasFiefids(){
        return fiefids != null;
    }


    public List<Long> getFiefidsList()
    {
        return fiefids == null?  new ArrayList<Long>():fiefids;
    }

    public int getFiefidsCount()
    {
        return fiefids == null?0:fiefids.size();
    }

    public Long getFiefids(int i)
    {
        return fiefids == null?null:fiefids.get(i);
    }


    public MsgRspFavoriteFiefQuery setFiefidsList(List<Long> fiefids)
    {
        this.fiefids = fiefids;
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

    public Schema<MsgRspFavoriteFiefQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspFavoriteFiefQuery newMessage()
    {
        return new MsgRspFavoriteFiefQuery();
    }

    public Class<MsgRspFavoriteFiefQuery> typeClass()
    {
        return MsgRspFavoriteFiefQuery.class;
    }

    public String messageName()
    {
        return MsgRspFavoriteFiefQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspFavoriteFiefQuery.class.getName();
    }

    public boolean isInitialized(MsgRspFavoriteFiefQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspFavoriteFiefQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.fiefids == null)
                        message.fiefids = new ArrayList<Long>();
                    message.fiefids.add(input.readUInt64());
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspFavoriteFiefQuery message) throws IOException
    {
        if(message.fiefids != null)
        {
            for(Long fiefids : message.fiefids)
            {
                if(fiefids != null)
                    output.writeUInt64(10, fiefids, true);
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
