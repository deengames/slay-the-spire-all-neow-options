package com.deengames.slaythespire.allneowoptions;

import java.lang.reflect.Field;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
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

        private static void onClick(AbstractEvent e, int buttonPressed, Field screenNumField, int sn) throws IllegalAccessException {

            System.out.println("Hello, onClick. e=" + e + ", button=" + buttonPressed + ", sn=" + sn);
            if (buttonPressed == 1 && sn == 1)
            {
                e.roomEventText.clear();
                e.roomEventText.addDialogOption("Welcome to MOAR MEOW");
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
