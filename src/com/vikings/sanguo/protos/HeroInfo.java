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

public final class HeroInfo implements Externalizable, Message<HeroInfo>, Schema<HeroInfo>
{

    public static Schema<HeroInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static HeroInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final HeroInfo DEFAULT_INSTANCE = new HeroInfo();

    
    private BaseHeroInfo bi;
    private ServerHeroInfo si;

    public HeroInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseHeroInfo getBi()
    {
        return bi == null ? new BaseHeroInfo() : bi;
    }

    public HeroInfo setBi(BaseHeroInfo bi)
    {
        this.bi = bi;
        return this;
    }

    // si

    public boolean hasSi(){
        return si != null;
    }


    public ServerHeroInfo getSi()
    {
        return si == null ? new ServerHeroInfo() : si;
    }

    public HeroInfo setSi(ServerHeroInfo si)
    {
        this.si = si;
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

    public Schema<HeroInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public HeroInfo newMessage()
    {
        return new HeroInfo();
    }

    public Class<HeroInfo> typeClass()
    {
        return HeroInfo.class;
    }

    public String messageName()
    {
        return HeroInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return HeroInfo.class.getName();
    }

    public boolean isInitialized(HeroInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, HeroInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseHeroInfo.getSchema());
                    break;

                case 20:
                    message.si = input.mergeObject(message.si, ServerHeroInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, HeroInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseHeroInfo.getSchema(), false);


        if(message.si != null)
             output.writeObject(20, message.si, ServerHeroInfo.getSchema(), false);

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
