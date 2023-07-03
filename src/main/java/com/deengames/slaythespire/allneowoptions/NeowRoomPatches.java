package com.deengames.slaythespire.allneowoptions;

import java.lang.reflect.Field;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;

public class NeowRoomPatches {
    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowRoom.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
	
    public static class AddButton {
		@SpirePostfixPatch
		public static void Postfix(NeowRoom room, boolean b) {
			room.event.roomEventText.addDialogOption("[All Neow Options]");
		}
	}

    @SpirePatch(clz = com.megacrit.cardcrawl.neow.NeowEvent.class, method = "buttonEffect")
	public static class HandleButtonClick {
		@SpirePrefixPatch
		public static void Prefix(AbstractEvent e, int buttonPressed) {
			try {
				Field screenNumField = NeowEvent.class.getDeclaredField("screenNum");
				screenNumField.setAccessible(true);
				int sn = screenNumField.getInt(e);
				onClick(e, buttonPressed, screenNumField, sn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

        @SpirePostfixPatch
        public static void Postfix(AbstractEvent e, int buttonPressed) {
            boolean clearedAll = false;

			try {
				Field screenNumField = NeowEvent.class.getDeclaredField("screenNum");
				screenNumField.setAccessible(true);
				int sn = screenNumField.getInt(e);
                System.out.println("***** postfix ***: e=" + e + ", bn=" + buttonPressed + ", umm..." + sn);
                
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

                e.roomEventText.addDialogOption("Welcome to MOAR MEOW");
                e.roomEventText.addDialogOption("Welcome to MOAR MEOW");
                e.roomEventText.addDialogOption("Welcome to MOAR MEOW");
                e.roomEventText.addDialogOption("Welcome to MOAR MEOW");
			
            } catch (Exception ex) {
				ex.printStackTrace();
			}
		}

        // screenNum = 0, 1 or 2 mean talk option
	    // 10 is only ok for trial (Custom Mode) I think
        private static boolean acceptableScreenNum(int sn) {
            return sn == 0 || sn == 1 || sn == 2 || (Settings.isTrial && sn == 10);
        }

        private static void onClick(AbstractEvent e, int buttonPressed, Field screenNumField, int sn) throws IllegalAccessException {

            System.out.println("Hello, onClick. e=" + e + ", button=" + buttonPressed + ", sn=" + sn);
            if (buttonPressed == 1 && acceptableScreenNum(sn))
            {
            }
            else
            {
                // screenNum = 99 is the default value for leave event. This
                // calls openMap, which is patched to start a BetterRewards
                screenNumField.setInt(e, 99);
            }
        }
	}
}
