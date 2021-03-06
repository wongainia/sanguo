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

public final class LordFiefInfos implements Externalizable, Message<LordFiefInfos>, Schema<LordFiefInfos>
{

    public static Schema<LordFiefInfos> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static LordFiefInfos getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final LordFiefInfos DEFAULT_INSTANCE = new LordFiefInfos();

    
    private DataCtrl ctrl;
    private List<ExLordFiefInfo> infos;
    private Integer totalCount;

    public LordFiefInfos()
    {
        
    }

    // getters and setters

    // ctrl

    public boolean hasCtrl(){
        return ctrl != null;
    }


    public DataCtrl getCtrl()
    {
        return ctrl == null ? new DataCtrl() : ctrl;
    }

    public LordFiefInfos setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<ExLordFiefInfo> getInfosList()
    {
        return infos == null?  new ArrayList<ExLordFiefInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public ExLordFiefInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public LordFiefInfos setInfosList(List<ExLordFiefInfo> infos)
    {
        this.infos = infos;
        return this;    
    }

    // totalCount

    public boolean hasTotalCount(){
        return totalCount != null;
    }


    public Integer getTotalCount()
    {
        return totalCount == null ? 0 : totalCount;
    }

    public LordFiefInfos setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
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

    public Schema<LordFiefInfos> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public LordFiefInfos newMessage()
    {
        return new LordFiefInfos();
    }

    public Class<LordFiefInfos> typeClass()
    {
        return LordFiefInfos.class;
    }

    public String messageName()
    {
        return LordFiefInfos.class.getSimpleName();
    }

    public String messageFullName()
    {
        return LordFiefInfos.class.getName();
    }

    public boolean isInitialized(LordFiefInfos message)
    {
        return true;
    }

    public void mergeFrom(Input input, LordFiefInfos message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ctrl = input.mergeObject(message.ctrl, DataCtrl.getSchema());
                    break;

                case 20:
                    if(message.infos == null)
                        message.infos = new ArrayList<ExLordFiefInfo>();
                    message.infos.add(input.mergeObject(null, ExLordFiefInfo.getSchema()));
                    break;

                case 30:
                    message.totalCount = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, LordFiefInfos message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.infos != null)
        {
            for(ExLordFiefInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(20, infos, ExLordFiefInfo.getSchema(), true);
            }
        }


        if(message.totalCount != null)
            output.writeUInt32(30, message.totalCount, false);
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
