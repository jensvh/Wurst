package net.wurstclient.hacks;

import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.wurstclient.SearchTags;
import net.wurstclient.WurstClient;
import net.wurstclient.events.MobTargetSetListener;
import net.wurstclient.hack.Hack;

@SearchTags(
{ "Attack Notfy", "Notify mob attack" })
public class AttackNotifierHack extends Hack implements MobTargetSetListener
{

	public AttackNotifierHack()
	{
		super("AttackNotifier");
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(MobTargetSetListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(MobTargetSetListener.class, this);
	}

	@Override
	public void onMobTargetSet(MobTargetSetEvent event)
	{
		if (event.getTarget() == null) return;
		if (!event.getTarget().getType().equals(EntityType.PLAYER)) return;
		
		LiteralText text = new LiteralText("Under attack from a: " + EntityType.getId(event.getAttacker().getType()).getPath());
		Style style = Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true).withUnderline(true);
		text.setStyle(style);
		
		WurstClient.MC.player.sendMessage(text, true);
	}

}
