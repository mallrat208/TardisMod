package tardis.client;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.MinecraftForgeClient;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tardis.client.renderer.DecoratorRenderer;
import tardis.client.renderer.ManualItemRenderer;
import tardis.client.renderer.SonicScrewdriverRenderer;
import tardis.client.renderer.SummonerRenderer;
import tardis.client.renderer.tileents.BatteryRenderer;
import tardis.client.renderer.tileents.ClosedRoundelRenderer;
import tardis.client.renderer.tileents.ComponentRenderer;
import tardis.client.renderer.tileents.ConsoleRenderer;
import tardis.client.renderer.tileents.CoreRenderer;
import tardis.client.renderer.tileents.EngineRenderer;
import tardis.client.renderer.tileents.LabRenderer;
import tardis.client.renderer.tileents.LandingPadRenderer;
import tardis.client.renderer.tileents.MagicDoorTileEntityRenderer;
import tardis.client.renderer.tileents.ManualRenderer;
import tardis.client.renderer.tileents.TardisRenderer;
import tardis.common.TMRegistry;
import tardis.common.TardisProxy;
import tardis.common.core.TardisOutput;
import tardis.common.items.extensions.ScrewTypeRegister;
import tardis.common.tileents.BatteryTileEntity;
import tardis.common.tileents.ComponentTileEntity;
import tardis.common.tileents.ConsoleTileEntity;
import tardis.common.tileents.CoreTileEntity;
import tardis.common.tileents.EngineTileEntity;
import tardis.common.tileents.LabTileEntity;
import tardis.common.tileents.LandingPadTileEntity;
import tardis.common.tileents.MagicDoorTileEntity;
import tardis.common.tileents.ManualTileEntity;
import tardis.common.tileents.SummonerTileEntity;
import tardis.common.tileents.TardisTileEntity;
import tardis.common.tileents.extensions.DummyRoundelTE;
import tardis.common.tileents.extensions.chameleon.tardis.AbstractTardisChameleon;

public class TardisClientProxy extends TardisProxy
{
	private TardisRenderer tardisRenderer = new TardisRenderer();
	private SummonerRenderer summonerRenderer;

	public HashMap<String,ResourceLocation> skins = new HashMap<String,ResourceLocation>();
	public static World cWorld = null;
	public TardisClientProxy()
	{
	}

	@Override
	public void handleTardisTransparency(int worldID,int x, int y, int z)
	{
		WorldServer world = MinecraftServer.getServer().worldServerForDimension(worldID);
		world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
	}

	@Override
	public void init()
	{
		TardisOutput.print("TM", "Sending message to WAILA");
		FMLInterModComms.sendMessage("Waila","register","tardis.common.integration.waila.WailaCallback.wailaRegister");
	}

	public static SonicScrewdriverRenderer screwRenderer;

	@Override
	public void postAssignment()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TardisTileEntity.class, tardisRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(CoreTileEntity.class, new CoreRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(ConsoleTileEntity.class, new ConsoleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(ComponentTileEntity.class, new ComponentRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(EngineTileEntity.class, new EngineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LabTileEntity.class, new LabRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LandingPadTileEntity.class, new LandingPadRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(BatteryTileEntity.class, new BatteryRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(DummyRoundelTE.class, new ClosedRoundelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(ManualTileEntity.class, new ManualRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(SummonerTileEntity.class, summonerRenderer = new SummonerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicDoorTileEntity.class, new MagicDoorTileEntityRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TMRegistry.labBlock), new LabRenderer());
		MinecraftForgeClient.registerItemRenderer(TMRegistry.screwItem, screwRenderer = new SonicScrewdriverRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TMRegistry.battery), new BatteryRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TMRegistry.tardisBlock), tardisRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TMRegistry.summonerBlock), summonerRenderer);
		MinecraftForgeClient.registerItemRenderer(TMRegistry.manualItem, new ManualItemRenderer());
		MinecraftForgeClient.registerItemRenderer(TMRegistry.decoTool, new DecoratorRenderer());
		ScrewTypeRegister.registerClientResources();
	}

	@Override
	public World getWorld(int id)
	{
		if(ServerHelper.isClient())
			if(Minecraft.getMinecraft() != null)
				if(Minecraft.getMinecraft().thePlayer != null)
					cWorld = Minecraft.getMinecraft().thePlayer.worldObj;
		if(cWorld != null)
			if(id == cWorld.provider.dimensionId)
				return cWorld;
		return super.getWorld(id);
	}

	@SideOnly(Side.CLIENT)
	private ITextureObject loadSkin(TextureManager texMan, TardisTileEntity tte, AbstractTardisChameleon cham)
	{
		if(tte.owner == null)
			return null;
		String key = cham.getTextureDir() + "." + tte.owner;
		String dir = cham.getTextureDir();
		texMan = Minecraft.getMinecraft().getTextureManager();
		ResourceLocation skin = new ResourceLocation("tardismod","textures/tardis/" + dir + "/" + StringUtils.stripControlCodes(tte.owner) +".png");
		ITextureObject object = texMan.getTexture(skin);
		if(object == null)
		{
			TardisOutput.print("TTE", "Downloading " + tte.owner + " skin");
			object = new ThreadDownloadTardisData(null, TardisTileEntity.baseURL + dir + "/" +tte.owner+".png", cham.defaultTex(), new ImageBufferDownload());
		}
		texMan.loadTexture(skin, object);
		skins.put(key, skin);
		return object;
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getSkin(TextureManager texMan,TardisTileEntity tte, AbstractTardisChameleon cham)
	{
		if(tte == null)
			return cham.defaultTex();

		String key = cham.getTextureDir() + "." + tte.owner;
		if(!skins.containsKey(key))
			loadSkin(texMan,tte, cham);

		return skins.containsKey(key) ? skins.get(key) : cham.defaultTex();
	}
}
