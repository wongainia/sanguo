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

public final class MsgReqUserSearch implements Externalizable, Message<MsgReqUserSearch>, Schema<MsgReqUserSearch>
{

    public static Schema<MsgReqUserSearch> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqUserSearch getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqUserSearch DEFAULT_INSTANCE = new MsgReqUserSearch();

    
    private Integer start;
    private Integer count;
    private List<ConditionNum> conditionNums;
    private List<ConditionStr> conditionStrs;

    public MsgReqUserSearch()
    {
        
    }

    // getters and setters

    // start

    public boolean hasStart(){
        return start != null;
    }


    public Integer getStart()
    {
        return start == null ? 0 : start;
    }

    public MsgReqUserSearch setStart(Integer start)
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

    public MsgReqUserSearch setCount(Integer count)
    {
        this.count = count;
        return this;
    }

    // conditionNums

    public boolean hasConditionNums(){
        return conditionNums != null;
    }


    public List<ConditionNum> getConditionNumsList()
    {
        return conditionNums == null?  new ArrayList<ConditionNum>():conditionNums;
    }

    public int getConditionNumsCount()
    {
        return conditionNums == null?0:conditionNums.size();
    }

    public ConditionNum getConditionNums(int i)
    {
        return conditionNums == null?null:conditionNums.get(i);
    }


    public MsgReqUserSearch setConditionNumsList(List<ConditionNum> conditionNums)
    {
        this.conditionNums = conditionNums;
        return this;    
    }

    // conditionStrs

    public boolean hasConditionStrs(){
        return conditionStrs != null;
    }


    public List<ConditionStr> getConditionStrsList()
    {
        return conditionStrs == null?  new ArrayList<ConditionStr>():conditionStrs;
    }

    public int getConditionStrsCount()
    {
        return conditionStrs == null?0:conditionStrs.size();
    }

    public ConditionStr getConditionStrs(int i)
    {
        return conditionStrs == null?null:conditionStrs.get(i);
    }


    public MsgReqUserSearch setConditionStrsList(List<ConditionStr> conditionStrs)
    {
        this.conditionStrs = conditionStrs;
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

    public Schema<MsgReqUserSearch> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqUserSearch newMessage()
    {
        return new MsgReqUserSearch();
    }

    public Class<MsgReqUserSearch> typeClass()
    {
        return MsgReqUserSearch.class;
    }

    public String messageName()
    {
        return MsgReqUserSearch.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqUserSearch.class.getName();
    }

    public boolean isInitialized(MsgReqUserSearch message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqUserSearch message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.start = input.readUInt32();
                    break;
                case 20:
                    message.count = input.readUInt32();
                    break;
                case 30:
                    if(message.conditionNums == null)
                        message.conditionNums = new ArrayList<ConditionNum>();
                    message.conditionNums.add(input.mergeObject(null, ConditionNum.getSchema()));
                    break;

                case 40:
                    if(message.conditionStrs == null)
                        message.conditionStrs = new ArrayList<ConditionStr>();
                    message.conditionStrs.add(input.mergeObject(null, ConditionStr.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqUserSearch message) throws IOException
    {
        if(message.start != null)
            output.writeUInt32(10, message.start, false);

        if(message.count != null)
            output.writeUInt32(20, message.count, false);

        if(message.conditionNums != null)
        {
            for(ConditionNum conditionNums : message.conditionNums)
            {
                if(conditionNums != null)
                    output.writeObject(30, conditionNums, ConditionNum.getSchema(), true);
            }
        }


        if(message.conditionStrs != null)
        {
            for(ConditionStr conditionStrs : message.conditionStrs)
            {
                if(conditionStrs != null)
                    output.writeObject(40, conditionStrs, ConditionStr.getSchema(), true);
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
