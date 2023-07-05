package com.deengames.slaythespire.allneowoptions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.random.Random;

@SpirePatch(clz=NeowEvent.class, method="talk")
public class NeowEventPatches {

	@SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class SeedRng {
		@SpirePostfixPatch
		public static void Postfix(NeowEvent event, boolean isDone) {
			event.rng = new Random(Settings.seed);
		}
	}
}