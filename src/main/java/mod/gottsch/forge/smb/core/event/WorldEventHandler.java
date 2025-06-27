/*
 * This file is part of  Stronger Mobs Below.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Stronger Mobs Below is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stronger Mobs Below is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Stronger Mobs Below.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.smb.core.event;

import mod.gottsch.forge.eechelonsapi.api.EnemyEchelonsApi;
import mod.gottsch.forge.eechelonsapi.core.network.DifficultyRequestToServer;
import mod.gottsch.forge.eechelonsapi.core.network.ModNetwork;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.smb.SMB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling on Jul 31, 2022
 *
 */
public class WorldEventHandler {

	/**
	 * Forge Bus Event Subscriber class
	 */
	@Mod.EventBusSubscriber(modid = SMB.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
	public static class ForgeBusSubscriber {

		/**
		 * @param event
		 */
		@SubscribeEvent
		public static void onJoin(EntityJoinLevelEvent event) {

			Entity entity = event.getEntity();

			if (EnemyEchelonsApi.isValidEntity(entity)) {
//				EEchelons.LOGGER.debug("entity joining world -> {} : {}", entity.getName().getString(), entity.getId());
				/*
				 * if on the client, request an update from the server
				 */
				if (WorldInfo.isClientSide(event.getEntity().level())) {
					// get cap, ensure that level hasn't already been set.
					if (EnemyEchelonsApi.hasDifficultyCapability(entity) && EnemyEchelonsApi.getDifficulty(entity) == -1) {
						DifficultyRequestToServer message = new DifficultyRequestToServer(entity.getId(), entity.level().dimension().location().toString(),
								entity.level().dimension().location().toString());
						ModNetwork.CHANNEL.sendToServer(message);
					}
				} else {
					Mob mob = (Mob) entity;
					EnemyEchelonsApi.apply(mob);
				}
			}
		}
	}
}
