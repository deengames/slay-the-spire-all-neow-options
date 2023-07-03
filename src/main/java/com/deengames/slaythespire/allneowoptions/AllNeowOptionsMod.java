package com.deengames.slaythespire.allneowoptions;

import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.StartGameSubscriber;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class AllNeowOptionsMod implements ISubscriber, StartGameSubscriber
{
	public static void initialize() {
        new AllNeowOptionsMod();
    }
	
	public AllNeowOptionsMod() {
		BaseMod.subscribe(this);
	}

	@Override
	public void receiveStartGame() {
		System.out.println("@@@@@@@@@@@@@@@@@@@2");
	}
}
