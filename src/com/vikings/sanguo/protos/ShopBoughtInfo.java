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

public final class ShopBoughtInfo implements Externalizable, Message<ShopBoughtInfo>, Schema<ShopBoughtInfo>
{

    public static Schema<ShopBoughtInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ShopBoughtInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ShopBoughtInfo DEFAULT_INSTANCE = new ShopBoughtInfo();

    
    private List<ShopItemInfo> infos;

    public ShopBoughtInfo()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<ShopItemInfo> getInfosList()
    {
        return infos == null?  new ArrayList<ShopItemInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public ShopItemInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public ShopBoughtInfo setInfosList(List<ShopItemInfo> infos)
    {
        this.infos = infos;
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

    public Schema<ShopBoughtInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ShopBoughtInfo newMessage()
    {
        return new ShopBoughtInfo();
    }

    public Class<ShopBoughtInfo> typeClass()
    {
        return ShopBoughtInfo.class;
    }

    public String messageName()
    {
        return ShopBoughtInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ShopBoughtInfo.class.getName();
    }

    public boolean isInitialized(ShopBoughtInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ShopBoughtInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<ShopItemInfo>();
                    message.infos.add(input.mergeObject(null, ShopItemInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ShopBoughtInfo message) throws IOException
    {
        if(message.infos != null)
        {
            for(ShopItemInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, ShopItemInfo.getSchema(), true);
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
