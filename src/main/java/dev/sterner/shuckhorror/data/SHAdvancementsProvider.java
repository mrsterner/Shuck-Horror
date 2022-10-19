package dev.sterner.shuckhorror.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.server.AdvancementProvider;

import java.util.function.Consumer;

public class SHAdvancementsProvider extends FabricAdvancementProvider {
	protected SHAdvancementsProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateAdvancement(Consumer<Advancement> consumer) {
		new SHAdvancementTab().accept(consumer);
	}
}
