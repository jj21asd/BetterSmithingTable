package dev.jjblock21.bst;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;

import java.util.List;
import java.util.Set;

public class BstMixinConfigPlugin extends RestrictiveMixinConfigPlugin {
    // these methods are required for Forge where IMixinConfigPlugin doesn't have
    // default implementations for them

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public List<String> getMixins() {
        return null;
    }
}
