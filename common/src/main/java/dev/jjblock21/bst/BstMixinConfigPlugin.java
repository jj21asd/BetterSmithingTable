package dev.jjblock21.bst;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BstMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        AnnotationNode annotation = getRequireModAnnotation(mixinClassName);
        if (annotation != null) {
            List<String> modIds = Annotations.getValue(annotation);
            boolean any = Annotations.getValue(annotation, "any", (Boolean) false);
            return any
                ? modIds.stream().anyMatch(Platform::isModLoaded)
                : modIds.stream().allMatch(Platform::isModLoaded);
        }
        return true;
    }

    private static AnnotationNode getRequireModAnnotation(String className) {
        try {
            ClassNode classNode = MixinService.getService()
                .getBytecodeProvider()
                .getClassNode(className);

            return Annotations.getVisible(classNode, RequiresMod.class);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Failed to inspect mixin class " + className, e);
        }
    }

    // these have to be declared for NeoForge where IMixinConfigPlugin doesn't have
    // default implementations for them
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
