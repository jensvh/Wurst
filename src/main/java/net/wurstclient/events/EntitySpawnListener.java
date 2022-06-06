package net.wurstclient.events;

import java.util.ArrayList;

import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.wurstclient.event.Event;
import net.wurstclient.event.Listener;

public interface EntitySpawnListener extends Listener {

	public void onEntitySpawn(EntitySpawnEvent event);
	
	public static class EntitySpawnEvent
		extends Event<EntitySpawnListener>
	{
		private MobSpawnS2CPacket packet;
		
		public EntitySpawnEvent(MobSpawnS2CPacket packet)
		{
			this.packet = packet;
		}
		
		public MobSpawnS2CPacket getEntitySpawnPacket()
		{
			return packet;
		}
		
		@Override
		public void fire(ArrayList<EntitySpawnListener> listeners)
		{
			for(EntitySpawnListener listener : listeners)
				listener.onEntitySpawn(this);
		}
		
		@Override
		public Class<EntitySpawnListener> getListenerType()
		{
			return EntitySpawnListener.class;
		}
	}
}
