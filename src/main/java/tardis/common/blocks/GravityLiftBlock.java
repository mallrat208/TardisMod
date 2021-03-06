package tardis.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;

import cpw.mods.fml.common.registry.GameRegistry;
import tardis.Configs;
import tardis.TardisMod;
import tardis.common.tileents.GravityLiftTileEntity;
import tardis.common.tileents.extensions.CraftingComponentType;

public class GravityLiftBlock extends AbstractBlockContainer
{
	private final String[] suffixes = new String[] { "top", "bottomsides" };

	public GravityLiftBlock()
	{
		super(TardisMod.modName);
		setCreativeTab(TardisMod.cTab);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new GravityLiftTileEntity();
	}

	@Override
	public void initData()
	{
		setBlockName("GravityLift");
		setSubNames("Normal","Craftable");
		setLightLevel(Configs.lightBlocks ? 1 : 0);
	}

	@Override
	public void initRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this,1,1), false, "ddd", "ici", "iii",
				'd', CraftingComponentType.DALEKANIUM.getIS(1),
				'i', "ingotIron",
				'c', CraftingComponentType.CHRONOSTEEL.getIS(1)));
	}

	@Override
	public String[] getIconSuffix()
	{
		return suffixes;
	}

	@Override
	public int getIconSuffixes()
	{
		return 2;
	}

	@Override
	public Class<? extends TileEntity> getTEClass()
	{
		return GravityLiftTileEntity.class;
	}

}
