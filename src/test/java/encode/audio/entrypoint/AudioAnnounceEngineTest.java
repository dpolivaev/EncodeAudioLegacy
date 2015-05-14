package encode.audio.entrypoint;

import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.file;
import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Runner.runner;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.approvaltests.Approvals;
import org.approvaltests.legacycode.LegacyApprovals;
import org.approvaltests.reporters.UseReporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.dreamhead.moco.HttpServer;

import encode.audio.entrypoint.reporter.BeyondCompareReporter86;
import flux.AudioAnnounceTmlg;
import flux.IFluxTmlg;

//@UseReporter(BeyondCompareReporter86.class)
public class AudioAnnounceEngineTest {

    private static final String HOST = "http://localhost:12306/";
    private static final String TEST_RESOURCE_DIR = "./src/test/resources/";
    private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
    private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";
    private com.github.dreamhead.moco.Runner runner;

/*    
  // Http Server
   @Before
    public void setuphttp() {
    	int port = 12306;
		HttpServer server = httpserver(port);
    	server.request(by(uri("/" + REMOTE_AUDIO_FILE_NAME))).response(file(TEST_RESOURCE_DIR + REMOTE_AUDIO_FILE_NAME));
    	server.request(by(uri("/" + REMOTE_AUDIO_FILE_NAME2))).response(file(TEST_RESOURCE_DIR + REMOTE_AUDIO_FILE_NAME2));
        runner = runner(server);
        runner.start();
    }

    @After
    public void tearDown() {
        runner.stop();
    }
*/
    @Test 
    public void audioAnnounceEngine() throws Exception {
    	String log = publishAudioFile(REMOTE_AUDIO_FILE_NAME, "mp3","10.151.156.180Tue_Nov_05_141112_CET_2013343");
    	Approvals.verify(log);
    }

    public String publishAudioFile(String sourceFileName, String targetFormat, String finalUrl) throws AppTechnicalException, IOException {
        AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(HOST + finalUrl, targetFormat, sourceFileName);
        DataObject configAudioTmp = new AudioDataObject("." + targetFormat);

        TemporaryFolder tempFolder = new TemporaryFolder();
        tempFolder.create();
        String audioTempPath = tempFolder.getRoot().getAbsolutePath() + "/";
        DataObject httpDataObj = new HttpDataObj(audioTempPath, "http://localhost/get");

        File localServerFolder = tempFolder.newFolder("local_server_folder");
        localServerFolder.mkdirs();
        
//        ByteArrayOutputStream log = new ApprovalUtilities(). writeSystemOutToStringBuffer();

        AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder.getAbsolutePath() + "/");
		try {
			IFluxTmlg availableEncodedAudioFile = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
			return availableEncodedAudioFile.toString();
//			String fileInformation = new XStream().toXML(availableEncodedAudioFile);
//			String fileList = String.join("\n", localServerFolder.list());
//			String logWithoutTempFolder = removePath(log.toString(), tempFolder);
//			String result = "\nLog : " + logWithoutTempFolder + "\nFiles:\n" + fileList + "\n" + fileInformation + "\n";
//			return  result;
		} catch (Exception e) {
			return e.getMessage();
//			String logWithoutTempFolder = removePath(log.toString(), tempFolder);
//			return "\nLog : " + logWithoutTempFolder + "\n" + e.getMessage();
		}
		finally{
			tempFolder.delete();
		}

    }

	String removePath(String log, TemporaryFolder tempFolder) {
		String absolutePath = tempFolder.getRoot().getAbsolutePath();
		return log.replaceAll(Pattern.quote(absolutePath), "");
	}

    @Ignore 
    @Test 
    public void coverageAudioAnnounceEngineLockdown() throws Exception {
        Object[] sourceFileNames = {REMOTE_AUDIO_FILE_NAME, REMOTE_AUDIO_FILE_NAME2};
        Object[] targetFormats = {"wav", "mp3", "ogg"};
        Object[] finalUrls = { "10.151.156.180Mon_Nov_04_140724_CET_2013343", "10.151.156.180Tue_Nov_05_141112_CET_2013343"} ;

        LegacyApprovals.LockDown(this, "publishAudioFile", sourceFileNames, targetFormats, finalUrls);
    }

}
