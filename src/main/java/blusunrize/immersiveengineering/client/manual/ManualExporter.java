package blusunrize.immersiveengineering.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Writes the manual into a folder as a set of HTML pages.
 */
public class ManualExporter
{
	private final File root;
	private final Map<Item, File> itemRenders=new HashMap<>();

	private ManualExporter(File root) {
		this.root = root;
	}

	public static void export() throws IOException
	{
		File root = new File(Minecraft.getInstance().gameDir, "IEManual");
		if (root.isDirectory())
		{
			FileUtils.cleanDirectory(root);
		} else if (!root.mkdir() || root.exists()) {
			throw new IOException();
		}
		ManualExporter exp = new ManualExporter(root);
		ManualInstance manual = ManualHelper.getManual();
		for(Entry<ResourceLocation, ManualEntry> entry : manual.contentsByName.entrySet())
		{
			ResourceLocation chaptName = entry.getKey();
			ManualEntry chapt = entry.getValue();
			File sub = new File(new File(root, chaptName.getNamespace()), chaptName.getPath());
			sub.getParentFile().mkdirs();
			FileWriter file = new FileWriter(sub);
		}
	}
}
