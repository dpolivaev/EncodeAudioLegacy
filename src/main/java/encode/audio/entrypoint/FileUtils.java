package encode.audio.entrypoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	/**
	 * Post a file. Create it and write its content.
	 *
	 * @param localServerFolder
	 *            the url where to post the file
	 * @param fileName
	 *            the name of the file
	 * @param content
	 *            the content of the file
	 * @throws IOException
	 */
	public static void postFile(final String localServerFolder, final String fileName, final byte[] content) throws IOException {
		FileOutputStream fos = null;
		try {
			final File localFile = createFile(localServerFolder, fileName);
			fos = new FileOutputStream(localFile);
			fos.write(content);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * Create a file.
	 *
	 * @param path
	 *            the path of the file
	 * @param name
	 *            the name of the file
	 * @return an object of type {@link File} representing the file to create
	 * @throws IOException
	 */
	public static File createFile(final String path, final String name) throws IOException {
		final File file = new File(path + "/" + name);
		file.createNewFile();
		return file;
	}

}
