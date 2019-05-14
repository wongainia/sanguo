// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

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

public final class GuildLogInfo implements Externalizable, Message<GuildLogInfo>, Schema<GuildLogInfo>
{

    public static Schema<GuildLogInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GuildLogInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GuildLogInfo DEFAULT_INSTANCE = new GuildLogInfo();

    
    private Long id;
    private Integer time;
    private Integer guildid;
    private Integer userid;
    private Integer targetid;
    private GuildLogEvent event;

    public GuildLogInfo()
    {
        
    }

    // getters and setters

    // id

    public boolean hasId(){
        return id != null;
    }


    public Long getId()
    {
        return id == null ? 0L : id;
    }

    public GuildLogInfo setId(Long id)
    {
        this.id = id;
        return this;
    }

    // time

    public boolean hasTime(){
        return time != null;
    }


    public Integer getTime()
    {
        return time == null ? 0 : time;
    }

    public GuildLogInfo setTime(Integer time)
    {
        this.time = time;
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

    public GuildLogInfo setGuildid(Integer guildid)
    {
        this.guildid = guildid;
        return this;
    }

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public GuildLogInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // targetid

    public boolean hasTargetid(){
        return targetid != null;
    }


    public Integer getTargetid()
    {
        return targetid == null ? 0 : targetid;
    }

    public GuildLogInfo setTargetid(Integer targetid)
    {
        this.targetid = targetid;
        return this;
    }

    // event

    public boolean hasEvent(){
        return event != null;
    }


    public GuildLogEvent getEvent()
    {
        return event == null ? GuildLogEvent.GUILD_LOG_EVENT_JOIN : event;
    }

    public GuildLogInfo setEvent(GuildLogEvent event)
    {
        this.event = event;
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

    public Schema<GuildLogInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GuildLogInfo newMessage()
    {
        return new GuildLogInfo();
    }

    public Class<GuildLogInfo> typeClass()
    {
        return GuildLogInfo.class;
    }

    public String messageName()
    {
        return GuildLogInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GuildLogInfo.class.getName();
    }

    public boolean isInitialized(GuildLogInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, GuildLogInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.id = input.readUInt64();
                    break;
                case 15:
                    message.time = input.readUInt32();
                    break;
                case 20:
                    message.guildid = input.readUInt32();
                    break;
                case 30:
                    message.userid = input.readUInt32();
                    break;
                case 35:
                    message.targetid = input.readUInt32();
                    break;
                case 40:
                    message.event = GuildLogEvent.valueOf(input.readEnum());
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GuildLogInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.time != null)
            output.writeUInt32(15, message.time, false);

        if(message.guildid != null)
            output.writeUInt32(20, message.guildid, false);

        if(message.userid != null)
            output.writeUInt32(30, message.userid, false);

        if(message.targetid != null)
            output.writeUInt32(35, message.targetid, false);

        if(message.event != null)
             output.writeEnum(40, message.event.number, false);
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