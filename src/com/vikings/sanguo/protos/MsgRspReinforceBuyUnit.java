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

public final class MsgRspReinforceBuyUnit implements Externalizable, Message<MsgRspReinforceBuyUnit>, Schema<MsgRspReinforceBuyUnit>
{

    public static Schema<MsgRspReinforceBuyUnit> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspReinforceBuyUnit getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspReinforceBuyUnit DEFAULT_INSTANCE = new MsgRspReinforceBuyUnit();

    
    private ReturnInfo ri;
    private MoveTroopInfo moveTroopInfo;
    private KeyValue times;

    public MsgRspReinforceBuyUnit()
    {
        
    }

    // getters and setters

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspReinforceBuyUnit setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // moveTroopInfo

    public boolean hasMoveTroopInfo(){
        return moveTroopInfo != null;
    }


    public MoveTroopInfo getMoveTroopInfo()
    {
        return moveTroopInfo == null ? new MoveTroopInfo() : moveTroopInfo;
    }

    public MsgRspReinforceBuyUnit setMoveTroopInfo(MoveTroopInfo moveTroopInfo)
    {
        this.moveTroopInfo = moveTroopInfo;
        return this;
    }

    // times

    public boolean hasTimes(){
        return times != null;
    }


    public KeyValue getTimes()
    {
        return times == null ? new KeyValue() : times;
    }

    public MsgRspReinforceBuyUnit setTimes(KeyValue times)
    {
        this.times = times;
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

    public Schema<MsgRspReinforceBuyUnit> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspReinforceBuyUnit newMessage()
    {
        return new MsgRspReinforceBuyUnit();
    }

    public Class<MsgRspReinforceBuyUnit> typeClass()
    {
        return MsgRspReinforceBuyUnit.class;
    }

    public String messageName()
    {
        return MsgRspReinforceBuyUnit.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspReinforceBuyUnit.class.getName();
    }

    public boolean isInitialized(MsgRspReinforceBuyUnit message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspReinforceBuyUnit message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                case 20:
                    message.moveTroopInfo = input.mergeObject(message.moveTroopInfo, MoveTroopInfo.getSchema());
                    break;

                case 30:
                    message.times = input.mergeObject(message.times, KeyValue.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspReinforceBuyUnit message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.moveTroopInfo != null)
             output.writeObject(20, message.moveTroopInfo, MoveTroopInfo.getSchema(), false);


        if(message.times != null)
             output.writeObject(30, message.times, KeyValue.getSchema(), false);

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
