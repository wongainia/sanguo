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

public final class MsgRspFriendAdd implements Externalizable, Message<MsgRspFriendAdd>, Schema<MsgRspFriendAdd>
{

    public static Schema<MsgRspFriendAdd> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspFriendAdd getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspFriendAdd DEFAULT_INSTANCE = new MsgRspFriendAdd();

    
    private List<Integer> failedUserids;

    public MsgRspFriendAdd()
    {
        
    }

    // getters and setters

    // failedUserids

    public boolean hasFailedUserids(){
        return failedUserids != null;
    }


    public List<Integer> getFailedUseridsList()
    {
        return failedUserids == null?  new ArrayList<Integer>():failedUserids;
    }

    public int getFailedUseridsCount()
    {
        return failedUserids == null?0:failedUserids.size();
    }

    public Integer getFailedUserids(int i)
    {
        return failedUserids == null?null:failedUserids.get(i);
    }


    public MsgRspFriendAdd setFailedUseridsList(List<Integer> failedUserids)
    {
        this.failedUserids = failedUserids;
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

    public Schema<MsgRspFriendAdd> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspFriendAdd newMessage()
    {
        return new MsgRspFriendAdd();
    }

    public Class<MsgRspFriendAdd> typeClass()
    {
        return MsgRspFriendAdd.class;
    }

    public String messageName()
    {
        return MsgRspFriendAdd.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspFriendAdd.class.getName();
    }

    public boolean isInitialized(MsgRspFriendAdd message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspFriendAdd message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.failedUserids == null)
                        message.failedUserids = new ArrayList<Integer>();
                    message.failedUserids.add(input.readUInt32());
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspFriendAdd message) throws IOException
    {
        if(message.failedUserids != null)
        {
            for(Integer failedUserids : message.failedUserids)
            {
                if(failedUserids != null)
                    output.writeUInt32(10, failedUserids, true);
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
