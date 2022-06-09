package net.wurstclient.events;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.wurstclient.event.Event;
import net.wurstclient.event.Listener;

public interface MobTargetSetListener extends Listener
{

	public void onMobTargetSet(MobTargetSetEvent event);
	
	public static class MobTargetSetEvent extends Event<MobTargetSetListener>
	{
		private MobEntity attacker;
		private LivingEntity target;
		
		public MobTargetSetEvent(MobEntity attacker, LivingEntity target)
		{
			this.attacker = attacker;
			this.target = target;
		}
		
		public MobEntity getAttacker()
		{
			return attacker;
		}

		public LivingEntity getTarget()
		{
			return target;
		}

		@Override
		public void fire(ArrayList<MobTargetSetListener> listeners)
		{
			for(MobTargetSetListener listener : listeners)
				listener.onMobTargetSet(this);
		}
		
		@Override
		public Class<MobTargetSetListener> getListenerType()
		{
			return MobTargetSetListener.class;
		}
	}
	
}
