// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

public enum ConditionFieldType implements com.dyuproject.protostuff.EnumLite<ConditionFieldType>
{
    CONDITION_FIELD_NONE(0),
    CONDITION_FIELD_MOBILE(1),
    CONDITION_FIELD_NICK(2),
    CONDITION_FIELD_SEX(3),
    CONDITION_FIELD_AGE(4),
    CONDITION_FIELD_PROVINCE(5),
    CONDITION_FIELD_CITY(6),
    CONDITION_FIELD_MOBILE_LIKE(7),
    CONDITION_FIELD_USERID(8),
    CONDITION_FIELD_IMAGE(9),
    CONDITION_FIELD_NEW_PLAYER(10),
    CONDITION_FIELD_COUNTRY(11);
    
    public final int number;
    
    private ConditionFieldType (int number)
    {
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public static ConditionFieldType valueOf(int number)
    {
        switch(number) 
        {
            case 0: return CONDITION_FIELD_NONE;
            case 1: return CONDITION_FIELD_MOBILE;
            case 2: return CONDITION_FIELD_NICK;
            case 3: return CONDITION_FIELD_SEX;
            case 4: return CONDITION_FIELD_AGE;
            case 5: return CONDITION_FIELD_PROVINCE;
            case 6: return CONDITION_FIELD_CITY;
            case 7: return CONDITION_FIELD_MOBILE_LIKE;
            case 8: return CONDITION_FIELD_USERID;
            case 9: return CONDITION_FIELD_IMAGE;
            case 10: return CONDITION_FIELD_NEW_PLAYER;
            case 11: return CONDITION_FIELD_COUNTRY;
            default: return null;
        }
    }
}