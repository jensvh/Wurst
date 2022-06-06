package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.EntitySpawnListener.EntitySpawnEvent;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	
	@Inject(at = @At("HEAD"),
		method = "readFromPacket(Lnet/minecraft/network/packet/s2c/play/MobSpawnS2CPacket;)V",
		cancellable = false)
	private void onEntitySpawn(MobSpawnS2CPacket packet, CallbackInfo ci)
	{
		EntitySpawnEvent event = new EntitySpawnEvent(packet);
		EventManager.fire(event);
	}

}
