package encode.audio.entrypoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import encode.audio.utils.AudioFile;
import encode.audio.utils.CoreException;
import encode.audio.utils.CoreUtil;
import encode.audio.utils.DummyLogService;
import encode.audio.utils.HttpRequestFactory;
import encode.audio.utils.LogService;
import flux.AudioAnnounceTmlg;
import flux.FluxTmlg;
import flux.IAudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngine {

	private LogService logger = new DummyLogService();
	private DataObject audioConfig;
	private DataObject httpConfig;

	private LocalHTTPSServer localHTTPSServer;
	private LocalTmpFolder localTmpFolder;

	public AudioAnnounceEngine(LocalHTTPSServer localHTTPSServer, LocalTmpFolder localTmpFolder) {
		super();
		this.localHTTPSServer = localHTTPSServer;
		this.localTmpFolder = localTmpFolder;
	}

	public IFluxTmlg publishAudioFile(AudioAnnounceTmlg audioAnnounceTmlg, DataObject audioConfigTmp, DataObject httpConfigTmp) throws AppTechnicalException {
		this.audioConfig = audioConfigTmp;
		this.httpConfig = httpConfigTmp;
		IFluxTmlg targetAudioFileMessage = new FluxTmlg(audioAnnounceTmlg);

		IAudioAnnounceTmlg audioAnnounce = targetAudioFileMessage.getBody().getTravelInfo().getAudioAnnounce();
		AudioFile newAudioFile;
        try {
            newAudioFile = processAudioAnnounce(targetAudioFileMessage, audioAnnounce);
            targetAudioFileMessage = updateAudioFileMessage(targetAudioFileMessage, newAudioFile);
            return targetAudioFileMessage;
        } catch (CoreException e) {

            logger.log(LogService.LOG_ERROR, "Failed handling the file '" + audioAnnounce.getFileName() + "' ", e);
            throw new AppTechnicalException(e);
        }

	}

	/**
	 * process audio announce
	 *
	 * @param flux
	 * @param audioAnnounce
	 * @return AudioFile
	 * @throws CoreException
	 * */
	public AudioFile processAudioAnnounce(IFluxTmlg flux, IAudioAnnounceTmlg audioAnnounce) throws CoreException {

		String fileName = audioAnnounce.getFileName();
		String fileUrl = audioAnnounce.getUrl();
		String audioTempPath = httpConfig.getString("audio_temp_path");
		String filePath = audioTempPath + fileName;

		AudioFile newAudioFile = CoreUtil.simulateEncodedAudioFileProperties(fileName, audioConfig.getString("final_audio_file_extension"));
		String encodedFilename = newAudioFile.getName();

		logger.log(LogService.LOG_DEBUG, "The audio file '" + encodedFilename + "' does not exist on the HTTPS server");

		downnloadAudioFile(fileName, filePath, fileUrl);

		logger.log(LogService.LOG_DEBUG, "Encoding audio file :" + filePath + " (path : " + httpConfig.getString("audio_temp_path") + ")");
		newAudioFile = CoreUtil.encodeAudioFile(audioTempPath, fileName, audioConfig);

		// Uploading encoded audio file to HTTPS server
		uploadAudioAnnounce(localHTTPSServer, newAudioFile);

		return newAudioFile;
	}

	/**
	 * Download the audio file if not already exists in the temp directory
	 *
	 * @param fileName
	 * @param fileUrl
	 * @param filePath
	 *
	 * */
	public void downnloadAudioFile(String fileName, String destinationfilePath, String fileUrl) throws CoreException{
	    File audioFile = new File(httpConfig.getString("audio_temp_path") + fileName);
	    // Le fichier audio originel existe en local sur la machine
	    if(audioFile.exists()){
	        logger.log(LogService.LOG_DEBUG, "Le fichier audio originel existe deje en local : '"+httpConfig.getString("audio_temp_path") +fileName+"'");
	    }
	    // Le fichier audio originel n'existe pas en local sur la machine
	    else{
	        // Recuperation du fichier audio et enregistrement temporaire pour encodage
	        byte[] fileData = downloadAudioFileFromHttpServer(fileUrl);
	        try {
                CoreUtil.writeBytes(destinationfilePath, fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }
	}


	private byte[] downloadAudioFileFromHttpServer(String fileUrl) throws CoreException {
	    return HttpRequestFactory.getFileFromHttpServer(fileUrl);
	}


	public void uploadAudioAnnounce(LocalHTTPSServer localServerFolder, AudioFile newAudioFile) {
		logger.log(LogService.LOG_DEBUG, "Uploading audio file to HTTPS server");
		localServerFolder.uploadAudioAnnounce(newAudioFile.getName());
		logger.log(LogService.LOG_DEBUG, "Uploading audio file to HTTPS server : OK");
	}

	public IFluxTmlg updateAudioFileMessage(IFluxTmlg flux, AudioFile newAudioFile) {
		String serverAudioEmbarqueURLGet = httpConfig.getString("url_embarque_server_get");

		flux.getBody().getTravelInfo().getAudioAnnounce().setFileName(newAudioFile.getName());
		flux.getBody().getTravelInfo().getAudioAnnounce().setFormat(newAudioFile.getFormat());
		flux.getBody().getTravelInfo().getAudioAnnounce().setUrl(serverAudioEmbarqueURLGet + newAudioFile.getName());

		logger.log(LogService.LOG_DEBUG, "Send message oBix to the adaptor: " + flux.toString());
		return flux;
	}
}
