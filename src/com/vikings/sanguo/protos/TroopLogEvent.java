// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

public enum TroopLogEvent implements com.dyuproject.protostuff.EnumLite<TroopLogEvent>
{
    EVENT_TROOP_ATTACK_RISE(1),
    EVENT_TROOP_ASSIST(2),
    EVENT_TROOP_MOVE(3),
    EVENT_TROOP_BACK_FIEF(4),
    EVENT_TROOP_BACK_MANOR(5),
    EVENT_TROOP_BATTLE_OVER(6),
    EVENT_TROOP_STARVATION(7);
    
    public final int number;
    
    private TroopLogEvent (int number)
    {
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public static TroopLogEvent valueOf(int number)
    {
        switch(number) 
        {
            case 1: return EVENT_TROOP_ATTACK_RISE;
            case 2: return EVENT_TROOP_ASSIST;
            case 3: return EVENT_TROOP_MOVE;
            case 4: return EVENT_TROOP_BACK_FIEF;
            case 5: return EVENT_TROOP_BACK_MANOR;
            case 6: return EVENT_TROOP_BATTLE_OVER;
            case 7: return EVENT_TROOP_STARVATION;
            default: return null;
        }
    }
}
