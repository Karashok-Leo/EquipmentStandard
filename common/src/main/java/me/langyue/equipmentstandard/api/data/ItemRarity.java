package me.langyue.equipmentstandard.api.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ItemRarity {
    private final List<ItemVerifier> verifiers;
    private final List<ItemVerifier> exclude;
    private final List<Rarity> rarities;

    public ItemRarity(List<ItemVerifier> verifiers, List<ItemVerifier> exclude, List<Rarity> rarities) {
        this.verifiers = verifiers;
        this.exclude = exclude;
        this.rarities = rarities;
    }

    public boolean isValid(ItemStack itemStack) {
        if (exclude != null && exclude.stream().anyMatch(it -> it.isValid(itemStack))) {
            return false;
        }
        return verifiers.stream().anyMatch(it -> it.isValid(itemStack));
    }

    public Rarity getRarity(Integer score) {
        if (score == null || score == 0d) return null;
        return rarities.stream()
                .sorted(Comparator.comparing(Rarity::getScore).reversed())
                .filter(rarity -> rarity.score <= score)
                .findFirst().orElse(null);
    }

    public static class Rarity {
        private final String name;
        private final Integer score;
        private MutableComponent prefix;
        private final ChatFormatting[] formatting;

        public Rarity(String name, Integer score, MutableComponent prefix, ChatFormatting... formatting) {
            this.name = name;
            this.score = score;
            this.prefix = prefix;
            this.formatting = formatting;
        }

        public String getName() {
            return name;
        }

        public double getScore() {
            return score;
        }

        public MutableComponent getPrefix() {
            if (prefix == null) {
                prefix = Component.empty();
            }
            if (formatting != null && formatting.length > 0) {
                prefix.withStyle(formatting);
            }
            return prefix;
        }

        public ChatFormatting[] getFormatting() {
            return formatting;
        }
    }
}
