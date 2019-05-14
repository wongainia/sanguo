// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

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

public final class ReturnInfo implements Externalizable, Message<ReturnInfo>, Schema<ReturnInfo>
{

    public static Schema<ReturnInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ReturnInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ReturnInfo DEFAULT_INSTANCE = new ReturnInfo();

    
    private Integer userid;
    private List<ReturnThingInfo> rtis;
    private List<ReturnAttrInfo> rais;

    public ReturnInfo()
    {
        
    }

    // getters and setters

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public ReturnInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // rtis

    public boolean hasRtis(){
        return rtis != null;
    }


    public List<ReturnThingInfo> getRtisList()
    {
        return rtis == null?  new ArrayList<ReturnThingInfo>():rtis;
    }

    public int getRtisCount()
    {
        return rtis == null?0:rtis.size();
    }

    public ReturnThingInfo getRtis(int i)
    {
        return rtis == null?null:rtis.get(i);
    }


    public ReturnInfo setRtisList(List<ReturnThingInfo> rtis)
    {
        this.rtis = rtis;
        return this;    
    }

    // rais

    public boolean hasRais(){
        return rais != null;
    }


    public List<ReturnAttrInfo> getRaisList()
    {
        return rais == null?  new ArrayList<ReturnAttrInfo>():rais;
    }

    public int getRaisCount()
    {
        return rais == null?0:rais.size();
    }

    public ReturnAttrInfo getRais(int i)
    {
        return rais == null?null:rais.get(i);
    }


    public ReturnInfo setRaisList(List<ReturnAttrInfo> rais)
    {
        this.rais = rais;
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

    public Schema<ReturnInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ReturnInfo newMessage()
    {
        return new ReturnInfo();
    }

    public Class<ReturnInfo> typeClass()
    {
        return ReturnInfo.class;
    }

    public String messageName()
    {
        return ReturnInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ReturnInfo.class.getName();
    }

    public boolean isInitialized(ReturnInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ReturnInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.userid = input.readUInt32();
                    break;
                case 20:
                    if(message.rtis == null)
                        message.rtis = new ArrayList<ReturnThingInfo>();
                    message.rtis.add(input.mergeObject(null, ReturnThingInfo.getSchema()));
                    break;

                case 30:
                    if(message.rais == null)
                        message.rais = new ArrayList<ReturnAttrInfo>();
                    message.rais.add(input.mergeObject(null, ReturnAttrInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ReturnInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.rtis != null)
        {
            for(ReturnThingInfo rtis : message.rtis)
            {
                if(rtis != null)
                    output.writeObject(20, rtis, ReturnThingInfo.getSchema(), true);
            }
        }


        if(message.rais != null)
        {
            for(ReturnAttrInfo rais : message.rais)
            {
                if(rais != null)
                    output.writeObject(30, rais, ReturnAttrInfo.getSchema(), true);
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