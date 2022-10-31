package dev.sterner.shuckhorror.common.registry;

import dev.sterner.shuckhorror.common.entity.ChildOfTheCornEntity;
import dev.sterner.shuckhorror.common.entity.CornCoblinEntity;
import dev.sterner.shuckhorror.common.util.Constants;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class SHEntityTypes {
	private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

	/*TODO
	public static final EntityType<CornCoblinEntity> CORN_COBLIN = register("corn_coblin", FabricEntityTypeBuilder
			.<CornCoblinEntity>createMob()
			.spawnGroup(SpawnGroup.MONSTER)
			.entityFactory(CornCoblinEntity::new)
			.defaultAttributes(CornCoblinEntity::createCornCoblinAttributes)
			.dimensions(EntityDimensions.fixed(1f, 1f))
			.build());

	public static final EntityType<ChildOfTheCornEntity> CHILD_OF_THE_CORN = register("child_of_the_corn", FabricEntityTypeBuilder
			.<ChildOfTheCornEntity>createMob()
			.spawnGroup(SpawnGroup.MONSTER)
			.entityFactory(ChildOfTheCornEntity::new)
			.defaultAttributes(ChildOfTheCornEntity::createChildOfTheCornAttributes)
			.dimensions(EntityDimensions.fixed(1f, 1f))
			.build());

	 */

	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}

	public static void init() {
		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
