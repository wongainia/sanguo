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

public final class MsgReqStaticGuildDataQuery implements Externalizable, Message<MsgReqStaticGuildDataQuery>, Schema<MsgReqStaticGuildDataQuery>
{

    public static Schema<MsgReqStaticGuildDataQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqStaticGuildDataQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqStaticGuildDataQuery DEFAULT_INSTANCE = new MsgReqStaticGuildDataQuery();

    
    private StaticGuildDataType dataType;
    private Integer guildid;
    private Long id;
    private Integer count;

    public MsgReqStaticGuildDataQuery()
    {
        
    }

    // getters and setters

    // dataType

    public boolean hasDataType(){
        return dataType != null;
    }


    public StaticGuildDataType getDataType()
    {
        return dataType == null ? StaticGuildDataType.STATIC_GUILD_DATA_TYPE_GUILD_LOG : dataType;
    }

    public MsgReqStaticGuildDataQuery setDataType(StaticGuildDataType dataType)
    {
        this.dataType = dataType;
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

    public MsgReqStaticGuildDataQuery setGuildid(Integer guildid)
    {
        this.guildid = guildid;
        return this;
    }

    // id

    public boolean hasId(){
        return id != null;
    }


    public Long getId()
    {
        return id == null ? 0L : id;
    }

    public MsgReqStaticGuildDataQuery setId(Long id)
    {
        this.id = id;
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

    public MsgReqStaticGuildDataQuery setCount(Integer count)
    {
        this.count = count;
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

    public Schema<MsgReqStaticGuildDataQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqStaticGuildDataQuery newMessage()
    {
        return new MsgReqStaticGuildDataQuery();
    }

    public Class<MsgReqStaticGuildDataQuery> typeClass()
    {
        return MsgReqStaticGuildDataQuery.class;
    }

    public String messageName()
    {
        return MsgReqStaticGuildDataQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqStaticGuildDataQuery.class.getName();
    }

    public boolean isInitialized(MsgReqStaticGuildDataQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqStaticGuildDataQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.dataType = StaticGuildDataType.valueOf(input.readEnum());
                    break;
                case 20:
                    message.guildid = input.readUInt32();
                    break;
                case 30:
                    message.id = input.readUInt64();
                    break;
                case 40:
                    message.count = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqStaticGuildDataQuery message) throws IOException
    {
        if(message.dataType != null)
             output.writeEnum(10, message.dataType.number, false);

        if(message.guildid != null)
            output.writeUInt32(20, message.guildid, false);

        if(message.id != null)
            output.writeUInt64(30, message.id, false);

        if(message.count != null)
            output.writeUInt32(40, message.count, false);
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
