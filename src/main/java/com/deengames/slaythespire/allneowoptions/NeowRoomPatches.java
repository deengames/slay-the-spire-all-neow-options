package com.deengames.slaythespire.allneowoptions;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDef;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardDrawback;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardType;
import com.megacrit.cardcrawl.neow.NeowRoom;

public class NeowRoomPatches {
    
    public static final String[] UNIQUE_REWARDS = NeowReward.UNIQUE_REWARDS;
    public static final String[] TEXT = NeowReward.TEXT;
    public static final int ITEMS_PER_PAGE = 10;

    private static final int hpBonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);
    private static int pageNumber = 0; // WHICH neow options?

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowRoom.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class AddButton {
		@SpirePostfixPatch
		public static void Postfix(NeowRoom room, boolean b) {
            pageNumber = 0;
			room.event.roomEventText.addDialogOption("[All Neow Options 1/2]");
            room.event.roomEventText.addDialogOption("[All Neow Options 2/2]");
		}
	}

    // Prefix so it doesn't fire at the same time as the above post-fix one.
    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = "buttonEffect")
	public static class HandleButtonClick {

		@SpirePostfixPatch
		public static void Postfix(AbstractEvent e, int buttonPressed) {
			try {
				Field screenNumField = NeowEvent.class.getDeclaredField("screenNum");
				screenNumField.setAccessible(true);
				int sn = screenNumField.getInt(e);

                if (sn == 3)
                {
                    pageNumber = buttonPressed - 1;
                    showNeowOptions(e);
                } else { // sn == 99
                    System.out.println("**************** activate option " + buttonPressed + " on page " + pageNumber);
                    int rewardIndex = (pageNumber * ITEMS_PER_PAGE) + buttonPressed;
                    
                    NeowRewardDef def = getRewardOptions().get(rewardIndex);
                    NeowReward reward = defToReward(def);
                    reward.activate();

                    buttonPressed = 0; // ???
                }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

        private static NeowReward defToReward(NeowRewardDef def)
        {
            NeowReward toReturn = new NeowReward(true);
            toReturn.optionLabel = def.desc;
            toReturn.type = def.type;
            toReturn.drawback = NeowRewardDrawback.NONE;
            return toReturn;
        }

        private static void clearAllOptions(AbstractEvent e) {
            boolean clearedAll = false;
            // quick, cheap, and dirty. real dirty.
            while (!clearedAll)
            {
                try {
                    e.roomEventText.removeDialogOption(0);
                } catch (Exception eee) {
                    // repeat after me: "exceptions are for exceptional cases" ... :S
                    clearedAll = true;
                }
            } 
        }

        private static void showNeowOptions(AbstractEvent e) {
            clearAllOptions(e);
            ArrayList<NeowRewardDef> rewardOptions = getRewardOptions();

            for (int i = 0; i < ITEMS_PER_PAGE; i++)
            {
                int itemIndex = (pageNumber * ITEMS_PER_PAGE) + i;
                if (itemIndex >= rewardOptions.size())
                {
                    break;
                }

                NeowRewardDef def = rewardOptions.get(itemIndex);
                NeowReward reward = defToReward(def);
                e.roomEventText.addDialogOption(reward.optionLabel);
            }
		}

        // screenNum = 0, 1 or 2 mean talk option
	    // 10 is only ok for trial (Custom Mode) I think
        // stoeln from BetterRewardsMod
        private static boolean acceptableScreenNum(int sn) {
            return sn == 0 || sn == 1 || sn == 2 || (Settings.isTrial && sn == 10);
        }

        // screenNum = 99 is the default value for leave event. This
        // calls openMap, which is patched to start a BetterRewards
        // screenNumField.setInt(e, 99);

        private static ArrayList<NeowRewardDef> getRewardOptions()
        {
            ArrayList<NeowRewardDef> rewardOptions = new ArrayList<>();

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

            return rewardOptions;
        }
	}
}
