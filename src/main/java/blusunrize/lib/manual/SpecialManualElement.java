/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.lib.manual;

import blusunrize.immersiveengineering.client.manual.ManualExporter;
import blusunrize.lib.manual.gui.ManualScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public abstract class SpecialManualElement
{
	//The number of vertical pixels this element occupies.
	public abstract int getPixelsTaken();

	public abstract void onOpened(ManualScreen gui, int x, int y, List<Button> buttons);

	public abstract void render(ManualScreen gui, int x, int y, int mouseX, int mouseY);

	public abstract void mouseDragged(int x, int y, double clickX, double clickY, double mx, double my, double lastX,
									  double lastY, int mouseButton);

	public abstract boolean listForSearch(String searchTag);

	public abstract void recalculateCraftingRecipes();

	public ItemStack[] getProvidedRecipes()
	{
		return new ItemStack[0];
	}

	public ItemStack getHighlightedStack()
	{
		return ItemStack.EMPTY;
	}

	public boolean isAbove()
	{
		return true;
	}

	public void genHTML(ManualExporter exp, Writer stream) throws IOException
	{
		stream.write("<div>&lt;" + this.getClass().getName() + "&gt;</div>\n");
	}
}
