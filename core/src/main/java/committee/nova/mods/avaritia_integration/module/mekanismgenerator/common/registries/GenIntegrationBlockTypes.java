package committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries;

import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import mekanism.common.block.attribute.AttributeHasBounding.HandleBoundingBlock;
import mekanism.common.block.attribute.AttributeHasBounding.TriBooleanFunction;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.BlockShapes;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.content.blocktype.Generator.GeneratorBuilder;
import mekanism.generators.common.registries.GeneratorsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GenIntegrationBlockTypes {

    private GenIntegrationBlockTypes() {
    }

    // Infinity Solar Generator
    public static final Generator<InfinitySolarGeneratorBlockEntity> INFINITY_SOLAR_GENERATOR = GeneratorBuilder
            .createGenerator(() -> GenIntegrationBlockEntityTypes.INFINITY_SOLAR_GENERATOR, GeneratorsLang.DESCRIPTION_SOLAR_GENERATOR)
            .withGui(() -> GenIntegrationContainerTypes.INFINITY_SOLAR_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.solarGenerator)
            .withCustomShape(BlockShapes.SOLAR_GENERATOR)
            .withSound(GeneratorsSounds.SOLAR_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .withComputerSupport("infinitySolarGenerator")
            .replace(Attributes.ACTIVE)
            .build();

    // Neutron Solar Generator
    public static final Generator<NeutronSolarGeneratorBlockEntity> NEUTRON_SOLAR_GENERATOR = GeneratorBuilder
            .createGenerator(() -> GenIntegrationBlockEntityTypes.NEUTRON_SOLAR_GENERATOR, GeneratorsLang.DESCRIPTION_SOLAR_GENERATOR)
            .withGui(() -> GenIntegrationContainerTypes.NEUTRON_SOLAR_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.solarGenerator)
            .withCustomShape(BlockShapes.SOLAR_GENERATOR)
            .withSound(GeneratorsSounds.SOLAR_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .withComputerSupport("neutronSolarGenerator")
            .replace(Attributes.ACTIVE)
            .build();

    // Infinity Advanced Solar Generator
    public static final Generator<InfinityAdvancedSolarGeneratorBlockEntity> INFINITY_ADVANCED_SOLAR_GENERATOR = GeneratorBuilder
            .createGenerator(() -> GenIntegrationBlockEntityTypes.INFINITY_ADVANCED_SOLAR_GENERATOR, GeneratorsLang.DESCRIPTION_ADVANCED_SOLAR_GENERATOR)
            .withGui(() -> GenIntegrationContainerTypes.INFINITY_ADVANCED_SOLAR_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.advancedSolarGenerator)
            .withCustomShape(BlockShapes.ADVANCED_SOLAR_GENERATOR)
            .withSound(GeneratorsSounds.SOLAR_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .withBounding(new HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    MutableBlockPos mutable = new MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                    if (!consumer.accept(level, mutable, data)) {
                        return false;
                    }
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            mutable.setWithOffset(pos, x, 2, z);
                            if (!consumer.accept(level, mutable, data)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("infinityAdvancedSolarGenerator")
            .replace(Attributes.ACTIVE)
            .build();

    // Neutron Advanced Solar Generator
    public static final Generator<NeutronAdvancedSolarGeneratorBlockEntity> NEUTRON_ADVANCED_SOLAR_GENERATOR = GeneratorBuilder
            .createGenerator(() -> GenIntegrationBlockEntityTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR, GeneratorsLang.DESCRIPTION_ADVANCED_SOLAR_GENERATOR)
            .withGui(() -> GenIntegrationContainerTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.advancedSolarGenerator)
            .withCustomShape(BlockShapes.ADVANCED_SOLAR_GENERATOR)
            .withSound(GeneratorsSounds.SOLAR_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .withBounding(new HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    MutableBlockPos mutable = new MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                    if (!consumer.accept(level, mutable, data)) {
                        return false;
                    }
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            mutable.setWithOffset(pos, x, 2, z);
                            if (!consumer.accept(level, mutable, data)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("neutronAdvancedSolarGenerator")
            .replace(Attributes.ACTIVE)
            .build();
}
