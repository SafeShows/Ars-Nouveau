package com.hollingsworth.arsnouveau.common.entity;

import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.IPickupResponder;
import com.hollingsworth.arsnouveau.api.spell.IPlaceBlockResponder;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellRecipeUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.SummoningCrytalTile;
import com.hollingsworth.arsnouveau.common.items.SpellParchment;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class EntityWhelp extends FlyingEntity implements IPickupResponder, IPlaceBlockResponder, IDispellable {
    public static final DataParameter<String> SPELL_STRING = EntityDataManager.createKey(EntityWhelp.class, DataSerializers.STRING);
    public static final DataParameter<ItemStack> HELD_ITEM = EntityDataManager.createKey(EntityWhelp.class, DataSerializers.ITEMSTACK);

    BlockPos crystalPos;
    int ticksSinceLastSpell;
    public List<AbstractSpellPart> spellRecipe;

    @Override
    public boolean canDespawn(double distanceFromPlayer) {
        return false;
    }

    protected EntityWhelp(EntityType<? extends FlyingEntity> p_i48568_1_, World world) {
        super(p_i48568_1_, world);
        this.moveController =  new FlyingMovementController(this, 10, true);
    }

    public EntityWhelp setRecipe(List<AbstractSpellPart> recipe){
        this.spellRecipe = recipe;
        return this;
    }

    public EntityWhelp(World world) {
        super(ModEntities.ENTITY_WHELP_TYPE, world);
        this.moveController = new FlyingMovementController(this, 10, true);
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if(world.isRemote)
            return true;
        ItemStack stack = player.getHeldItem(hand);


        if(stack != ItemStack.EMPTY && stack.getItem() instanceof SpellParchment){
            ArrayList<AbstractSpellPart> spellParts = SpellParchment.getSpellRecipe(stack);
            if(new EntitySpellResolver(spellParts).canCast(this)) {
                this.spellRecipe = SpellParchment.getSpellRecipe(stack);
                setRecipeString(SpellRecipeUtil.serializeForNBT(spellRecipe));
                player.sendMessage(new StringTextComponent("Spell set."));
                return true;
            } else{
                player.sendMessage(new StringTextComponent("A whelp cannot cast an invalid spell."));
                return false;
            }
        }else if(stack == ItemStack.EMPTY){
            if(spellRecipe == null || spellRecipe.isEmpty()){
                player.sendMessage(new StringTextComponent("Give this whelp a spell by giving it some inscribed Spell Parchment. "));
            }else
                player.sendMessage(new StringTextComponent("This whelp is casting " + SpellRecipeUtil.getDisplayString(spellRecipe)));
            return true;
        }
        setHeldStack(new ItemStack(stack.getItem()));
        player.sendMessage(new StringTextComponent("This whelp will use " + stack.getItem().getDisplayName(stack).getFormattedText() +  " in spells if this item is in a Summoning Crystal chest."));
        return true;
    }

    public EntityWhelp(World world, BlockPos crystalPos){
        this(world);
        this.crystalPos = crystalPos;
    }

    @Override
    public void tick() {
        super.tick();
        if(world == null || this.dead || crystalPos == null)
            return;
        ticksSinceLastSpell += 1;
        if(world.getGameTime() % 20 == 0 && !(world.getTileEntity(crystalPos) instanceof SummoningCrytalTile)){
            if(!world.isRemote){
                this.attackEntityFrom(DamageSource.causePlayerDamage(FakePlayerFactory.getMinecraft((ServerWorld)world)), 99);
            }
            if(world.isRemote){
                for(int i =0; i < 2; i++){
                    double d0 = getPosX();
                    double d1 = getPosY();
                    double d2 = getPosZ();

                    world.addParticle(ParticleTypes.ENCHANTED_HIT, d0, d1, d2, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new PerformTaskGoal(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public boolean canPerformAnotherTask(){
        return ticksSinceLastSpell > 60 && new EntitySpellResolver(spellRecipe).canCast(this);
    }

    public @Nullable BlockPos getTaskLoc(){
        if(world.getTileEntity(crystalPos) instanceof SummoningCrytalTile){
            return ((SummoningCrytalTile) world.getTileEntity(crystalPos)).getNextTaskLoc(spellRecipe, this);
        }
        return null;
    }

    public void castSpell(BlockPos target){
        if(world.isRemote)
            return;
        if(world instanceof ServerWorld){
            double d0 = target.getX() +0.5;
            double d1 = target.getY() + 1.0;
            double d2 = target.getZ() +0.5;
            ((ServerWorld)world).spawnParticle(ParticleTypes.ENCHANTED_HIT, d0, d1, d2,rand.nextInt(4), 0,0.3,0, 0.1);
        }
        if(!(world.getTileEntity(crystalPos) instanceof SummoningCrytalTile))
            return;
        if(((SummoningCrytalTile) world.getTileEntity(crystalPos)).removeManaAround(spellRecipe)){
            EntitySpellResolver resolver = new EntitySpellResolver(this.spellRecipe);
            resolver.onCastOnBlock(new BlockRayTraceResult(new Vec3d(target.getX(), target.getY(), target.getZ()), Direction.UP,target, false ), this);
        }
        this.ticksSinceLastSpell = 0;
    }

    public boolean enoughManaForTask(){
        if(!(world.getTileEntity(crystalPos) instanceof SummoningCrytalTile || spellRecipe == null || spellRecipe.isEmpty()))
            return false;
        return ((SummoningCrytalTile) world.getTileEntity(crystalPos)).enoughMana(spellRecipe);
    }

    @Override
    public void onDeath(DamageSource source) {
        if(!world.isRemote){
            ItemStack stack = new ItemStack(ItemsRegistry.whelpCharm);
            world.addEntity(new ItemEntity(world, getPosX(), getPosY(), getPosZ(), stack));
        }

        super.onDeath(source);
    }

    @Override
    public ItemStack onPickup(ItemStack stack) {
        SummoningCrytalTile tile = world.getTileEntity(crystalPos) instanceof SummoningCrytalTile ? (SummoningCrytalTile) world.getTileEntity(crystalPos) : null;
        return tile == null ? stack : tile.insertItem(stack);
    }

    @Override
    public ItemStack onPlaceBlock() {
        ItemStack heldStack = getHeldStack();
        if(heldStack == null )
            return  ItemStack.EMPTY;
        SummoningCrytalTile tile = world.getTileEntity(crystalPos) instanceof SummoningCrytalTile ? (SummoningCrytalTile) world.getTileEntity(crystalPos) : null;
        return tile == null ? heldStack : tile.getItem(heldStack.getItem());
    }

    public static class PerformTaskGoal extends Goal {
        EntityWhelp kobold;
        BlockPos taskLoc;
        int timePerformingTask;
        public PerformTaskGoal(EntityWhelp kobold){
            this.kobold = kobold;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            taskLoc = this.kobold.getTaskLoc();
            timePerformingTask = 0;
            if(this.kobold.navigator != null && taskLoc != null)
                 this.kobold.navigator.setPath(this.kobold.navigator.getPathToPos(taskLoc, 1), 1.0f);
        }

        @Override
        public void tick() {
            super.tick();
            timePerformingTask++;
            if(kobold == null  || taskLoc == null)
                return;

            if(BlockUtil.distanceFrom(kobold.getPosition(), taskLoc) <= 2){
                kobold.castSpell(taskLoc);
                kobold.navigator.clearPath();
                timePerformingTask = 0;
            }else if(kobold.navigator != null){
                this.kobold.navigator.setPath(this.kobold.navigator.getPathToPos(taskLoc.up(2), 0), 1f);
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return kobold.ticksSinceLastSpell > 60 && this.taskLoc != null && timePerformingTask < 300;
        }

        @Override
        public boolean shouldExecute() {
            return kobold.canPerformAnotherTask() && kobold.enoughManaForTask();
        }
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.ENTITY_WHELP_TYPE;
    }


    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        if(crystalPos != null){
            tag.putInt("summoner_x", crystalPos.getX());
            tag.putInt("summoner_y", crystalPos.getY());
            tag.putInt("summoner_z", crystalPos.getZ());
        }
        tag.putInt("last_spell", ticksSinceLastSpell);
        if(spellRecipe != null){
            tag.putString("spell", SpellRecipeUtil.serializeForNBT(spellRecipe));
        }
        if(getHeldStack() != null) {
            CompoundNBT itemTag = new CompoundNBT();
            getHeldStack().write(itemTag);
            tag.put("held", itemTag);
        }
    }

    public String getRecipeString(){
        return this.dataManager.get(SPELL_STRING);
    }

    public void setRecipeString(String recipeString){
        this.dataManager.set(SPELL_STRING, recipeString);
    }

    public ItemStack getHeldStack(){
        return this.dataManager.get(HELD_ITEM);
    }

    public void setHeldStack(ItemStack stack){
        this.dataManager.set(HELD_ITEM,stack);
    }


    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if(this.removed)
            return false;

        if(!world.isRemote){
            ItemStack stack = new ItemStack(ItemsRegistry.whelpCharm);
            world.addEntity(new ItemEntity(world, getPosX(), getPosY(), getPosZ(), stack));
            ParticleUtil.spawnPoof((ServerWorld)world, getPosition());
            this.remove();
        }
        return true;
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        if(tag.contains("summoner_x"))
            crystalPos = new BlockPos(tag.getInt("summoner_x"), tag.getInt("summoner_y"), tag.getInt("summoner_z"));
        spellRecipe = SpellRecipeUtil.getSpellsFromTagString(tag.getString("spell"));
        ticksSinceLastSpell = tag.getInt("last_spell");
        if(tag.contains("held"))
            setHeldStack(ItemStack.read((CompoundNBT)tag.get("held")));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue((double)0.4F);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.2F);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HELD_ITEM, ItemStack.EMPTY);
        this.dataManager.register(SPELL_STRING, "");
    }
}
