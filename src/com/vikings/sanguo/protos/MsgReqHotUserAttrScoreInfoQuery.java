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

public final class MsgReqHotUserAttrScoreInfoQuery implements Externalizable, Message<MsgReqHotUserAttrScoreInfoQuery>, Schema<MsgReqHotUserAttrScoreInfoQuery>
{

    public static Schema<MsgReqHotUserAttrScoreInfoQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqHotUserAttrScoreInfoQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqHotUserAttrScoreInfoQuery DEFAULT_INSTANCE = new MsgReqHotUserAttrScoreInfoQuery();

    static final Integer DEFAULT_TYPE = new Integer(1);
    
    private Integer country;
    private Integer start;
    private Integer level;
    private Integer type = DEFAULT_TYPE;

    public MsgReqHotUserAttrScoreInfoQuery()
    {
        
    }

    // getters and setters

    // country

    public boolean hasCountry(){
        return country != null;
    }


    public Integer getCountry()
    {
        return country == null ? 0 : country;
    }

    public MsgReqHotUserAttrScoreInfoQuery setCountry(Integer country)
    {
        this.country = country;
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

    public MsgReqHotUserAttrScoreInfoQuery setStart(Integer start)
    {
        this.start = start;
        return this;
    }

    // level

    public boolean hasLevel(){
        return level != null;
    }


    public Integer getLevel()
    {
        return level == null ? 0 : level;
    }

    public MsgReqHotUserAttrScoreInfoQuery setLevel(Integer level)
    {
        this.level = level;
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

    public MsgReqHotUserAttrScoreInfoQuery setType(Integer type)
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

    public Schema<MsgReqHotUserAttrScoreInfoQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqHotUserAttrScoreInfoQuery newMessage()
    {
        return new MsgReqHotUserAttrScoreInfoQuery();
    }

    public Class<MsgReqHotUserAttrScoreInfoQuery> typeClass()
    {
        return MsgReqHotUserAttrScoreInfoQuery.class;
    }

    public String messageName()
    {
        return MsgReqHotUserAttrScoreInfoQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqHotUserAttrScoreInfoQuery.class.getName();
    }

    public boolean isInitialized(MsgReqHotUserAttrScoreInfoQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqHotUserAttrScoreInfoQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.country = input.readUInt32();
                    break;
                case 20:
                    message.start = input.readUInt32();
                    break;
                case 30:
                    message.level = input.readUInt32();
                    break;
                case 40:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqHotUserAttrScoreInfoQuery message) throws IOException
    {
        if(message.country != null)
            output.writeUInt32(10, message.country, false);

        if(message.start != null)
            output.writeUInt32(20, message.start, false);

        if(message.level != null)
            output.writeUInt32(30, message.level, false);

        if(message.type != null && message.type != DEFAULT_TYPE)
            output.writeUInt32(40, message.type, false);
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
