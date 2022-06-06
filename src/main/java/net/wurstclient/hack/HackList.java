/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hack;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.wurstclient.WurstClient;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hacks.AntiAfkHack;
import net.wurstclient.hacks.AntiBlindHack;
import net.wurstclient.hacks.AntiCactusHack;
import net.wurstclient.hacks.AntiKnockbackHack;
import net.wurstclient.hacks.AntiSpamHack;
import net.wurstclient.hacks.AntiWaterPushHack;
import net.wurstclient.hacks.AntiWobbleHack;
import net.wurstclient.hacks.AutoArmorHack;
import net.wurstclient.hacks.AutoBuildHack;
import net.wurstclient.hacks.AutoDropHack;
import net.wurstclient.hacks.AutoEatHack;
import net.wurstclient.hacks.AutoFarmHack;
import net.wurstclient.hacks.AutoFishHack;
import net.wurstclient.hacks.AutoLeaveHack;
import net.wurstclient.hacks.AutoMineHack;
import net.wurstclient.hacks.AutoReconnectHack;
import net.wurstclient.hacks.AutoRespawnHack;
import net.wurstclient.hacks.AutoSignHack;
import net.wurstclient.hacks.AutoSoupHack;
import net.wurstclient.hacks.AutoSprintHack;
import net.wurstclient.hacks.AutoStealHack;
import net.wurstclient.hacks.AutoSwimHack;
import net.wurstclient.hacks.AutoSwordHack;
import net.wurstclient.hacks.AutoToolHack;
import net.wurstclient.hacks.AutoTotemHack;
import net.wurstclient.hacks.AutoWalkHack;
import net.wurstclient.hacks.BaseFinderHack;
import net.wurstclient.hacks.BlinkHack;
import net.wurstclient.hacks.BonemealAuraHack;
import net.wurstclient.hacks.BunnyHopHack;
import net.wurstclient.hacks.CameraNoClipHack;
import net.wurstclient.hacks.CaveFinderHack;
import net.wurstclient.hacks.ChatTranslatorHack;
import net.wurstclient.hacks.ChestEspHack;
import net.wurstclient.hacks.ClickGuiHack;
import net.wurstclient.hacks.CriticalsHack;
import net.wurstclient.hacks.DolphinHack;
import net.wurstclient.hacks.ExcavatorHack;
import net.wurstclient.hacks.ExtraElytraHack;
import net.wurstclient.hacks.FancyChatHack;
import net.wurstclient.hacks.FeedAuraHack;
import net.wurstclient.hacks.FightBotHack;
import net.wurstclient.hacks.FollowHack;
import net.wurstclient.hacks.FreecamHack;
import net.wurstclient.hacks.FullbrightHack;
import net.wurstclient.hacks.HealthTagsHack;
import net.wurstclient.hacks.InstantBunkerHack;
import net.wurstclient.hacks.InvWalkHack;
import net.wurstclient.hacks.ItemEspHack;
import net.wurstclient.hacks.KillauraHack;
import net.wurstclient.hacks.KillauraLegitHack;
import net.wurstclient.hacks.MobEspHack;
import net.wurstclient.hacks.MobSpawnEspHack;
import net.wurstclient.hacks.NameTagsHack;
import net.wurstclient.hacks.NavigatorHack;
import net.wurstclient.hacks.NoBackgroundHack;
import net.wurstclient.hacks.NoFallHack;
import net.wurstclient.hacks.NoHurtcamHack;
import net.wurstclient.hacks.NoOverlayHack;
import net.wurstclient.hacks.NoPumpkinHack;
import net.wurstclient.hacks.NoWeatherHack;
import net.wurstclient.hacks.OpenWaterEspHack;
import net.wurstclient.hacks.OverlayHack;
import net.wurstclient.hacks.PanicHack;
import net.wurstclient.hacks.ParkourHack;
import net.wurstclient.hacks.PlayerEspHack;
import net.wurstclient.hacks.PortalGuiHack;
import net.wurstclient.hacks.RadarHack;
import net.wurstclient.hacks.RainbowUiHack;
import net.wurstclient.hacks.SafeWalkHack;
import net.wurstclient.hacks.ScaffoldWalkHack;
import net.wurstclient.hacks.SearchHack;
import net.wurstclient.hacks.SneakHack;
import net.wurstclient.hacks.TillauraHack;
import net.wurstclient.hacks.TooManyHaxHack;
import net.wurstclient.hacks.TrajectoriesHack;
import net.wurstclient.hacks.TreeBotHack;
import net.wurstclient.hacks.TriggerBotHack;
import net.wurstclient.hacks.TrueSightHack;
import net.wurstclient.hacks.TunnellerHack;
import net.wurstclient.util.json.JsonException;

public final class HackList implements UpdateListener
{
	public final AntiAfkHack antiAfkHack = new AntiAfkHack();
	public final AntiBlindHack antiBlindHack = new AntiBlindHack();
	public final AntiCactusHack antiCactusHack = new AntiCactusHack();
	public final AntiKnockbackHack antiKnockbackHack = new AntiKnockbackHack();
	public final AntiSpamHack antiSpamHack = new AntiSpamHack();
	public final AntiWaterPushHack antiWaterPushHack = new AntiWaterPushHack();
	public final AntiWobbleHack antiWobbleHack = new AntiWobbleHack();
	public final AutoArmorHack autoArmorHack = new AutoArmorHack();
	public final AutoBuildHack autoBuildHack = new AutoBuildHack();
	public final AutoDropHack autoDropHack = new AutoDropHack();
	public final AutoLeaveHack autoLeaveHack = new AutoLeaveHack();
	public final AutoEatHack autoEatHack = new AutoEatHack();
	public final AutoFarmHack autoFarmHack = new AutoFarmHack();
	public final AutoFishHack autoFishHack = new AutoFishHack();
	public final AutoMineHack autoMineHack = new AutoMineHack();
	public final AutoReconnectHack autoReconnectHack = new AutoReconnectHack();
	public final AutoRespawnHack autoRespawnHack = new AutoRespawnHack();
	public final AutoSignHack autoSignHack = new AutoSignHack();
	public final AutoSoupHack autoSoupHack = new AutoSoupHack();
	public final AutoSprintHack autoSprintHack = new AutoSprintHack();
	public final AutoStealHack autoStealHack = new AutoStealHack();
	public final AutoSwimHack autoSwimHack = new AutoSwimHack();
	public final AutoSwordHack autoSwordHack = new AutoSwordHack();
	public final AutoToolHack autoToolHack = new AutoToolHack();
	public final AutoTotemHack autoTotemHack = new AutoTotemHack();
	public final AutoWalkHack autoWalkHack = new AutoWalkHack();
	public final BaseFinderHack baseFinderHack = new BaseFinderHack();
	public final BlinkHack blinkHack = new BlinkHack();
	public final BonemealAuraHack bonemealAuraHack = new BonemealAuraHack();
	public final BunnyHopHack bunnyHopHack = new BunnyHopHack();
	public final CameraNoClipHack cameraNoClipHack = new CameraNoClipHack();
	public final CaveFinderHack caveFinderHack = new CaveFinderHack();
	public final ChatTranslatorHack chatTranslatorHack =
		new ChatTranslatorHack();
	public final ChestEspHack chestEspHack = new ChestEspHack();
	public final ClickGuiHack clickGuiHack = new ClickGuiHack();
	public final CriticalsHack criticalsHack = new CriticalsHack();
	public final DolphinHack dolphinHack = new DolphinHack();
	public final ExcavatorHack excavatorHack = new ExcavatorHack();
	public final ExtraElytraHack extraElytraHack = new ExtraElytraHack();
	public final FancyChatHack fancyChatHack = new FancyChatHack();
	public final FeedAuraHack feedAuraHack = new FeedAuraHack();
	public final FightBotHack fightBotHack = new FightBotHack();
	public final FollowHack followHack = new FollowHack();
	public final FreecamHack freecamHack = new FreecamHack();
	public final FullbrightHack fullbrightHack = new FullbrightHack();
	public final HealthTagsHack healthTagsHack = new HealthTagsHack();
	public final InstantBunkerHack instantBunkerHack = new InstantBunkerHack();
	public final InvWalkHack invWalkHack = new InvWalkHack();
	public final ItemEspHack itemEspHack = new ItemEspHack();
	public final KillauraLegitHack killauraLegitHack = new KillauraLegitHack();
	public final KillauraHack killauraHack = new KillauraHack();
	public final MobEspHack mobEspHack = new MobEspHack();
	public final MobSpawnEspHack mobSpawnEspHack = new MobSpawnEspHack();
	public final NameTagsHack nameTagsHack = new NameTagsHack();
	public final NavigatorHack navigatorHack = new NavigatorHack();
	public final NoBackgroundHack noBackgroundHack = new NoBackgroundHack();
	public final NoFallHack noFallHack = new NoFallHack();
	public final NoHurtcamHack noHurtcamHack = new NoHurtcamHack();
	public final NoOverlayHack noOverlayHack = new NoOverlayHack();
	public final NoPumpkinHack noPumpkinHack = new NoPumpkinHack();
	public final NoWeatherHack noWeatherHack = new NoWeatherHack();
	public final OpenWaterEspHack openWaterEspHack = new OpenWaterEspHack();
	public final OverlayHack overlayHack = new OverlayHack();
	public final PanicHack panicHack = new PanicHack();
	public final ParkourHack parkourHack = new ParkourHack();
	public final PlayerEspHack playerEspHack = new PlayerEspHack();
	public final PortalGuiHack portalGuiHack = new PortalGuiHack();
	public final RadarHack radarHack = new RadarHack();
	public final RainbowUiHack rainbowUiHack = new RainbowUiHack();
	public final SafeWalkHack safeWalkHack = new SafeWalkHack();
	public final ScaffoldWalkHack scaffoldWalkHack = new ScaffoldWalkHack();
	public final SearchHack searchHack = new SearchHack();
	public final SneakHack sneakHack = new SneakHack();
	public final TillauraHack tillauraHack = new TillauraHack();
	public final TooManyHaxHack tooManyHaxHack = new TooManyHaxHack();
	public final TrajectoriesHack trajectoriesHack = new TrajectoriesHack();
	public final TreeBotHack treeBotHack = new TreeBotHack();
	public final TriggerBotHack triggerBotHack = new TriggerBotHack();
	public final TrueSightHack trueSightHack = new TrueSightHack();
	public final TunnellerHack tunnellerHack = new TunnellerHack();
	
	private final TreeMap<String, Hack> hax =
		new TreeMap<>(String::compareToIgnoreCase);
	
	private final EnabledHacksFile enabledHacksFile;
	private final Path profilesFolder =
		WurstClient.INSTANCE.getWurstFolder().resolve("enabled hacks");
	
	private final EventManager eventManager =
		WurstClient.INSTANCE.getEventManager();
	
	public HackList(Path enabledHacksFile)
	{
		this.enabledHacksFile = new EnabledHacksFile(enabledHacksFile);
		
		try
		{
			for(Field field : HackList.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Hack"))
					continue;
				
				Hack hack = (Hack)field.get(this);
				hax.put(hack.getName(), hack);
			}
			
		}catch(Exception e)
		{
			String message = "Initializing Wurst hacks";
			CrashReport report = CrashReport.create(e, message);
			throw new CrashException(report);
		}
		
		eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		enabledHacksFile.load(this);
		eventManager.remove(UpdateListener.class, this);
	}
	
	public void saveEnabledHax()
	{
		enabledHacksFile.save(this);
	}
	
	public Hack getHackByName(String name)
	{
		return hax.get(name);
	}
	
	public Collection<Hack> getAllHax()
	{
		return Collections.unmodifiableCollection(hax.values());
	}
	
	public int countHax()
	{
		return hax.size();
	}
	
	public ArrayList<Path> listProfiles()
	{
		if(!Files.isDirectory(profilesFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(profilesFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadProfile(String fileName) throws IOException, JsonException
	{
		enabledHacksFile.loadProfile(this, profilesFolder.resolve(fileName));
	}
	
	public void saveProfile(String fileName) throws IOException, JsonException
	{
		enabledHacksFile.saveProfile(this, profilesFolder.resolve(fileName));
	}
}
