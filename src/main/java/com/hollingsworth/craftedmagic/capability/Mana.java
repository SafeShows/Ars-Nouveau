package com.hollingsworth.craftedmagic.capability;

import com.hollingsworth.craftedmagic.api.mana.IMana;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SEntityPropertiesPacket;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collections;

public class Mana implements IMana {

    private final LivingEntity livingEntity;

    private int mana;

    public Mana(@Nullable final LivingEntity entity) {
        this.livingEntity = entity;
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public int getMaxMana() {
        return 100;
    }

    @Override
    public void setMana(int mana) {
        if(mana > getMaxMana()){
            this.mana = getMaxMana();
        }else if(mana < 0){
            this.mana = 0;
        }else {
            this.mana = mana;
        }
    }

    @Override
    public int addMana(int manaToAdd) {
        this.setMana(this.getCurrentMana() + manaToAdd);
        return this.mana;
    }

    @Override
    public int removeMana(int manaToRemove) {
        this.setMana(this.getCurrentMana() - manaToRemove);
        return this.mana;
    }

    @Override
    public void synchronise() {
        if (livingEntity != null && !livingEntity.getEntityWorld().isRemote) {
            final IAttributeInstance entityMaxHealthAttribute = livingEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
            final SEntityPropertiesPacket packet = new SEntityPropertiesPacket(livingEntity.getEntityId(), Collections.singleton(entityMaxHealthAttribute));

            ((ServerWorld) livingEntity.getEntityWorld()).getChunkProvider().sendToTrackingAndSelf(livingEntity, packet);
        }
    }
}