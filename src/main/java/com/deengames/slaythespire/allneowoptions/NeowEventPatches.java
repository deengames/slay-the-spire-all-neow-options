package com.deengames.slaythespire.allneowoptions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;

@SpirePatch(clz=NeowEvent.class, method="talk")
public class NeowEventPatches {

    private static NeowEvent event;
    
    @SpirePostfixPatch
	// Pilfered from NeowEvent.dailyBlessing
	private static void talk(NeowEvent __instance, String msg) 
	{
		System.out.println("***************************** arrrr");
        event = __instance;
	}
}