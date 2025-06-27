/*
 * This file is part of  Stronger Mobs Below.
 * Copyright (c) 2025, Mark Gottschling (gottsch)
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
package mod.gottsch.forge.smb.core.client;

import java.util.Optional;

import mod.gottsch.forge.smb.core.config.ModConfig;
import mod.gottsch.forge.eechelonsapi.core.capability.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * This class was derived from Champions by TheIllusiveC4
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
public class MouseUtil {
	public static Optional<LivingEntity> getMouseOverEchelonMob(Minecraft mc, float partialTicks) {
		Entity entity = mc.getCameraEntity();
		if (entity != null) {
			if (mc.level != null) {
				double range = ModConfig.SERVER.hudRange.get();
				HitResult rayTraceResult = entity.pick(range, partialTicks, false);
				Vec3 vec3d = entity.getEyePosition(partialTicks);
				double distance = rayTraceResult.getLocation().distanceToSqr(vec3d);
				Vec3 viewVector = entity.getViewVector(1.0F);
				Vec3 vec3d2 = vec3d.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
				AABB aabb = entity.getBoundingBox().expandTowards(viewVector.scale(range)).inflate(1.0D, 1.0D, 1.0D);
				EntityHitResult entityRayTraceResult =
						ProjectileUtil.getEntityHitResult(entity, vec3d, vec3d2, aabb, (e) -> !e.isSpectator() && e.isPickable(), distance);

				if (entityRayTraceResult != null) {
					Entity hoverEntity = entityRayTraceResult.getEntity();
					if (hoverEntity.getCapability(ModCapabilities.DIFFICULTY_CAPABILITY).isPresent()) {
						return Optional.of((LivingEntity)hoverEntity);
					}
				}
			}
		}
		return Optional.empty();
	}
}
