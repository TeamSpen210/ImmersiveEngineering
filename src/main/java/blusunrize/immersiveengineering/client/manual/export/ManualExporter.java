package blusunrize.immersiveengineering.client.manual.export;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
		File root = new File(Minecraft.getInstance().gameDir, "IEManual").getAbsoluteFile();
		IELogger.info("Writing manual to " + root.toString());
		if (root.isDirectory())
		{
			FileUtils.cleanDirectory(root);
		} else if (!root.mkdir() || root.exists()) {
			throw new IOException();
		}
		ManualExporter exp = new ManualExporter(root);
		ManualInstance manual = ManualHelper.getManual();

		for(ManualEntry manualEntry : manual.contentsByName.values())
		{
			exp.genEntry(manualEntry);
		}
	}

	public File file(String... parts) {
		return new File(root, String.join("/", parts));
	}

	public File file(String folder, ResourceLocation loc, String ext) {
		return file(folder, loc.getNamespace(), loc.getPath() + "." + ext);
	}

	@FunctionalInterface
	public interface IPageWriter {
		void writeBody(Writer stream) throws IOException;
	}

	private void writePage(File dest, String title, IPageWriter writer) throws IOException
	{
		dest.getParentFile().mkdirs();
		IELogger.info("Writing page: " + dest.getAbsolutePath());
		try(OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(dest), StandardCharsets.UTF_8))
		{
			stream.write("<!DOCTYPE html>\n<html>");
			stream.write("<head>\n");
				stream.write("<title>" + title + "</title>\n");
			stream.write("</head><body>\n");

			writer.writeBody(stream);

			stream.write("</body>\n</html>\n");
		}
	}

	private void genEntry(ManualEntry entry) throws IOException
	{
		writePage(
				file("pages", entry.getLocation(), "html"),
				entry.getTitle(),
				(stream) -> {
			stream.write("<h1>" + entry.getTitle() + "</h1>\n");
			stream.write("<h2>" + entry.getSubtext() + "</h2>\n");
			for(int i = 0; i < entry.getPageCount(); i++)
			{
				stream.write("<section>\n");
				entry.genHTML(this, stream, i);
				stream.write("</section>\n");
			}
		});
	}
}
