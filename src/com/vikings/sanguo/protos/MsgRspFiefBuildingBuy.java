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

public final class MsgRspFiefBuildingBuy implements Externalizable, Message<MsgRspFiefBuildingBuy>, Schema<MsgRspFiefBuildingBuy>
{

    public static Schema<MsgRspFiefBuildingBuy> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspFiefBuildingBuy getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspFiefBuildingBuy DEFAULT_INSTANCE = new MsgRspFiefBuildingBuy();

    
    private ReturnInfo ri;
    private LordFiefInfo lordFiefInfo;

    public MsgRspFiefBuildingBuy()
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

    public MsgRspFiefBuildingBuy setRi(ReturnInfo ri)
    {
        this.ri = ri;
        return this;
    }

    // lordFiefInfo

    public boolean hasLordFiefInfo(){
        return lordFiefInfo != null;
    }


    public LordFiefInfo getLordFiefInfo()
    {
        return lordFiefInfo == null ? new LordFiefInfo() : lordFiefInfo;
    }

    public MsgRspFiefBuildingBuy setLordFiefInfo(LordFiefInfo lordFiefInfo)
    {
        this.lordFiefInfo = lordFiefInfo;
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

    public Schema<MsgRspFiefBuildingBuy> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspFiefBuildingBuy newMessage()
    {
        return new MsgRspFiefBuildingBuy();
    }

    public Class<MsgRspFiefBuildingBuy> typeClass()
    {
        return MsgRspFiefBuildingBuy.class;
    }

    public String messageName()
    {
        return MsgRspFiefBuildingBuy.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspFiefBuildingBuy.class.getName();
    }

    public boolean isInitialized(MsgRspFiefBuildingBuy message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspFiefBuildingBuy message) throws IOException
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
                    message.lordFiefInfo = input.mergeObject(message.lordFiefInfo, LordFiefInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspFiefBuildingBuy message) throws IOException
    {
        if(message.ri != null)
             output.writeObject(10, message.ri, ReturnInfo.getSchema(), false);


        if(message.lordFiefInfo != null)
             output.writeObject(20, message.lordFiefInfo, LordFiefInfo.getSchema(), false);

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