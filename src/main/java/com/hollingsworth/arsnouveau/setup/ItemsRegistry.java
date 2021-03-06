package com.hollingsworth.arsnouveau.setup;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.ISpellTier;
import com.hollingsworth.arsnouveau.common.armor.ApprenticeArmor;
import com.hollingsworth.arsnouveau.common.armor.MasterArmor;
import com.hollingsworth.arsnouveau.common.armor.NoviceArmor;
import com.hollingsworth.arsnouveau.common.items.*;
import com.hollingsworth.arsnouveau.common.items.curios.*;
import com.hollingsworth.arsnouveau.common.lib.LibItemNames;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

import static com.hollingsworth.arsnouveau.setup.InjectionUtil.Null;

@ObjectHolder(ArsNouveau.MODID)
public class ItemsRegistry {

    @ObjectHolder(LibItemNames.NOVICE_SPELL_BOOK) public static SpellBook noviceSpellBook;

    @ObjectHolder(LibItemNames.APPRENTICE_SPELL_BOOK) public static SpellBook apprenticeSpellBook;
    @ObjectHolder(LibItemNames.ARCHMAGE_SPELL_BOOK) public static SpellBook archmageSpellBook;
    @ObjectHolder(LibItemNames.CREATIVE_SPELL_BOOK) public static SpellBook creativeSpellBook;


    @ObjectHolder(LibItemNames.BLANK_GLYPH) public static  Item blankGlyph;
    @ObjectHolder(LibItemNames.BUCKET_OF_MANA) public static ModItem bucketOfMana;

    @ObjectHolder(LibItemNames.MAGIC_CLAY) public static ModItem magicClay;
    @ObjectHolder(LibItemNames.MARVELOUS_CLAY) public static ModItem marvelousClay;
    @ObjectHolder(LibItemNames.MYTHICAL_CLAY) public static ModItem mythicalClay;

    @ObjectHolder(LibItemNames.ARCANE_BRICK) public static ModItem arcaneBrick;
    @ObjectHolder(LibItemNames.MANA_BLOOM) public static ModItem manaBloom;
//    @ObjectHolder(LibItemNames.MANA_GEM) public static ModItem manaGem;

    @ObjectHolder(LibItemNames.MANA_FIBER) public static ModItem manaFiber;
    @ObjectHolder(LibItemNames.BLAZE_FIBER) public static ModItem blazeFiber;
    @ObjectHolder(LibItemNames.END_FIBER) public static ModItem endFiber;

    @ObjectHolder(LibItemNames.MUNDANE_BELT) public static ModItem mundaneBelt;
    @ObjectHolder(LibItemNames.JAR_OF_LIGHT) public static JarOfLight jarOfLight;

    @ObjectHolder(LibItemNames.BELT_OF_LEVITATION)public static BeltOfLevitation beltOfLevitation;

    @ObjectHolder(LibItemNames.WORN_NOTEBOOK) public static WornNotebook wornNotebook = Null();

    @ObjectHolder(LibItemNames.RING_OF_POTENTIAL) public  static ModItem ringOfPotential;

    @ObjectHolder(LibItemNames.RING_OF_LESSER_DISCOUNT) public static DiscountRing ringOfLesserDiscount;


    @ObjectHolder(LibItemNames.RING_OF_GREATER_DISCOUNT) public static DiscountRing ringOfGreaterDiscount;

    @ObjectHolder(LibItemNames.BELT_OF_UNSTABLE_GIFTS) public static BeltOfUnstableGifts beltOfUnstableGifts;

    @ObjectHolder(LibItemNames.WARP_SCROLL) public static WarpScroll warpScroll;

    @ObjectHolder(LibItemNames.SPELL_PARCHMENT) public static SpellParchment spellParchment;

    @ObjectHolder(LibItemNames.WHELP_CHARM) public static WhelpCharm whelpCharm;
    @ObjectHolder(LibItemNames.CARBUNCLE_CHARM) public static CarbuncleCharm carbuncleCharm;
    @ObjectHolder(LibItemNames.DOMINION_WAND) public static DominionWand DOMAIN_CONTROLLER;

    @ObjectHolder(LibItemNames.AMULET_OF_MANA_BOOST)public static AbstractManaCurio amuletOfManaBoost;
    @ObjectHolder(LibItemNames.AMULET_OF_MANA_REGEN)public static AbstractManaCurio amuletOfManaRegen;
    @ObjectHolder(LibItemNames.DULL_TRINKET)public static ModItem dullTrinket;

    @ObjectHolder(LibItemNames.DOMINION_WAND)public static DominionWand dominionWand;
    @ObjectHolder(LibItemNames.RUNIC_CHALK)public static RunicChalk runicChalk;
    @ObjectHolder(LibItemNames.CARBUNCLE_SHARD)public static ModItem carbuncleShard;
    @ObjectHolder(LibItemNames.EARTH_ELEMENTAL_CHARM)public static EarthElementalCharm earthElementalCharm;
    @ObjectHolder(LibItemNames.EARTH_ELEMENTAL_SHARD)public static ModItem earthElementalShard;
    @ObjectHolder(LibItemNames.SYLPH_CHARM)public static SylphCharm sylphCharm;
    @ObjectHolder(LibItemNames.SYLPH_SHARD)public static ModItem sylphShard;
    @ObjectHolder(LibItemNames.MANA_GEM)public static ModItem manaGem;

    @Mod.EventBusSubscriber(modid = ArsNouveau.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler{
        public static final Set<Item> ITEMS = new HashSet<>();

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {

            Item[] items = {
                    new WhelpCharm(),
                    new ModItem(LibItemNames.CARBUNCLE_SHARD),
                    new DominionWand(),
                    new CarbuncleCharm(),
                    new RunicChalk(),
//                    new ModItem(LibItemNames.MANA_GEM),
                    new ModItem(LibItemNames.BLANK_GLYPH),
                    new ModItem(LibItemNames.DULL_TRINKET),
                    new ModItem(LibItemNames.MARVELOUS_CLAY),
                    new ModItem(LibItemNames.MAGIC_CLAY),
                    new ModItem(LibItemNames.MYTHICAL_CLAY),
                    new ModItem(LibItemNames.BLAZE_FIBER),
                    new ModItem(LibItemNames.END_FIBER),
                    new ModItem(LibItemNames.MANA_BLOOM),
                    new ModItem(LibItemNames.MANA_FIBER),
                    new ModItem(LibItemNames.MUNDANE_BELT),
                    new ModItem(LibItemNames.ARCANE_BRICK).withTooltip(new TranslationTextComponent("tooltip.arcane_brick")),
                    new ModItem(LibItemNames.RING_OF_POTENTIAL),
                    new ModItem(LibItemNames.EARTH_ELEMENTAL_SHARD),
                    new EarthElementalCharm(),
                    new BeltOfUnstableGifts(LibItemNames.BELT_OF_UNSTABLE_GIFTS),
                    new ModItem(defaultItemProperties().maxStackSize(1), "bucket_of_mana"),

                    new NoviceArmor(EquipmentSlotType.FEET).setRegistryName("novice_boots"),
                    new NoviceArmor(EquipmentSlotType.LEGS).setRegistryName("novice_leggings"),
                    new NoviceArmor(EquipmentSlotType.CHEST).setRegistryName("novice_robes"),
                    new NoviceArmor(EquipmentSlotType.HEAD).setRegistryName("novice_hood"),

                    new ApprenticeArmor(EquipmentSlotType.FEET).setRegistryName("apprentice_boots"),
                    new ApprenticeArmor(EquipmentSlotType.LEGS).setRegistryName("apprentice_leggings"),
                    new ApprenticeArmor(EquipmentSlotType.CHEST).setRegistryName("apprentice_robes"),
                    new ApprenticeArmor(EquipmentSlotType.HEAD).setRegistryName("apprentice_hood"),

                    new MasterArmor(EquipmentSlotType.FEET).setRegistryName("archmage_boots"),
                    new MasterArmor(EquipmentSlotType.LEGS).setRegistryName("archmage_leggings"),
                    new MasterArmor(EquipmentSlotType.CHEST).setRegistryName("archmage_robes"),
                    new MasterArmor(EquipmentSlotType.HEAD).setRegistryName("archmage_hood"),

                    new SpellBook(ISpellTier.Tier.ONE).setRegistryName(LibItemNames.NOVICE_SPELL_BOOK),
                    new SpellBook(ISpellTier.Tier.TWO).setRegistryName(LibItemNames.APPRENTICE_SPELL_BOOK),
                    new SpellBook(ISpellTier.Tier.THREE).setRegistryName(LibItemNames.ARCHMAGE_SPELL_BOOK),
                    new SpellBook(ISpellTier.Tier.THREE).setRegistryName(LibItemNames.CREATIVE_SPELL_BOOK),

                    new RingOfAmplify(),
                    new BeltOfLevitation(),
                    new WarpScroll(),
                    new JarOfLight(),
                    new WornNotebook(),
                    new DiscountRing(LibItemNames.RING_OF_LESSER_DISCOUNT) {
                        @Override
                        public int getManaDiscount() {
                            return 10;
                        }
                    },
                    new DiscountRing(LibItemNames.RING_OF_GREATER_DISCOUNT) {
                        @Override
                        public int getManaDiscount() {
                            return 20;
                        }
                    },
                    new SpellParchment(),
                    new AbstractManaCurio(LibItemNames.AMULET_OF_MANA_BOOST){
                        @Override
                        public int getMaxManaBoost() {
                            return 50;
                        }
                    },
                    new AbstractManaCurio(LibItemNames.AMULET_OF_MANA_REGEN){
                        @Override
                        public int getManaRegenBonus() {
                            return 3;
                        }
                    },
                    new ModItem(LibItemNames.SYLPH_SHARD),
                    new SylphCharm(),
                    new ModItem(LibItemNames.MANA_GEM).withTooltip(new TranslationTextComponent("tooltip.mana_gem"))
            };

            final IForgeRegistry<Item> registry = event.getRegistry();
            for(Glyph glyph : ArsNouveauAPI.getInstance().getGlyphMap().values()){
                registry.register(glyph);
                ITEMS.add(glyph);
            }
            for (final Item item : items) {
                registry.register(item);
                ITEMS.add(item);
            }
        }
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties().group(ArsNouveau.itemGroup);
    }
}

