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

public final class MsgRspItemUse implements Externalizable, Message<MsgRspItemUse>, Schema<MsgRspItemUse>
{

    public static Schema<MsgRspItemUse> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspItemUse getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspItemUse DEFAULT_INSTANCE = new MsgRspItemUse();

    
    private ReturnInfo ri;
    private ManorInfo mi;
    private List<HeroInfo> his;

    public MsgRspItemUse()
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

    public MsgRspItemUse setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // mi

    public boolean hasMi(){
        return mi != null;
    }


    public ManorInfo getMi()
    {
        return mi == null ? new ManorInfo() : mi;
    }

    public MsgRspItemUse setMi(ManorInfo mi)
    {
        this.mi = mi;
        return this;
    }

    // his

    public boolean hasHis(){
        return his != null;
    }


    public List<HeroInfo> getHisList()
    {
        return his == null?  new ArrayList<HeroInfo>():his;
    }

    public int getHisCount()
    {
        return his == null?0:his.size();
    }

    public HeroInfo getHis(int i)
    {
        return his == null?null:his.get(i);
    }


    public MsgRspItemUse setHisList(List<HeroInfo> his)
    {
        this.his = his;
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

    public Schema<MsgRspItemUse> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspItemUse newMessage()
    {
        return new MsgRspItemUse();
    }

    public Class<MsgRspItemUse> typeClass()
    {
        return MsgRspItemUse.class;
    }

    public String messageName()
    {
        return MsgRspItemUse.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspItemUse.class.getName();
    }

    public boolean isInitialized(MsgRspItemUse message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspItemUse message) throws IOException
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

                case 30:
                    message.mi = input.mergeObject(message.mi, ManorInfo.getSchema());
                    break;

                case 40:
                    if(message.his == null)
                        message.his = new ArrayList<HeroInfo>();
                    message.his.add(input.mergeObject(null, HeroInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspItemUse message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.mi != null)
             output.writeObject(30, message.mi, ManorInfo.getSchema(), false);


        if(message.his != null)
        {
            for(HeroInfo his : message.his)
            {
                if(his != null)
                    output.writeObject(40, his, HeroInfo.getSchema(), true);
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
