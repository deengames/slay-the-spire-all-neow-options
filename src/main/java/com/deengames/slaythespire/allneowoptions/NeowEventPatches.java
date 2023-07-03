package com.deengames.slaythespire.allneowoptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDef;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardType;

@SpirePatch(clz=NeowEvent.class, method="talk")
public class NeowEventPatches {

	private static boolean ranUpdateLogic = false;

    private static NeowEvent event = null; // current event
	
	public static final String[] TEXT = NeowReward.TEXT;
	public static final String[] UNIQUE_REWARDS = NeowReward.UNIQUE_REWARDS;

	// from NeowRward.class, getRewardOptions method
	private static ArrayList<NeowRewardDef> rewardOptions = new ArrayList<>();
	private static int hpBonus = 0;

    @SpirePrefixPatch
	// Pilfered from NeowEvent.dailyBlessing
	private static void talk(NeowEvent __instance, String msg) 
	{
		if (__instance != event)
		{
			event = __instance;
			System.out.println("***************************** arrrr: " + event);
			hpBonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);

			populateRewards();

			for (int i = 0; i < rewardOptions.size(); i++)
			{
				System.out.println("Reward " + (i + 1) + ": " + rewardOptions.get(i));
			}
		}
	}

	@SpirePrefixPatch
	private static void update(NeowEvent __instance)
	{
		if (event != null && !ranUpdateLogic)
		{
			ranUpdateLogic = true;
			System.out.println("@@@@@ update");
			Object result = getInstanceField(__instance, "rewards");
			if (result != null)
			{
				ArrayList<NeowReward> asRewards = (ArrayList<NeowReward>)result;
				if (asRewards != null)
				{
					for (int i = 0; i < rewardOptions.size(); i++)
					{
						NeowRewardDef def = rewardOptions.get(i);
						NeowReward reward = defToReward(def);
						asRewards.add(reward);
					}

					System.out.println("@@@@@ DONE");
				} else {
					printError("asrewards is null");
				}
			} else {
				printError("rewards is null");
			}
		}
	}

	private static NeowReward defToReward(NeowRewardDef def)
	{
		NeowReward toReturn = new NeowReward(true);
		toReturn.optionLabel = def.desc;
		toReturn.type = def.type;
		return toReturn;
	}
	
	private static void populateRewards()
	{
		// category 0
		rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_CARDS, TEXT[0]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.ONE_RANDOM_RARE_CARD, TEXT[1]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.REMOVE_CARD, TEXT[2]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.UPGRADE_CARD, TEXT[3]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.TRANSFORM_CARD, TEXT[4]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COLORLESS, TEXT[30]));

		// category 1
		rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_SMALL_POTIONS, TEXT[5]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COMMON_RELIC, TEXT[6]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.TEN_PERCENT_HP_BONUS, TEXT[7] + hpBonus + " ]"));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_ENEMY_KILL, TEXT[28]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.HUNDRED_GOLD, TEXT[8] + 'd' + TEXT[9]));

		// category 2
        rewardOptions.add(new NeowRewardDef(NeowRewardType.RANDOM_COLORLESS_2, TEXT[31]));
		rewardOptions.add(new NeowRewardDef(NeowRewardType.REMOVE_TWO, TEXT[10])); 
        rewardOptions.add(new NeowRewardDef(NeowRewardType.ONE_RARE_RELIC, TEXT[11]));
        rewardOptions.add(new NeowRewardDef(NeowRewardType.THREE_RARE_CARDS, TEXT[12]));
		rewardOptions.add(new NeowRewardDef(NeowRewardType.TWO_FIFTY_GOLD, TEXT[13] + TEXT[14])); 
        rewardOptions.add(new NeowRewardDef(NeowRewardType.TRANSFORM_TWO_CARDS, TEXT[15]));
		rewardOptions.add(new NeowRewardDef(NeowRewardType.TWENTY_PERCENT_HP_BONUS, TEXT[16] + (hpBonus * 2) + " ]")); 

		// category 3
		rewardOptions.add(new NeowRewardDef(NeowRewardType.BOSS_RELIC, UNIQUE_REWARDS[0]));
	}

	public static Object getInstanceField(Object instance, String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		}
		catch (Exception e)
		{
			printException("error accessing field " + fieldName, e);
		}
		return null;
    }

	private static Object invokeMethod(Object o, String methodName, Object ... args) {
		try {
			Method method = o.getClass().getDeclaredMethod(methodName);
			method.setAccessible(true);
			Object result = method.invoke(event, args);
			return result;
		} catch (InvocationTargetException e) {
			printException("exception on " + methodName, e.getTargetException());
		} catch (Exception e) {
			printException("exception on " + methodName, e);
		}
		return null;
	}

	private static void printError(String message) {
		printException(message, null);
	}

	private static void printException(String message, Throwable e)
	{
		String eMessage = "";
		if (e != null)
		{
			eMessage = e.toString();
		}
		System.out.println("*** All Neow Options: " + message + ", please report to dev: " + eMessage);
	}
}