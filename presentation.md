Workshop Encode Audio Legacy 
================================

https://github.com/martinsson/EncodeAudioLegacy
http://en.slideshare.net/sanlaville/20140530-itakegolden-master


Moco
===============

Easy Setup Stub Server

Site: https://github.com/dreamhead/moco

    	int port = 12306;
		HttpServer server = httpserver(port);
    	server.request(by(uri("/" + REMOTE_AUDIO_FILE_NAME)))
    		.response(file(TEST_RESOURCE_DIR + REMOTE_AUDIO_FILE_NAME));
    	server.request(by(uri("/" + REMOTE_AUDIO_FILE_NAME2)))
    		.response(file(TEST_RESOURCE_DIR + REMOTE_AUDIO_FILE_NAME2));
        runner = runner(server);
        runner.start();

ApprovalTests
===============

Unit testing asserts can be difficult to use. 
Approval tests simplify this 
by taking a snapshot of the results, 
and confirming that they have not changed.

Site: http://approvaltests.sourceforge.net/
Available for Java, C#, VB.Net, PHP or Ruby

1. Approvals.verify
 *	verify(String)
 *	verify(ExecutableQuery)
 *	verify(SqlLoader)
 *	verify(Component)
 *	verify(Image)
 *	verify(BufferedImage)
 *	verify(File)
 *	verify(String)
 *	verify(ResultSet)
 *	verify(Map)
 *	verify(FileApprover, ApprovalFailureReporter)
 *	verify(ApprovalWriter, String)
 *	verify(ApprovalWriter, ApprovalNamer, ApprovalFailureReporter)
 *	verify(RackResponse)
 *	verifyHtml(String)
 *	verifyXml(String)

2. Approvals.verifyAll(String)
 *	verifyAll(String, Iterable<T>)
 *	verifyAll(String, Iterable<T>, Function1<T, String>)
 *	verifyAll(String, String, Iterable<T>)
 *	verifyAll(String, String, T[])
 *	verifyAll(String, T[])
 *	verifyAll(String, T[], Function1<T, String>)
 *	verifyAll(T[], Function1<T, String>)
 *	verifyEachFileAgainstMasterDirectory(File[])
 *	verifyEachFileInDirectory(File)
 *	verifyEachFileInDirectory(File, FileFilter)
 *	verifyEachFileInDirectory(File, FilenameFilter)
 
3. @UseReporter(BeyondCompareReporter86.class)

		public class BeyondCompareReporter86 extends GenericDiffReporter
		{
		  static final String  DIFF_PROGRAM = "C:\\Program Files (x86)\\Beyond Compare 3\\BCompare.exe";
		  static final String  MESSAGE = MessageFormat.format("Unable to find Beyond Compare at {0}", DIFF_PROGRAM);
		  public BeyondCompareReporter86(){super(DIFF_PROGRAM, MESSAGE);}
		}

3. XStream

  XStream is a simple library to serialize objects to XML and back again.

  Site: http://xstream.codehaus.org/

  `object.toString()` -> `new XStream().toXML(object)` 		

4. ApprovalUtilities(). writeSystemOutToStringBuffer()

5. LegacyApprovals.LockDown(Object call, String method, Object[]... parametersVariations)

Eclemma
========

Java code coverage for eclipse

Site: http://www.eclemma.org/



