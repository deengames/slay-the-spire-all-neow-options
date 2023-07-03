package com.deengames.slaythespire.allneowoptions;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class AllNeowOptionsMod implements ISubscriber
{
	public static void initialize() {
        new AllNeowOptionsMod();
    }
	
	public AllNeowOptionsMod() {
		BaseMod.subscribe(this);
	}
}
