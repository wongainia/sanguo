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

public final class MsgReqItemBuy implements Externalizable, Message<MsgReqItemBuy>, Schema<MsgReqItemBuy>
{

    public static Schema<MsgReqItemBuy> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqItemBuy getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqItemBuy DEFAULT_INSTANCE = new MsgReqItemBuy();

    
    private Integer itemid;
    private Integer amount;
    private Integer currency;

    public MsgReqItemBuy()
    {
        
    }

    // getters and setters

    // itemid

    public boolean hasItemid(){
        return itemid != null;
    }


    public Integer getItemid()
    {
        return itemid == null ? 0 : itemid;
    }

    public MsgReqItemBuy setItemid(Integer itemid)
    {
        this.itemid = itemid;
        return this;
    }

    // amount

    public boolean hasAmount(){
        return amount != null;
    }


    public Integer getAmount()
    {
        return amount == null ? 0 : amount;
    }

    public MsgReqItemBuy setAmount(Integer amount)
    {
        this.amount = amount;
        return this;
    }

    // currency

    public boolean hasCurrency(){
        return currency != null;
    }


    public Integer getCurrency()
    {
        return currency == null ? 0 : currency;
    }

    public MsgReqItemBuy setCurrency(Integer currency)
    {
        this.currency = currency;
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

    public Schema<MsgReqItemBuy> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqItemBuy newMessage()
    {
        return new MsgReqItemBuy();
    }

    public Class<MsgReqItemBuy> typeClass()
    {
        return MsgReqItemBuy.class;
    }

    public String messageName()
    {
        return MsgReqItemBuy.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqItemBuy.class.getName();
    }

    public boolean isInitialized(MsgReqItemBuy message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqItemBuy message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.itemid = input.readUInt32();
                    break;
                case 20:
                    message.amount = input.readUInt32();
                    break;
                case 30:
                    message.currency = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqItemBuy message) throws IOException
    {
        if(message.itemid != null)
            output.writeUInt32(10, message.itemid, false);

        if(message.amount != null)
            output.writeUInt32(20, message.amount, false);

        if(message.currency != null)
            output.writeUInt32(30, message.currency, false);
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
