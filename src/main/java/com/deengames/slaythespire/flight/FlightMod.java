package com.deengames.slaythespire.allneowoptions;

import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.daily.mods.AllNeowOptions;
import com.megacrit.cardcrawl.helpers.ModHelper;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostCreateStartingRelicsSubscriber;

import java.util.ArrayList;
import java.util.List;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class AllNeowOptionsMod implements ISubscriber, PostCreateStartingRelicsSubscriber
{
	public static void initialize() {
        new AllNeowOptionsMod();
    }
	
	public AllNeowOptionsMod() {
		BaseMod.subscribe(this);
	}

	@Override
	public void receivePostCreateStartingRelics(PlayerClass arg0, ArrayList<String> arg1) {
		List<String> list = new ArrayList<String>();
		list.add(AllNeowOptions.ID);
		ModHelper.setMods(list);
	}
}
