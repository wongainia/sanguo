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

public final class MessageInfo implements Externalizable, Message<MessageInfo>, Schema<MessageInfo>
{

    public static Schema<MessageInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MessageInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MessageInfo DEFAULT_INSTANCE = new MessageInfo();

    
    private Long id;
    private Integer type;
    private Integer time;
    private Integer from;
    private Integer duration;
    private String context;

    public MessageInfo()
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

    public MessageInfo setId(Long id)
    {
        this.id = id;
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

    public MessageInfo setType(Integer type)
    {
        this.type = type;
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

    public MessageInfo setTime(Integer time)
    {
        this.time = time;
        return this;
    }

    // from

    public boolean hasFrom(){
        return from != null;
    }


    public Integer getFrom()
    {
        return from == null ? 0 : from;
    }

    public MessageInfo setFrom(Integer from)
    {
        this.from = from;
        return this;
    }

    // duration

    public boolean hasDuration(){
        return duration != null;
    }


    public Integer getDuration()
    {
        return duration == null ? 0 : duration;
    }

    public MessageInfo setDuration(Integer duration)
    {
        this.duration = duration;
        return this;
    }

    // context

    public boolean hasContext(){
        return context != null;
    }


    public String getContext()
    {
        return context == null ? "" : context;
    }

    public MessageInfo setContext(String context)
    {
        this.context = context;
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

    public Schema<MessageInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MessageInfo newMessage()
    {
        return new MessageInfo();
    }

    public Class<MessageInfo> typeClass()
    {
        return MessageInfo.class;
    }

    public String messageName()
    {
        return MessageInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MessageInfo.class.getName();
    }

    public boolean isInitialized(MessageInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, MessageInfo message) throws IOException
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
                case 20:
                    message.type = input.readUInt32();
                    break;
                case 30:
                    message.time = input.readUInt32();
                    break;
                case 40:
                    message.from = input.readUInt32();
                    break;
                case 50:
                    message.duration = input.readUInt32();
                    break;
                case 60:
                    message.context = input.readString();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MessageInfo message) throws IOException
    {
        if(message.id != null)
            output.writeUInt64(10, message.id, false);

        if(message.type != null)
            output.writeUInt32(20, message.type, false);

        if(message.time != null)
            output.writeUInt32(30, message.time, false);

        if(message.from != null)
            output.writeUInt32(40, message.from, false);

        if(message.duration != null)
            output.writeUInt32(50, message.duration, false);

        if(message.context != null)
            output.writeString(60, message.context, false);
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
