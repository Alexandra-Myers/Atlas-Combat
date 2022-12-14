package net.alexandra.atlas.atlas_combat;

import eu.midnightdust.lib.config.MidnightConfig;
import net.alexandra.atlas.atlas_combat.config.AtlasConfig;
import net.alexandra.atlas.atlas_combat.config.ConfigHelper;
import net.alexandra.atlas.atlas_combat.extensions.ItemExtensions;
import net.alexandra.atlas.atlas_combat.networking.NetworkingHandler;
import net.alexandra.atlas.atlas_combat.util.NeuralNetwork;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class AtlasCombat implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Atlas Combat");
	public static Player player;
	public static final AtlasConfig CONFIG = AtlasConfig.createAndLoad();

	public static ConfigHelper helper = new ConfigHelper();
	NeuralNetwork nn_custom_lr_with_multithreading = new NeuralNetwork(2, 10, 1, 0.01, true);

	@Override
	public void onInitialize(ModContainer mod) {
		MidnightConfig.init("atlas_combat", ConfigHelper.class);

		NetworkingHandler networkingHandler = new NetworkingHandler();

		DispenserBlock.registerBehavior(Items.TRIDENT, new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
				ThrownTrident trident = new ThrownTrident(EntityType.TRIDENT, world);
				trident.tridentItem = stack.copy();
				trident.setPosRaw(position.x(), position.y(), position.z());
				trident.pickup = AbstractArrow.Pickup.ALLOWED;
				return trident;
			}
		});

		List<Item> items = Registry.ITEM.stream().toList();

		for(Item item : items) {
			if(Objects.equals(Registry.ITEM.getKey(item).getNamespace(), "minecraft")) {
				int newStackSize = helper.getInt(helper.itemsJsonObject, Registry.ITEM.getKey(item).getPath());

				if (item.maxStackSize == newStackSize) continue;

				((ItemExtensions) item).setStackSize(newStackSize);
			}
		}

	}
}
