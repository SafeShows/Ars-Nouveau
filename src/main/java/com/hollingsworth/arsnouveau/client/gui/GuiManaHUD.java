package com.hollingsworth.arsnouveau.client.gui;

import com.hollingsworth.arsnouveau.api.util.StackUtil;
import com.hollingsworth.arsnouveau.common.capability.ManaCapability;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class GuiManaHUD extends AbstractGui {
    private static final Minecraft minecraft = Minecraft.getInstance();

    public void drawHUD() {
        ItemStack stack = StackUtil.getHeldSpellbook(minecraft.player);
        if(stack != ItemStack.EMPTY && stack.getItem() instanceof SpellBook && stack.getTag() != null){
            ManaCapability.getMana(minecraft.player).ifPresent(mana ->{
                int offsetLeft = 10;
                double x = 100 ; //Length
                x = (x) * (((double) mana.getCurrentMana()) / ((double) mana.getMaxMana() - 0.0))  + offsetLeft;

                int y = minecraft.getMainWindow().getScaledHeight() - 5;

                int offsetStop =  minecraft.getMainWindow().getScaledHeight() - 15;; // This determines the thickness from Y. Draws from Y to this value. Must be > y.
                fill(offsetLeft, y, (int)100+ offsetLeft, offsetStop, 0xFF000000 | Integer.parseInt("C9CAB9", 16));
                fillGradient((int)x, y, offsetLeft, offsetStop, 0xFF000000 | Integer.parseInt("337CFF", 16), new Color(0xFF000000 | Integer.parseInt("1145A1", 16)).darker().getRGB());
                for(int i = 100; i <= mana.getMaxMana(); i+=100){
                    double marker = (100) * ((i) / ((double) mana.getMaxMana() - 0.0))  + offsetLeft;
                    fill((int)marker, y, (int)marker+1, offsetStop, 0xFF000000 | Integer.parseInt("E4F10A", 16));
                }
            });
        }

    }
}
