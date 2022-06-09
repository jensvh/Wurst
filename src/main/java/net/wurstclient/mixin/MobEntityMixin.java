package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.MobTargetSetListener.MobTargetSetEvent;

@Mixin(MobEntity.class)
public class MobEntityMixin
{

	@Inject(at =
	{ @At("HEAD") }, method =
	{ "setTarget(Lnet/minecraft/entity/LivingEntity;)V" })
	private void onMobTargetSet(LivingEntity target, CallbackInfo ci)
	{
		MobTargetSetEvent event = new MobTargetSetEvent(
				(MobEntity) (Object) this, target);
		EventManager.fire(event);
	}

}
