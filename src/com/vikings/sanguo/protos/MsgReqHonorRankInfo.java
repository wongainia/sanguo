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

public final class MsgReqHonorRankInfo implements Externalizable, Message<MsgReqHonorRankInfo>, Schema<MsgReqHonorRankInfo>
{

    public static Schema<MsgReqHonorRankInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqHonorRankInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqHonorRankInfo DEFAULT_INSTANCE = new MsgReqHonorRankInfo();

    
    private HonorRankType type;
    private Integer start;
    private Integer count;
    private Integer guildid;

    public MsgReqHonorRankInfo()
    {
        
    }

    // getters and setters

    // type

    public boolean hasType(){
        return type != null;
    }


    public HonorRankType getType()
    {
        return type == null ? HonorRankType.HONOR_RANK_MARS : type;
    }

    public MsgReqHonorRankInfo setType(HonorRankType type)
    {
        this.type = type;
        return this;
    }

    // start

    public boolean hasStart(){
        return start != null;
    }


    public Integer getStart()
    {
        return start == null ? 0 : start;
    }

    public MsgReqHonorRankInfo setStart(Integer start)
    {
        this.start = start;
        return this;
    }

    // count

    public boolean hasCount(){
        return count != null;
    }


    public Integer getCount()
    {
        return count == null ? 0 : count;
    }

    public MsgReqHonorRankInfo setCount(Integer count)
    {
        this.count = count;
        return this;
    }

    // guildid

    public boolean hasGuildid(){
        return guildid != null;
    }


    public Integer getGuildid()
    {
        return guildid == null ? 0 : guildid;
    }

    public MsgReqHonorRankInfo setGuildid(Integer guildid)
    {
        this.guildid = guildid;
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

    public Schema<MsgReqHonorRankInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqHonorRankInfo newMessage()
    {
        return new MsgReqHonorRankInfo();
    }

    public Class<MsgReqHonorRankInfo> typeClass()
    {
        return MsgReqHonorRankInfo.class;
    }

    public String messageName()
    {
        return MsgReqHonorRankInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqHonorRankInfo.class.getName();
    }

    public boolean isInitialized(MsgReqHonorRankInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqHonorRankInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.type = HonorRankType.valueOf(input.readEnum());
                    break;
                case 20:
                    message.start = input.readUInt32();
                    break;
                case 30:
                    message.count = input.readUInt32();
                    break;
                case 40:
                    message.guildid = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqHonorRankInfo message) throws IOException
    {
        if(message.type != null)
             output.writeEnum(10, message.type.number, false);

        if(message.start != null)
            output.writeUInt32(20, message.start, false);

        if(message.count != null)
            output.writeUInt32(30, message.count, false);

        if(message.guildid != null)
            output.writeUInt32(40, message.guildid, false);
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
