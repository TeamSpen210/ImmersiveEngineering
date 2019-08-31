/*
 * BluSunrize
 * Copyright (c) 2018
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.gui;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.gui.MaintenanceKitContainer;
import blusunrize.immersiveengineering.common.network.MessageMaintenanceKit;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class MaintenanceKitScreen extends ToolModificationScreen
{
	public MaintenanceKitScreen(PlayerInventory inventoryPlayer, World world, EquipmentSlotType slot, ItemStack item)
	{
		super(new MaintenanceKitContainer(inventoryPlayer, world, slot, item), inventoryPlayer);
		this.xSize = 195;
	}

	@Override
	protected void sendMessage(CompoundNBT data)
	{
		ImmersiveEngineering.packetHandler.sendToServer(new MessageMaintenanceKit(((MaintenanceKitContainer)container).getEquipmentSlot(), data));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture("immersiveengineering:textures/gui/maintenance_kit.png");
		this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		for(int i = 0; i < ((MaintenanceKitContainer)container).internalSlots; i++)
		{
			Slot s = container.getSlot(i);
			ClientUtils.drawSlot(guiLeft+s.xPos, guiTop+s.yPos, 16, 16, 0x44);
		}
	}
}