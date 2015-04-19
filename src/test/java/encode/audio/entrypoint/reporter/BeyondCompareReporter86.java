package encode.audio.entrypoint.reporter;

import java.text.MessageFormat;

import org.approvaltests.reporters.GenericDiffReporter;

public class BeyondCompareReporter86 extends GenericDiffReporter
{
  public static final BeyondCompareReporter86 INSTANCE     = new BeyondCompareReporter86();
  static final String                       DIFF_PROGRAM = "C:\\Program Files (x86)\\Beyond Compare 3\\BCompare.exe";
  static final String                       MESSAGE      = MessageFormat.format(
                                                             "Unable to find Beyond Compare at {0}", DIFF_PROGRAM);
  public BeyondCompareReporter86()
  {
    super(DIFF_PROGRAM, MESSAGE);
  }
}
