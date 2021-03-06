package tardis.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import tardis.api.IControlMatrix;
import tardis.client.renderer.model.console.ButtonModel;
import tardis.client.renderer.model.console.GaugeDisplayModel;
import tardis.client.renderer.model.console.GaugeNeedleModel;
import tardis.client.renderer.model.console.LeverBaseModel;
import tardis.client.renderer.model.console.LeverModel;
import tardis.client.renderer.model.console.PushLeverModel;
import tardis.client.renderer.model.console.SchemaDisplayModel;
import tardis.client.renderer.model.console.ScreenFrameModel;
import tardis.client.renderer.model.console.ScreenModel;
import tardis.client.renderer.model.console.SonicScrewdriverHolderModel;
import tardis.client.renderer.model.console.SpecialLeverModel;
import tardis.client.renderer.model.console.TardisLightModel;
import tardis.client.renderer.model.console.ValveWheelModel;
import tardis.common.core.helpers.ScrewdriverHelper;

public class ControlRenderer
{
	private static SonicScrewdriverHolderModel holder = new SonicScrewdriverHolderModel();
	private static GaugeDisplayModel gaugeDisplay = new GaugeDisplayModel();
	private static GaugeNeedleModel gaugeNeedle = new GaugeNeedleModel();
	private static ValveWheelModel wheel = new ValveWheelModel();
	private static LeverModel lever = new LeverModel();
	private static LeverBaseModel leverBase = new LeverBaseModel();
	private static ScreenFrameModel screenFrame = new ScreenFrameModel();
	private static ScreenModel screen = new ScreenModel();
	private static ButtonModel button = new ButtonModel();
	private static PushLeverModel pushSwitch = new PushLeverModel();
	private static SpecialLeverModel specLever  = new SpecialLeverModel();
	private static SchemaDisplayModel schemaDisplay = new SchemaDisplayModel();
	private static TardisLightModel light = new TardisLightModel();

	private FontRenderer fontRenderer;
	private TextureManager textureManager;
	int old = -1;

	public ControlRenderer(FontRenderer fr, TextureManager tm)
	{
		fontRenderer = fr;
		textureManager = tm;
	}

	protected void bindTexture(ResourceLocation par1ResourceLocation)
    {
        if (textureManager != null)
        	textureManager.bindTexture(par1ResourceLocation);
    }

	private void handleSettings(double x,double y, double z, double rX, double rY, double rZ, double sX,double sY, double sZ)
	{
		GL11.glTranslated(x, y, z);
		GL11.glRotated(rZ, 0, 0, 1);
		GL11.glRotated(rY, 0, 1, 0);
		GL11.glRotated(rX, 1, 0, 0);
		GL11.glScaled(sX, sY, sZ);
	}

	private void resetHighlight()
	{
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glColor3d(1, 1, 1);
	}

	private void setHighlight(IControlMatrix te, int controlID)
	{
		if((controlID >= 0) && (te.getControlHighlight(controlID) >= 0))
		{
			double hi = te.getControlHighlight(controlID);
			GL11.glDepthFunc(GL11.GL_ALWAYS);
			GL11.glColor3d(0.2*hi, 0.5 * hi, hi);
		}
		else
		{
			double[] colRat = te.getColorRatio(controlID);
			if ((colRat != null) && (colRat.length == 3) && ((colRat[0] != 0) || (colRat[1] != 0) || (colRat[2] != 0)))
				GL11.glColor3d(colRat[0],colRat[1],colRat[2]);
		}
	//	else
	//		resetHighlight();
	}

	public void renderTextScreen(Tessellator tess, IControlMatrix tce,String s, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glTranslated(-0.0125, -0.03, 0.0025);
		bindTexture(new ResourceLocation("tardismod","textures/models/SchemaDisplay.png"));
		schemaDisplay.render(null,0F,0F,0F,0F,0F,0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glScaled(0.022, 0.022, 0.022);
		if(s != null)
			fontRenderer.drawString(s.length() > 18 ? s.substring(0,18):s, 0, 0, 16579836);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		resetHighlight();
	}

	public void renderScrewdriverHolder(Tessellator tess, IControlMatrix te, double x, double y, double z, double rX,double rY, double rZ,double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		resetHighlight();
		bindTexture(new ResourceLocation("tardismod","textures/models/SonicScrewdriverHolder.png"));
		holder.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void renderScrewdriver(Tessellator tess, IControlMatrix te, int slot, double x, double y, double z, double rX,double rY, double rZ,double sX, double sY, double sZ)
	{
		ScrewdriverHelper helper = te.getScrewHelper(slot);
		if(helper != null)
		{
			GL11.glPushMatrix();
			handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
			resetHighlight();
			helper.render();
			resetHighlight();
			GL11.glPopMatrix();
		}
	}

	public void renderGauge(Tessellator tess, IControlMatrix te, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		resetHighlight();
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleGaugeDisplay.png"));
		gaugeDisplay.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleGaugeNeedle.png"));
		GL11.glTranslated(0.15, 0.175, 0.5/8);
		GL11.glRotated(-90-(te.getControlState(id,true) * 180), 0, 0, 1);
		GL11.glScaled(0.25, 0.40, 0.25);
		gaugeNeedle.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderGauges(Tessellator tess, IControlMatrix te, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ, int... ids)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		resetHighlight();
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleGaugeDisplay.png"));
		gaugeDisplay.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
		if(ids.length > 0)
		{
			for(int i=0;i<ids.length;i++)
			{
				GL11.glPushMatrix();
				bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleGaugeNeedle.png"));
				GL11.glTranslated(0.15, 0.175, 0.5/8);
				GL11.glRotated(-90-(te.getControlState(ids[i],true) * 180), 0, 0, 1);
				GL11.glScaled(0.25, 0.40, 0.25);
				gaugeNeedle.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();
	}

	public void renderWheel(Tessellator tess, IControlMatrix te, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glRotated(te.getControlState(id)*360, 0, 1, 0);
		GL11.glTranslated(-0.03125, 0, -0.03125);
		setHighlight(te,id);
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisValveWheel.png"));
		wheel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void renderLight(Tessellator tess, IControlMatrix te,int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		resetHighlight();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/SonicScrewdriverHolder.png"));
		holder.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glScaled(0.6,0.6,0.6);
		GL11.glTranslated(-0.125, -0.05, -0.125);
		bindTexture(new ResourceLocation("tardismod","textures/models/Light.png"));
		double[] colRat = te.getColorRatio(id);
		double am = te.getControlState(id);
		if((colRat != null) && (colRat.length == 3))
			GL11.glColor3d(colRat[0] * am,colRat[1] * am,colRat[2] * am);
		light.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		resetHighlight();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderLever(Tessellator tess, IControlMatrix te, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glRotated((te.getControlState(id)*140) - 70, 1, 0, 0);
		setHighlight(te,id);
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleLever.png"));
		lever.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		resetHighlight();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleLeverBase.png"));
		leverBase.render(null,0F,0F,0F,0F,0F,0.0625F);
		GL11.glColor3d(1, 1, 1);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderSpecialLever(Tessellator tess, IControlMatrix te, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glRotated(70 - (te.getControlState(id)*140), 0, 0, 1);
		GL11.glTranslated(-0.03125, -0.15625, 0);
		setHighlight(te,id);
		bindTexture(new ResourceLocation("tardismod","textures/models/SpecialLever.png"));
		specLever.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		resetHighlight();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		resetHighlight();
		GL11.glTranslated(0, 0, 0.0625);
		bindTexture(new ResourceLocation("tardismod","textures/models/TardisConsoleLeverBase.png"));
		leverBase.render(null,0F,0F,0F,0F,0F,0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderScreen(Tessellator tess, IControlMatrix te, int id, String texture, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.03125, -0.0625);
		bindTexture(new ResourceLocation("tardismod","textures/models/ScreenFrame.png"));
		screenFrame.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		bindTexture(new ResourceLocation("tardismod","textures/models/screen/"+texture+".png"));
		screen.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderPushSwitch(Tessellator tess, IControlMatrix tce, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glTranslated(0.03125, 0, 0.03125);
		bindTexture(new ResourceLocation("tardismod","textures/models/SonicScrewdriverHolder.png"));
		holder.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.03125 - (0.3 * tce.getControlState(id)), 0);
		setHighlight(tce,id);
		bindTexture(new ResourceLocation("tardismod","textures/models/PushLever.png"));
		pushSwitch.render(null,0F,0F,0F,0F,0F,0.0625F);
		resetHighlight();
		GL11.glColor3d(1, 1, 1);
		GL11.glPopMatrix();
		GL11.glPopMatrix();

	}



	public void renderButton(Tessellator tess, IControlMatrix tce, int id, double x, double y, double z,double rX,double rY,double rZ, double sX, double sY, double sZ)
	{
		GL11.glPushMatrix();
		handleSettings(x,y,z,rX,rY,rZ,sX,sY,sZ);
		GL11.glPushMatrix();
		GL11.glTranslated(0.03125, 0, 0.03125);
		bindTexture(new ResourceLocation("tardismod","textures/models/SonicScrewdriverHolder.png"));
		holder.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.0015 + (0.06 * tce.getControlState(id)), 0);
		setHighlight(tce, id);
		bindTexture(new ResourceLocation("tardismod","textures/models/PushLever.png"));
		button.render(null,0F,0F,0F,0F,0F,0.0625F);
		resetHighlight();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		resetHighlight();
	}
}
