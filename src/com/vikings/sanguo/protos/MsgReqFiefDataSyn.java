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

public final class MsgReqFiefDataSyn implements Externalizable, Message<MsgReqFiefDataSyn>, Schema<MsgReqFiefDataSyn>
{

    public static Schema<MsgReqFiefDataSyn> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqFiefDataSyn getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqFiefDataSyn DEFAULT_INSTANCE = new MsgReqFiefDataSyn();

    
    private List<FiefDataSynInfo> infos;

    public MsgReqFiefDataSyn()
    {
        
    }

    // getters and setters

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<FiefDataSynInfo> getInfosList()
    {
        return infos == null?  new ArrayList<FiefDataSynInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public FiefDataSynInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public MsgReqFiefDataSyn setInfosList(List<FiefDataSynInfo> infos)
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

    public Schema<MsgReqFiefDataSyn> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqFiefDataSyn newMessage()
    {
        return new MsgReqFiefDataSyn();
    }

    public Class<MsgReqFiefDataSyn> typeClass()
    {
        return MsgReqFiefDataSyn.class;
    }

    public String messageName()
    {
        return MsgReqFiefDataSyn.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqFiefDataSyn.class.getName();
    }

    public boolean isInitialized(MsgReqFiefDataSyn message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqFiefDataSyn message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.infos == null)
                        message.infos = new ArrayList<FiefDataSynInfo>();
                    message.infos.add(input.mergeObject(null, FiefDataSynInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqFiefDataSyn message) throws IOException
    {
        if(message.infos != null)
        {
            for(FiefDataSynInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(10, infos, FiefDataSynInfo.getSchema(), true);
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
