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

public final class UserTroopEffectInfos implements Externalizable, Message<UserTroopEffectInfos>, Schema<UserTroopEffectInfos>
{

    public static Schema<UserTroopEffectInfos> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UserTroopEffectInfos getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UserTroopEffectInfos DEFAULT_INSTANCE = new UserTroopEffectInfos();

    
    private DataCtrl ctrl;
    private List<ExUserTroopEffectInfo> infos;

    public UserTroopEffectInfos()
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

    public UserTroopEffectInfos setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<ExUserTroopEffectInfo> getInfosList()
    {
        return infos == null?  new ArrayList<ExUserTroopEffectInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public ExUserTroopEffectInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public UserTroopEffectInfos setInfosList(List<ExUserTroopEffectInfo> infos)
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

    public Schema<UserTroopEffectInfos> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UserTroopEffectInfos newMessage()
    {
        return new UserTroopEffectInfos();
    }

    public Class<UserTroopEffectInfos> typeClass()
    {
        return UserTroopEffectInfos.class;
    }

    public String messageName()
    {
        return UserTroopEffectInfos.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UserTroopEffectInfos.class.getName();
    }

    public boolean isInitialized(UserTroopEffectInfos message)
    {
        return true;
    }

    public void mergeFrom(Input input, UserTroopEffectInfos message) throws IOException
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
                        message.infos = new ArrayList<ExUserTroopEffectInfo>();
                    message.infos.add(input.mergeObject(null, ExUserTroopEffectInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UserTroopEffectInfos message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.infos != null)
        {
            for(ExUserTroopEffectInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(20, infos, ExUserTroopEffectInfo.getSchema(), true);
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