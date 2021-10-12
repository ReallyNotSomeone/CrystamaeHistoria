package io.github.sefiraat.crystamaehistoria.magic.spells;

import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import io.github.sefiraat.crystamaehistoria.magic.spells.core.Spell;
import io.github.sefiraat.crystamaehistoria.magic.spells.core.SpellCoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ThreadLocalRandom;

public class KnowledgeShare extends Spell {

    private static final int BASE_EXP = 50;
    private static final int EXP_PER_ORB = 5;

    public KnowledgeShare() {
        SpellCoreBuilder spellCoreBuilder = new SpellCoreBuilder(360, true, 15, false, 10, true)
            .makeTickingSpell(this::onTick, 5, false, 2, false);
        setSpellCore(spellCoreBuilder.build());
    }

    @ParametersAreNonnullByDefault
    public void onTick(CastInformation castInformation) {
        // TODO SHIT SHIT SHIT
        Player caster = Bukkit.getPlayer(castInformation.getCaster());
        if (caster != null) {
            int casterExp = caster.getTotalExperience();

            int exp = Math.min(casterExp, BASE_EXP * castInformation.getStaveLevel());
            int remainder = exp % EXP_PER_ORB;
            int waves = ((exp - remainder) / EXP_PER_ORB);

            for (int i = 0; i < waves; i++) {
                double xOffset = ThreadLocalRandom.current().nextDouble(-getRange(castInformation), getRange(castInformation));
                double zOffset = ThreadLocalRandom.current().nextDouble(-getRange(castInformation), getRange(castInformation));
                Location location = caster.getLocation().clone().add(xOffset, 20, zOffset);
                ExperienceOrb experienceOrb = (ExperienceOrb) caster.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
                experienceOrb.setExperience(EXP_PER_ORB);
                location.getWorld().playEffect(location, Effect.DRIPPING_DRIPSTONE, 1);
                caster.giveExp(-EXP_PER_ORB);
            }
        }
    }

    @Nonnull
    @Override
    public String getId() {
        return "KNOWLEDGE_SHARE";
    }

    @Nonnull
    @Override
    public String[] getLore() {
        return new String[] {
            "Removes EXP from the caster and makes",
            "it rain down from the sky around them."
        };
    }

    @Nonnull
    @Override
    public ItemStack getStack() {
        return new ItemStack(Material.EXPERIENCE_BOTTLE);
    }
}
