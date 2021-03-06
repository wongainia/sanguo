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

public final class MsgReqDungeonActReset implements Externalizable, Message<MsgReqDungeonActReset>, Schema<MsgReqDungeonActReset>
{

    public static Schema<MsgReqDungeonActReset> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqDungeonActReset getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqDungeonActReset DEFAULT_INSTANCE = new MsgReqDungeonActReset();

    
    private Integer actid;
    private List<Integer> campaignids;
    private Integer type;

    public MsgReqDungeonActReset()
    {
        
    }

    // getters and setters

    // actid

    public boolean hasActid(){
        return actid != null;
    }


    public Integer getActid()
    {
        return actid == null ? 0 : actid;
    }

    public MsgReqDungeonActReset setActid(Integer actid)
    {
        this.actid = actid;
        return this;
    }

    // campaignids

    public boolean hasCampaignids(){
        return campaignids != null;
    }


    public List<Integer> getCampaignidsList()
    {
        return campaignids == null?  new ArrayList<Integer>():campaignids;
    }

    public int getCampaignidsCount()
    {
        return campaignids == null?0:campaignids.size();
    }

    public Integer getCampaignids(int i)
    {
        return campaignids == null?null:campaignids.get(i);
    }


    public MsgReqDungeonActReset setCampaignidsList(List<Integer> campaignids)
    {
        this.campaignids = campaignids;
        return this;    
    }

    // type

    public boolean hasType(){
        return type != null;
    }


    public Integer getType()
    {
        return type == null ? 0 : type;
    }

    public MsgReqDungeonActReset setType(Integer type)
    {
        this.type = type;
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

    public Schema<MsgReqDungeonActReset> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqDungeonActReset newMessage()
    {
        return new MsgReqDungeonActReset();
    }

    public Class<MsgReqDungeonActReset> typeClass()
    {
        return MsgReqDungeonActReset.class;
    }

    public String messageName()
    {
        return MsgReqDungeonActReset.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqDungeonActReset.class.getName();
    }

    public boolean isInitialized(MsgReqDungeonActReset message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqDungeonActReset message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.actid = input.readUInt32();
                    break;
                case 20:
                    if(message.campaignids == null)
                        message.campaignids = new ArrayList<Integer>();
                    message.campaignids.add(input.readUInt32());
                    break;
                case 30:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqDungeonActReset message) throws IOException
    {
        if(message.actid != null)
            output.writeUInt32(10, message.actid, false);

        if(message.campaignids != null)
        {
            for(Integer campaignids : message.campaignids)
            {
                if(campaignids != null)
                    output.writeUInt32(20, campaignids, true);
            }
        }

        if(message.type != null)
            output.writeUInt32(30, message.type, false);
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
