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

public final class MsgRspHeroStaminaRecovery implements Externalizable, Message<MsgRspHeroStaminaRecovery>, Schema<MsgRspHeroStaminaRecovery>
{

    public static Schema<MsgRspHeroStaminaRecovery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspHeroStaminaRecovery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspHeroStaminaRecovery DEFAULT_INSTANCE = new MsgRspHeroStaminaRecovery();

    
    private HeroInfo info;
    private ReturnInfo returnInfo;

    public MsgRspHeroStaminaRecovery()
    {
        
    }

    // getters and setters

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public HeroInfo getInfo()
    {
        return info == null ? new HeroInfo() : info;
    }

    public MsgRspHeroStaminaRecovery setInfo(HeroInfo info)
    {
        this.info = info;
        return this;
    }

    // returnInfo

    public boolean hasReturnInfo(){
        return returnInfo != null;
    }


    public ReturnInfo getReturnInfo()
    {
        return returnInfo == null ? new ReturnInfo() : returnInfo;
    }

    public MsgRspHeroStaminaRecovery setReturnInfo(ReturnInfo returnInfo)
    {
        this.returnInfo = returnInfo;
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

    public Schema<MsgRspHeroStaminaRecovery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspHeroStaminaRecovery newMessage()
    {
        return new MsgRspHeroStaminaRecovery();
    }

    public Class<MsgRspHeroStaminaRecovery> typeClass()
    {
        return MsgRspHeroStaminaRecovery.class;
    }

    public String messageName()
    {
        return MsgRspHeroStaminaRecovery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspHeroStaminaRecovery.class.getName();
    }

    public boolean isInitialized(MsgRspHeroStaminaRecovery message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspHeroStaminaRecovery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.info = input.mergeObject(message.info, HeroInfo.getSchema());
                    break;

                case 20:
                    message.returnInfo = input.mergeObject(message.returnInfo, ReturnInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspHeroStaminaRecovery message) throws IOException
    {
        if(message.info != null)
             output.writeObject(10, message.info, HeroInfo.getSchema(), false);


        if(message.returnInfo != null)
             output.writeObject(20, message.returnInfo, ReturnInfo.getSchema(), false);

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
